/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.*;

class Day25 {
    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day25.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 25: Code Chronicle ---");
        out.println("--- Part One ---");

        // Read from file.
        List<List<String>> grids = readGridsFromFile(args[0]);

        // Rotate each grid 90 degrees counterclockwise.
        List<List<String>> rotatedGrids = grids.stream()
                .map(Day25::rotateCounterClockwise)
                .toList();

        // Print rotated grids.
        out.println("Rotated grids:");
        for (List<String> rotatedGrid : rotatedGrids) {
            rotatedGrid.forEach(out::println);
            out.println();
        }

        // Split the list of grids into two lists, one starting with a '#' and the other starting with a '.'.
        List<List<String>> gridsStartingWithHash = rotatedGrids.stream()
                .filter(grid -> grid.getFirst().startsWith("#"))
                .toList();

        List<List<String>> gridsStartingWithDot = rotatedGrids.stream()
                .filter(grid -> grid.getFirst().startsWith("."))
                .toList();

        // Print the two lists of grids (locks and keys).
        out.println("Grids starting with '#' (locks):");
        gridsStartingWithHash.forEach(grid -> {
            grid.forEach(out::println);
            out.println();
        });

        out.println("Grids starting with '.' (keys):");
        gridsStartingWithDot.forEach(grid -> {
            grid.forEach(out::println);
            out.println();
        });

        // Count hashes (excluding first occurrence) for grids starting with '#'.
        List<List<Integer>> hashCounts = gridsStartingWithHash.stream()
                .map(Day25::countOccurrencesAfterFirst)
                .toList();

        // Count dots (excluding first occurrence) for grids starting with '.'.
        List<List<Integer>> dotCounts = gridsStartingWithDot.stream()
                .map(Day25::countOccurrencesAfterFirst)
                .toList();

        // Print the results.
        out.println("Hash (locks) counts:");
        hashCounts.forEach(out::println);

        out.println("Dot (keys) counts:");
        dotCounts.forEach(out::println);

        // Perform the verification between hashCounts and dotCounts
        int successfulVerifications = performCrossVerification(hashCounts, dotCounts);

        // Print the number of successful verifications
        System.out.println("Total successful verifications: " + successfulVerifications);
    }

    // Rotate the block of text 90 degrees counterclockwise.
    private static List<String> rotateCounterClockwise(List<String> grid) {
        int rows = grid.size();
        int cols = grid.getFirst().length();

        return IntStream.range(0, cols)
                .mapToObj(col ->
                        grid.stream()
                                .map(s -> String.valueOf(s.charAt(cols - 1 - col)))
                                .collect(Collectors.joining()))
                .toList();
    }

    private static List<List<String>> readGridsFromFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));

        List<List<String>> grids = new ArrayList<>();
        List<String> currentGrid = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank()) {
                // If a blank line is encountered, save the current grid and start a new one
                if (!currentGrid.isEmpty()) {
                    grids.add(new ArrayList<>(currentGrid));
                    currentGrid.clear();
                }
            } else {
                currentGrid.add(line);
            }
        }

        // Add the last grid if not already added
        if (!currentGrid.isEmpty()) {
            grids.add(currentGrid);
        }

        return grids;
    }

    private static List<Integer> countOccurrencesAfterFirst(List<String> grid) {
        return IntStream.range(0, grid.size())
                .mapToObj(i -> {
                    String row = grid.get(grid.size() - 1 - i); // Process rows in reverse order
                    char firstChar = row.charAt(0);

                    if (firstChar == '.') {
                        // Skip the first character (.) and the last character (#), count '#'
                        return (int) row.chars()
                                .skip(1)
                                .limit(row.length() - 2)
                                .filter(c -> c == '#')
                                .count();
                    } else {
                        // Skip the first character (#), count consecutive '#'
                        return (int) row.chars()
                                .skip(1)
                                .filter(c -> c == '#') // Count '#'
                                .count();
                    }
                })
                .toList();
    }

    public static int performCrossVerification(List<List<Integer>> hashCounts, List<List<Integer>> dotCounts) {
        int successfulVerifications = 0;

        // Iterate through each list from hashCounts
        for (List<Integer> hashList : hashCounts) {
            // Iterate through each list from dotCounts and perform the verification
            for (List<Integer> dotList : dotCounts) {

                out.println("- Hash: " + hashList + " Dot: " + dotList);
                boolean isValid = true;

                // Check if the sum of corresponding elements smaller or equals 5.
                for (int i = 0; i < 5; i++) {
                    if (hashList.get(i) + dotList.get(i) > 5) {
                        isValid = false;
                        out.println("\tVerification failed at index " + i + " with sum " + (hashList.get(i) + dotList.get(i)));
                        break;
                    }
                }

                if (isValid) {
                    successfulVerifications++;
                }
            }
        }

        return successfulVerifications;
    }
}