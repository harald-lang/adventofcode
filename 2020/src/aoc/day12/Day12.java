package aoc.day12;

import lombok.Getter;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.System.out;

public class Day12 {

    private static class Ferry1 {

        // orientation
        @Getter
        int dx = 1;
        @Getter
        int dy = 0;

        // position
        @Getter
        int x = 0;
        @Getter
        int y = 0;

        private void move(char dir, int d) {
            var _dx = 0;
            var _dy = 0;
            switch (dir) {
                case 'N': _dx =  0; _dy = -1; break;
                case 'S': _dx =  0; _dy =  1; break;
                case 'E': _dx =  1; _dy =  0; break;
                case 'W': _dx = -1; _dy =  0; break;
            }
            x += d * _dx;
            y += d * _dy;
        }

        private void moveForward(int d) {
            x += d * dx;
            y += d * dy;
        }

        // assuming degrees is in {90, 180, 270}
        private void rotate(char dir, int degrees) {
            if (dir == 'L') {
                degrees = 360 - degrees;
            }
            val cnt = degrees / 90;
            for (int i = 0; i < cnt; i++) {
                rotateRightBy90Degrees(); // sorry, i'm in a hurry ;)
            }
        }

        private void rotateRightBy90Degrees() {
            val dx_tmp = dy * -1;
            val dy_tmp = dx;
            dx = dx_tmp;
            dy = dy_tmp;
        }

        public void dispatch(char instr, int arg) {
            switch (instr) {
                case 'F':
                    moveForward(arg);
                case 'N':
                case 'S':
                case 'E':
                case 'W':
                    move(instr, arg);
                    break;
                case 'L':
                case 'R':
                    rotate(instr, arg);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid instruction: " + instr + " " + arg);
            }
        }

        public void print() {
            out.println("pos (x,y): " + x + "," + y);
            out.println("orientation (dx,dy): " + dx + "," + dy);
            out.println("part 1: " + (Math.abs(x) + Math.abs(y)));
        }
    }

    private static class Ferry2 {

        // waypoint
        @Getter
        int dx = 10;
        @Getter
        int dy = -1;

        // position
        @Getter
        int x = 0;
        @Getter
        int y = 0;

        private void moveWaypoint(char dir, int d) {
            switch (dir) {
                case 'N': dx +=  0; dy += -d; break;
                case 'S': dx +=  0; dy +=  d; break;
                case 'E': dx +=  d; dy +=  0; break;
                case 'W': dx += -d; dy +=  0; break;
            }
        }

        private void moveForward(int d) {
            x += d * dx;
            y += d * dy;
        }

        // assuming degrees is in {90, 180, 270}.
        private void rotate(char dir, int degrees) {
            if (dir == 'L') {
                degrees = 360 - degrees;
            }
            val cnt = degrees / 90;
            for (int i = 0; i < cnt; i++) {
                rotateRightBy90Degrees();
            }
        }

        private void rotateRightBy90Degrees() {
            val dx_tmp = dy * -1;
            val dy_tmp = dx;
            dx = dx_tmp;
            dy = dy_tmp;
        }

        public void dispatch(char instr, int arg) {
            switch (instr) {
                case 'F':
                    moveForward(arg);
                case 'N':
                case 'S':
                case 'E':
                case 'W':
                    moveWaypoint(instr, arg);
                    break;
                case 'L':
                case 'R':
                    rotate(instr, arg);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid instruction: " + instr + " " + arg);
            }
        }

        public void print() {
            out.println("pos (x,y): " + x + "," + y);
            out.println("waypoint (dx,dy): " + dx + "," + dy);
            out.println("part 2: " + (Math.abs(x) + Math.abs(y)));
        }
    }

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day12.class.getResource("input.txt").getPath());
        val inputs = Files.readAllLines(inputPath);

        val ferry1 = new Ferry1();
        val ferry2 = new Ferry2();
        inputs.forEach(input -> {
            val instr = input.charAt(0);
            val arg = Integer.parseInt(input.substring(1));
            ferry1.dispatch(instr, arg);
            ferry2.dispatch(instr, arg);
        });
        ferry1.print();
        ferry2.print();
    }

}