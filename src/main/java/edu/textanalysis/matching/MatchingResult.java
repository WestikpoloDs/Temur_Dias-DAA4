package edu.textanalysis.matching;

import java.util.Collections;
import java.util.List;

public class MatchingResult {
    private final List<Integer> occurrences;
    private final long characterComparisons;
    private final long hashCollisions;

    public MatchingResult(List<Integer> occurrences, long characterComparisons, long hashCollisions) {
        this.occurrences = occurrences != null ? Collections.unmodifiableList(occurrences) : Collections.emptyList();
        this.characterComparisons = characterComparisons;
        this.hashCollisions = hashCollisions;
    }

    public MatchingResult(List<Integer> occurrences, long characterComparisons) {
        this(occurrences, characterComparisons, 0);
    }

    public List<Integer> getOccurrences() {
        return occurrences;
    }

    public long getCharacterComparisons() {
        return characterComparisons;
    }

    public long getHashCollisions() {
        return hashCollisions;
    }

    public int getMatchCount() {
        return occurrences.size();
    }
}
