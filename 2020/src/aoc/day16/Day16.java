package aoc.day16;

import lombok.val;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Day16 {

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day16.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath);

        // read field names and their valid ranges
        val allValidValues = new TreeSet<Integer>();
        val validValuesPerField = new HashMap<String, TreeSet<Integer>>();

        int lineNo = 0;
        for (; lineNo < inputs.size(); lineNo++) {
            val line = inputs.get(lineNo);
            if (line.length() == 0) break;

            val fieldName = line.split(":")[0];
            val validValues = new TreeSet<Integer>();
            validValuesPerField.put(fieldName, validValues);

            val rangeStrings = line.split(":")[1].trim().split(" or ");
            var ranges = Arrays.stream(rangeStrings)
                    .map(r -> {
                        val split = r.split("-");
                        val range = Pair.of(parseInt(split[0]), parseInt(split[1]));
                        return range;
                    })
                    .collect(toList());
            for (var range : ranges) {
                for (int i = range.getLeft(); i < range.getRight() + 1; i++) { // works, as long as the ranges are small
                    allValidValues.add(i);
                    validValues.add(i);
                }
            }
        }

        // read own ticket
        lineNo++; // empty line
        lineNo++; // header
        val ownTicket = Arrays.stream(inputs.get(lineNo).split(","))
                .map(Integer::parseInt)
                .collect(toList());
        lineNo++;

        // read nearby tickets
        lineNo++; // empty line
        lineNo++; // header
        var ticketScanningErrorRate = 0;
        val validTickets = new ArrayList<List<Integer>>();
        for (; lineNo < inputs.size(); lineNo++) {
            val line = inputs.get(lineNo);
            val ticket = Arrays.stream(line.split(","))
                    .map(Integer::parseInt)
                    .collect(toList());
            val invalidValueCnt = ticket.stream()
                    .filter(i -> !allValidValues.contains(i))
                    .reduce(0, Integer::sum);
            ticketScanningErrorRate += invalidValueCnt;
            if (invalidValueCnt == 0) {
                validTickets.add(ticket); // invalid tickets are discarded
            }
        }


        // candidate matrix. a bit at <f,i> indicates that f is a candidate field for index i
        val m = new HashMap<String, BitSet>();

        val fieldNames = new ArrayList<>(validValuesPerField.keySet());
        val fieldCnt = fieldNames.size();

        for (String fieldName : fieldNames) {
            val b = new BitSet(fieldCnt);
            b.flip(0, fieldCnt);
            m.put(fieldName, b);
        }

        for (var ticket : validTickets) {
            for (int valueIdx = 0; valueIdx < fieldCnt; ++valueIdx) {
                val ticketValue = ticket.get(valueIdx);
                for (var fieldName : fieldNames) {
                    if (!validValuesPerField.get(fieldName).contains(ticketValue)) {
                        out.println("rule out " + fieldName + " at index " + valueIdx);
                        m.get(fieldName).clear(valueIdx);
                    }
                }
            }
        }

        for(int i = 0; i < fieldCnt; ++i) {
            for (var fieldName : fieldNames) {
                if (m.get(fieldName).cardinality() == 1) { // uniquely identified
                    val valueIdx = m.get(fieldName).nextSetBit(0);
                    for (var fieldNameInner : fieldNames) {
                        if (fieldNameInner.equals(fieldName)) continue;
                        m.get(fieldNameInner).clear(valueIdx);
                    }
                }
            }
        }

        val fieldNameToValueIdx = new HashMap<String, Integer>();
        for (var fieldName : fieldNames) {
            fieldNameToValueIdx.put(fieldName, m.get(fieldName).nextSetBit(0));
        }

        val part2 = fieldNames.stream()
                .filter(n -> n.startsWith("departure"))
                .map(fieldNameToValueIdx::get)
                .map(i -> (long) ownTicket.get(i))
                .reduce(1L, (a, b) -> a * b);

        out.println("part 1: " + ticketScanningErrorRate);
        out.println("part 2: " + part2);
    }

}
