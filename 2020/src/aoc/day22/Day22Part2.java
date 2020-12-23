package aoc.day22;

import lombok.Value;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

public class Day22Part2 {

    @Value
    static class Config {
        LinkedList<Integer> deckA;
        LinkedList<Integer> deckB;

        static Config of(LinkedList<Integer> deckA, LinkedList<Integer> deckB) {
          return new Config(new LinkedList<>(deckA), new LinkedList<>(deckB));
        }
    }

    static void game(final LinkedList<Integer> deckA, final LinkedList<Integer> deckB) {
        final Set<Config> priorConfigs = new HashSet<>();
        while (!(deckA.isEmpty() || deckB.isEmpty())) {
            val config = Config.of(deckA, deckB);
            if (priorConfigs.contains(config)) {
                // Clear deck B to denote A as winner.
                deckB.clear();
                return;
            }
            priorConfigs.add(config);

            final int valueA = deckA.pollFirst();
            final int valueB = deckB.pollFirst();

            val aCouldRecurse = valueA <= deckA.size();
            val bCouldRecurse = valueB <= deckB.size();

            if (aCouldRecurse && bCouldRecurse) {
                // Sub-game.
                val subDeckA = new LinkedList<>(deckA.subList(0, valueA));
                val subDeckB = new LinkedList<>(deckB.subList(0, valueB));
                game(subDeckA, subDeckB);
                if (!subDeckA.isEmpty()) {
                    // A won the sub-game.
                    deckA.add(valueA);
                    deckA.add(valueB);
                }
                else {
                    // B won the sub-game.
                    deckB.add(valueB);
                    deckB.add(valueA);
                }
            }
            else {
                if (valueA > valueB) {
                    deckA.add(valueA);
                    deckA.add(valueB);
                }
                else {
                    deckB.add(valueB);
                    deckB.add(valueA);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        val m = System.currentTimeMillis();
        val inputPath = Paths.get(Day22Part2.class.getResource("input.txt").toURI());
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
        game(deckA, deckB);
        val winningDeck = new ArrayList<>(deckA.isEmpty() ? deckB : deckA);
        int part2 = computeScore(winningDeck);
        out.println("part 2: " + part2);
        out.println("runtime: " + (System.currentTimeMillis() - m) + " [ms]");
    }

    private static int computeScore(ArrayList<Integer> deck) {
        int result = 0;
        for (int i = 0; i < deck.size(); ++i) {
            result += deck.get(i) * (deck.size() - i);
        }
        return result;
    }

}
