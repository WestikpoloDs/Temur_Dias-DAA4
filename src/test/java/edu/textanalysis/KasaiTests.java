package edu.textanalysis;

import edu.textanalysis.suffix.KasaiLCP;
import edu.textanalysis.suffix.KasaiResult;
import edu.textanalysis.suffix.SuffixArrayBuilder;

import java.util.Arrays;

public class KasaiTests {

    public static void runAll() {
        System.out.println("--- Running Kasai's LCP Algorithm Tests ---");
        testKasaiManualBanana();
        testKasaiAbracadabra();
        testKasaiRepeatedCharacters();
        testKasaiSingleCharacter();
        testPrefixDoublingMatchesNaive();
        System.out.println("All Kasai LCP tests passed successfully!\n");
    }

    private static void assertEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(String.format("FAILED: %s%nExpected: %s%nActual:   %s",
                    message, Arrays.toString(expected), Arrays.toString(actual)));
        }
    }


    public static void testKasaiManualBanana() {
        String text = "banana";
        int[] manualSA = new int[]{5, 3, 1, 0, 4, 2};
        int[] expectedLCP = new int[]{0, 1, 3, 0, 0, 2};

        KasaiResult result = KasaiLCP.computeLCP(text, manualSA);
        assertEquals(expectedLCP, result.getLcp(), "Kasai LCP on 'banana' must match manually verified array");
        System.out.println("  [PASS] Kasai Manual Test: 'banana' -> " + Arrays.toString(result.getLcp()));
    }

    public static void testKasaiAbracadabra() {
        String text = "abracadabra";
        int[] sa = SuffixArrayBuilder.build(text);
        int[] lcp = KasaiLCP.buildLCP(text, sa);

        for (int i = 1; i < text.length(); i++) {
            int saPrev = sa[i - 1];
            int saCurr = sa[i];
            int expectedCommonPrefix = 0;
            while (saPrev + expectedCommonPrefix < text.length() &&
                   saCurr + expectedCommonPrefix < text.length() &&
                   text.charAt(saPrev + expectedCommonPrefix) == text.charAt(saCurr + expectedCommonPrefix)) {
                expectedCommonPrefix++;
            }
            if (lcp[i] != expectedCommonPrefix) {
                throw new AssertionError(String.format("LCP mismatch at index %d: expected %d, got %d",
                        i, expectedCommonPrefix, lcp[i]));
            }
        }
        System.out.println("  [PASS] Kasai Definition Verified on 'abracadabra'");
    }

    public static void testKasaiRepeatedCharacters() {
        String text = "aaaa";
        int[] sa = SuffixArrayBuilder.build(text);
        int[] expectedLCP = new int[]{0, 1, 2, 3};
        int[] lcp = KasaiLCP.buildLCP(text, sa);
        assertEquals(expectedLCP, lcp, "Kasai on 'aaaa' must yield [0, 1, 2, 3]");
        System.out.println("  [PASS] Kasai Repeated Characters 'aaaa'");
    }

    public static void testKasaiSingleCharacter() {
        String text = "a";
        int[] sa = new int[]{0};
        int[] expectedLCP = new int[]{0};
        int[] lcp = KasaiLCP.buildLCP(text, sa);
        assertEquals(expectedLCP, lcp, "Kasai on single char must yield [0]");
        System.out.println("  [PASS] Kasai Single Character 'a'");
    }

    public static void testPrefixDoublingMatchesNaive() {
        String text = "mississippi";
        int[] saNaive = SuffixArrayBuilder.buildNaive(text);
        int[] saDoubling = SuffixArrayBuilder.buildPrefixDoubling(text);
        assertEquals(saNaive, saDoubling, "Prefix-doubling SA must match Naive SA exactly");

        int[] lcpNaive = KasaiLCP.buildLCP(text, saNaive);
        int[] lcpDoubling = KasaiLCP.buildLCP(text, saDoubling);
        assertEquals(lcpNaive, lcpDoubling, "LCP must match regardless of SA construction method");
        System.out.println("  [PASS] Suffix Array Builder Equivalence on 'mississippi'");
    }
}
