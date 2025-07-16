<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
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

$ErrorActionPreference = "Stop"
if ($env:MVNW_VERBOSE -eq "true") {
  $VerbosePreference = "Continue"
} else {
  $VerbosePreference = "SilentlyContinue"
}

# Calculate distributionUrl, requires .mvn/wrapper/maven-wrapper.properties
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$propsPath = Join-Path -Path $scriptDir -ChildPath ".mvn/wrapper/maven-wrapper.properties"
if (-Not (Test-Path $propsPath)) {
  Write-Error "Maven wrapper properties file not found at $propsPath"
  exit 1
}

$wrapperProps = Get-Content -Raw $propsPath | ConvertFrom-StringData
$distributionUrl = $wrapperProps.distributionUrl

if (-Not $distributionUrl) {
  Write-Error "Cannot read distributionUrl property in $propsPath"
  exit 1
}

switch -wildcard -casesensitive ($distributionUrl -replace '^.*/','') {
  "maven-mvnd-*" {
    $USE_MVND = $true
    $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$', "-windows-amd64.zip"
    $MVN_CMD = "mvnd.cmd"
    break
  }
  default {
    $USE_MVND = $false
    $MVN_CMD = $script -replace '^mvnw', 'mvn'
    break
  }
}

# Apply MVNW_REPOURL and calculate MAVEN_HOME
# Maven home pattern: ~/.m2/wrapper/dists/{apache-maven-<version>,maven-mvnd-<version>-<platform>}/<hash>
if ($env:MVNW_REPOURL) {
  $MVNW_REPO_PATTERN = if ($USE_MVND) { "/maven/mvnd/" } else { "/org/apache/maven/" }
  $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$($distributionUrl -replace '^.*' + [regex]::Escape($MVNW_REPO_PATTERN), '')"
}

$distributionUrlName = $distributionUrl -replace '^.*/', ''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]*$', '' -replace '-bin$', ''

# Determine Maven user home and base directory for wrapper distributions
$MAVEN_USER_HOME = if ($env:MAVEN_USER_HOME) { $env:MAVEN_USER_HOME } else { Join-Path -Path $HOME -ChildPath ".m2" }
$MAVEN_HOME_PARENT = Join-Path -Path $MAVEN_USER_HOME -ChildPath "wrapper/dists/$distributionUrlNameMain"

# Compute hash as hex string for distribution URL
$bytes = [System.Text.Encoding]::UTF8.GetBytes($distributionUrl)
$md5 = [System.Security.Cryptography.MD5]::Create()
$hashBytes = $md5.ComputeHash($bytes)
$MAVEN_HOME_NAME = ($hashBytes | ForEach-Object { $_.ToString("x2") }) -join ''

$MAVEN_HOME = Join-Path -Path $MAVEN_HOME_PARENT -ChildPath $MAVEN_HOME_NAME

if (Test-Path -Path $MAVEN_HOME -PathType Container) {
  Write-Verbose "Found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"
  exit 0
}

if (-not $distributionUrlNameMain -or $distributionUrlName -eq $distributionUrlNameMain) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
  exit 1
}

# Prepare temporary directory for download and extraction
try {
  $TMP_DOWNLOAD_DIR = New-Item -ItemType Directory -Path ([System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), [System.Guid]::NewGuid().ToString()))
} catch {
  Write-Error "Cannot create temporary directory for Maven download."
  exit 1
}

# Setup cleanup on script exit
Register-EngineEvent PowerShell.Exiting -Action {
  if (Test-Path -Path $TMP_DOWNLOAD_DIR.FullName) {
    try {
      Remove-Item -Path $TMP_DOWNLOAD_DIR.FullName -Recurse -Force -ErrorAction SilentlyContinue
    } catch {
      Write-Warning "Cannot remove temporary directory $($TMP_DOWNLOAD_DIR.FullName)"
    }
  }
} | Out-Null

# Ensure Maven home parent directory exists
New-Item -ItemType Directory -Path $MAVEN_HOME_PARENT -Force | Out-Null

# Download and Install Apache Maven
Write-Verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
Write-Verbose "Downloading from: $distributionUrl"
Write-Verbose "Downloading to: $($TMP_DOWNLOAD_DIR.FullName)\$distributionUrlName"

$webclient = New-Object System.Net.WebClient

if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
  $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
}
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

try {
  $destinationPath = Join-Path -Path $TMP_DOWNLOAD_DIR.FullName -ChildPath $distributionUrlName
  $webclient.DownloadFile($distributionUrl, $destinationPath)
} catch {
  Write-Error "Failed to download Maven distribution from $distributionUrl"
  exit 1
}

# If specified, validate the SHA-256 sum of the Maven distribution zip file
$distributionSha256Sum = $wrapperProps.distributionSha256Sum
if ($distributionSha256Sum) {
  if ($USE_MVND) {
    Write-Error "Checksum validation is not supported for maven-mvnd.`nPlease disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
    exit 1
  }
  Import-Module Microsoft.PowerShell.Utility -ErrorAction Stop
  $fileHash = (Get-FileHash -Path $destinationPath -Algorithm SHA256).Hash.ToLower()
  if ($fileHash -ne $distributionSha256Sum.ToLower()) {
    Write-Error "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised.`nIf you updated your Maven version, you need to update the specified distributionSha256Sum property."
    exit 1
  }
}

# Extract and move installation
try {
  Expand-Archive -Path $destinationPath -DestinationPath $TMP_DOWNLOAD_DIR.FullName -Force
} catch {
  Write-Error "Failed to extract Maven distribution archive."
  exit 1
}

$extractedPath = Join-Path -Path $TMP_DOWNLOAD_DIR.FullName -ChildPath $distributionUrlNameMain
$targetPath = Join-Path -Path $MAVEN_HOME_PARENT -ChildPath $MAVEN_HOME_NAME

try {
  Rename-Item -Path $extractedPath -NewName $MAVEN_HOME_NAME -ErrorAction Stop
} catch {
  Write-Error "Failed to rename extracted Maven directory."
  exit 1
}

try {
  Move-Item -Path (Join-Path $TMP_DOWNLOAD_DIR.FullName $MAVEN_HOME_NAME) -Destination $MAVEN_HOME_PARENT -ErrorAction Stop
} catch {
  if (-not (Test-Path -Path $MAVEN_HOME -PathType Container)) {
    Write-Error "Failed to move Maven installation to $MAVEN_HOME_PARENT"
    exit 1
  }
}

# Cleanup temporary directory
try {
  Remove-Item -Path $TMP_DOWNLOAD_DIR.FullName -Recurse -Force -ErrorAction SilentlyContinue
} catch {
  Write-Warning "Cannot remove temporary directory $($TMP_DOWNLOAD_DIR.FullName)"
}

Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"