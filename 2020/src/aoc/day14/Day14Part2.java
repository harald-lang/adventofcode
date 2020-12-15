package aoc.day14;

import lombok.val;
import org.apache.commons.lang3.mutable.MutableLong;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static java.lang.System.out;

public class Day14Part2 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day14Part2.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);

        val copyMask = new MutableLong(0);
        val setMask = new MutableLong(0);
        val floatingMask = new MutableLong(0);
        val mem = new HashMap<Long, Long>();
        inputs.forEach(line -> {
            if (line.startsWith("mask")) {
                val maskString = line.split(" ")[2];
                copyMask.setValue(parseCopyMask(maskString));
                setMask.setValue(parseSetMask(maskString));
                floatingMask.setValue(parseFloatingMask(maskString));
            }
            else if (line.startsWith("mem")) {
                val litAddr = Long.parseLong(line.replaceAll("\\[", " ").replaceAll("\\]", " ").split(" ")[1]);
                val floatingAddr = (litAddr & copyMask.getValue()) | setMask.getValue();
                val value = Long.parseLong(line.split(" ")[2]);
                // write
                val popCnt = Long.bitCount(floatingMask.getValue());
                for (long i = 0; i < 1L << popCnt; ++i) {
                    val f = bitDeposit(floatingMask.getValue(), i);
                    val a = floatingAddr | f;
                    mem.put(a, value);
                }
            }
            else {
                throw new IllegalArgumentException("Invalid input.");
            }
        });

        val part2 = mem.values().stream().reduce(0L, Long::sum);
        out.println("part 2: " + part2); // expected result 4275496544925
    }

    private static long parseCopyMask(String maskString) {
        return ~Long.parseLong(maskString.replaceAll("1", "X").replaceAll("X", "1"), 2);
    }

    private static long parseSetMask(String maskString) {
        return Long.parseLong(maskString.replaceAll("X", "0"), 2);
    }

    private static long parseFloatingMask(String maskString) {
        return Long.parseLong(maskString.replaceAll("1", "0").replaceAll("X", "1"), 2);
    }

    // A quite efficient implementation of the pdep instruction.
    // See https://software.intel.com/sites/landingpage/IntrinsicsGuide/#text=pdep&expand=4152,4152 for details.
    private static long bitDeposit(final long mask, final long src) {
        if (src == 0L || mask == 0L) {
            return 0L;
        }
        var m = mask;
        var srcIdx = 0;
        var r = 0L;
        for (var i = 0; i < Long.bitCount(mask); i++) {
            val lsbIdx = Long.numberOfTrailingZeros(m);
            val b = ((src >> srcIdx) & 1L) << lsbIdx;
            r |= b;
            m ^= (1L << lsbIdx);
            srcIdx++;
        }
        return r;
    }

}
