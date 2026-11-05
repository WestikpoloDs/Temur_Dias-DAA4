package edu.textanalysis;

import edu.textanalysis.dp.DPResult;
import edu.textanalysis.dp.EditDistance;
import edu.textanalysis.dp.LCS;

import java.util.Arrays;
import java.util.List;

public class DPTests {

    public static void runAll() {
        System.out.println("--- Running Dynamic Programming Tests (LCS & Edit Distance) ---");
        testLCSIdenticalStrings();
        testLCSCompletelyDifferentStrings();
        testLCSEmptyStrings();
        testLCSGeneralCase();
        testLCSReconstruction();

        testEditDistanceIdenticalStrings();
        testEditDistanceOneEmptyString();
        testEditDistanceBothEmpty();
        testEditDistanceSubstitution();
        testEditDistanceInsertion();
        testEditDistanceDeletion();
        testEditDistanceClassicLevenshtein();
        testEditDistanceOperationReconstruction();
        System.out.println("All DP tests passed successfully!\n");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(String.format("FAILED: %s (expected %d, got %d)", message, expected, actual));
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(String.format("FAILED: %s (expected %s, got %s)", message, expected, actual));
        }
    }


    public static void testLCSIdenticalStrings() {
        String s = "DYNAMICPROGRAMMING";
        int len = LCS.computeLength(s, s);
        assertEquals(s.length(), len, "LCS of identical strings must equal string length");
        System.out.println("  [PASS] LCS Identical Strings");
    }

    public static void testLCSCompletelyDifferentStrings() {
        String s1 = "ABCDEF";
        String s2 = "UVWXYZ";
        int len = LCS.computeLength(s1, s2);
        assertEquals(0, len, "LCS of disjoint alphabets must be 0");
        System.out.println("  [PASS] LCS Completely Different Strings");
    }

    public static void testLCSEmptyStrings() {
        assertEquals(0, LCS.computeLength("", ""), "LCS of two empty strings must be 0");
        assertEquals(0, LCS.computeLength("ABC", ""), "LCS with empty target must be 0");
        assertEquals(0, LCS.computeLength("", "XYZ"), "LCS with empty source must be 0");
        System.out.println("  [PASS] LCS Empty String Cases");
    }

    public static void testLCSGeneralCase() {
        String s1 = "AGGTAB";
        String s2 = "GXTXAYB";
        assertEquals(4, LCS.computeLength(s1, s2), "LCS of AGGTAB and GXTXAYB must be 4");

        String s3 = "ABCBDAB";
        String s4 = "BDCABA";
        assertEquals(4, LCS.computeLength(s3, s4), "LCS of ABCBDAB and BDCABA must be 4");
        System.out.println("  [PASS] LCS General Cases");
    }

    public static void testLCSReconstruction() {
        String s1 = "AGGTAB";
        String s2 = "GXTXAYB";
        DPResult res = LCS.compute(s1, s2);
        String lcsStr = LCS.reconstructLCS(s1, s2, res.getTable());
        assertEquals("GTAB", lcsStr, "Reconstructed LCS must be GTAB");
        System.out.println("  [PASS] LCS String Reconstruction");
    }


    public static void testEditDistanceIdenticalStrings() {
        String s = "ALGORITHM";
        int dist = EditDistance.computeDistance(s, s);
        assertEquals(0, dist, "Edit distance of identical strings must be 0");
        System.out.println("  [PASS] Edit Distance Identical Strings");
    }

    public static void testEditDistanceOneEmptyString() {
        String s = "KITTEN";
        assertEquals(6, EditDistance.computeDistance("", s), "Edit distance from empty to length 6 must be 6 (insertions)");
        assertEquals(6, EditDistance.computeDistance(s, ""), "Edit distance from length 6 to empty must be 6 (deletions)");
        System.out.println("  [PASS] Edit Distance One Empty String");
    }

    public static void testEditDistanceBothEmpty() {
        assertEquals(0, EditDistance.computeDistance("", ""), "Edit distance of two empty strings must be 0");
        System.out.println("  [PASS] Edit Distance Both Empty");
    }

    public static void testEditDistanceSubstitution() {
        assertEquals(1, EditDistance.computeDistance("CAT", "HAT"), "Single substitution distance must be 1");
        assertEquals(2, EditDistance.computeDistance("CAT", "HOT"), "Two substitutions distance must be 2");
        System.out.println("  [PASS] Edit Distance Substitutions");
    }

    public static void testEditDistanceInsertion() {
        assertEquals(1, EditDistance.computeDistance("CAT", "CATS"), "Single insertion distance must be 1")
        assertEquals(2, EditDistance.computeDistance("CAT", "SCATS"), "Two insertions distance must be 2");
        System.out.println("  [PASS] Edit Distance Insertions");
    }

    public static void testEditDistanceDeletion() {
        assertEquals(1, EditDistance.computeDistance("CATS", "CAT"), "Single deletion distance must be 1");
        assertEquals(2, EditDistance.computeDistance("BOATS", "BAT"), "Two deletions distance must be 2");
        System.out.println("  [PASS] Edit Distance Deletions");
    }

    public static void testEditDistanceClassicLevenshtein() {
        int dist = EditDistance.computeDistance("kitten", "sitting");
        assertEquals(3, dist, "Distance between kitten and sitting must be 3");

        assertEquals(3, EditDistance.computeDistance("Saturday", "Sunday"), "Distance between Saturday and Sunday must be 3");
        System.out.println("  [PASS] Edit Distance Classic Benchmark Pairs");
    }

    public static void testEditDistanceOperationReconstruction() {
        DPResult res = EditDistance.compute("cat", "hats");
        List<String> ops = EditDistance.reconstructOperations("cat", "hats", res.getTable());
        assertEquals(2, res.getValue(), "Distance must be 2");
        assertTrue(ops.size() == 4, "Trace must have 4 steps (3 chars + 1 insert)");
        System.out.println("  [PASS] Edit Distance Operation Reconstruction");
    }
}
