package aoc.day25;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.out;

public class Day25 {

    public static final int MOD = 20201227;

    static int determineLoopSize(int subjectNo, int pk) {
        var value = subjectNo;
        var loopSize = 1;
        while (value != pk) {
            value = (value * subjectNo)  % MOD;
            loopSize++;
        }
        return loopSize;
    }

    static int transform(int subjectNo, int loopSize) {
        var value = 1L;
        for (int i = 0; i < loopSize; i++) {
            value = (value * subjectNo) % MOD;
        }
        return (int)value;
    }

    public static void main(String[] args) throws Exception {
        val m = System.currentTimeMillis();
        val inputPath = Paths.get(Day25.class.getResource("input.txt").toURI());
        val input = Files.readAllLines(inputPath);

        val subjectNo = 7;

        val cardPk = Integer.parseInt(input.get(0));
        val doorPk = Integer.parseInt(input.get(1));

        val cardLoopSize = determineLoopSize(subjectNo, cardPk);
        out.println(cardLoopSize);
        val doorLoopSize = determineLoopSize(subjectNo, doorPk);
        out.println(doorLoopSize);

        val encryptionKey = transform(doorPk, cardLoopSize);

        val part1 = encryptionKey;
        out.println("part 1: " + part1);
        out.println("runtime: " + (System.currentTimeMillis() - m) + " [ms]");
    }

}
