/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.System.*;

public class Day11 {

    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day11.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 11: Plutonian Pebbles ---");

        // Read all lines from the file
        List<String> inputLines;
        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            inputLines = lines.toList();
        }

        out.println("--- Part One ---");

        List<String> initialList = List.of(inputLines.getFirst().split(" "));
        out.println("Initial list: " + initialList);

        List<String> stones = blinking(initialList, 25);
        out.println("Stones count: " + stones.size());

    }

    private static List<String> blinking(List<String> list, int times) {
        List<String> result = List.copyOf(list);
        // blink `times` times
        for (int i = 1; i <= times; i++) {
            out.println("After blink #" + i);

            List<String> newList  = result.stream()
                    .flatMap(it -> {
                        if (it.equals("0")) {
                            return Stream.of("1");
                        } else if (it.length() % 2 == 0) {
                            return Stream.of(
                                    trimIntValue(it.substring(0, it.length() / 2)),
                                    trimIntValue(it.substring(it.length() / 2))
                            );
                        } else {
                            return Stream.of(String.valueOf(Long.parseLong(it) * 2024));
                        }

                    })
                    .toList();
            out.println(newList);
            result = newList;
        }

        return result;
    }

    private static String trimIntValue(String binary) {
        return String.valueOf(Integer.parseInt(binary));
    }
}
