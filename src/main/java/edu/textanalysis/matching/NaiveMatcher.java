package edu.textanalysis.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NaiveMatcher implements StringMatcher {

    @Override
    public String getName() {
        return "Naive";
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

        List<Integer> occurrences = new ArrayList<>();
        long comparisons = 0;

        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                occurrences.add(i);
            }
        }

        return new MatchingResult(occurrences, comparisons);
    }
}
