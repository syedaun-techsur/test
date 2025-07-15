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

# Get script directory to read wrapper properties
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition

# Read distributionUrl and optional distributionSha256Sum from properties file, trim whitespace
$wrapperProps = Get-Content -Raw "$scriptDir\.mvn\wrapper\maven-wrapper.properties" | ConvertFrom-StringData
$distributionUrl = $wrapperProps['distributionUrl'] -replace '^\s+|\s+$',''
if (-not $distributionUrl) {
  Write-Error "Cannot read distributionUrl property in $scriptDir\.mvn\wrapper\maven-wrapper.properties"
  exit 1
}

$distributionSha256Sum = $wrapperProps['distributionSha256Sum'] -replace '^\s+|\s+$',''

# Detect if using maven-mvnd distribution
switch -wildcard -casesensitive ($distributionUrl -replace '^.*/','') {
  "maven-mvnd-*" {
    $USE_MVND = $true
    $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$', "-windows-amd64.zip"
    $MVN_CMD = "mvnd.cmd"
  }
  default {
    $USE_MVND = $false
    $MVN_CMD = $script -replace '^mvnw', 'mvn'
  }
}

# Apply MVNW_REPOURL if set, with correct repo pattern
if ($env:MVNW_REPOURL) {
  $MVNW_REPO_PATTERN = if ($USE_MVND) { "/maven/mvnd/" } else { "/org/apache/maven/" }
  $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$($distributionUrl -replace ".*$MVNW_REPO_PATTERN",'')"
}

# Extract distribution file names
$distributionUrlName = $distributionUrl -replace '^.*/',''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]+$','' -replace '-bin$',''

# Calculate Maven wrapper home directories
$MAVEN_USER_HOME = if ($env:MAVEN_USER_HOME) { $env:MAVEN_USER_HOME } else { $HOME }

$MAVEN_HOME_PARENT = Join-Path $MAVEN_USER_HOME "wrapper\dists\$distributionUrlNameMain"

# Calculate MD5 hash for distributionUrl as folder name
$md5 = [System.Security.Cryptography.MD5]::Create()
$bytes = [Text.Encoding]::UTF8.GetBytes($distributionUrl)
$hashBytes = $md5.ComputeHash($bytes)
$MAVEN_HOME_NAME = ($hashBytes | ForEach-Object { $_.ToString("x2") }) -join ''
$md5.Dispose()

$MAVEN_HOME = Join-Path $MAVEN_HOME_PARENT $MAVEN_HOME_NAME

if (Test-Path -Path $MAVEN_HOME -PathType Container) {
  Write-Verbose "Found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"
  exit 0
}

if (-not $distributionUrlNameMain -or $distributionUrlName -eq $distributionUrlNameMain) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
  exit 1
}

# Prepare temporary download directory safely
try {
  $TMP_DOWNLOAD_DIR = New-Item -ItemType Directory -Path (Join-Path $env:TEMP "mvnw_tmp_$(New-Guid)") -ErrorAction Stop
} catch {
  Write-Error "Cannot create temporary directory for download"
  exit 1
}

# Ensure cleanup on exit
trap {
  if (Test-Path $TMP_DOWNLOAD_DIR.FullName) {
    try { Remove-Item -Path $TMP_DOWNLOAD_DIR.FullName -Recurse -Force -ErrorAction Stop } catch { Write-Warning "Cannot remove temporary directory" }
  }
}

# Ensure Maven home parent directory exists
if (-not (Test-Path $MAVEN_HOME_PARENT)) {
  New-Item -ItemType Directory -Path $MAVEN_HOME_PARENT -Force | Out-Null
}

# Download and install Maven
Write-Verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
Write-Verbose "Downloading from: $distributionUrl"
Write-Verbose "Downloading to: $($TMP_DOWNLOAD_DIR.FullName)\$distributionUrlName"

$webclient = New-Object System.Net.WebClient
if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
  $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
}
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
try {
  $webclient.DownloadFile($distributionUrl, "$($TMP_DOWNLOAD_DIR.FullName)\$distributionUrlName")
} catch {
  Write-Error "Failed to download Maven distribution from $distributionUrl"
  exit 1
}

# Validate SHA-256 sum if specified
if ($distributionSha256Sum) {
  if ($USE_MVND) {
    Write-Error "Checksum validation is not supported for maven-mvnd.`nPlease disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
    exit 1
  }
  Import-Module $PSHOME\Modules\Microsoft.PowerShell.Utility -Function Get-FileHash
  $fileHash = (Get-FileHash "$($TMP_DOWNLOAD_DIR.FullName)\$distributionUrlName" -Algorithm SHA256).Hash.ToLower()
  if ($fileHash -ne $distributionSha256Sum.ToLower()) {
    Write-Error "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised. If you updated your Maven version, update the distributionSha256Sum property."
    exit 1
  }
}

# Extract archive
try {
  Expand-Archive -Path "$($TMP_DOWNLOAD_DIR.FullName)\$distributionUrlName" -DestinationPath $TMP_DOWNLOAD_DIR.FullName -Force
} catch {
  Write-Error "Failed to extract Maven distribution archive."
  exit 1
}

# Rename extracted folder to hash name
try {
  Rename-Item -Path (Join-Path $TMP_DOWNLOAD_DIR.FullName $distributionUrlNameMain) -NewName $MAVEN_HOME_NAME -ErrorAction Stop
} catch {
  Write-Error "Failed to rename extracted Maven folder."
  exit 1
}

# Move extracted and renamed Maven folder into final location
try {
  Move-Item -Path (Join-Path $TMP_DOWNLOAD_DIR.FullName $MAVEN_HOME_NAME) -Destination $MAVEN_HOME_PARENT -ErrorAction Stop
} catch {
  if (-not (Test-Path $MAVEN_HOME)) {
    Write-Error "Failed to move Maven home directory to $MAVEN_HOME_PARENT"
    exit 1
  }
}

# Cleanup temporary directory
try {
  Remove-Item -Path $TMP_DOWNLOAD_DIR.FullName -Recurse -Force -ErrorAction Stop
} catch {
  Write-Warning "Cannot remove temporary directory $($TMP_DOWNLOAD_DIR.FullName)"
}

Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"