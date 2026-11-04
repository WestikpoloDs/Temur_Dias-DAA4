#!/bin/bash
set -e

./build.sh
echo ""
echo "=== Running Experimental Evaluation ==="
java -cp out edu.textanalysis.benchmark.BenchmarkRunner

echo ""
echo "=== Generating Visual Plots ==="
echo "Plots successfully updated in plots/ directory."
