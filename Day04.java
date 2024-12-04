///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.*;

class Day04 {
    private static final String TARGET = "XMAS";
    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day04.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 4: Ceres Search ---");

        List<String> inputLines;
        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            inputLines = lines.toList();
        }

        out.println("--- Part One ---");

        char[][] grid = inputLines.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int count = countOccurrences(grid);

        out.printf("Count of XMAS: %d%n", count);

        out.println("--- Part Two ---");

        out.printf("Sum of multiplications with mutex: %d%n", 0);
    }

    public static int countOccurrences(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Directions for traversal (dx, dy)
        List<int[]> directions = List.of(
                new int[]{0, 1},   // Horizontal right
                new int[]{0, -1},  // Horizontal left
                new int[]{1, 0},   // Vertical down
                new int[]{-1, 0},  // Vertical up
                new int[]{1, 1},   // Diagonal down-right
                new int[]{1, -1},  // Diagonal down-left
                new int[]{-1, 1},  // Diagonal up-right
                new int[]{-1, -1}  // Diagonal up-left
        );

        BiFunction<Integer, Integer, Boolean> isValid = (r, c) ->
                   r >= 0
                && c >= 0
                && r < rows
                && c < cols;

        return IntStream.range(0, rows)
                .map(row -> IntStream.range(0, cols)
                        .map(col -> directions.stream()
                                .mapToInt(dir -> {
                                    int dx = dir[0], dy = dir[1];
                                    return matches(grid, row, col, dx, dy, isValid)
                                            ? 1
                                            : 0;
                                }).sum()
                        ).sum()
                ).sum();
    }

    private static boolean matches(
            char[][] grid,
            int row,
            int col,
            int dx,
            int dy,
            BiFunction<Integer, Integer, Boolean> isValid
    ) {
        for (int i = 0; i < TARGET.length(); i++) {
            int r = row + i * dx, c = col + i * dy;
            if (!isValid.apply(r, c) || grid[r][c] != TARGET.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}