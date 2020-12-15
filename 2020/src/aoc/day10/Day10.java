package aoc.day10;

import lombok.val;
import org.apache.commons.lang3.mutable.MutableInt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Day10 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day10.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);
        val intInput = inputs.stream()
                .map(Integer::parseInt)
                .sorted()
                .collect(toList());


        val max = intInput.get(intInput.size() - 1);
        intInput.add(0 , 0); // the charging outlet
        intInput.add(max + 3); // the device

        val diff1Cnt = new MutableInt(0);
        val diff3Cnt = new MutableInt(0);

        val prev = new MutableInt(intInput.get(0));

        intInput.stream().skip(1).forEach(i -> {
            val diff = i - prev.getValue();
            if (diff == 1) {
                diff1Cnt.increment();
            }
            else if (diff == 3) {
                diff3Cnt.increment();
            }
            prev.setValue(i);
        });

        out.println("part 1: " + (diff1Cnt.getValue() * diff3Cnt.getValue()));
        out.println("part 2: " + rec(intInput, 0));
    }

    private static final Map<Integer, Long> dpTable = new HashMap<>();

    private static long rec(List<Integer> ints, int curIdx) {
        if (curIdx == ints.size() - 1) {
            return 1;
        }
        val curVal = ints.get(curIdx);

        long cnt = 0;
        for (int i = curIdx + 1; i < ints.size(); i++) {
            val nextVal = ints.get(i);
            if (nextVal - 3 <= curVal) {
                if (dpTable.containsKey(i)) {
                    cnt += dpTable.get(i);
                }
                else {
                    cnt += rec(ints, i);
                }
            }
            else {
                break;
            }
        }
        dpTable.put(curIdx, cnt);
        return cnt;
    }

}
