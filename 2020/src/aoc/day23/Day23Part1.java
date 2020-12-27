package aoc.day23;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import static java.util.Collections.sort;

public class Day23Part1 {

    @Data
    @AllArgsConstructor
    static class Link {
        int label;
        int prev;
        int next;
        int lower;
        int higher;
    }

    static HashMap<Integer, Link> parseInput(int input) {
        val labels = new ArrayList<Integer>();
        while (input > 0) {
            labels.add(0, input % 10);
            input /= 10;
        }

        val labelsSorted = new ArrayList<>(labels);
        sort(labelsSorted);

        val index = new HashMap<Integer, Link>();
        val mod = labels.size();
        for (int i = 0; i < mod; i++) {
            val label = labels.get(i);
            val prev = labels.get(((i - 1) % mod + mod) % mod);
            val next = labels.get(((i + 1) % mod + mod) % mod);
            val j = labelsSorted.indexOf(label);
            val lower = labelsSorted.get(((j - 1) % mod + mod) % mod);
            val higher = labelsSorted.get(((j + 1) % mod + mod) % mod);
            val link = new Link(label, prev, next, lower, higher);
            index.put(label, link);
        }
        return index;
    }

    public static void main(String[] args) throws Exception {
        val m = System.currentTimeMillis();
        val inputPath = Paths.get(Day23Part1.class.getResource("input.txt").toURI());
        val line = Files.readAllLines(inputPath).get(0);
        val input = parseInt(line);

        val cups = parseInput(input);
        var currentCup = parseInt(line.substring(0, 1));
        print(cups, currentCup);

        val moveCnt = 100;
        for (int i = 0; i < moveCnt; ++i) {
            // Pick the next three cups.
            val pickedCups = new TreeSet<Integer>();
            val pickedCup1 = cups.get(currentCup).next;
            val pickedCup2 = cups.get(pickedCup1).next;
            val pickedCup3 = cups.get(pickedCup2).next;
            pickedCups.add(pickedCup1);
            pickedCups.add(pickedCup2);
            pickedCups.add(pickedCup3);
            var destinationCup = cups.get(currentCup).lower;
            while (pickedCups.contains(destinationCup)) {
                destinationCup = cups.get(destinationCup).lower;
            }
            // Unlink the three cups.
            cups.get(currentCup).next = cups.get(cups.get(pickedCup3).next).label;
            cups.get(cups.get(pickedCup3).next).prev = currentCup;
            // Re-link the three cups.
            val destinationCupNext = cups.get(destinationCup).next;
            cups.get(destinationCup).next = cups.get(pickedCup1).label;
            cups.get(pickedCup1).prev = destinationCup;
            cups.get(pickedCup3).next = destinationCupNext;
            cups.get(destinationCupNext).prev = destinationCup;
            // Next cup.
            currentCup = cups.get(currentCup).next;
            print(cups, currentCup);
        }

        var c = cups.get(1).next;
        var part1 = 0;
        while (c != 1) {
           part1 = (part1 * 10) + c;
           c = cups.get(c).next;
        }
        out.println("part 1: " + part1);
        out.println("runtime: " + (System.currentTimeMillis() - m) + " [ms]");
    }

    private static void print(HashMap<Integer, Link> cups, int currentCup) {
        out.print("cups: ");
        var c = cups.get(currentCup).label;
        for (int j = 0; j < cups.size(); j++) {
            if (c == currentCup) {
                out.print("(" + c + ") ");
            }
            else {
                out.print(c + " ");
            }
            c = cups.get(c).next;
        }
        out.println();
    }

}
