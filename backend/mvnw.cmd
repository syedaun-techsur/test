<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM
@REM Optional ENV vars
@REM   MVNW_REPOURL - repo url base for downloading maven distribution
@REM   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven
@REM   MVNW_VERBOSE - true: enable verbose log; others: silence the output
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0'; $script='%__MVNW_ARG0_NAME__%'; icm -ScriptBlock ([Scriptblock]::Create((Get-Content -Raw '%~f0'))) -NoNewScope}"`) DO @(
  IF "%%A"=="MVN_CMD" (set __MVNW_CMD__=%%B) ELSE IF "%%B"=="" (echo %%A) ELSE (echo %%A=%%B)
)
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%
@SET __MVNW_PSMODULEP_SAVE=
@SET __MVNW_ARG0_NAME__=
@SET MVNW_USERNAME=
@SET MVNW_PASSWORD=
@IF NOT "%__MVNW_CMD__%"=="" (%__MVNW_CMD__% %*)
@echo Cannot start maven from wrapper >&2 && exit /b 1
@GOTO :EOF
: end batch / begin powershell #>

$ErrorActionPreference = 'Stop'
if ($env:MVNW_VERBOSE -eq 'true') {
    $VerbosePreference = 'Continue'
} else {
    $VerbosePreference = 'SilentlyContinue'
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$wrapperPropertiesPath = Join-Path $scriptDir '.mvn\wrapper\maven-wrapper.properties'
if (-Not (Test-Path $wrapperPropertiesPath)) {
    Write-Error "Cannot find maven-wrapper.properties at $wrapperPropertiesPath"
    exit 1
}
$wrapperProps = Get-Content -Raw $wrapperPropertiesPath | ConvertFrom-StringData

if (-not $wrapperProps.distributionUrl) {
    Write-Error "Cannot read 'distributionUrl' property in $wrapperPropertiesPath"
    exit 1
}

$distributionUrl = $wrapperProps.distributionUrl.Trim()

# Determine if using mvnd
$filename = [System.IO.Path]::GetFileName($distributionUrl)
$USE_MVND = $false
if ($filename -like 'maven-mvnd-*') {
    $USE_MVND = $true
}

# Based on mvnd usage, assign correct repo pattern and mvn command
if ($USE_MVND) {
    # Fixing the repository pattern and distribution URL for mvnd Windows platform
    $MVN_CMD = 'mvnd.cmd'
    # Replace the -bin suffix with platform-specific archive
    $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$', '-windows-amd64.zip'
    $MVNW_REPO_PATTERN = '/maven/mvnd/'
} else {
    $script = Split-Path -Leaf $MyInvocation.MyCommand.Path
    $MVN_CMD = $script -replace '^mvnw', 'mvn'
    $MVNW_REPO_PATTERN = '/org/apache/maven/'
}

# Apply MVNW_REPOURL if set
if ($env:MVNW_REPOURL) {
    # Correct pattern usage; use the right one based on mvnd or not
    $urlTail = $distributionUrl -replace ".*$MVNW_REPO_PATTERN", ''
    $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$urlTail"
}

$distributionUrlName = [System.IO.Path]::GetFileName($distributionUrl)
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]+$', '' -replace '-bin$', ''

$MAVEN_USER_HOME = if ($env:MAVEN_USER_HOME) { $env:MAVEN_USER_HOME } else { Join-Path $HOME '.m2' }
$MAVEN_HOME_PARENT = Join-Path $MAVEN_USER_HOME 'wrapper\dists' -Resolve
$MAVEN_HOME_PARENT = Join-Path $MAVEN_HOME_PARENT $distributionUrlNameMain

# Compute MD5 hash of distribution URL for unique folder
$md5 = [System.Security.Cryptography.MD5]::Create()
$bytes = [System.Text.Encoding]::UTF8.GetBytes($distributionUrl)
$hashBytes = $md5.ComputeHash($bytes)
$MAVEN_HOME_HASH = -join ($hashBytes | ForEach-Object { $_.ToString('x2') })

$MAVEN_HOME = Join-Path $MAVEN_HOME_PARENT $MAVEN_HOME_HASH

if (Test-Path -Path $MAVEN_HOME -PathType Container) {
    Write-Verbose "Found existing MAVEN_HOME at $MAVEN_HOME"
    Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"
    exit 0
}

if (-not $distributionUrlNameMain -or $distributionUrlName -eq $distributionUrlNameMain) {
    Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
    exit 1
}

# Prepare temporary download directory
try {
    $tmpFile = New-TemporaryFile
    $TMP_DOWNLOAD_DIR = "${tmpFile}.dir"
    Remove-Item $tmpFile -Force
    New-Item -ItemType Directory -Path $TMP_DOWNLOAD_DIR | Out-Null
} catch {
    Write-Error "Cannot create temporary download directory"
    exit 1
}

# Register cleanup for temporary directory
$cleanupAction = {
    if (Test-Path -Path $using:TMP_DOWNLOAD_DIR) {
        try { Remove-Item -Path $using:TMP_DOWNLOAD_DIR -Recurse -Force -ErrorAction SilentlyContinue } catch { Write-Warning "Failed to remove $using:TMP_DOWNLOAD_DIR" }
    }
}
Register-EngineEvent PowerShell.Exiting -Action $cleanupAction | Out-Null

# Ensure parent directory exists for Maven install
try {
    if (-not (Test-Path $MAVEN_HOME_PARENT)) {
        New-Item -Path $MAVEN_HOME_PARENT -ItemType Directory -Force | Out-Null
    }
} catch {
    Write-Error "Failed to create parent directory for Maven: $MAVEN_HOME_PARENT"
    exit 1
}

Write-Verbose "Downloading Maven distribution from $distributionUrl to $TMP_DOWNLOAD_DIR\$distributionUrlName"

# Download Maven distribution using WebClient with optional credentials and TLS1.2
try {
    $webclient = New-Object System.Net.WebClient
    if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
        $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
    }
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    $targetFile = Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName
    $webclient.DownloadFile($distributionUrl, $targetFile)
} catch {
    Write-Error "Failed to download Maven distribution: $_"
    exit 1
}

# Verify SHA-256 checksum if specified and not mvnd
if ($wrapperProps.distributionSha256Sum) {
    if ($USE_MVND) {
        Write-Error "Checksum validation is not supported for maven-mvnd.`nPlease disable validation by removing 'distributionSha256Sum' from maven-wrapper.properties."
        exit 1
    }
    Import-Module Microsoft.PowerShell.Utility -ErrorAction Stop
    try {
        $hashFile = Get-FileHash -Path $targetFile -Algorithm SHA256
        if ($hashFile.Hash.ToLower() -ne $wrapperProps.distributionSha256Sum.ToLower()) {
            Write-Error "SHA-256 checksum mismatch. Maven distribution may be compromised or updated. Please update maven-wrapper.properties accordingly."
            exit 1
        }
    } catch {
        Write-Error "Failed to verify SHA-256 checksum: $_"
        exit 1
    }
}

# Extract archive
try {
    Expand-Archive -Path $targetFile -DestinationPath $TMP_DOWNLOAD_DIR -Force
} catch {
    Write-Error "Failed to extract Maven archive: $_"
    exit 1
}

# Rename extracted folder to hash directory name and move to Maven home parent
$extractedDir = Join-Path $TMP_DOWNLOAD_DIR $distributionUrlNameMain
$targetDir = Join-Path $TMP_DOWNLOAD_DIR $MAVEN_HOME_HASH
try {
    Rename-Item -Path $extractedDir -NewName $MAVEN_HOME_HASH -Force
} catch {
    Write-Warning "Failed to rename extracted directory: $_"
}

try {
    Move-Item -Path $targetDir -Destination $MAVEN_HOME_PARENT -Force
} catch {
    if (-not (Test-Path -Path $MAVEN_HOME -PathType Container)) {
        Write-Error "Failed to move Maven home to destination: $MAVEN_HOME_PARENT"
        exit 1
    }
} finally {
    # Cleanup temporary directory forcibly
    try {
        Remove-Item -Path $TMP_DOWNLOAD_DIR -Recurse -Force -ErrorAction SilentlyContinue
    } catch {
        Write-Warning "Cannot remove temporary download directory: $TMP_DOWNLOAD_DIR"
    }
}

Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"
exit 0