@echo off
@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with Windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Determine the directory of the script
set "DIRNAME=%~dp0"
if "%DIRNAME%"=="" set "DIRNAME=."

@rem Resolve any "." and ".." in APP_HOME to make it shorter
for %%i in ("%DIRNAME%") do set "APP_HOME=%%~fi"

@rem Set the base name of the application
set "APP_BASE_NAME=%~n0"

@rem Set default JVM options
set "DEFAULT_JVM_OPTS=-Xmx64m -Xms64m"

@rem Check if JAVA_HOME is set
if defined JAVA_HOME goto findJavaFromJavaHome

@rem If JAVA_HOME is not set, attempt to find java.exe in the PATH
set "JAVA_EXE=java.exe"
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

@rem If java.exe is not found, display an error message
echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
goto fail

:findJavaFromJavaHome
@rem Remove any quotes from JAVA_HOME
set "JAVA_HOME=%JAVA_HOME:"=%"
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"

@rem Check if java.exe exists at the specified JAVA_HOME
if exist "%JAVA_EXE%" goto execute

@rem If java.exe is not found at JAVA_HOME, display an error message
echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
goto fail

:execute
@rem Set up the classpath for the Gradle Wrapper
set "CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar"

@rem Execute the Gradle Wrapper
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

:end
@rem End local scope for the variables with Windows NT shell
if "%OS%"=="Windows_NT" endlocal

:fail
@rem Set variable GRADLE_EXIT_CONSOLE if you need the script's return code instead of
@rem the command prompt's return code when the batch file is called from a command line.
if not "" == "%GRADLE_EXIT_CONSOLE%" exit /b 1
exit /b 1