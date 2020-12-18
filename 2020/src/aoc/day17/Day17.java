package aoc.day17;

import com.google.common.collect.ComparisonChain;
import lombok.Value;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static java.lang.System.out;

public class Day17 {

    private interface Coordinate extends Comparable<Coordinate> {

        @Override
        int compareTo(Coordinate o);

        void neighbors(Consumer<Coordinate> consumer);

        Set<Coordinate> getNeighbors();

    }

    @Value(staticConstructor = "of")
    private static class C3 implements Coordinate {

        int x;
        int y;
        int z;

        @Override
        public int compareTo(Coordinate other) {
            val o = (C3) other;
            return ComparisonChain.start()
                    .compare(x, o.x)
                    .compare(y, o.y)
                    .compare(z, o.z)
                    .result();
        }

        @Override
        public void neighbors(Consumer<Coordinate> consumer) {
            for (int x = this.x - 1; x < this.x + 2; x++) {
                for (int y = this.y - 1; y < this.y + 2; y++) {
                    for (int z = this.z - 1; z < this.z + 2; z++) {
                        if (x == this.x && y == this.y && z == this.z) continue;
                        consumer.accept(C3.of(x, y, z));
                    }
                }
            }
        }

        public Set<Coordinate> getNeighbors() {
            val neighbors = new HashSet<Coordinate>();
            neighbors(neighbors::add);
            return neighbors;
        }

    }


    @Value(staticConstructor = "of")
    private static class C4 implements Coordinate {

        int x;
        int y;
        int z;
        int w;

        @Override
        public int compareTo(Coordinate other) {
            val o = (C4) other;
            return ComparisonChain.start()
                    .compare(x, o.x)
                    .compare(y, o.y)
                    .compare(z, o.z)
                    .compare(w, o.w)
                    .result();
        }

        @Override
        public void neighbors(Consumer<Coordinate> consumer) {
            for (int x = this.x - 1; x < this.x + 2; x++) {
                for (int y = this.y - 1; y < this.y + 2; y++) {
                    for (int z = this.z - 1; z < this.z + 2; z++) {
                        for (int w = this.w - 1; w < this.w + 2; w++) {
                            if (x == this.x && y == this.y && z == this.z && w == this.w) continue;
                            consumer.accept(C4.of(x, y, z, w));
                        }
                    }
                }
            }
        }

        public Set<Coordinate> getNeighbors() {
            val neighbors = new HashSet<Coordinate>();
            neighbors(neighbors::add);
            return neighbors;
        }

    }

    private enum CubeState {
        ACTIVE, INACTIVE
    }

    private static class PocketDimension {

        private final Map<Coordinate, CubeState> grid = new HashMap<>();

        public void activate(Coordinate coordinate) {
            // Activate the cube at the given coordinate.
            grid.put(coordinate, CubeState.ACTIVE);
            // Make sure the halo is indexed.
            coordinate.neighbors(n -> {
                if (!isActive(n)) {
                    grid.put(n, CubeState.INACTIVE);
                }
            });
        }

        public boolean isActive(Coordinate c) {
            val cubeState = grid.get(c);
            return cubeState != null && cubeState != CubeState.INACTIVE;
        }

        public int countActiveNeighbors(Coordinate c) {
            return (int) c.getNeighbors().stream()
                    .filter(this::isActive)
                    .count();
        }

        public long countActiveCubes() {
            return grid.keySet().stream()
                    .filter(this::isActive)
                    .count();
        }

        public Set<Coordinate> indexedCoordinates() {
            return grid.keySet();
        }

    }

    // Calculates the state of the pocket dimension after one cycle.
    private static PocketDimension cycle(PocketDimension d) {
        val next = new PocketDimension();
        for (var c : d.indexedCoordinates()) {
            val isActive = d.isActive(c);
            val activeNeighborCnt = d.countActiveNeighbors(c);
            if (isActive && activeNeighborCnt >= 2 && activeNeighborCnt <= 3) {
                next.activate(c);
            }
            else if (!isActive && activeNeighborCnt == 3) {
                next.activate(c);
            }
        }
        return next;
    }

    public static void main(String[] args) throws Exception {
        val m = System.currentTimeMillis();
        val inputPath = Path.of(Day17.class.getResource("input.txt").getPath());
        val input =  Files.readAllLines(inputPath);

        var d3 = new PocketDimension();
        var d4 = new PocketDimension();
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(0).length(); x++) {
                if (input.get(y).charAt(x) == '#') {
                    d3.activate(C3.of(x, y, 0));
                    d4.activate(C4.of(x, y, 0, 0));
                }
            }
        }

        for (int cycle = 0; cycle < 6; cycle++) {
            d3 = cycle(d3);
            d4 = cycle(d4);
        }

        out.println("part 1: " + d3.countActiveCubes());
        out.println("part 2: " + d4.countActiveCubes());
        out.println("runtime: " + (System.currentTimeMillis() - m) + " [ms]");
    }

}
