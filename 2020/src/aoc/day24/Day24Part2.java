package aoc.day24;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;


public class Day24Part2 {

    static final HexagonCoordinate ORIGIN = HexagonCoordinate.of(0, 0);

    enum TileColor {

        BLACK,
        WHITE;

        TileColor toggle() {
            if (this == BLACK) {
                return WHITE;
            }
            return BLACK;
        }
    }

    public static void main(String[] args) throws Exception {
        val m = System.currentTimeMillis();
        val inputPath = Paths.get(Day24Part2.class.getResource("input.txt").toURI());
        val input = Files.readAllLines(inputPath);
        val hexPaths = input.stream()
                .map(HexagonUtils::parsePath)
                .collect(toList());

        // Populate the initial grid.
        var grid = new HashMap<HexagonCoordinate, TileColor>();
        for (val hexPath : hexPaths) {
            var tile = ORIGIN;
            for (val direction : hexPath) {
                tile = tile.in(direction);
            }
            if (grid.containsKey(tile)) {
                grid.put(tile, grid.get(tile).toggle());
            }
            else {
                grid.put(tile, TileColor.BLACK);
            }
            // Add halo.
            for (val neighbor : HexagonUtils.neighborsOf(tile)) {
                if (!grid.containsKey(neighbor)) {
                    grid.put(neighbor, TileColor.WHITE);
                }
            }
        }

        // Compute the following 'generations'.
        for (int i = 0; i < 100; i++) {
            grid = nextGen(grid);
        }

        val part2 = grid.entrySet().stream()
                .filter(e -> e.getValue() == TileColor.BLACK)
                .count();
        out.println("part 2: " + part2);
        out.println("runtime: " + (System.currentTimeMillis() - m) + " [ms]");
    }

    private static HashMap<HexagonCoordinate, TileColor> nextGen(HashMap<HexagonCoordinate, TileColor> grid) {
        val next = new HashMap<HexagonCoordinate, TileColor>();
        grid.forEach((tile, tileColor) -> {
            val neighbors = HexagonUtils.neighborsOf(tile);
            val blackNeighborCnt = neighbors.stream()
                    .filter(n -> grid.get(n) == TileColor.BLACK)
                    .count();
            if (tileColor == TileColor.BLACK) {
                if (blackNeighborCnt == 1 || blackNeighborCnt == 2) {
                    next.put(tile, TileColor.BLACK);
                    // Add halo.
                    neighbors.forEach(n -> {
                        if (!next.containsKey(n)) {
                            next.put(n, TileColor.WHITE);
                        }
                    });
                }
            } else {
                if (blackNeighborCnt == 2) {
                    next.put(tile, TileColor.BLACK);
                    // Add halo.
                    neighbors.forEach(n -> {
                        if (!next.containsKey(n)) {
                            next.put(n, TileColor.WHITE);
                        }
                    });
                }
            }
        });
        return next;
    }

}
// wrong answer: 214