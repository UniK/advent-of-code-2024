///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.MatchResult;
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

        // Regex pattern to match `mul(<digit>,<digit>)`
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

        inputLines.forEach(line -> {
            System.out.printf("Processing line: %s%n", line);

            Matcher matcher = pattern.matcher(line);
            matcher.results()
                    .map(MatchResult::group)
                    .forEach(match -> out.printf("Found match: %s%n", match));
        });

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


        out.println("Sum of multiplications: " + totalSum);

        out.println("--- Part Two ---");

        out.println(": ");
    }
}