package aoc.day22;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

public class Day22Part1 {

    public static void main(String[] args) throws Exception {
        val m = System.currentTimeMillis();
        val inputPath = Paths.get(Day22Part1.class.getResource("input.txt").toURI());
        val input = Files.readAllLines(inputPath);

        val decks = new ArrayList<LinkedList<Integer>>();
        for (int i = 0; i < input.size(); ++i) {
            val line = input.get(i);
            if (line.startsWith("//")) continue;
            if (line.isEmpty()) continue;
            if (line.startsWith("Player")) {
                decks.add(new LinkedList<>());
                continue;
            }
            val value = parseInt(line);
            decks.get(decks.size() - 1).add(value);
        }

        val deckA = decks.get(0);
        val deckB = decks.get(1);
        while (!(deckA.isEmpty() || deckB.isEmpty())) {
            final int valueA = deckA.pollFirst();
            final int valueB = deckB.pollFirst();
            if (valueA > valueB) {
                deckA.add(valueA);
                deckA.add(valueB);
            }
            else {
                deckB.add(valueB);
                deckB.add(valueA);
            }
        }

        val winningDeck = new ArrayList<>(deckA.isEmpty() ? deckB : deckA);
        int part1 = 0;
        for (int i = 0; i < winningDeck.size(); ++i) {
            part1 += winningDeck.get(i) * (winningDeck.size() - i);
        }
        out.println("part 1: " + part1);
        out.println("runtime: " + (System.currentTimeMillis() - m) + " [ms]");
    }

}
