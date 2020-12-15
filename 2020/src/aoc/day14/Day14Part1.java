package aoc.day14;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static java.lang.System.out;

public class Day14Part1 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day14Part1.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);

        var copyMask = 0L;
        var override = 0L;
        val mem = new HashMap<Long, Long>();
        for(var line : inputs) {
                if (line.startsWith("mask")) {
                val maskString = line.split(" ")[2];
                copyMask = parseCopyMask(maskString);
                override = parseOverrideValue(maskString);
            }
            else if (line.startsWith("mem")) {
                val addr = Long.parseLong(line.replaceAll("\\[", " ").replaceAll("\\]", " ").split(" ")[1]);
                val value = Long.parseLong(line.split(" ")[2]);
                mem.put(addr, (value & copyMask) | override);
            }
            else {
                throw new IllegalArgumentException("Invalid input.");
            }
        }

        val part1 = mem.values().stream().reduce(0L, Long::sum);
        out.println("part 1: " + part1); // expected result 9615006043476
    }

    private static long parseCopyMask(String maskString) {
        return Long.parseLong(maskString.replaceAll("1", "0").replaceAll("X", "1"), 2);
    }

    private static long parseOverrideValue(String maskString) {
        return Long.parseLong(maskString.replaceAll("X", "0"), 2);
    }

}
