@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM M2_HOME - location of maven2's installed home dir
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo off
@REM Set command window title to script name
title %~nx0
@REM Enable echoing by setting MAVEN_BATCH_ECHO to 'on'
@if "%MAVEN_BATCH_ECHO%" == "on" echo %MAVEN_BATCH_ECHO%

@REM Set HOME equivalent if not set
if "%HOME%"=="" (
    set "HOME=%HOMEDRIVE%%HOMEPATH%"
)

@REM Execute user defined pre script
if not "%MAVEN_SKIP_RC%"=="" goto skipRcPre
if exist "%USERPROFILE%\mavenrc_pre.bat" call "%USERPROFILE%\mavenrc_pre.bat" %*
if exist "%USERPROFILE%\mavenrc_pre.cmd" call "%USERPROFILE%\mavenrc_pre.cmd" %*
:skipRcPre

@setlocal

set "ERROR_CODE=0"

@REM To isolate internal variables from possible post scripts, we use another setlocal
@setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%"=="" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" (
    goto init
)

echo.
echo Error: JAVA_HOME is set to an invalid directory. 1>&2
echo JAVA_HOME = "%JAVA_HOME%" 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2
echo.
goto error

:init
@REM Isolate environment variables after JAVA_HOME check
@setlocal

@REM Decide how to startup depending on the version of windows

@REM -- 4NT shell
if "%@eval[2+2]"=="4" goto 4nt_args

@REM -- Regular WNT shell
set "MAVEN_CMD_LINE_ARGS=%*"
goto endInit

:4nt_args
@REM -- 4NT shell args
set "MAVEN_CMD_LINE_ARGS=%$"
goto endInit

:endInit
@REM End local scope for the windows with 4NT shell
if "%OS%"=="Windows_NT" endlocal

@REM Set APP_HOME for locating wrapper jar - fallback to script directory if not defined
if "%APP_HOME%"=="" (
    set "APP_HOME=%~dp0"
)

@REM Trim trailing backslash if any
if "%APP_HOME:~-1%"=="\" set "APP_HOME=%APP_HOME:~0,-1%"

@REM Now execute the command
"%JAVA_HOME%\bin\java.exe" ^
  %MAVEN_OPTS% ^
  %MAVEN_DEBUG_OPTS% ^
  -classpath "%APP_HOME%\mvnw\maven-wrapper.jar" ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  org.apache.maven.wrapper.MavenWrapperMain %MAVEN_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set "ERROR_CODE=1"

:end
@endlocal
@endlocal

if not "%MAVEN_SKIP_RC%"=="" goto skipRcPost
@REM Execute user defined post script
if exist "%USERPROFILE%\mavenrc_post.bat" call "%USERPROFILE%\mavenrc_post.bat"
if exist "%USERPROFILE%\mavenrc_post.cmd" call "%USERPROFILE%\mavenrc_post.cmd"
:skipRcPost

@REM Pause the script if MAVEN_BATCH_PAUSE is 'on'
if "%MAVEN_BATCH_PAUSE%"=="on" pause

if "%MAVEN_TERMINATE_CMD%"=="on" exit /b %ERROR_CODE%

cmd /C exit /B %ERROR_CODE%