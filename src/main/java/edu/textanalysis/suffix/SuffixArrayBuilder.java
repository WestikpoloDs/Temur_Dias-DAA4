package edu.textanalysis.suffix;

import java.util.Arrays;
import java.util.Comparator;

public class SuffixArrayBuilder {

    public static int[] buildNaive(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null.");
        }
        int n = text.length();
        Integer[] sa = new Integer[n];
        for (int i = 0; i < n; i++) {
            sa[i] = i;
        }

        Arrays.sort(sa, (a, b) -> {
            int lenA = n - a;
            int lenB = n - b;
            int minLen = Math.min(lenA, lenB);
            for (int k = 0; k < minLen; k++) {
                char c1 = text.charAt(a + k);
                char c2 = text.charAt(b + k);
                if (c1 != c2) {
                    return Character.compare(c1, c2);
                }
            }
            return Integer.compare(lenA, lenB);
        });

        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = sa[i];
        }
        return result;
    }

    private static class SuffixRank {
        int index;
        int rank0;
        int rank1;
    }

    public static int[] buildPrefixDoubling(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null.");
        }
        int n = text.length();
        if (n == 0) {
            return new int[0];
        }

        SuffixRank[] suffixes = new SuffixRank[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new SuffixRank();
            suffixes[i].index = i;
            suffixes[i].rank0 = (int) text.charAt(i);
            suffixes[i].rank1 = (i + 1 < n) ? (int) text.charAt(i + 1) : -1;
        }

        Comparator<SuffixRank> comp = (a, b) -> {
            if (a.rank0 != b.rank0) {
                return Integer.compare(a.rank0, b.rank0);
            }
            return Integer.compare(a.rank1, b.rank1);
        };

        Arrays.sort(suffixes, comp);

        int[] ind = new int[n];
        for (int k = 4; k < 2 * n; k *= 2) {
            int rank = 0;
            int prevRank0 = suffixes[0].rank0;
            int prevRank1 = suffixes[0].rank1;
            suffixes[0].rank0 = 0;
            ind[suffixes[0].index] = 0;

            for (int i = 1; i < n; i++) {
                if (suffixes[i].rank0 == prevRank0 && suffixes[i].rank1 == prevRank1) {
                    suffixes[i].rank0 = rank;
                } else {
                    prevRank0 = suffixes[i].rank0;
                    prevRank1 = suffixes[i].rank1;
                    rank++;
                    suffixes[i].rank0 = rank;
                }
                ind[suffixes[i].index] = i;
            }

            for (int i = 0; i < n; i++) {
                int nextIndex = suffixes[i].index + k / 2;
                suffixes[i].rank1 = (nextIndex < n) ? suffixes[ind[nextIndex]].rank0 : -1;
            }

            Arrays.sort(suffixes, comp);
        }

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i].index;
        }
        return sa;
    }

    public static int[] build(String text) {
        if (text == null) {
            return new int[0];
        }
        if (text.length() <= 200) {
            return buildNaive(text);
        }
        return buildPrefixDoubling(text);
    }
}
