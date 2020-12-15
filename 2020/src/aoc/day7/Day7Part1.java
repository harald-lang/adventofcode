package aoc.day7;

import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class Day7Part1 {

    private static int colCnt = 0;
    private static final HashMap<String, Integer> colIdx = new HashMap<>();
    private static final HashMap<Integer, Set<Integer>> adj = new HashMap<>();

    private static Integer getColId(String colName) {
        var id = colIdx.get(colName);
        if (id == null) {
            id = colCnt;
            colIdx.put(colName, id);
            colCnt++;
        }
        return id;
    }

    private static Set<Integer> dfs(Set<Integer> cols) {
        val retVal = new HashSet<>(cols);

        cols.forEach(col -> {
            if (adj.containsKey(col)) {
                retVal.addAll(dfs(adj.get(col)));
            }
        });

        return retVal;
    }

    public static void main(String[] args) throws Exception {
        val inputPath = Path.of(Day7Part1.class.getResource("input.txt").getPath());
        val inputs =  Files.readAllLines(inputPath).stream()
                .map(line -> line.replaceAll("\\.$", ""))
                .map(line -> line.replaceAll(", ", ","))
                .map(line -> line.replaceAll("bags", "bag"))
                .map(line -> line.replaceAll(" bag", ""))
                .collect(toList());

        // inner -> outer*

        inputs.forEach(line -> {
            val splitLine = line.split(" contain ");

            val outerCol = splitLine[0];
            val outerColId = getColId(outerCol);

            val splitInner = splitLine[1].split(",");
            out.print(outerCol + "(" + outerColId + ")" + " -> ");
            stream(splitInner).forEach(inner -> {
                if (inner.startsWith("no other")) {
                    return;
                }
                val qty = Integer.parseInt(inner.split(" ")[0]);
                val col = inner.replaceFirst("^\\d ", "");
                val colId = getColId(col);
                out.print("["  + qty + " [" + col + "(" + colId + ")" + "]]");

                val list = adj.getOrDefault(colId, new HashSet<>());
                list.add(outerColId);
                adj.put(colId, list);
            });
            out.println();
        });

        val result = dfs(adj.get(getColId("shiny gold")));
        out.println("part 1: " + result.size());
    }
}
