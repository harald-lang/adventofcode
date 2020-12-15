package aoc.day6;

import lombok.val;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;

import static java.lang.System.out;
import static java.util.Arrays.fill;
import static java.util.Arrays.stream;

public class Day6 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day6.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);
        inputs.add("");

        val markedYes = new BitSet(26);
        val sum = new MutableInt(0);

        val groupSize = new MutableInt(0);
        val markedYesCnt = new int[26];
        val sumPart2 = new MutableLong(0L);

        inputs.forEach(input -> {
            if (input.isEmpty()) {
                sum.add(markedYes.cardinality());
                markedYes.clear();

                sumPart2.add(
                        stream(markedYesCnt)
                                .filter(c -> c == groupSize.getValue())
                                .count());
                fill(markedYesCnt, 0);
                groupSize.setValue(0);
                return;
            }

            groupSize.increment();

            input.chars().forEach(c -> {
                markedYes.set(c - 'a');
                markedYesCnt[c - 'a']++;
            });
        });

        out.println("part 1: " + sum.getValue());
        out.println("part 2: " + sumPart2.getValue());
    }

}
