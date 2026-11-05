package edu.textanalysis;

import edu.textanalysis.matching.KMPMatcher;
import edu.textanalysis.matching.MatchingResult;
import edu.textanalysis.matching.NaiveMatcher;
import edu.textanalysis.matching.RabinKarpMatcher;
import edu.textanalysis.matching.StringMatcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringMatchingTests {

    private static final StringMatcher naive = new NaiveMatcher();
    private static final StringMatcher kmp = new KMPMatcher();
    private static final StringMatcher rabinKarp = new RabinKarpMatcher();

    public static void runAll() {
        System.out.println("--- Running String Matching Tests (Naive, KMP, Rabin-Karp) ---");
        testPatternAtBeginning();
        testPatternInMiddle();
        testPatternAtEnd();
        testMultipleOccurrences();
        testOverlappingOccurrences();
        testPatternAbsent();
        testPatternLongerThanText();
        testEmptyPattern();
        testLongRandomConsistency();
        System.out.println("All String Matching tests passed and verified consistent across all 3 algorithms!\n");
    }

    private static void verifyConsistency(String text, String pattern, List<Integer> expected, String testName) {
        MatchingResult rNaive = naive.search(text, pattern);
        MatchingResult rKmp = kmp.search(text, pattern);
        MatchingResult rRk = rabinKarp.search(text, pattern);

        if (expected != null) {
            if (!rNaive.getOccurrences().equals(expected)) {
                throw new AssertionError(String.format("Naive failed %s: expected %s, got %s",
                        testName, expected, rNaive.getOccurrences()));
            }
        }

        if (!rNaive.getOccurrences().equals(rKmp.getOccurrences())) {
            throw new AssertionError(String.format("KMP differs from Naive in %s: Naive=%s, KMP=%s",
                    testName, rNaive.getOccurrences(), rKmp.getOccurrences()));
        }
        if (!rNaive.getOccurrences().equals(rRk.getOccurrences())) {
            throw new AssertionError(String.format("Rabin-Karp differs from Naive in %s: Naive=%s, RK=%s",
                    testName, rNaive.getOccurrences(), rRk.getOccurrences()));
        }

        System.out.println("  [PASS] " + testName + " -> Matches at " + rNaive.getOccurrences());
    }

    public static void testPatternAtBeginning() {
        String text = "APPLE PIE AND CIDER";
        String pattern = "APPLE";
        verifyConsistency(text, pattern, Arrays.asList(0), "Pattern at Beginning");
    }

    public static void testPatternInMiddle() {
        String text = "THE QUICK BROWN FOX JUMPS";
        String pattern = "QUICK";
        verifyConsistency(text, pattern, Arrays.asList(4), "Pattern in Middle");
    }

    public static void testPatternAtEnd() {
        String text = "WELCOME TO THE FUTURE";
        String pattern = "FUTURE";
        verifyConsistency(text, pattern, Arrays.asList(15), "Pattern at End");
    }

    public static void testMultipleOccurrences() {
        String text = "BANANA BANANA";
        String pattern = "ANA";
        verifyConsistency(text, pattern, Arrays.asList(1, 3, 8, 10), "Multiple Occurrences");
    }

    public static void testOverlappingOccurrences() {
        String text = "AAAAA";
        String pattern = "AAA";
        verifyConsistency(text, pattern, Arrays.asList(0, 1, 2), "Overlapping Occurrences");
    }

    public static void testPatternAbsent() {
        String text = "ABCDEF GHIJKL MNOPQR";
        String pattern = "XYZ";
        verifyConsistency(text, pattern, Collections.emptyList(), "Pattern Absent");
    }

    public static void testPatternLongerThanText() {
        String text = "SHORT";
        String pattern = "VERY LONG PATTERN EXCEEDING TEXT LENGTH";
        verifyConsistency(text, pattern, Collections.emptyList(), "Pattern Longer Than Text");
    }

    public static void testEmptyPattern() {
        String text = "SOME TEXT";
        String pattern = "";
        verifyConsistency(text, pattern, Collections.emptyList(), "Empty Pattern (Assumption: Empty list)");
    }

    public static void testLongRandomConsistency() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append("ABACABA");
        }
        String text = sb.toString();
        String pattern = "ABACABA";
        verifyConsistency(text, pattern, null, "Stress Consistency on Repetitive Text");
    }
}
