/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.System.*;

public class Day13 {

    public record Button(String name, int x, int y) {}
    public record Prize(int x, int y) {}
    public record GameRound(Button buttonA, Button buttonB, Prize prize) {}

    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day13.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 13: Claw Contraption ---");

        Path filePath = Paths.get(args[0]);

        // Regular expressions to extract the data
        Pattern buttonPattern = Pattern.compile("Button (\\w): X\\+(\\d+), Y\\+(\\d+)");
        Pattern prizePattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

        // Read and parse the file
        List<GameRound> gameRoundList = parseFile(filePath, buttonPattern, prizePattern);

        // Print the parsed records
        gameRoundList.forEach(out::println);

        out.println("--- Part One ---");

        int totalTokens = 0;

        // Process each game round
        for (GameRound round : gameRoundList) {
            out.print("Game round: ");

            int minTokens = calculateMinTokens(round);
            if (minTokens == Integer.MAX_VALUE) {
                out.println("No solution found for this round.");
            } else {
                out.println("Minimum tokens for this round: " + minTokens);
                totalTokens += minTokens;
            }
        }

        out.println("Total tokens to win all rounds: " + totalTokens);

    }

    private static List<GameRound> parseFile(
            Path filePath,
            Pattern buttonPattern,
            Pattern prizePattern
    ) throws IOException {
        List<GameRound> gameRounds = new ArrayList<>();
        List<String> currentBlock = new ArrayList<>();

        // Read lines and group them into blocks separated by blank lines
        try (Stream<String> lines = Files.lines(filePath)) {
            for (String line : lines.toList()) {
                if (line.isBlank()) {
                    // Process the current block when a blank line is encountered
                    if (!currentBlock.isEmpty()) {
                        gameRounds.add(parseGameRound(currentBlock, buttonPattern, prizePattern));
                        currentBlock.clear();
                    }
                } else {
                    currentBlock.add(line);
                }
            }
            // Process the last block if the file does not end with a blank line
            if (!currentBlock.isEmpty()) {
                gameRounds.add(parseGameRound(currentBlock, buttonPattern, prizePattern));
            }
        }

        return gameRounds;
    }

    private static GameRound parseGameRound(
            List<String> lines,
            Pattern buttonPattern,
            Pattern prizePattern
    ) {
        Button buttonA = null;
        Button buttonB = null;
        Prize prize = null;

        // Parse each line
        for (String line : lines) {
            Matcher buttonMatcher = buttonPattern.matcher(line);
            Matcher prizeMatcher = prizePattern.matcher(line);

            if (buttonMatcher.matches()) {
                String name = buttonMatcher.group(1);
                int x = Integer.parseInt(buttonMatcher.group(2));
                int y = Integer.parseInt(buttonMatcher.group(3));
                if ("A".equals(name)) {
                    buttonA = new Button(name, x, y);
                } else if ("B".equals(name)) {
                    buttonB = new Button(name, x, y);
                }
            } else if (prizeMatcher.matches()) {
                int x = Integer.parseInt(prizeMatcher.group(1));
                int y = Integer.parseInt(prizeMatcher.group(2));
                prize = new Prize(x, y);
            }
        }

        // Ensure all parts are parsed
        if (buttonA == null || buttonB == null || prize == null) {
            throw new IllegalArgumentException("Incomplete data for a game round: " + lines);
        }

        return new GameRound(buttonA, buttonB, prize);
    }

    private static int calculateMinTokens(GameRound round) {
        Button buttonA = round.buttonA();
        Button buttonB = round.buttonB();
        Prize prize = round.prize();

        int minTokens = Integer.MAX_VALUE;

        // Brute-force: iterate over all possible presses for Button A and Button B
        for (int a = 0; a <= 100; a++) {
            for (int b = 0; b <= 100; b++) {
                int totalX = (a * buttonA.x()) + (b * buttonB.x());
                int totalY = (a * buttonA.y()) + (b * buttonB.y());

                // Check if this combination matches the prize
                if (totalX == prize.x() && totalY == prize.y()) {
                    int cost = (3 * a) + b;
                    minTokens = Math.min(minTokens, cost);
                }
            }
        }

        return minTokens; // Will be Integer.MAX_VALUE if no solution is found
    }
}
