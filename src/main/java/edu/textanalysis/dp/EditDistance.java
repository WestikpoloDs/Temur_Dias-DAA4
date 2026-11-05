package edu.textanalysis.dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
