package aoc.day11;

import lombok.val;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.exit;
import static java.lang.System.out;

public class Day11 {

    private static int it = 0;
    private static BitSet isSeat;

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day11.class.getResource("input.txt").getPath());
        val inputs = Files.readAllLines(inputPath);

        val width = inputs.get(0).length();
        val height = inputs.size();

        isSeat = new BitSet(width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (inputs.get(y).charAt(x) == 'L') {
                    isSeat.set(width * y + x);
                }
            }
        }

        out.println("# of seats: " + isSeat.cardinality());

        val a = new AtomicReference<>(new BitSet(width * height));
        val b = new AtomicReference<>(new BitSet(width * height));

        while (true) {
            it++;
            // swap the isOccupied bitsets
            b.set(a.getAndSet(b.get()));
            val src = a.get();
            val dst = b.get();
            dst.clear();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (!isSeat.get(width * y + x)) {
                        continue;
                    }
                    val isOccupied = src.get(width * y + x);
//                    val adjCnt = countAdjOccupied(src, width, height, x, y); // part 1
                    val adjCnt = countAdjOccupiedPart2(src, width, height, x, y); // part 2
                    if (!isOccupied && adjCnt == 0) {
                        dst.set(width * y + x);
//                    } else if (isOccupied && adjCnt >= 4) { // part 1
                    } else if (isOccupied && adjCnt >= 5) { // part 2
                        dst.clear(width * y + x);
                    } else {
                        if (isOccupied) {
                            dst.set(width * y + x);
                        }
                    }
                }
            }

            for (int i = 0; i < dst.size(); ++i) {
                if (dst.get(i) && !isSeat.get(i)) {
                    out.println("error");
                    exit(1);
                }
            }

//            print(dst, isSeat, width, height);
//            out.println(dst.cardinality());


            if (src.equals(dst)) {
                out.println("part x: " + src.cardinality());
                out.println("rounds: " + it);

                break;
            }

        }

    }

    private static void print(BitSet src, BitSet isSeat, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!isSeat.get(width * y + x)) {
                    out.print('.');
                } else {
                    out.print(src.get(width * y + x) ? '#' : 'L');
                }
            }
            out.println();
        }
        out.println();
    }

    private static int countAdjOccupied(BitSet b, int width, int height, int x, int y) {
        var cnt = 0;
        cnt += isOccupied(b, width, height, x    , y - 1);
        cnt += isOccupied(b, width, height, x + 1, y - 1);
        cnt += isOccupied(b, width, height, x + 1, y    );
        cnt += isOccupied(b, width, height, x + 1, y + 1);
        cnt += isOccupied(b, width, height, x    , y + 1);
        cnt += isOccupied(b, width, height, x - 1, y + 1);
        cnt += isOccupied(b, width, height, x - 1, y    );
        cnt += isOccupied(b, width, height, x - 1, y - 1);
        return cnt;
    }

    private static int isOccupied(BitSet b, int width, int height, int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 0;
        }
        return b.get(width * y + x) ? 1 : 0;
    }


    private static int countAdjOccupiedPart2(BitSet b, int width, int height, int x, int y) {
        var cnt = 0;
        {
            val s = findNextSeat(b, width, height, x, y, 0, -1);
            cnt += isOccupied(b, width, height, s.getLeft(), s.getRight());
        }
        {
            val s = findNextSeat(b, width, height, x, y, +1, -1);
            cnt += isOccupied(b, width, height, s.getLeft(), s.getRight());
        }
        {
            val s = findNextSeat(b, width, height, x, y, +1, 0);
            cnt += isOccupied(b, width, height, s.getLeft(), s.getRight());
        }
        {
            val s = findNextSeat(b, width, height, x, y, +1, +1);
            cnt += isOccupied(b, width, height, s.getLeft(), s.getRight());
        }
        {
            val s = findNextSeat(b, width, height, x, y, 0, +1);
            cnt += isOccupied(b, width, height, s.getLeft(), s.getRight());
        }
        {
            val s = findNextSeat(b, width, height, x, y, -1, +1);
            cnt += isOccupied(b, width, height, s.getLeft(), s.getRight());
        }
        {
            val s = findNextSeat(b, width, height, x, y, -1, 0);
            cnt += isOccupied(b, width, height, s.getLeft(), s.getRight());
        }
        {
            val s = findNextSeat(b, width, height, x, y, -1, -1);
            cnt += isOccupied(b, width, height, s.getLeft(), s.getRight());
        }
        return cnt;
    }

    private static Pair<Integer, Integer> findNextSeat(
            BitSet b, final int width, final int height,
            final int x, final int y,
            final int dx, final int dy) {
        int _x = x;
        int _y = y;
        while (true) {
            _x += dx;
            _y += dy;
            if (_x < 0 || _x >= width || _y < 0 || _y >= height) {
                return Pair.of(_x, _y);
            }
            if (isSeat.get(width * _y + _x)) {
                return Pair.of(_x, _y);
            }
        }
    }

}
