package aoc.day1;

import lombok.val;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Day1 {

    public static void main(String[] args) throws IOException {
        val inputPath = Path.of(Day1.class.getResource("input.txt").getPath());
        val input =  Files.readAllLines(inputPath);
        val intInput = input.stream().map(Integer::parseInt).collect(toList());

        // Part 1
        val part1 = new MutableInt(0);
        intInput.forEach(i1 -> {
            intInput.forEach(i2 -> {
                if (i1 + i2 == 2020) {
                    part1.setValue(i1 * i2);
                }
            });
        });
        out.println("part 1: " + part1.getValue());

        // Part 2
        val part2 = new MutableInt(0);
        intInput.forEach(i1 -> {
            intInput.forEach(i2 -> {
                intInput.forEach(i3 -> {
                    if (i1 + i2 + i3 == 2020) {
                        part2.setValue(i1 * i2 * i3);
                    }
                });
            });
        });
        out.println("part 2: " + part2.getValue());
    }

}
