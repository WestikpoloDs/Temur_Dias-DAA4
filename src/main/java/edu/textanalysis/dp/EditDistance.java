package edu.textanalysis.dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Edit Distance (Levenshtein distance) implementation using dynamic programming.
 *
 * Problem Definition:
 * Given two strings X of length m and Y of length n, find the minimum number of
 * edit operations (insertions, deletions, substitutions) required to transform
 * string X into string Y.
 *
 * Recurrence Relation:
 * Let D[i][j] denote the minimum edit distance between prefix X[0..i-1] and Y[0..j-1].
 *
 * Base Cases:
 *   D[i][0] = i  for all 0 <= i <= m  (transforming X[0..i-1] to empty string requires i deletions)
 *   D[0][j] = j  for all 0 <= j <= n  (transforming empty string to Y[0..j-1] requires j insertions)
 *
 * Recursive Step:
 *   For i >= 1 and j >= 1:
 *     cost = (X[i-1] == Y[j-1]) ? 0 : 1
 *     D[i][j] = min(
 *                 D[i-1][j] + 1,       // Deletion of X[i-1]
 *                 D[i][j-1] + 1,       // Insertion of Y[j-1]
 *                 D[i-1][j-1] + cost   // Match or Substitution of X[i-1] with Y[j-1]
 *               )
 *
 * Theoretical Complexity:
 *   - Time: O(m * n) since each of the (m + 1) * (n + 1) cells is filled in O(1) constant time.
 *   - Space: O(m * n) auxiliary space for the full 2D dynamic programming table.
 */
public class EditDistance {

    public static DPResult compute(String s1, String s2) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Input strings must not be null.");
        }

        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        long comparisons = 0;
        long tableUpdates = 0;

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            tableUpdates++;
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = j;
            tableUpdates++;
        }

        for (int i = 1; i <= m; i++) {
            char c1 = s1.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                char c2 = s2.charAt(j - 1);
                comparisons++;
                int cost = (c1 == c2) ? 0 : 1;

                int deletion = dp[i - 1][j] + 1;
                int insertion = dp[i][j - 1] + 1;
                int substitution = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(deletion, Math.min(insertion, substitution));
                tableUpdates++;
            }
        }

        return new DPResult(dp[m][n], dp, comparisons, tableUpdates);
    }

    public static int computeDistance(String s1, String s2) {
        return compute(s1, s2).getValue();
    }

    public static List<String> reconstructOperations(String s1, String s2, int[][] dp) {
        if (s1 == null || s2 == null || dp == null) {
            throw new IllegalArgumentException("Arguments must not be null.");
        }

        List<String> ops = new ArrayList<>();
        int i = s1.length();
        int j = s2.length();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                char c1 = s1.charAt(i - 1);
                char c2 = s2.charAt(j - 1);
                int cost = (c1 == c2) ? 0 : 1;

                if (dp[i][j] == dp[i - 1][j - 1] + cost) {
                    if (cost == 0) {
                        ops.add("MATCH '" + c1 + "'");
                    } else {
                        ops.add("SUBSTITUTE '" + c1 + "' -> '" + c2 + "'");
                    }
                    i--;
                    j--;
                    continue;
                }
            }

            if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                ops.add("DELETE '" + s1.charAt(i - 1) + "'");
                i--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                ops.add("INSERT '" + s2.charAt(j - 1) + "'");
                j--;
            }
        }

        Collections.reverse(ops);
        return ops;
    }
}
