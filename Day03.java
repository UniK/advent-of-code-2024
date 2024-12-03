///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.System.*;

class Day03 {
    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day03.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 3: Mull It Over ---");

        List<String> inputLines;
        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            inputLines = lines.toList();
        }

        out.println("--- Part One ---");

        // Regex pattern to match `mul(<number>,<number>)`
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

        int totalSum = inputLines.stream()
                .flatMap(line -> {
                    Matcher matcher = pattern.matcher(line);
                    return matcher.results()
                            .map(result -> new int[]{
                                    Integer.parseInt(result.group(1)),
                                    Integer.parseInt(result.group(2))
                            });
                })
                .mapToInt(pair -> pair[0] * pair[1])
                .sum();

        out.printf("Sum of multiplications: %d%n", totalSum);

        out.println("--- Part Two ---");

        boolean[] mulEnabled = { true }; // Mutex for enable/disable state
        int totalSumWithMutex = inputLines.stream()
                .mapToInt(line -> {
                    int lineSum = 0;

                    // Create a matcher to find all relevant patterns in sequence
                    Matcher matcher = Pattern.compile("mul\\((\\d+),(\\d+)\\)|\\bdo\\(\\)|\\bdon't\\(\\)")
                            .matcher(line);

                    while (matcher.find()) {
                        if (matcher.group().equals("do()")) {
                            mulEnabled[0] = true; // Enable `mul`
                        } else if (matcher.group().equals("don't()")) {
                            mulEnabled[0] = false; // Disable `mul`
                        } else if (matcher.group(1) != null && mulEnabled[0]) {
                            // Process `mul(<number>,<number>)` if enabled
                            int num1 = Integer.parseInt(matcher.group(1));
                            int num2 = Integer.parseInt(matcher.group(2));
                            lineSum += num1 * num2;
                        }
                    }

                    return lineSum;
                })
                .sum();

        out.printf("Sum of multiplications with mutex: %d%n", totalSumWithMutex);
    }
}