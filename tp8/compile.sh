#!/bin/bash
# Compile and run the University Course Management System

SRC_DIR="src/main/java"
BIN_DIR="bin"
MAIN_CLASS="com.ensta.university.Main"

echo "Compiling University Course Management System..."

# Create bin directory
mkdir -p $BIN_DIR

# Compile all Java files
javac -d $BIN_DIR $(find $SRC_DIR -name "*.java")

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    echo "Running the system..."
    echo "====================="
    java -cp $BIN_DIR $MAIN_CLASS
else
    echo "✗ Compilation failed!"
    exit 1
fi
