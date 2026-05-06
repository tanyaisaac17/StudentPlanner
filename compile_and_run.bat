@echo off
REM ================================================================
REM  Student Study Planner - Compile & Run (Windows)
REM ================================================================
REM  Uses the bundled MySQL Connector/J jar in the project root for RUNTIME:
REM    mysql-connector-j-9.6.0.jar
REM
REM  Note: The JDBC jar is not required for compilation (this project loads it
REM  via Class.forName at runtime). Keeping it out of javac's classpath avoids
REM  rare Windows "AccessDeniedException" issues seen with some JDK versions.
REM
REM  If your DB password is not empty, you can set:
REM    set STUDYPLANNER_DB_PASSWORD=your_password
REM ================================================================

setlocal enabledelayedexpansion

set PROJECT_ROOT=%~dp0
set JDBC_JAR=%PROJECT_ROOT%mysql-connector-j-9.6.0.jar

echo.
echo ========================================
echo Student Study Planner - Build + Run
echo ========================================
echo.

if not exist "%JDBC_JAR%" (
  echo ERROR: MySQL JDBC jar not found:
  echo   %JDBC_JAR%
  echo.
  echo Fix: place mysql-connector-j-9.6.0.jar in the project root.
  echo.
  pause
  exit /b 1
)

cd /d "%PROJECT_ROOT%"

echo Compiling all Java sources...
echo.

REM Build a source list for javac
set SOURCES_FILE=%PROJECT_ROOT%sources.txt
if exist "%SOURCES_FILE%" del "%SOURCES_FILE%"
for /r "src\com\studyplanner" %%F in (*.java) do echo %%F>>"%SOURCES_FILE%"

REM Compile everything (connector jar is only required at runtime)
javac -d . @"%SOURCES_FILE%"
if errorlevel 1 (
  echo.
  echo ERROR: Compilation failed.
  pause
  exit /b 1
)

del "%SOURCES_FILE%" >nul 2>&1

echo.
echo ✅ Compilation successful.
echo.

set /p RUN_NOW="Run the application now? (Y/N): "
if /i "%RUN_NOW%"=="Y" (
  echo.
  echo Starting app...
  echo.
  java -cp "%JDBC_JAR%;." com.studyplanner.main.StudyPlannerApp
) else (
  echo.
  echo To run later:
  echo   java -cp "%JDBC_JAR%;." com.studyplanner.main.StudyPlannerApp
  echo.
)

pause
