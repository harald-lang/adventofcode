package aoc.day2;

import lombok.val;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

public class Day2 {

    public static void main(String[] args) throws IOException {
        val inputPath = Path.of(Day2.class.getResource("input.txt").getPath());
        val input =  Files.readAllLines(inputPath);

        val validCnt = new MutableInt();
        input.forEach(i -> {
            val split = i.split(" ");

            val rep = split[0].split("-");
            val repMin = parseInt(rep[0]);
            val repMax = parseInt(rep[1]);

            val reqChar = split[1].charAt(0);

            val pwd = split[2];
            val occurrences = pwd.chars().filter(c -> c == reqChar).count();

            if (occurrences >= repMin && occurrences <= repMax) {
                validCnt.increment();
            }

        });
        out.println("part 1: " + validCnt.getValue());

        // Part 2
        validCnt.setValue(0);
        input.forEach(i -> {
            val split = i.split(" ");

            val rep = split[0].split("-");
            val pos1 = parseInt(rep[0]);
            val pos2 = parseInt(rep[1]);

            val reqChar = split[1].charAt(0);

            val pwd = split[2];
            val pos1Matches = pwd.charAt(pos1 - 1) == reqChar;
            val pos2Matches = pwd.charAt(pos2 - 1) == reqChar;

            val isValid = pos1Matches ^ pos2Matches;

            if (isValid) {
                validCnt.increment();
            }

        });
        out.println("part 2: " + validCnt.getValue());
    }

}
