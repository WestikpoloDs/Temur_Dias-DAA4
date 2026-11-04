package edu.textanalysis.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KMPMatcher implements StringMatcher {

    @Override
    public String getName() {
        return "KMP";
    }

    public static int[] computeLPS(String pattern, long[] compCounter) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        lps[0] = 0;

        while (i < m) {
            if (compCounter != null) {
                compCounter[0]++;
            }
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static int[] computeLPS(String pattern) {
        return computeLPS(pattern, null);
    }

    @Override
    public MatchingResult search(String text, String pattern) {
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("Text and pattern must not be null.");
        }

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return new MatchingResult(Collections.emptyList(), 0);
        }

        long[] prepComparisons = new long[1];
        int[] lps = computeLPS(pattern, prepComparisons);

        List<Integer> occurrences = new ArrayList<>();
        long searchComparisons = 0;

        int i = 0;
        int j = 0;

        while (i < n) {
            searchComparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == m) {
                    occurrences.add(i - m);
                    j = lps[j - 1];
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        long totalComparisons = prepComparisons[0] + searchComparisons;
        return new MatchingResult(occurrences, totalComparisons);
    }
}
