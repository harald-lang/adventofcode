package aoc.day15;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Day15 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day15.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);

        val ints = Arrays.stream(inputs.get(inputs.size() - 1).split(","))
                .map(Integer::parseInt)
                .collect(toList());

        val idx = new HashMap<Integer, Integer>();
        for (int turn = 0; turn < ints.size() - 1; ++turn) {
            idx.put(ints.get(turn), turn);
        }

        val initNumberCnt = ints.size();
//        val turnCnt = 2020; // part 1
        val turnCnt = 30000000; // part 2

        int prevSpokenNo = ints.get(initNumberCnt - 1);
        for (int turn = initNumberCnt; turn < turnCnt; ++turn) {
            val tmp = prevSpokenNo;
            val j = idx.get(tmp);
            if (j == null) {
                prevSpokenNo = 0;
            }
            else {
                prevSpokenNo = turn - 1 - j;
            }
            idx.put(tmp, turn - 1);
        }

        out.println("part x: " + prevSpokenNo);
    }

}
