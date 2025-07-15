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
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0'; $script='%__MVNW_ARG0_NAME__%'; icm -ScriptBlock ([Scriptblock]::Create((Get-Content -Raw '%~f0' -Encoding UTF8))) -NoNewScope}"`) DO @(
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

# calculate distributionUrl, requires .mvn/wrapper/maven-wrapper.properties
$propertiesPath = Join-Path $scriptDir ".mvn/wrapper/maven-wrapper.properties"
if (-Not (Test-Path $propertiesPath)) {
  Write-Error "Cannot find maven-wrapper.properties at $propertiesPath"
  exit 1
}
$props = Get-Content -Raw $propertiesPath -Encoding UTF8 | ConvertFrom-StringData

$distributionUrl = $props.distributionUrl
if (-not $distributionUrl) {
  Write-Error "Cannot read distributionUrl property in $propertiesPath"
  exit 1
}

# Determine mvn or mvnd usage and platform-specific distribution URL adjustment
if ($distributionUrl -match 'maven-mvnd-.*bin\..*$') {
  $USE_MVND = $true
  # Adjust mvnd distribution URL for windows-amd64 platform
  $distributionUrl = $distributionUrl -replace '-bin\.[^.]+$', '-windows-amd64.zip'
  $MVN_CMD = "mvnd.cmd"
  $MVNW_REPO_PATTERN = "/maven/mvnd/"
} else {
  $USE_MVND = $false
  # For standard maven, mvn command named by replacing mvnw prefix with mvn
  $MVN_CMD = ($MyInvocation.MyCommand.Name) -replace '^mvnw','mvn'
  $MVNW_REPO_PATTERN = "/org/apache/maven/"
}

# Apply MVNW_REPOURL if set, adjust distribution URL accordingly
if ($env:MVNW_REPOURL) {
  $urlSuffix = $distributionUrl -replace '^.*' + [regex]::Escape($MVNW_REPO_PATTERN), ''
  $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$urlSuffix"
}

$distributionUrlName = ($distributionUrl -split '/')[-1]
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]+$','' -replace '-bin$',''

$MAVEN_USER_HOME = $env:MAVEN_USER_HOME
if (-not $MAVEN_USER_HOME -or [string]::IsNullOrWhiteSpace($MAVEN_USER_HOME)) {
  $MAVEN_USER_HOME = Join-Path $env:USERPROFILE ".m2"
}

$MAVEN_HOME_PARENT = Join-Path -Path $MAVEN_USER_HOME -ChildPath "wrapper/dists/$distributionUrlNameMain"

$md5 = [System.Security.Cryptography.MD5]::Create()
$bytes = [System.Text.Encoding]::Unicode.GetBytes($distributionUrl)
$hash = $md5.ComputeHash($bytes)
$MAVEN_HOME_NAME = ($hash | ForEach-Object { $_.ToString("x2") }) -join ''
$MAVEN_HOME = Join-Path -Path $MAVEN_HOME_PARENT -ChildPath $MAVEN_HOME_NAME

if (Test-Path -Path $MAVEN_HOME -PathType Container) {
  Write-Verbose "found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"
  exit 0
}

if (-not $distributionUrlNameMain -or $distributionUrlName -eq $distributionUrlNameMain) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
  exit 1
}

# Prepare temp directory safely
$TMP_DOWNLOAD_DIR = New-Item -ItemType Directory -Path ([System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), [System.Guid]::NewGuid().ToString())) -Force

# Setup cleanup on script exit or error
$cleanup = {
  if (Test-Path $using:TMP_DOWNLOAD_DIR) {
    try {
      Remove-Item -Path $using:TMP_DOWNLOAD_DIR -Recurse -Force -ErrorAction SilentlyContinue
    } catch {
      Write-Warning "Cannot remove temporary directory $using:TMP_DOWNLOAD_DIR"
    }
  }
}
Register-EngineEvent PowerShell.Exiting -Action $cleanup | Out-Null

# Ensure Maven home parent directory exists
New-Item -ItemType Directory -Path $MAVEN_HOME_PARENT -Force | Out-Null

# Download and install Maven or MVND distribution
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
  exit 1
}

# Validate SHA-256 checksum if specified
$distributionSha256Sum = $props.distributionSha256Sum
if ($distributionSha256Sum) {
  if ($USE_MVND) {
    Write-Error "Checksum validation is not supported for maven-mvnd.`nPlease disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
    exit 1
  }
  Import-Module Microsoft.PowerShell.Utility -ErrorAction Stop
  $actualHash = (Get-FileHash -Algorithm SHA256 -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName)).Hash.ToLower()
  if ($actualHash -ne $distributionSha256Sum.ToLower()) {
    Write-Error "Error: Failed to validate Maven distribution SHA-256. Your distribution might be compromised. If updated version, update distributionSha256Sum property."
    exit 1
  }
}

# Extract the downloaded archive
Expand-Archive -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName) -DestinationPath $TMP_DOWNLOAD_DIR -Force

# Rename extracted folder to hash name
$extractedPath = Join-Path $TMP_DOWNLOAD_DIR $distributionUrlNameMain
$hashedPath = Join-Path $TMP_DOWNLOAD_DIR $MAVEN_HOME_NAME
Rename-Item -Path $extractedPath -NewName $MAVEN_HOME_NAME -Force

# Move to Maven home parent directory
try {
  Move-Item -Path $hashedPath -Destination $MAVEN_HOME_PARENT -Force
} catch {
  if (-not (Test-Path -Path $MAVEN_HOME -PathType Container)) {
    Write-Error "Failed to move Maven home directory to final destination."
    exit 1
  }
} finally {
  try {
    Remove-Item -Path $TMP_DOWNLOAD_DIR -Recurse -Force -ErrorAction SilentlyContinue
  } catch {
    Write-Warning "Cannot remove temporary directory $TMP_DOWNLOAD_DIR"
  }
}

Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"
exit 0