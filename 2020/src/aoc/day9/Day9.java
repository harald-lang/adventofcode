package aoc.day9;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.System.exit;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Day9 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day9.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);
        val intInput = inputs.stream().map(Long::parseLong).collect(toList());

        val preambleLength = 25;
        val prev = new long[preambleLength];

        long weakValue = -1;

        // read the preamble
        for (int i = 0; i < preambleLength; ++i) {
            prev[i] = intInput.get(i);
        }

        for (int i = preambleLength; i < intInput.size(); ++i) {
            val value = intInput.get(i);
            if (!isValid(prev, value)) {
                out.println("part 1: " + value);
                weakValue = value;
                break;
            }
            prev[i % preambleLength] = value;
        }


        // Find contiguous set that sums up to the weak value.
        for (int b = 0; b < intInput.size(); ++b) {
            for (int e = b + 2; e < intInput.size(); ++e) {
                val s = sum(intInput, b, e);
                val minVal = min(intInput, b, e);
                val maxVal = max(intInput, b, e);
//                out.println(b + ", " + e + " -> " + s + "  - " + (intInput.get(b) + intInput.get(e - 1)));
                if (s == weakValue) {
                    out.println("part 2: " + (minVal + maxVal));
                    exit(0);
                }
            }
        }
    }

    private static boolean isValid(long[] prev, long value) {
        for (int i = 0; i < prev.length; ++i) {
            for (int j = i + 1; j < prev.length; ++j) {
                if (prev[i] + prev[j] == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private static long sum(List<Long> values, int begin, int end) {
        long s = 0;
        for (int i = begin; i < end; ++i) {
            s += values.get(i);
        }
        return s;
    }

    private static long min(List<Long> values, int begin, int end) {
        long min = Long.MAX_VALUE;
        for (int i = begin; i < end; ++i) {
            min = Long.min(min, values.get(i));
        }
        return min;
    }

    private static long max(List<Long> values, int begin, int end) {
        long max = Long.MIN_VALUE;
        for (int i = begin; i < end; ++i) {
            max = Long.max(max, values.get(i));
        }
        return max;
    }

}
