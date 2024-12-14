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

    public record Button(String name, long x, long y) {}
    public record Prize(long x, long y) {}
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

        long totalTokens = 0;

        for (GameRound round : gameRoundList) {
            out.print("Game round: ");

            long minTokens = calculateMinTokens(round);
            if (minTokens == Long.MAX_VALUE) {
                System.out.println("No valid solution for this round.");
            } else {
                System.out.println("Minimum tokens for this round: " + minTokens);
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
                long x = Long.parseLong(prizeMatcher.group(1));
                long y = Long.parseLong(prizeMatcher.group(2));
                prize = new Prize(x, y);
            }
        }

        // Ensure all parts are parsed
        if (buttonA == null || buttonB == null || prize == null) {
            throw new IllegalArgumentException("Incomplete data for a game round: " + lines);
        }

        return new GameRound(buttonA, buttonB, prize);
    }

    private static long calculateMinTokens(GameRound round) {
        Button buttonA = round.buttonA();
        Button buttonB = round.buttonB();
        Prize prize = round.prize();

        long xA = buttonA.x(), yA = buttonA.y();
        long xB = buttonB.x(), yB = buttonB.y();
        long xPrize = prize.x(), yPrize = prize.y();

        // Compute GCDs
        long gcdX = gcd(xA, xB);
        long gcdY = gcd(yA, yB);

        // Check if solution exists
        if (xPrize % gcdX != 0 || yPrize % gcdY != 0) {
            return Long.MAX_VALUE; // No solution
        }

        // Reduce problem size
        xA /= gcdX; xB /= gcdX; xPrize /= gcdX;
        yA /= gcdY; yB /= gcdY; yPrize /= gcdY;

        long minTokens = Long.MAX_VALUE;

        // Modular arithmetic to jump directly to valid a
        for (long a = 0; a <= 1000_000_000_000L; a++) { // Adjust as needed
            long remainingX = xPrize - (a * xA);
            long remainingY = yPrize - (a * yA);

            if (remainingX >= 0 && remainingY >= 0 && remainingX % xB == 0 && remainingY % yB == 0) {
                long b = remainingX / xB;
                if (remainingY == b * yB) {
                    long cost = (3 * a) + b;
                    minTokens = Math.min(minTokens, cost);
                }
            }
        }

        return minTokens;
    }

    // Helper function to compute GCD
    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
