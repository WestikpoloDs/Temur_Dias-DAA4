#!/bin/bash
set -e

echo "=== Building Assignment 4 (Java 17) ==="
mkdir -p out

# Determine javac command: check for javac in PATH, otherwise use built-in JavacTool
if command -v javac &> /dev/null; then
    JAVAC_CMD="javac"
else
    JAVAC_CMD="java com.sun.tools.javac.Main"
fi

cat << 'SRCS' > .sources_list.txt
src/main/java/edu/textanalysis/dp/DPResult.java
src/main/java/edu/textanalysis/dp/LCS.java
src/main/java/edu/textanalysis/dp/EditDistance.java
src/main/java/edu/textanalysis/matching/MatchingResult.java
src/main/java/edu/textanalysis/matching/StringMatcher.java
src/main/java/edu/textanalysis/matching/NaiveMatcher.java
src/main/java/edu/textanalysis/matching/KMPMatcher.java
src/main/java/edu/textanalysis/matching/RabinKarpMatcher.java
src/main/java/edu/textanalysis/suffix/KasaiResult.java
src/main/java/edu/textanalysis/suffix/KasaiLCP.java
src/main/java/edu/textanalysis/suffix/SuffixArrayBuilder.java
src/main/java/edu/textanalysis/benchmark/BenchmarkRunner.java
src/test/java/edu/textanalysis/DPTests.java
src/test/java/edu/textanalysis/StringMatchingTests.java
src/test/java/edu/textanalysis/KasaiTests.java
src/test/java/edu/textanalysis/TestRunner.java
src/main/java/edu/textanalysis/Main.java
SRCS

$JAVAC_CMD -d out @.sources_list.txt
rm -f .sources_list.txt
echo "Build successful! Binaries compiled into out/"
