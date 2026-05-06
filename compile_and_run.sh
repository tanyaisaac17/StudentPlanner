#!/bin/bash

# ==================================================================
# Student Study Planner - One-Click Compilation & Run Script
# For macOS and Linux
# ==================================================================
#
# This script will:
# 1. Check if MySQL JDBC JAR exists
# 2. Compile all Java files in correct order
# 3. Run the application
#
# SETUP REQUIRED:
# - Download mysql-connector-java-8.0.33.jar
# - Save to: ~/Downloads/
# - Create study_planner database (see SETUP_GUIDE.md
#
# Usage: chmod +x compile_and_run.sh && ./compile_and_run.sh
# ==================================================================

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Paths
JDBC_JAR="$HOME/Downloads/mysql-connector-java-8.0.33.jar"
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CLASSPATH="$JDBC_JAR:."

echo
echo "╔════════════════════════════════════════════════════════════╗"
echo "║  📚 Student Study Planner - Compilation Script             ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo

# Check if JDBC JAR exists
if [ ! -f "$JDBC_JAR" ]; then
    echo -e "${RED}❌ ERROR: MySQL JDBC JAR not found!${NC}"
    echo
    echo "Expected location: $JDBC_JAR"
    echo
    echo "SOLUTION:"
    echo "1. Download mysql-connector-java-8.0.33.jar from:"
    echo "   https://dev.mysql.com/downloads/connector/j/"
    echo "2. Save it to: $HOME/Downloads/"
    echo "3. Run this script again"
    echo
    exit 1
fi

echo -e "${GREEN}✓ MySQL JDBC Driver found: $JDBC_JAR${NC}"
echo

# Check if source files exist
if [ ! -f "$PROJECT_ROOT/src/com/studyplanner/main/StudyPlannerApp.java" ]; then
    echo -e "${RED}❌ ERROR: Source files not found!${NC}"
    echo "Current directory: $PROJECT_ROOT"
    echo
    echo "Please run this script from the project root directory."
    echo
    exit 1
fi

echo -e "${GREEN}✓ Source files found${NC}"
echo

# Change to project directory
cd "$PROJECT_ROOT"

# Export CLASSPATH
export CLASSPATH="$JDBC_JAR:."

echo "════════════════════════════════════════════════════════════"
echo "STEP 1: Compiling Model Classes..."
echo "════════════════════════════════════════════════════════════"
javac -d . -cp . src/com/studyplanner/model/*.java
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to compile model classes${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Model classes compiled successfully${NC}"
echo

echo "════════════════════════════════════════════════════════════"
echo "STEP 2: Compiling Database Layer..."
echo "════════════════════════════════════════════════════════════"
javac -d . -cp . src/com/studyplanner/db/*.java
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to compile database layer${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Database layer compiled successfully${NC}"
echo

echo "════════════════════════════════════════════════════════════"
echo "STEP 3: Compiling Service Layer..."
echo "════════════════════════════════════════════════════════════"
javac -d . -cp . src/com/studyplanner/service/*.java
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to compile service layer${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Service layer compiled successfully${NC}"
echo

echo "════════════════════════════════════════════════════════════"
echo "STEP 4: Compiling GUI Components..."
echo "════════════════════════════════════════════════════════════"
javac -d . -cp . src/com/studyplanner/gui/*.java
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to compile GUI components${NC}"
    exit 1
fi
echo -e "${GREEN}✓ GUI components compiled successfully${NC}"
echo

echo "════════════════════════════════════════════════════════════"
echo "STEP 5: Compiling Main Application..."
echo "════════════════════════════════════════════════════════════"
javac -d . -cp . src/com/studyplanner/main/*.java
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to compile main application${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Main application compiled successfully${NC}"
echo

echo "════════════════════════════════════════════════════════════"
echo -e "${GREEN}✅ ALL FILES COMPILED SUCCESSFULLY!${NC}"
echo "════════════════════════════════════════════════════════════"
echo

echo "Compiled classes are in:"
echo "$PROJECT_ROOT/com/"
echo

# Ask user if they want to run the application
echo
read -p "Do you want to run the application now? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo
    echo "════════════════════════════════════════════════════════════"
    echo "Starting Student Study Planner Application..."
    echo "════════════════════════════════════════════════════════════"
    echo
    java -cp . com.studyplanner.main.StudyPlannerApp
else
    echo
    echo "To run the application later, use:"
    echo
    echo "export CLASSPATH=$JDBC_JAR:."
    echo "java -cp . com.studyplanner.main.StudyPlannerApp"
    echo
fi
