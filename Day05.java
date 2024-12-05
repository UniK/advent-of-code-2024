/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.System.*;

class Day05 {
    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day05.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 5: Print Queue ---");

        // Read all lines from the file
        List<String> inputLines;
        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            inputLines = lines.toList();
        }

        out.println("--- Part One ---");

        // Split the input into two sections: before and after the blank line
        var sections = splitByBlankLine(inputLines);

        List<String> pageOrderingRules = sections.get(0); // First section
        List<String> pageUpdates = sections.get(1); // Second section

        List<String> positive = pageUpdates.stream()
                .filter(update -> isInExpectedOrder(update, pageOrderingRules))
                .toList();

        long sum = positive.stream()
                .mapToInt(Day05::findMiddleElement)
                .sum();

        out.printf("Sum: %d%n", sum);

        out.println("--- Part Two ---");

        out.printf("Sum: %d%n", 0);
    }

    private static int findMiddleElement(String number) {
        String[] split = number.split(",");
        int middleIndex = split.length / 2;

        return Integer.parseInt(split[middleIndex]);
    }

    private static boolean isInExpectedOrder(String update, List<String> pageOrderingRules) {
        String[] pages = update.split(",");
        out.println("Pages*: " + Arrays.toString(pages));

        for (String rule : pageOrderingRules) {
            String[] ruleSet = rule.split("\\|");

            // skip to the next rule if the current rule is not applicable for the current update
            if (!update.contains(ruleSet[0]) || !update.contains(ruleSet[1])) {
                // out.println("\t\tRule ignored: " + ruleSet[0] + " < " + ruleSet[1]);
                continue;
            }

            // rule is not satisfied, return false
            if (update.indexOf(ruleSet[0]) > update.indexOf(ruleSet[1])) {
                // out.println("\tRule not satisfied: " + ruleSet[0] + " < " + ruleSet[1]);
                return false;
            }
            // out.println("\t+: " + ruleSet[0] + " < " + ruleSet[1]);
        }
        return true;
    }

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