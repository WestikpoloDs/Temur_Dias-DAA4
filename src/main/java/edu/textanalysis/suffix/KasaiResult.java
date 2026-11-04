package edu.textanalysis.suffix;

public class KasaiResult {
    private final int[] lcp;
    private final long characterComparisons;
    private final long hDecrements;

    public KasaiResult(int[] lcp, long characterComparisons, long hDecrements) {
        this.lcp = lcp;
        this.characterComparisons = characterComparisons;
        this.hDecrements = hDecrements;
    }

    public int[] getLcp() {
        return lcp;
    }

    public long getCharacterComparisons() {
        return characterComparisons;
    }

    public long getHDecrements() {
        return hDecrements;
    }
}
