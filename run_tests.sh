#!/bin/bash
set -e

./build.sh
echo ""
echo "=== Running Assignment 4 Tests ==="
java -cp out edu.textanalysis.TestRunner
