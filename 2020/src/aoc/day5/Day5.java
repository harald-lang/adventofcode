package aoc.day5;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;

import static java.lang.System.out;
import static java.util.stream.IntStream.range;

public class Day5 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day5.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);

        val occupiedSeats = new BitSet(2 << 10);

        // Each input line is basically a weirdly encoded integer, where R and B represent 1 binary digits, and F and L
        // 0 digits.
        inputs.forEach(input -> {
            // Convert to an integer
            var seatId = 0;
            for (int i = 0; i < 10; ++i) {
                val c = input.charAt(i);
                val b = (c == 'R' || c == 'B') ? 1 : 0;
                seatId = seatId | (b << (9 - i));
            }
            // ... and memoize it.
            occupiedSeats.set(seatId);
        });

        val minSeatId = occupiedSeats.nextSetBit(0);
        val maxSeatId = occupiedSeats.previousSetBit(occupiedSeats.length() - 1);
        out.println("part 1: " + maxSeatId);

        range(minSeatId, maxSeatId).forEach(sid -> {
            if (!occupiedSeats.get(sid) && occupiedSeats.get(sid - 1) && occupiedSeats.get(sid + 1) ) {
                out.println("part 2: " + sid);
            }
        });

    }

}
