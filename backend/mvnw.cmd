<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with additional information
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

# Calculate distributionUrl from maven-wrapper.properties
$distributionPropertiesPath = Join-Path -Path $PSScriptRoot -ChildPath ".mvn/wrapper/maven-wrapper.properties"
if (-not (Test-Path -Path $distributionPropertiesPath)) {
  Write-Error "Cannot find $distributionPropertiesPath"
  exit 1
}

$props = Get-Content -Raw $distributionPropertiesPath | ConvertFrom-StringData
$distributionUrl = $props.distributionUrl
$distributionSha256Sum = $props.distributionSha256Sum

if (-not $distributionUrl) {
  Write-Error "Cannot read distributionUrl property in $distributionPropertiesPath"
  exit 1
}

# Determine if using mvnd wrapper and set MVN_CMD and URL accordingly
switch -wildcard -casesensitive ($distributionUrl -replace '^.*/','') {
  "maven-mvnd-*" {
    $USE_MVND = $true
    $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$', '-windows-amd64.zip'
    $MVN_CMD = "mvnd.cmd"
  }
  default {
    $USE_MVND = $false
    # Fallback: replace script filename running with mvnw to mvn, robust to any renaming
    $MVN_CMD = if ($MyInvocation.MyCommand.Name -match '^mvnw(.cmd)?$') { "mvn.cmd" } else { $MyInvocation.MyCommand.Name -replace '^mvnw', 'mvn' }
  }
}

# Apply MVNW_REPOURL if set and calculate MAVEN_HOME
if ($env:MVNW_REPOURL) {
  $MVNW_REPO_PATTERN = if ($USE_MVND) { "/org/apache/maven/" } else { "/maven/mvnd/" }
  $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$($distributionUrl -replace '^.*' + [regex]::Escape($MVNW_REPO_PATTERN), '')"
}

$distributionUrlName = $distributionUrl -replace '^.*/',''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]*$','' -replace '-bin$',''

# Determine local Maven wrapper directory root
$MAVEN_USER_HOME = $env:MAVEN_USER_HOME
if (-not $MAVEN_USER_HOME) {
  $MAVEN_USER_HOME = Join-Path $HOME ".m2"
}
$MAVEN_HOME_PARENT = Join-Path -Path $MAVEN_USER_HOME -ChildPath ("wrapper/dists/$distributionUrlNameMain")

# Hash distributionUrl to uniquely identify Maven home folder
$hashProvider = [System.Security.Cryptography.MD5]::Create()
$bytes = [System.Text.Encoding]::UTF8.GetBytes($distributionUrl)
$hashBytes = $hashProvider.ComputeHash($bytes)
$MAVEN_HOME_NAME = ($hashBytes | ForEach-Object { $_.ToString("x2") }) -join ''
$MAVEN_HOME = Join-Path -Path $MAVEN_HOME_PARENT -ChildPath $MAVEN_HOME_NAME

if (Test-Path -Path $MAVEN_HOME -PathType Container) {
  Write-Verbose "Found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
  exit 0
}

if (-not $distributionUrlNameMain -or $distributionUrlName -eq $distributionUrlNameMain) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
  exit 1
}

# Prepare temp directory for download
$TMP_DOWNLOAD_DIR = New-Item -ItemType Directory -Path ([System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), [System.Guid]::NewGuid().ToString())) -Force

# Register cleanup
$cleanupScript = {
  param($path)
  if (Test-Path -Path $path) {
    try {
      Remove-Item -Path $path -Recurse -Force -ErrorAction SilentlyContinue
    } catch {
      Write-Warning "Cannot remove temporary directory $path"
    }
  }
}
Register-EngineEvent PowerShell.Exiting -Action { & $cleanupScript $TMP_DOWNLOAD_DIR }

# Ensure MAVEN_HOME_PARENT exists
New-Item -ItemType Directory -Path $MAVEN_HOME_PARENT -Force | Out-Null

Write-Verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
Write-Verbose "Downloading from: $distributionUrl"
Write-Verbose "Downloading to: $TMP_DOWNLOAD_DIR/$distributionUrlName"

$webclient = New-Object System.Net.WebClient
if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
  $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
}
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
try {
  $webclient.DownloadFile($distributionUrl, Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName)
} catch {
  Write-Error "Failed to download Maven distribution from $distributionUrl. $_"
  exit 1
}

if ($distributionSha256Sum) {
  if ($USE_MVND) {
    Write-Error "Checksum validation is not supported for maven-mvnd.`nPlease disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
    exit 1
  }
  Import-Module Microsoft.PowerShell.Utility -Force
  $downloadedHash = (Get-FileHash -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName) -Algorithm SHA256).Hash.ToLower()
  if ($downloadedHash -ne $distributionSha256Sum.ToLower()) {
    Write-Error "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised.`nIf you updated your Maven version, you need to update the specified distributionSha256Sum property."
    exit 1
  }
}

# Unzip Maven distribution
try {
  Expand-Archive -LiteralPath (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName) -DestinationPath $TMP_DOWNLOAD_DIR -Force
} catch {
  Write-Error "Failed to extract Maven distribution archive: $_"
  exit 1
}

# Rename extracted folder to hashed folder name
try {
  Rename-Item -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlNameMain) -NewName $MAVEN_HOME_NAME
} catch {
  Write-Error "Failed to rename extracted directory: $_"
  exit 1
}

# Move extracted and renamed directory to Maven home parent
try {
  Move-Item -Path (Join-Path $TMP_DOWNLOAD_DIR $MAVEN_HOME_NAME) -Destination $MAVEN_HOME_PARENT -Force
} catch {
  if (-not (Test-Path -Path $MAVEN_HOME -PathType Container)) {
    Write-Error "Failed to move Maven home directory to $MAVEN_HOME_PARENT. $_"
    exit 1
  }
}

# Cleanup temp directory
& $cleanupScript $TMP_DOWNLOAD_DIR

Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"