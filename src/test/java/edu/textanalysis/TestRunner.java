package edu.textanalysis;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  ASSIGNMENT 4 COMPREHENSIVE TEST SUITE");
        System.out.println("==================================================\n");

        int totalSuites = 3;
        int passedSuites = 0;

        try {
            DPTests.runAll();
            passedSuites++;
        } catch (Throwable t) {
            System.err.println("FAILED DPTests: " + t.getMessage());
            t.printStackTrace();
        }

        try {
            StringMatchingTests.runAll();
            passedSuites++;
        } catch (Throwable t) {
            System.err.println("FAILED StringMatchingTests: " + t.getMessage());
            t.printStackTrace();
        }

        try {
            KasaiTests.runAll();
            passedSuites++;
        } catch (Throwable t) {
            System.err.println("FAILED KasaiTests: " + t.getMessage());
            t.printStackTrace();
        }

        System.out.println("==================================================");
        if (passedSuites == totalSuites) {
            System.out.printf("  SUMMARY: ALL %d TEST SUITES PASSED (100%%)%n", totalSuites);
            System.out.println("==================================================");
        } else {
            System.err.printf("  SUMMARY: %d OF %d TEST SUITES PASSED%n", passedSuites, totalSuites);
            System.out.println("==================================================");
            System.exit(1);
        }
    }
}
