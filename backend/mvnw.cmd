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
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition

# Read maven-wrapper.properties and get distributionUrl and optional sha256 sum
$wrapperProps = @{}
try {
  $wrapperProps = Get-Content -Raw "$scriptDir\.mvn\wrapper\maven-wrapper.properties" | ConvertFrom-StringData
} catch {
  Write-Error "Cannot read maven-wrapper.properties from $scriptDir\.mvn\wrapper"
  exit 1
}

$distributionUrl = $wrapperProps['distributionUrl']
if (-not $distributionUrl) {
  Write-Error "Missing 'distributionUrl' property in maven-wrapper.properties"
  exit 1
}

switch -Wildcard -CaseSensitive ($distributionUrl -replace '^.*/','') {
  "maven-mvnd-*" {
    $USE_MVND = $true
    $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$', "-windows-amd64.zip"
    $MVN_CMD = "mvnd.cmd"
  }
  default {
    $USE_MVND = $false
    $MVN_CMD = $script -replace '^mvnw','mvn'
  }
}

# Apply MVNW_REPOURL if set; note: pattern logic corrected to match respective case
if ($env:MVNW_REPOURL) {
  $MVNW_REPO_PATTERN = if ($USE_MVND) { "/maven/mvnd/" } else { "/org/apache/maven/" }
  $tailPart = $distributionUrl -replace '^.*' + [regex]::Escape($MVNW_REPO_PATTERN), ''
  $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$tailPart"
}

$distributionUrlName = $distributionUrl -replace '^.*/',''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]+$','' -replace '-bin$',''

# Define maven user home directory for local storage
if ($env:MAVEN_USER_HOME) {
  $MAVEN_HOME_PARENT = "$env:MAVEN_USER_HOME/wrapper/dists/$distributionUrlNameMain"
} else {
  $MAVEN_HOME_PARENT = "$HOME/.m2/wrapper/dists/$distributionUrlNameMain"
}

# Compute MD5 hash of distributionUrl for unique directory naming
$MD5 = [System.Security.Cryptography.MD5]::Create()
$Bytes = [System.Text.Encoding]::UTF8.GetBytes($distributionUrl)
$HashBytes = $MD5.ComputeHash($Bytes)
$MAVEN_HOME_NAME = ($HashBytes | ForEach-Object { $_.ToString("x2") }) -join ''
$MAVEN_HOME = Join-Path $MAVEN_HOME_PARENT $MAVEN_HOME_NAME

# If already installed, return mvn command path
if (Test-Path -Path $MAVEN_HOME -PathType Container) {
  Write-Verbose "Found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
  exit 0
}

if (-not $distributionUrlNameMain -or $distributionUrlName -eq $distributionUrlNameMain) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
  exit 1
}

# Create temporary directory properly
$TMP_DOWNLOAD_DIR = Join-Path ([System.IO.Path]::GetTempPath()) ([System.IO.Path]::GetRandomFileName())
try {
  New-Item -ItemType Directory -Path $TMP_DOWNLOAD_DIR -ErrorAction Stop | Out-Null
} catch {
  Write-Error "Cannot create temporary directory at $TMP_DOWNLOAD_DIR"
  exit 1
}

# Ensure temporary directory cleanup on script exit
$script:CleanupTempDir = {
  if (Test-Path -Path $TMP_DOWNLOAD_DIR) {
    try {
      Remove-Item -Path $TMP_DOWNLOAD_DIR -Recurse -Force -ErrorAction SilentlyContinue
    } catch {
      Write-Warning "Cannot remove temporary directory $TMP_DOWNLOAD_DIR"
    }
  }
}
Register-EngineEvent PowerShell.Exiting -Action $script:CleanupTempDir | Out-Null

# Create maven home parent directory
try {
  New-Item -ItemType Directory -Path $MAVEN_HOME_PARENT -Force | Out-Null
} catch {
  Write-Error "Failed to create Maven home parent directory $MAVEN_HOME_PARENT"
  & $script:CleanupTempDir
  exit 1
}

Write-Verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
Write-Verbose "Downloading from: $distributionUrl"
Write-Verbose "Downloading to: $TMP_DOWNLOAD_DIR\$distributionUrlName"

$webclient = New-Object System.Net.WebClient
if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
  $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
}
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
try {
  $webclient.DownloadFile($distributionUrl, Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName)
} catch {
  Write-Error "Failed to download Maven distribution from $distributionUrl"
  & $script:CleanupTempDir
  exit 1
}

# Validate SHA-256 checksum if specified and not mvnd usage
$distributionSha256Sum = $wrapperProps['distributionSha256Sum']
if ($distributionSha256Sum) {
  if ($USE_MVND) {
    Write-Error "Checksum validation is not supported for maven-mvnd.`nPlease disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
    & $script:CleanupTempDir
    exit 1
  }
  Import-Module Microsoft.PowerShell.Utility -ErrorAction SilentlyContinue
  $fileHash = (Get-FileHash -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName) -Algorithm SHA256).Hash.ToLower()
  if ($fileHash -ne $distributionSha256Sum.ToLower()) {
    Write-Error "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised.`nIf you updated your Maven version, you need to update the specified distributionSha256Sum property."
    & $script:CleanupTempDir
    exit 1
  }
}

# Extract archive and move directory to MAVEN_HOME
try {
  Expand-Archive -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName) -DestinationPath $TMP_DOWNLOAD_DIR -Force -ErrorAction Stop
} catch {
  Write-Error "Failed to extract Maven distribution archive"
  & $script:CleanupTempDir
  exit 1
}

# Rename extracted folder to hash name for consistent directory
$extractedPath = Join-Path $TMP_DOWNLOAD_DIR $distributionUrlNameMain
if (Test-Path -Path $extractedPath) {
  try {
    Rename-Item -Path $extractedPath -NewName $MAVEN_HOME_NAME -ErrorAction Stop
  } catch {
    Write-Error "Failed to rename extracted folder to $MAVEN_HOME_NAME"
    & $script:CleanupTempDir
    exit 1
  }
} else {
  Write-Error "Expected extracted folder '$distributionUrlNameMain' not found"
  & $script:CleanupTempDir
  exit 1
}

try {
  Move-Item -Path (Join-Path $TMP_DOWNLOAD_DIR $MAVEN_HOME_NAME) -Destination $MAVEN_HOME_PARENT -ErrorAction Stop
} catch {
  if (-not (Test-Path -Path $MAVEN_HOME -PathType Container)) {
    Write-Error "Failed to move Maven distribution to $MAVEN_HOME_PARENT"
    & $script:CleanupTempDir
    exit 1
  }
}

# Cleanup temp directory explicitly
& $script:CleanupTempDir

Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"