/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.System.*;

public class Day09 {

    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            err.println("Usage: Day09.java <path-to-input-file>");
            exit(1);
        }

        out.println("--- Day 9: Disk Fragmenter ---");

        // Read all lines from the file
        List<String> inputLines;
        try (Stream<String> lines = Files.lines(Paths.get(args[0]))) {
            inputLines = lines.toList();
        }

        out.println("--- Part One ---");

        String diskMap = inputLines.getFirst(); //"12345";
        out.println("Input Disk Map: " + diskMap);

        // Parse disk map
        List<String> blocks = parseDiskMap(diskMap);
        out.println("Parsed Blocks: " + blocks);

        // Compact the disk
        List<String> compacted = compactDisk(blocks);
        out.println("Compacted Disk: " + compacted);

        // Calculate checksum
        long checksum = calculateChecksum(compacted);
        out.println("Filesystem Checksum: " + checksum);
    }

    // Parse the disk map into a list of blocks
    private static List<String> parseDiskMap(String diskMap) {
        List<String> result = new ArrayList<>();
        int fileId = 0;

        for (int i = 0; i < diskMap.length(); i++) {
            int length = Character.getNumericValue(diskMap.charAt(i));
            if (i % 2 == 0) { // File blocks
                for (int j = 0; j < length; j++) {
                    result.add(String.valueOf(fileId));
                }
                fileId++;
            } else { // Free space
                for (int j = 0; j < length; j++) {
                    result.add(".");
                }
            }
        }
        return result;
    }

    // Compact the disk by moving file blocks to the left
    private static List<String> compactDisk(List<String> blocks) {
        List<String> result = new ArrayList<>(blocks); // Create a mutable copy of the blocks list

        // Perform swaps until no free spaces exist between file blocks
        while (true) {
            int freeIndex = result.indexOf("."); // Find the leftmost free space
            int fileIndex = -1;

            // Find the rightmost file block that appears after the free space
            for (int i = result.size() - 1; i > freeIndex; i--) {
                if (!result.get(i).equals(".")) {
                    fileIndex = i;
                    break;
                }
            }

            // If no more file blocks exist to move, we're done
            if (fileIndex == -1) {
                break;
            }

            // Swap the file block with the leftmost free space
            result.set(freeIndex, result.get(fileIndex));
            result.set(fileIndex, ".");
        }

        return result;
    }

    // Calculate the filesystem checksum
    private static long calculateChecksum(List<String> compacted) {
        long checksum = 0;

        List<String> digitsOnly = compacted.subList(0, compacted.indexOf("."));

        for (int i = 0; i < digitsOnly.size(); i++) {
            int fileId = Integer.parseInt(digitsOnly.get(i));
            checksum += (long) i * fileId;
        }

        return checksum;
    }
}