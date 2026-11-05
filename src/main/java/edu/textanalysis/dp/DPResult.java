package edu.textanalysis.dp;
public class DPResult {
    private final int value;
    private final int[][] table;
    private final long characterComparisons;
    private final long tableUpdates;

    public DPResult(int value, int[][] table, long characterComparisons, long tableUpdates) {
        this.value = value;
        this.table = table;
        this.characterComparisons = characterComparisons;
        this.tableUpdates = tableUpdates;
    }

    public int getValue() {
        return value;
    }

    public int[][] getTable() {
        return table;
    }

    public long getCharacterComparisons() {
        return characterComparisons;
    }

    public long getTableUpdates() {
        return tableUpdates;
    }
}
