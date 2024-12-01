///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.*;

class Day01 {
    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day01.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 1: Historian Hysteria ---");

        Path path = Paths.get(args[0]);

        List<Integer> listLeft;
        List<Integer> listRight;

        try (Stream<String> lines = Files.lines(path)) {
            var lists = lines.map(line -> line.split("\\s+"))
                    .filter(pairs -> pairs.length == 2)
                    .map(pairs -> List.of(
                            Integer.parseInt(pairs[0]),
                            Integer.parseInt(pairs[1])
                    ))
                    .collect(
                            Collectors.teeing(
                                    Collectors.mapping(p -> p.get(0), Collectors.toList()),
                                    Collectors.mapping(p -> p.get(1), Collectors.toList()),
                                    List::of
                            )
                    );

            listLeft = lists.get(0).stream().sorted().toList();
            listRight = lists.get(1).stream().sorted().toList();
        }

        List<Integer> differences = IntStream
                .range(0, Math.min(listLeft.size(), listRight.size()))
                .mapToObj(i -> Math.abs(listLeft.get(i) - listRight.get(i)))
                .toList();

        int sumOfDifferences = differences.stream()
                .mapToInt(Integer::intValue)
                .sum();

        out.println("--- Part One ---");
        out.println("Sum of differences: " + sumOfDifferences);

        int sumOfProducts = IntStream
                .range(0, Math.min(listLeft.size(), listRight.size()))
                .map(i -> listLeft.get(i) * (int) listRight.stream()
                        .filter(j -> j.equals(listLeft.get(i)))
                        .count())
                .sum();

        out.println("--- Part Two ---");
        out.println("Sum of products: " + sumOfProducts);
    }
}
