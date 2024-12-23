import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.*;

public class Day23 {
    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day23.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 23: LAN Party ---");

        // Read all lines from the file
        List<String> inputLines;
        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            inputLines = lines.toList();
        }

        out.println("--- Part One ---");

        var connections = inputLines.stream()
                .map(line -> line.split("-"))
                .toList();

        Map<String, Set<String>> graph = new HashMap<>();
        for (var connection : connections) {
            graph.computeIfAbsent(connection[0], k -> new HashSet<>()).add(connection[1]);
            graph.computeIfAbsent(connection[1], k -> new HashSet<>()).add(connection[0]);
        }

        var trios = graph.keySet().stream()
                .flatMap(node -> graph.get(node).stream()
                        .flatMap(neighbor -> graph.get(node).stream()
                                .filter(other -> !other.equals(neighbor) && graph.get(neighbor).contains(other))
                                .map(other -> Set.of(node, neighbor, other))))
                .distinct()
                .toList();

        var triosWithT = trios.stream()
                .filter(triangle -> triangle.stream()
                        .anyMatch(name -> name.startsWith("t")))
                .toList();

        out.println("Total trios: " + trios.size());
        out.println("Trios with a 't'-starting node: " + triosWithT.size());
        triosWithT.forEach(out::println);
    }
}