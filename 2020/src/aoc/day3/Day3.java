package aoc.day3;

import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiFunction;

import static java.lang.System.out;

public class Day3 {

    public static void main(String[] args) throws IOException {
        val inputPath = Path.of(Day3.class.getResource("input.txt").getPath());
        val input =  Files.readAllLines(inputPath);

        val mapWidth = input.get(0).length();
        val mapHeight = input.size();

        final BiFunction<Integer, Integer, Boolean> isTree = (x, y) -> {
            val xm = x % mapWidth;
            return input.get(y).charAt(xm) == '#';
        };

        final BiFunction<Integer, Integer, Long> cntTrees = (dx, dy) ->  {
            var x = 0;
            var y = 0;
            var treeCnt = 0L;
            while (y < mapHeight) {
                if (isTree.apply(x, y)) {
                    treeCnt++;
                }
                x += dx;
                y += dy;
            }
            return treeCnt;
        };
        out.println("part 1: " + cntTrees.apply(3, 1));

        out.println("part 2: " +
                (cntTrees.apply(1, 1)
                * cntTrees.apply(3, 1)
                * cntTrees.apply(5, 1)
                * cntTrees.apply(7, 1)
                * cntTrees.apply(1, 2))
        );
    }

}
