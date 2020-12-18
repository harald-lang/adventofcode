package aoc.day17;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;
import lombok.Value;
import lombok.val;
import org.apache.commons.lang3.mutable.MutableInt;

import java.nio.file.Files;
import java.nio.file.Path;
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

        @Override
        public boolean equals(Object other) {
            C3 o = (C3) other;
            return x == o.x
                    & y == o.y
                    & z == o.z;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result =  97 * result + x;
            result =  79 * result + y;
            result = 101 * result + z;
            return result;
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

        @Override
        public boolean equals(Object other) {
            C4 o = (C4) other;
            return x == o.x
                    & y == o.y
                    & z == o.z
                    & w == o.w;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result =  97 * result + x;
            result =  79 * result + y;
            result = 101 * result + z;
            result =  31 * result + w;
            return result;
        }

    }

    private enum CubeState {
        ACTIVE, INACTIVE
    }

    private static class PocketDimension {

        private final Map<Coordinate, CubeState> grid = Maps.newHashMap();

        public void activate(Coordinate coordinate) {
            // Activate the cube at the given coordinate.
            grid.put(coordinate, CubeState.ACTIVE);
            // Make sure the halo is indexed as well.
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
            val cnt = new MutableInt(0);
            c.neighbors(n -> {
                if (isActive(n)) {
                    cnt.increment();
                }
            });
            return cnt.getValue();
        }

        public long countActiveCubes() {
            return grid.entrySet().stream()
                    .filter(kv -> kv.getValue() == CubeState.ACTIVE)
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
            val line = input.get(y);
            val lineLength = line.length();
            for (int x = 0; x < lineLength; x++) {
                if (line.charAt(x) == '#') {
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
