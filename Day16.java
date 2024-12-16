/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.lang.System.*;

public class Day16 {

    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day16.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 16: Reindeer Maze ---");

        out.println("--- Part One ---");

        // Day16.test should be producing the total sore of 7036.
        // Day16.test should be producing the total sore of 11048.

        // Read input into a grid (game map)
        char[][] grid = readInputIntoGrid(Files.lines(Paths.get(args[0])));

        // Print grid
        printGrid(grid);


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

}