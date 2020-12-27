package aoc.day21;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.MultimapBuilder;
import lombok.Value;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import static java.lang.System.out;

public class Day21 {

    @Value(staticConstructor = "of")
    static class Food {
        int id;
        TreeSet<String> ingredients;
        TreeSet<String> allergens;
    }

    public static void main(String[] args) throws Exception {
        val m = System.currentTimeMillis();
        val inputPath = Paths.get(Day21.class.getResource("input.txt").toURI());
        val input = Files.readAllLines(inputPath);

        val foods = parseFoodList(input);
        val allergenFoodIdx = MultimapBuilder.hashKeys().hashSetValues().<String, Food>build();
        for (val food : foods.values()) {
            for (val allergen : food.allergens) {
                allergenFoodIdx.put(allergen, food);
            }
        }
        val map = HashBiMap.<String, String>create();
        var done = false;
        while (!done) {
            done = true;
            for (val allergen : allergenFoodIdx.keySet()) {
                if (map.containsKey(allergen)) continue;
                out.println(allergen);
                val foodIt = allergenFoodIdx.get(allergen).iterator();
                val candidateIngredients = new TreeSet<>(foodIt.next().ingredients);
                out.println(candidateIngredients);
                while (foodIt.hasNext()) {
                    candidateIngredients.retainAll(foodIt.next().ingredients);
                    out.println(candidateIngredients);
                }
                candidateIngredients.removeAll(map.values());
                if (candidateIngredients.size() == 1) {
                    out.println("found unique mapping: " + allergen + " <-> " + candidateIngredients.first());
                    map.put(allergen, candidateIngredients.first());
                    done = false;
                }
                out.println("---");
            }
        }

        out.println("# of allergens: " + allergenFoodIdx.keySet().size());
        out.println("# of uniquely mapped allergens: " + map.keySet().size());

        val ingredients = new TreeSet<String>();
        for (val food : foods.values()) {
            ingredients.addAll(food.ingredients);
        }
        out.println("ingredients:");
        out.println(ingredients);

        val nonAllergicIngredients = new TreeSet<String>(ingredients);
        nonAllergicIngredients.removeAll(map.values());
        out.println("non-allergic ingredients:");
        out.println(nonAllergicIngredients);
        out.println();

        var part1 = 0;
        for (val food : foods.values()) {
            val in = new TreeSet<>(food.ingredients);
            in.retainAll(nonAllergicIngredients);
            part1 += in.size();
        }
        out.println("part 1: " + part1);

        val part2 = new StringBuilder();
        val allergens = new TreeSet<>(allergenFoodIdx.keySet());
        for (val allergen : allergens) {
            part2.append(map.get(allergen));
            part2.append(",");
        }
        part2.delete(part2.length() - 1, part2.length());
        out.println("part 2: " + part2);

        out.println("runtime: " + (System.currentTimeMillis() - m) + " [ms]");
    }

    static HashMap<Integer, Food> parseFoodList(List<String> input) {
        val foods = new HashMap<Integer, Food>();
        var foodId = 0;
        for (val line : input) {
            val split = line.split(" \\(contains ");
            val ingredientsString = split[0];
            val allergensString = split[1].substring(0, split[1].length() - 1);

            val ingredients = new TreeSet<>(Arrays.asList(ingredientsString.split(" ")));
            val allergens = new TreeSet<>(Arrays.asList(allergensString.split(", ")));

            val food = Food.of(foodId++, ingredients, allergens);
            foods.put(food.id, food);
        }
        return foods;
    }
    
}
