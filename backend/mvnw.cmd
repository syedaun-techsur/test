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

$ErrorActionPreference = 'Stop'
if ($env:MVNW_VERBOSE -eq 'true') {
  $VerbosePreference = 'Continue'
} else {
  $VerbosePreference = 'SilentlyContinue'
}

$scriptDir = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition

# Load properties file
$propsPath = Join-Path $scriptDir '.mvn\wrapper\maven-wrapper.properties'
if (-not (Test-Path $propsPath)) {
  Write-Error "Cannot find maven-wrapper.properties at $propsPath"
  exit 1
}

$props = Get-Content -Raw $propsPath | ConvertFrom-StringData

$distributionUrl = $props['distributionUrl']
if (-not $distributionUrl) {
  Write-Error "Cannot read distributionUrl property in $propsPath"
  exit 1
}

# Determine if mvnd or standard Maven
switch -wildcard -casesensitive ($distributionUrl -replace '^.*/','') {
  "maven-mvnd-*" {
    $USE_MVND = $true
    $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$', '-windows-amd64.zip'
    $MVN_CMD = 'mvnd.cmd'
    $MVNW_REPO_PATTERN = '/org/apache/maven/'
    break
  }
  default {
    $USE_MVND = $false
    $MVN_CMD = ($MyInvocation.MyCommand.Name) -replace '^mvnw','mvn'
    $MVNW_REPO_PATTERN = '/maven/mvnd/'
    break
  }
}

# Apply MVNW_REPOURL override if set
if ($env:MVNW_REPOURL) {
  # Correcting pattern usage according to mvnd or not
  if ($USE_MVND) {
    $MVNW_REPO_PATTERN = '/maven/mvnd/'
  } else {
    $MVNW_REPO_PATTERN = '/org/apache/maven/'
  }
  $distributionUrl = "$($env:MVNW_REPOURL)$MVNW_REPO_PATTERN$($distributionUrl -replace '^.*' + [Regex]::Escape($MVNW_REPO_PATTERN), '')"
}

$distributionUrlName = $distributionUrl -replace '^.*/',''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]*$','' -replace '-bin$',''

$MAVEN_USER_HOME = if ($env:MAVEN_USER_HOME) { $env:MAVEN_USER_HOME } else { Join-Path $env:USERPROFILE '.m2' }
$MAVEN_HOME_PARENT = Join-Path $MAVEN_USER_HOME "wrapper\dists\$distributionUrlNameMain"

$md5 = [System.Security.Cryptography.MD5]::Create()
$bytes = [Text.Encoding]::UTF8.GetBytes($distributionUrl)
$hashBytes = $md5.ComputeHash($bytes)
$MAVEN_HOME_NAME = ($hashBytes | ForEach-Object { $_.ToString("x2") }) -join ''
$MAVEN_HOME = Join-Path $MAVEN_HOME_PARENT $MAVEN_HOME_NAME

if (Test-Path -Path $MAVEN_HOME -PathType Container) {
  Write-Verbose "Found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"
  exit 0
}

if (-not $distributionUrlNameMain -or ($distributionUrlName -eq $distributionUrlNameMain)) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
  exit 1
}

# Prepare temp directory
$TMP_DOWNLOAD_DIR = New-Item -ItemType Directory -Path ((New-Guid).Guid) -PathType Container -Force -ErrorAction Stop
Register-EngineEvent PowerShell.Exiting -Action {
  if (Test-Path $using:TMP_DOWNLOAD_DIR) {
    try { Remove-Item -Path $using:TMP_DOWNLOAD_DIR -Recurse -Force -ErrorAction SilentlyContinue } catch {}
  }
} | Out-Null

New-Item -ItemType Directory -Path $MAVEN_HOME_PARENT -Force | Out-Null

# Download and Install Apache Maven
Write-Verbose "MAVEN_HOME not found, downloading and installing ..."
Write-Verbose "Downloading from: $distributionUrl"
Write-Verbose "Downloading to: $TMP_DOWNLOAD_DIR\$distributionUrlName"

$webclient = New-Object System.Net.WebClient
if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
  $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
}
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12 -bor [Net.SecurityProtocolType]::Tls11 -bor [Net.SecurityProtocolType]::Tls
try {
  $webclient.DownloadFile($distributionUrl, Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName)
} catch {
  Write-Error "Failed to download Maven distribution from '$distributionUrl': $_"
  exit 1
}

# If specified, validate the SHA-256 sum of the Maven distribution zip file
$distributionSha256Sum = $props['distributionSha256Sum']
if ($distributionSha256Sum) {
  if ($USE_MVND) {
    Write-Error "Checksum validation is not supported for maven-mvnd. Please disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
    exit 1
  }
  Import-Module Microsoft.PowerShell.Utility -ErrorAction Stop
  $fileHash = (Get-FileHash -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName) -Algorithm SHA256).Hash.ToLower()
  if ($fileHash -ne $distributionSha256Sum.ToLower()) {
    Write-Error "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised. If you updated your Maven version, update the distributionSha256Sum."
    exit 1
  }
}

# Unzip and move
try {
  Expand-Archive -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlName) -DestinationPath $TMP_DOWNLOAD_DIR -Force
} catch {
  Write-Error "Failed to unzip the Maven distribution: $_"
  exit 1
}

try {
  Rename-Item -Path (Join-Path $TMP_DOWNLOAD_DIR $distributionUrlNameMain) -NewName $MAVEN_HOME_NAME -Force
  Move-Item -Path (Join-Path $TMP_DOWNLOAD_DIR $MAVEN_HOME_NAME) -Destination $MAVEN_HOME_PARENT -Force
} catch {
  if (-not (Test-Path -Path $MAVEN_HOME -PathType Container)) {
    Write-Error "Failed to move Maven home directory to $MAVEN_HOME_PARENT: $_"
    exit 1
  }
}

# Cleanup tmp directory is handled by registered event

Write-Output "MVN_CMD=$MAVEN_HOME\bin\$MVN_CMD"