///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        List<String> inputLines;
        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            inputLines = lines.toList();
        }

        out.println("--- Part One ---");

        long countOfStrictlyMonotone = inputLines.stream()
                .map(Day02::lineToIntegers)
                .map(Day02::numbersToPairs)
                .filter(Day02::isStrictlyMonotone)
                .count();

        out.println("Count of safe reports: " + countOfStrictlyMonotone);

        out.println("--- Part Two ---");

        long countOfPossiblyStrictlyMonotone = inputLines.stream()
                .map(Day02::lineToIntegers)
                .map(Day02::leaveOneOutVariants)
                .map(variants -> variants.stream()
                        .map(Day02::numbersToPairs)
                        .toList())
                .filter(Day02::isPossiblyStrictlyMonotone)
                .count();

        out.println("Count of safe reports: " + countOfPossiblyStrictlyMonotone);
    }

    private static List<Integer> lineToIntegers(String line) {
        return Stream.of(line.split("\\s+"))
                .map(Integer::parseInt)
                .toList();
    }

    private static List<Pair> numbersToPairs(List<Integer> levels) {
        return IntStream.range(0, levels.size() - 1)
                .mapToObj(i -> new Pair(levels.get(i), levels.get(i + 1)))
                .toList();
    }

    private static List<List<Integer>> leaveOneOutVariants(List<Integer> levels) {
        return IntStream.concat(
                        IntStream.of(-1),
                        IntStream.range(0, levels.size()))
                .mapToObj(i -> {
                    // Create a sub-sequence excluding the element at index i
                    return IntStream.range(0, levels.size())
                            .filter(j -> j != i)
                            .mapToObj(levels::get)
                            .toList();
                })
                .toList();
    }

    private static boolean isStrictlyMonotone(List<Pair> report) {
        boolean isInitiallyIncreasing = report.getFirst().first() < report.getFirst().second();
        for (Pair pair : report) {
            int abs = Math.abs(pair.first() - pair.second());
            if (abs < 1 || abs > 3) {
                return false;
            }
            if (isInitiallyIncreasing != (pair.first() < pair.second())) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPossiblyStrictlyMonotone(List<List<Pair>> alternatives) {
        long count = alternatives.stream()
                .filter(Day02::isStrictlyMonotone)
                .count();

        return count > 0;
    }
}

record Pair(int first, int second) {}
