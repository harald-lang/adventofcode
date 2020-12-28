package aoc.day24;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Day24Part1 {

    static final HexagonCoordinate ORIGIN = HexagonCoordinate.of(0, 0);

    public static void main(String[] args) throws Exception {
        val m = System.currentTimeMillis();
        val inputPath = Paths.get(Day24Part1.class.getResource("input.txt").toURI());
        val input = Files.readAllLines(inputPath);
        val hexPaths = input.stream()
                .map(HexagonUtils::parsePath)
                .collect(toList());

        val grid = new HashSet<HexagonCoordinate>();
        for (val hexPath : hexPaths) {
            var tile = ORIGIN;
            for (val direction : hexPath) {
                tile = tile.in(direction);
            }
            if (grid.contains(tile)) {
                grid.remove(tile);
            }
            else {
                grid.add(tile);
            }
        }

        int part1 = grid.size();
        out.println("part 1: " + part1);
        out.println("runtime: " + (System.currentTimeMillis() - m) + " [ms]");
    }

}