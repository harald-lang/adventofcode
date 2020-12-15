package aoc.day13;

import lombok.val;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Day13Part1 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day13Part1.class.getResource("input.txt").getPath());
        val lines = Files.readAllLines(inputPath);

        val earliestTs = Long.parseLong(lines.get(0));
        val ids = Arrays.stream(lines.get(1).split(","))
                .filter(x -> !x.equals("x"))
                .map(Long::parseLong)
                .collect(toList());

        var departures = ids.stream()
                .map(id -> {
                    val departureTs = ((earliestTs + (id - 1)) / id) * id;
                    return Pair.of(id, departureTs);
                })
                .sorted(Comparator.comparingLong(Pair::getRight))
                .collect(toList());

        var delay = departures.get(0).getRight() - earliestTs;
        out.println("part1: " + departures.get(0).getLeft() * delay
                + ", id:" + departures.get(0).getLeft() + ", ts: " + departures.get(0).getRight());
    }

}