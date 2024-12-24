/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.*;
import static java.util.stream.Collectors.toMap;

class Day24 {
    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day24.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 24: Crossed Wires ---");

        // Read all lines from the file
        List<String> inputLines;
        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            inputLines = lines.toList();
        }

        var sections = splitByBlankLine(inputLines);

        out.println("--- Part One ---");

        // Logic gate operations
        Map<String, BiFunction<Integer, Integer, Integer>> operations = Map.of(
                "AND", (a, b) -> a & b,
                "XOR", (a, b) -> a ^ b,
                "OR", (a, b) -> a | b
        );

        // Signal map
        Map<String, String> signalMap = sections.getFirst().stream()
                .collect(toMap(
                        s -> s.split(":")[0],
                        s -> s.split(":")[1].strip()
                ));

        out.println("Signal map:");
        out.println(signalMap);
        out.println("Expressions:");
        out.println(sections.getLast());

        //

        // Track unresolved expressions
        List<String> unresolvedExpressions = new ArrayList<>(sections.getLast());

        // Process expressions until resolved or no progress
        boolean progress;
        do {
            progress = false;
            Iterator<String> iterator = unresolvedExpressions.iterator();

            while (iterator.hasNext()) {
                String expression = iterator.next();
                var parts = expression.split(" ");
                String signal1 = parts[0];
                String operator = parts[1];
                String signal2 = parts[2];
                String resultSignal = parts[4];

                // Check if required signals are available
                if (signalMap.containsKey(signal1) && signalMap.containsKey(signal2)) {
                    int value1 = Integer.parseInt(signalMap.get(signal1));
                    int value2 = Integer.parseInt(signalMap.get(signal2));

                    // Perform operation and update map
                    var operation = operations.get(operator);
                    if (operation == null) {
                        throw new IllegalArgumentException("Unsupported operator: " + operator);
                    }
                    int result = operation.apply(value1, value2);
                    signalMap.put(resultSignal, String.valueOf(result));

                    // Mark this expression as resolved
                    iterator.remove();
                    progress = true;
                }
            }
        } while (progress && !unresolvedExpressions.isEmpty());

        // Check for unresolved expressions
        if (!unresolvedExpressions.isEmpty()) {
            err.println("The following expressions could not be resolved due to missing signals: ");
            unresolvedExpressions.forEach(err::println);
        }

        // Sort the final signal map by key and filter out signals that do not start with 'z'.
        Map<String, String> sortedMapByKey = signalMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(it -> it.getKey().startsWith("z"))
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        // Print the final signal map.
        sortedMapByKey.forEach((key, value) -> out.println(key + " = " + value));

        // Collect values in reversed order to form the binary sequence.
        String binarySequence = sortedMapByKey.values().stream()
                .map(String::valueOf)
                .collect(Collectors.collectingAndThen(
                        Collectors.joining(),
                        sb -> new StringBuilder(sb).reverse().toString()
                ));

        // Convert binary sequence to decimal.
        long decimalValue = Long.parseLong(binarySequence, 2);

        // Output
        out.println("Binary Sequence: " + binarySequence);
        out.println("Decimal Value: " + decimalValue);

        out.println("--- Part Two ---");

    }

    // Split the input lines into sections separated by a blank line.
    private static List<List<String>> splitByBlankLine(List<String> lines) {
        List<List<String>> sections = new ArrayList<>();
        List<String> currentSection = new ArrayList<>();

        for (var line : lines) {
            if (line.isBlank()) {
                if (!currentSection.isEmpty()) {
                    sections.add(new ArrayList<>(currentSection));
                    currentSection.clear();
                }
            } else {
                currentSection.add(line);
            }
        }

        if (!currentSection.isEmpty()) {
            sections.add(currentSection);
        }

        return sections;
    }
}