/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.*;

public class Day16 {

    private static final List<int[]> DIRECTIONS = List.of(
            new int[]{0, 1, 0},  // East
            new int[]{1, 0, 1},  // South
            new int[]{0, -1, 2}, // West
            new int[]{-1, 0, 3}  // North
    );

    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day16.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 16: Reindeer Maze ---");

        out.println("--- Part One ---");

        // `Day16.test` should produce a total sore of 7036.
        // `Day16.test2` should produce a total sore of 11048.

        // Read input into a grid (game map)
        char[][] grid = readInputIntoGrid(Files.lines(Paths.get(args[0])));

        // Print grid
        printGrid(grid);

        int minimalScore = findMinimalScore(grid);

        out.println("Minimal score: " + minimalScore);
    }

    private static char[][] readInputIntoGrid(Stream<String> inputLines) {
        return inputLines
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                out.print(cell + " ");
            }
            out.println();
        }
    }

    public static int findMinimalScore(char[][] grid) {

        // Locate Start (S) and End (E) positions
        int[] start = findPosition(grid, 'S');
        int[] end = findPosition(grid, 'E');

        record State(int row, int col, int direction, int score) {}

        // Priority queue to explore states with the smallest score first
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.score));
        pq.add(new State(start[0], start[1], 0, 0)); // Start facing East

        // Visited map: (row, col, direction) -> minimal score for the state
        Map<String, Integer> visited = new HashMap<>();
        visited.put(encode(start[0], start[1], 0), 0);

        while (!pq.isEmpty()) {
            State current = pq.poll();

            // Reached the end
            if (current.row == end[0] && current.col == end[1]) {
                return current.score;
            }

            // Move forward
            int[] forward = DIRECTIONS.get(current.direction);
            int newRow = current.row + forward[0];
            int newCol = current.col + forward[1];
            String forwardKey = encode(newRow, newCol, current.direction);

            if (isInBounds(grid, newRow, newCol) && grid[newRow][newCol] != '#') {
                int newScore = current.score + 1;
                if (!visited.containsKey(forwardKey) || visited.get(forwardKey) > newScore) {
                    visited.put(forwardKey, newScore);
                    pq.add(new State(newRow, newCol, current.direction, newScore));
                }
            } else if (current.score == 0) {
                // Handle initial turn if forward is blocked
                for (int turn : List.of(1, -1)) {
                    int newDirection = (current.direction + turn + 4) % 4;
                    String turnKey = encode(current.row, current.col, newDirection);
                    int newScore = current.score + 1000;

                    if (!visited.containsKey(turnKey) || visited.get(turnKey) > newScore) {
                        visited.put(turnKey, newScore);
                        pq.add(new State(current.row, current.col, newDirection, newScore));
                    }
                }
            }

            // Add turn states only if necessary
            for (int turn : List.of(1, -1)) {
                int newDirection = (current.direction + turn + 4) % 4;
                String turnKey = encode(current.row, current.col, newDirection);
                int newScore = current.score + 1000;

                if (!visited.containsKey(turnKey) || visited.get(turnKey) > newScore) {
                    visited.put(turnKey, newScore);
                    pq.add(new State(current.row, current.col, newDirection, newScore));
                }
            }
        }

        throw new IllegalStateException("Path to the end not found!");
    }

    private static boolean isInBounds(char[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    private static int[] findPosition(char[][] grid, char target) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == target) {
                    return new int[]{row, col};
                }
            }
        }
        throw new IllegalArgumentException("Position of `" + target + "` not found in the grid.");
    }

    private static String encode(int row, int col, int direction) {
        return row + "," + col + "," + direction;
    }
}