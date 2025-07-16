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

# Calculate script directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition

# Read distributionUrl from properties file
$props = Get-Content -Raw "$scriptDir\.mvn\wrapper\maven-wrapper.properties" | ConvertFrom-StringData
$distributionUrl = $props['distributionUrl']
if (-not $distributionUrl) {
  Write-Error "cannot read distributionUrl property in $scriptDir\.mvn\wrapper\maven-wrapper.properties"
  exit 1
}

# Determine if mvnd is used
if ($distributionUrl -like "*maven-mvnd-*") {
  $USE_MVND = $true
  # Adjust for Windows platform
  $distributionUrl = $distributionUrl -replace '-bin\.[^.]+$', "-windows-amd64.zip"
  $MVN_CMD = "mvnd.cmd"
  $repoPattern = "/maven/mvnd/"
} else {
  $USE_MVND = $false
  $MVN_CMD = (Split-Path -Leaf $MyInvocation.MyCommand.Name) -replace '^mvnw','mvn'
  $repoPattern = "/org/apache/maven/"
}

# Apply MVNW_REPOURL if set and construct Maven home path
if ($env:MVNW_REPOURL) {
  # Replace repo pattern part of URL with MVNW_REPOURL base if present
  $distributionUrl = $env:MVNW_REPOURL + $repoPattern + ($distributionUrl -replace "^.*$repoPattern",'')
}

$distributionUrlName = [IO.Path]::GetFileName($distributionUrl)
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]+$','' -replace '-bin$',''

$MavenUserHome = if ($env:MAVEN_USER_HOME) { $env:MAVEN_USER_HOME } else { Join-Path $HOME ".m2" }
$MavenHomeParent = Join-Path -Path $MavenUserHome -ChildPath "wrapper/dists/$distributionUrlNameMain"

# Create hash for path
$md5 = [System.Security.Cryptography.MD5]::Create()
$bytes = [System.Text.Encoding]::UTF8.GetBytes($distributionUrl)
$hashBytes = $md5.ComputeHash($bytes)
$MavenHomeName = -join ($hashBytes | ForEach-Object { $_.ToString("x2") })

$MAVEN_HOME = Join-Path -Path $MavenHomeParent -ChildPath $MavenHomeName

if (Test-Path -Path $MAVEN_HOME -PathType Container) {
  Write-Verbose "Found existing MAVEN_HOME at $MAVEN_HOME"
  Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
  exit 0
}

if (-not $distributionUrlNameMain -or $distributionUrlName -eq $distributionUrlNameMain) {
  Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
  exit 1
}

# Prepare temporary directory for download
$TMP_DOWNLOAD_DIR = New-Item -ItemType Directory -Path ([System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), [System.Guid]::NewGuid().ToString())) -Force

# Ensure cleanup on exit
try {
  New-Item -ItemType Directory -Path $MavenHomeParent -Force | Out-Null

  Write-Verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
  Write-Verbose "Downloading from: $distributionUrl"
  Write-Verbose "Downloading to: $TMP_DOWNLOAD_DIR\$distributionUrlName"

  $webclient = New-Object System.Net.WebClient
  if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
    $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
  }
  [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
  $webclient.DownloadFile($distributionUrl, "$TMP_DOWNLOAD_DIR\$distributionUrlName")

  # Validate SHA-256 sum if provided
  $distributionSha256Sum = $props['distributionSha256Sum']
  if ($distributionSha256Sum) {
    if ($USE_MVND) {
      Write-Error "Checksum validation is not supported for maven-mvnd.`nPlease disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
      exit 1
    }
    Import-Module Microsoft.PowerShell.Utility -ErrorAction Stop
    $fileHash = (Get-FileHash "$TMP_DOWNLOAD_DIR\$distributionUrlName" -Algorithm SHA256).Hash.ToLower()
    if ($fileHash -ne $distributionSha256Sum.ToLower()) {
      Write-Error "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised.`nIf you updated your Maven version, you need to update the specified distributionSha256Sum property."
      exit 1
    }
  }

  # Unzip and install Maven
  Expand-Archive -Path "$TMP_DOWNLOAD_DIR\$distributionUrlName" -DestinationPath "$TMP_DOWNLOAD_DIR" -Force

  Rename-Item -Path "$TMP_DOWNLOAD_DIR\$distributionUrlNameMain" -NewName $MavenHomeName -ErrorAction Stop

  $destinationPath = $MavenHomeParent
  $sourcePath = Join-Path -Path $TMP_DOWNLOAD_DIR -ChildPath $MavenHomeName

  # Move installation folder, handle existing installation gracefully
  if (-not (Test-Path -Path $MAVEN_HOME -PathType Container)) {
    Move-Item -Path $sourcePath -Destination $destinationPath -Force
  } else {
    Write-Verbose "$MAVEN_HOME already exists, skipping move."
  }

} finally {
  if (Test-Path -Path $TMP_DOWNLOAD_DIR) {
    try {
      Remove-Item -Path $TMP_DOWNLOAD_DIR -Recurse -Force
    } catch {
      Write-Warning "Cannot remove temporary directory $TMP_DOWNLOAD_DIR"
    }
  }
}

Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"