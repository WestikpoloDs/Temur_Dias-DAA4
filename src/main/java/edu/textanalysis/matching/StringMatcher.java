package edu.textanalysis.matching;

public interface StringMatcher {
    MatchingResult search(String text, String pattern);
    String getName();
}
