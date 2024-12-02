///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.*;

class Day02 {
    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day02.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 2: Red-Nosed Reports ---");


        List<List<Pair>> reports;

        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            reports = lines
                    .map(Day02::lineToPairs)
                    .toList();
        }

        long count = reports.stream()
                .filter(Day02::isStrictlyMonotone)
                .count();

        out.println("--- Part One ---");
        out.println("Count of safe reports: " + count);

        out.println("--- Part Two ---");
        out.println("Sum of products: ");
    }

    private static List<Pair> lineToPairs(String line) {
        List<Integer> levels = Arrays.stream(line.split("\\s+"))
                .map(Integer::parseInt)
                .toList();

        return IntStream.range(0, levels.size() - 1)
                .mapToObj(i -> new Pair(levels.get(i), levels.get(i + 1))).toList();

    }

    private static boolean isStrictlyMonotone(List<Pair> report) {
        boolean isInitiallyIncreasing = (report.getFirst().first() - report.getFirst().second()) < 0;
        for (Pair pair : report) {
            int abs = Math.abs(pair.first() - pair.second());
            if (abs < 1 || abs > 3) {
                return false;
            }
            if (isInitiallyIncreasing != (pair.first() - pair.second()) < 0) {
                return false;
            }

        }
        return true;
    }
}

record Pair(int first, int second) {}
