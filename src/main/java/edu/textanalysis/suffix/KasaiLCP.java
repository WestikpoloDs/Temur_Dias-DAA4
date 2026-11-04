package edu.textanalysis.suffix;

public class KasaiLCP {

    public static KasaiResult computeLCP(String text, int[] suffixArray) {
        if (text == null || suffixArray == null) {
            throw new IllegalArgumentException("Text and suffix array must not be null.");
        }

        int n = text.length();
        if (suffixArray.length != n) {
            throw new IllegalArgumentException("Suffix array length must match text length.");
        }

        if (n == 0) {
            return new KasaiResult(new int[0], 0, 0);
        }

        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int[] lcp = new int[n];
        lcp[0] = 0;

        long comparisons = 0;
        long hDecrements = 0;
        int h = 0;

        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r > 0) {
                int k = suffixArray[r - 1];

                while (i + h < n && k + h < n) {
                    comparisons++;
                    if (text.charAt(i + h) == text.charAt(k + h)) {
                        h++;
                    } else {
                        break;
                    }
                }

                lcp[r] = h;

                if (h > 0) {
                    h--;
                    hDecrements++;
                }
            } else {
                lcp[0] = 0;
            }
        }

        return new KasaiResult(lcp, comparisons, hDecrements);
    }

    public static int[] buildLCP(String text, int[] suffixArray) {
        return computeLCP(text, suffixArray).getLcp();
    }
}
