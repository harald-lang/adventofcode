package aoc.day13;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import static java.lang.System.out;

public class Day13Part2 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day13Part2.class.getResource("input.txt").getPath());
        val lines = Files.readAllLines(inputPath);

        val busIds = new TreeSet<Long>(Comparator.reverseOrder()); // higher IDs first
        val busIdToOffset = new HashMap<Long, Long>();

        val split = lines.get(1).split(",");
        for (int i = 0; i < split.length; i++) {
            if (!split[i].equals("x")) {
                val busId = Long.parseLong(split[i]);
                val busOffset = Long.valueOf(i);
                busIds.add(busId);
                busIdToOffset.put(busId, busOffset);
            }
        }

        val maxBusId = busIds.first();
        val maxBusOffset = busIdToOffset.get(maxBusId);
        var maxBusTs = maxBusId;
        var tsStep = maxBusTs;
        val m = new TreeSet<Long>(); // memoize the prime factors of tsStep
        m.add(maxBusId);
        while (true) {
            var match = true;
            for (var curBusId : busIds) {
                val curBusOffset = busIdToOffset.get(curBusId);
                val diff = curBusOffset - maxBusOffset;
                val expectedDepartureTs = maxBusTs + diff;
                if (expectedDepartureTs % curBusId != 0) {
                    match = false;
                    break;
                }
                else {
                    if (!m.contains(curBusId)) {
                        m.add(curBusId);
                        tsStep *= curBusId; // compute the LCM - note that all busIds are prime
                    }
                }
            }

            if (match) {
                break;
            }

            maxBusTs += tsStep;
        }

        out.println("part 2: " + (maxBusTs - maxBusOffset));
    }

}