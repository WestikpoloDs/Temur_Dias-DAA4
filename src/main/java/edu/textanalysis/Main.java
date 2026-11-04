package edu.textanalysis;

import edu.textanalysis.benchmark.BenchmarkRunner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            TestRunner.main(args);
        } else {
            System.out.println("Starting Assignment 4 - Text Analysis Suite");
            BenchmarkRunner.main(args);
        }
    }
}
