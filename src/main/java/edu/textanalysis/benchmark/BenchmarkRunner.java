package edu.textanalysis.benchmark;

import edu.textanalysis.dp.DPResult;
import edu.textanalysis.dp.EditDistance;
import edu.textanalysis.dp.LCS;
import edu.textanalysis.matching.KMPMatcher;
import edu.textanalysis.matching.MatchingResult;
import edu.textanalysis.matching.NaiveMatcher;
import edu.textanalysis.matching.RabinKarpMatcher;
import edu.textanalysis.matching.StringMatcher;
import edu.textanalysis.suffix.KasaiLCP;
import edu.textanalysis.suffix.KasaiResult;
import edu.textanalysis.suffix.SuffixArrayBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BenchmarkRunner {

    public static final long FIXED_SEED = 42L;
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final int TRIALS = 5;
    public static final int WARMUP_ROUNDS = 3;

    public static class Record {
        public String category;
        public String algorithm;
        public int inputSize;
        public String condition;
        public double avgTimeMs;
        public double avgTimeUs;
        public long avgComparisons;
        public long avgAuxOperations;

        public Record(String category, String algorithm, int inputSize, String condition,
                      double avgTimeMs, double avgTimeUs, long avgComparisons, long avgAuxOperations) {
            this.category = category;
            this.algorithm = algorithm;
            this.inputSize = inputSize;
            this.condition = condition;
            this.avgTimeMs = avgTimeMs;
            this.avgTimeUs = avgTimeUs;
            this.avgComparisons = avgComparisons;
            this.avgAuxOperations = avgAuxOperations;
        }
    }

    public static String generateRandomString(Random rng, int length, String alphabet) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(rng.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public static void runWarmup() {
        System.out.println("Executing JVM JIT warm-up cycles...");
        Random rng = new Random(FIXED_SEED);
        for (int w = 0; w < WARMUP_ROUNDS; w++) {
            String s1 = generateRandomString(rng, 200, ALPHABET);
            String s2 = generateRandomString(rng, 200, ALPHABET);
            LCS.compute(s1, s2);
            EditDistance.compute(s1, s2);

            String text = generateRandomString(rng, 5000, ALPHABET);
            String pat = generateRandomString(rng, 20, ALPHABET);
            new NaiveMatcher().search(text, pat);
            new KMPMatcher().search(text, pat);
            new RabinKarpMatcher().search(text, pat);

            int[] sa = SuffixArrayBuilder.build(text);
            KasaiLCP.computeLCP(text, sa);
        }
        System.out.println("Warm-up complete.\n");
    }

    public static List<Record> runAllExperiments() {
        List<Record> results = new ArrayList<>();
        Random rng = new Random(FIXED_SEED);

        System.out.println("=========================================================================================");
        System.out.println("                      ASSIGNMENT 4 EXPERIMENTAL EVALUATION");
        System.out.println("  Fixed Random Seed: " + FIXED_SEED + " | Alphabet: [a-z] (|Sigma|=26) | Trials: " + TRIALS);
        System.out.println("=========================================================================================\n");

        // ----------------------------------------------------
        // Experiment 1: Dynamic Programming (LCS & Edit Distance)
        // Sizes: 100, 500, 1,000
        // ----------------------------------------------------
        int[] dpSizes = {100, 500, 1000};
        System.out.println(">>> 1. Dynamic Programming Experiments (LCS & Edit Distance)");
        for (int size : dpSizes) {
            String s1 = generateRandomString(rng, size, ALPHABET);
            String s2 = generateRandomString(rng, size, ALPHABET);

            // LCS Evaluation
            long totalTimeLCS = 0;
            DPResult lcsRes = null;
            for (int t = 0; t < TRIALS; t++) {
                long start = System.nanoTime();
                lcsRes = LCS.compute(s1, s2);
                long end = System.nanoTime();
                totalTimeLCS += (end - start);
            }
            double avgLcsMs = (totalTimeLCS / (double) TRIALS) / 1_000_000.0;
            double avgLcsUs = (totalTimeLCS / (double) TRIALS) / 1_000.0;
            results.add(new Record("DP", "LCS", size, "Random Strings",
                    avgLcsMs, avgLcsUs, lcsRes.getCharacterComparisons(), lcsRes.getTableUpdates()));
            System.out.printf("  [LCS]           Size: %-5d | Avg Time: %8.4f ms (%8.2f us) | Comparisons: %10d | Updates: %10d%n",
                    size, avgLcsMs, avgLcsUs, lcsRes.getCharacterComparisons(), lcsRes.getTableUpdates());

            // Edit Distance Evaluation
            long totalTimeED = 0;
            DPResult edRes = null;
            for (int t = 0; t < TRIALS; t++) {
                long start = System.nanoTime();
                edRes = EditDistance.compute(s1, s2);
                long end = System.nanoTime();
                totalTimeED += (end - start);
            }
            double avgEdMs = (totalTimeED / (double) TRIALS) / 1_000_000.0;
            double avgEdUs = (totalTimeED / (double) TRIALS) / 1_000.0;
            results.add(new Record("DP", "Edit Distance", size, "Random Strings",
                    avgEdMs, avgEdUs, edRes.getCharacterComparisons(), edRes.getTableUpdates()));
            System.out.printf("  [Edit Distance] Size: %-5d | Avg Time: %8.4f ms (%8.2f us) | Comparisons: %10d | Updates: %10d%n",
                    size, avgEdMs, avgEdUs, edRes.getCharacterComparisons(), edRes.getTableUpdates());
        }
        System.out.println();

        // ----------------------------------------------------
        // Experiment 2: String Matching (Naive, KMP, Rabin-Karp)
        // Sizes: 10,000, 50,000, 100,000
        // Workloads: Pattern Present and Pattern Absent
        // ----------------------------------------------------
        int[] matchingSizes = {10000, 50000, 100000};
        int patternLength = 25;
        StringMatcher[] matchers = {new NaiveMatcher(), new KMPMatcher(), new RabinKarpMatcher()};

        System.out.println(">>> 2. String Matching Experiments (Naive, KMP, Rabin-Karp)");
        for (int size : matchingSizes) {
            String text = generateRandomString(rng, size, ALPHABET);

            // Condition A: Pattern Present
            int startIdx = size / 2;
            String patternPresent = text.substring(startIdx, Math.min(size, startIdx + patternLength));

            // Condition B: Pattern Absent (using uppercase letters not in text alphabet)
            String patternAbsent = generateRandomString(rng, patternLength, "0123456789");

            String[][] conditions = {
                    {"Pattern Present", patternPresent},
                    {"Pattern Absent", patternAbsent}
            };

            for (String[] condPair : conditions) {
                String condName = condPair[0];
                String pat = condPair[1];

                for (StringMatcher matcher : matchers) {
                    long totalTime = 0;
                    MatchingResult lastRes = null;
                    for (int t = 0; t < TRIALS; t++) {
                        long start = System.nanoTime();
                        lastRes = matcher.search(text, pat);
                        long end = System.nanoTime();
                        totalTime += (end - start);
                    }
                    double avgMs = (totalTime / (double) TRIALS) / 1_000_000.0;
                    double avgUs = (totalTime / (double) TRIALS) / 1_000.0;
                    results.add(new Record("String Matching", matcher.getName(), size, condName,
                            avgMs, avgUs, lastRes.getCharacterComparisons(), lastRes.getHashCollisions()));
                    System.out.printf("  [%-10s] Text Len: %-6d | Cond: %-15s | Avg: %8.4f ms (%8.2f us) | Comp: %10d | Matches: %d%n",
                            matcher.getName(), size, condName, avgMs, avgUs, lastRes.getCharacterComparisons(), lastRes.getMatchCount());
                }
            }
        }
        System.out.println();

        // ----------------------------------------------------
        // Experiment 3: Kasai's Algorithm for LCP
        // Sizes: 1,000, 5,000, 10,000
        // Suffix array generated outside the timed block!
        // ----------------------------------------------------
        int[] kasaiSizes = {1000, 5000, 10000};
        System.out.println(">>> 3. Kasai's LCP Algorithm Experiments");
        for (int size : kasaiSizes) {
            String text = generateRandomString(rng, size, ALPHABET);
            // Precompute suffix array outside timed region
            int[] sa = SuffixArrayBuilder.build(text);

            long totalTime = 0;
            KasaiResult lastRes = null;
            for (int t = 0; t < TRIALS; t++) {
                long start = System.nanoTime();
                lastRes = KasaiLCP.computeLCP(text, sa);
                long end = System.nanoTime();
                totalTime += (end - start);
            }
            double avgMs = (totalTime / (double) TRIALS) / 1_000_000.0;
            double avgUs = (totalTime / (double) TRIALS) / 1_000.0;
            results.add(new Record("Suffix", "Kasai LCP", size, "Random Text",
                    avgMs, avgUs, lastRes.getCharacterComparisons(), lastRes.getHDecrements()));
            System.out.printf("  [Kasai LCP]    Length: %-6d | Avg Time: %8.4f ms (%8.2f us) | Comparisons: %10d | Decrements: %10d%n",
                    size, avgMs, avgUs, lastRes.getCharacterComparisons(), lastRes.getHDecrements());
        }
        System.out.println();

        return results;
    }

    public static void exportCsv(List<Record> records, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Category,Algorithm,InputSize,Condition,AvgTimeMs,AvgTimeUs,AvgComparisons,AuxOperations");
            for (Record r : records) {
                pw.printf("%s,%s,%d,%s,%.6f,%.3f,%d,%d%n",
                        r.category, r.algorithm, r.inputSize, r.condition,
                        r.avgTimeMs, r.avgTimeUs, r.avgComparisons, r.avgAuxOperations);
            }
            System.out.println("Exported benchmark metrics to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        runWarmup();
        List<Record> records = runAllExperiments();
        exportCsv(records, "benchmark_results.csv");
    }
}
