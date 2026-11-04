package edu.textanalysis.dp;

/**
 * Longest Common Subsequence (LCS) implementation using dynamic programming.
 *
 * Problem Definition:
 * Given two strings X of length m and Y of length n, find the length of the longest
 * subsequence common to both strings. A subsequence is derived by deleting zero or
 * more characters without changing the relative order of the remaining characters.
 *
 * Recurrence Relation:
 * Let L[i][j] denote the length of an LCS of prefixes X[0..i-1] and Y[0..j-1].
 *
 * Base Cases:
 *   L[i][0] = 0  for all 0 <= i <= m  (empty Y prefix)
 *   L[0][j] = 0  for all 0 <= j <= n  (empty X prefix)
 *
 * Recursive Step:
 *   For i >= 1 and j >= 1:
 *     If X[i-1] == Y[j-1]:
 *       L[i][j] = L[i-1][j-1] + 1
 *     Else:
 *       L[i][j] = max(L[i-1][j], L[i][j-1])
 *
 * Theoretical Complexity:
 *   - Time: O(m * n) since each of the (m + 1) * (n + 1) table cells is filled in O(1) time.
 *   - Space: O(m * n) auxiliary space for the dynamic programming table.
 */
public class LCS {

    public static DPResult compute(String s1, String s2) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Input strings must not be null.");
        }

        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        long comparisons = 0;
        long tableUpdates = 0;

        tableUpdates += (m + 1) + (n + 1);

        for (int i = 1; i <= m; i++) {
            char c1 = s1.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                char c2 = s2.charAt(j - 1);
                comparisons++;
                if (c1 == c2) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
                tableUpdates++;
            }
        }

        return new DPResult(dp[m][n], dp, comparisons, tableUpdates);
    }

    public static int computeLength(String s1, String s2) {
        return compute(s1, s2).getValue();
    }

    public static String reconstructLCS(String s1, String s2, int[][] dp) {
        if (s1 == null || s2 == null || dp == null) {
            throw new IllegalArgumentException("Arguments must not be null.");
        }

        StringBuilder sb = new StringBuilder();
        int i = s1.length();
        int j = s2.length();

        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                sb.append(s1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        return sb.reverse().toString();
    }
}
