package edu.textanalysis.dp;

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
