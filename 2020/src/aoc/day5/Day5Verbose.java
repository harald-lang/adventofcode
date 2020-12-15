package aoc.day5;

import lombok.val;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeSet;

import static java.lang.System.out;
import static java.util.stream.IntStream.range;

public class Day5Verbose {

    public static void main(String[] args) throws IOException {
        val inputPath = Path.of(Day5Verbose.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);

        val seats = new TreeSet<Integer>();

        inputs.forEach(input -> {

            val rowBegin = new MutableInt();
            val rowEnd = new MutableInt(128);

            range(0, 7).forEach(i -> {
                val rowSpan = rowEnd.getValue() - rowBegin.getValue();
                val rowHalfSpan = rowSpan / 2;
                switch (input.charAt(i)) {
                    case 'F':
                        rowEnd.add(-rowHalfSpan);
                        break;
                    case 'B':
                        rowBegin.add(rowHalfSpan);
                        break;
                    default:
                        throw new IllegalArgumentException("invalid input");
                }
            });


            val colBegin = new MutableInt();
            val colEnd = new MutableInt(8);

            range(7, 10).forEach(i -> {
                val colSpan = colEnd.getValue() - colBegin.getValue();
                val colHalfSpan = colSpan / 2;
                switch (input.charAt(i)) {
                    case 'L':
                        colEnd.add(-colHalfSpan);
                        break;
                    case 'R':
                        colBegin.add(colHalfSpan);
                        break;
                    default:
                        throw new IllegalArgumentException("invalid input");
                }
            });

            val seatId = rowBegin.getValue() * 8 + colBegin.getValue();

            seats.add(seatId);

        });

        val minSeatId = seats.first();
        val maxSeatId = seats.last();
        out.println("part 1: " + maxSeatId);

        range(minSeatId, maxSeatId).forEach(sid -> {
            if (seats.contains(sid)) {
                return;
            }
            if (seats.contains(sid - 1) && seats.contains(sid + 1) ) {
                out.println("part 2: " + sid);
            }
        });
    }

}
