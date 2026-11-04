package edu.textanalysis.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RabinKarpMatcher implements StringMatcher {

    private static final int BASE = 256;
    private static final long PRIME = 1_000_000_007L;

    @Override
    public String getName() {
        return "Rabin-Karp";
    }

    @Override
    public MatchingResult search(String text, String pattern) {
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("Text and pattern must not be null.");
        }

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return new MatchingResult(Collections.emptyList(), 0, 0);
        }

        List<Integer> occurrences = new ArrayList<>();
        long comparisons = 0;
        long collisions = 0;

        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % PRIME;
        }

        long patternHash = 0;
        long windowHash = 0;

        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + (int) pattern.charAt(i)) % PRIME;
            windowHash = (windowHash * BASE + (int) text.charAt(i)) % PRIME;
        }

        for (int s = 0; s <= n - m; s++) {
            if (patternHash == windowHash) {
                boolean fullMatch = true;
                for (int j = 0; j < m; j++) {
                    comparisons++;
                    if (text.charAt(s + j) != pattern.charAt(j)) {
                        fullMatch = false;
                        collisions++;
                        break;
                    }
                }
                if (fullMatch) {
                    occurrences.add(s);
                }
            }

            if (s < n - m) {
                long leading = ((long) text.charAt(s) * h) % PRIME;
                windowHash = (windowHash - leading) % PRIME;
                if (windowHash < 0) {
                    windowHash += PRIME;
                }
                windowHash = (windowHash * BASE + (int) text.charAt(s + m)) % PRIME;
            }
        }

        return new MatchingResult(occurrences, comparisons, collisions);
    }
}
