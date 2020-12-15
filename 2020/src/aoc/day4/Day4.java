package aoc.day4;

import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.System.out;
import static java.util.stream.Collectors.toMap;

public class Day4 {

    public static void main(String[] args) throws IOException {
        val inputPath = Path.of(Day4.class.getResource("input.txt").getPath());
        val input =  Files.readAllLines(inputPath);

        val validCnt = input.stream().filter(line -> {
            out.println("-------------------------");
            val kvPairs = Arrays.stream(line.split(" "))
                    .map(pair -> pair.split(":"))
                    .collect(toMap(splitPair -> splitPair[0], splitPair -> splitPair[1]));
            kvPairs.remove("cid");

            val keys = new TreeSet<>(kvPairs.keySet());
            keys.forEach(key -> out.print(key + "=" + kvPairs.get(key) + " "));
            out.println();


            if (kvPairs.containsKey("byr")) {
                val value = kvPairs.get("byr");
                try {
                    val byr = Integer.parseInt(value);
                    if (byr < 1920 || byr > 2002) {
                        out.println("byr invalid");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    out.println("byr invalid number");
                    return false;
                }
            } else {
                out.println("byr missing");
                return false;
            }

            if (kvPairs.containsKey("iyr")) {
                val value = kvPairs.get("iyr");
                try {
                    val iyr = Integer.parseInt(value);
                    if (iyr < 2010 || iyr > 2020) {
                        out.println("iyr invalid");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    out.println("iyr invalid number");
                    return false;
                }
            } else {
                out.println("iyr missing");
                return false;
            }

            if (kvPairs.containsKey("eyr")) {
                val value = kvPairs.get("eyr");
                try {
                    val byr = Integer.parseInt(value);
                    if (byr < 2020 || byr > 2030) {
                        out.println("eyr invalid");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    out.println("eyr invalid number");
                    return false;
                }
            } else {
                out.println("eyr missing");
                return false;
            }

            if (kvPairs.containsKey("hgt")) {
                val value = kvPairs.get("hgt");
                if (value.length() > 3 && (value.endsWith("cm") || value.endsWith("in"))) {
                    var intValue = 0;
                    try {
                        intValue = Integer.parseInt(value.substring(0, value.length() - 2));
                    } catch (NumberFormatException e) {
                        out.println("hgt invalid number");
                        return false;
                    }

                    if (value.endsWith("cm")) {
                        if (intValue < 150 || intValue > 193) {
                            out.println("hgt invalid (cm)");
                            return false;
                        }
                    } else {
                        if (intValue < 59 || intValue > 76) {
                            out.println("hgt invalid (in)");
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            } else {
                out.println("hgt missing");
                return false;
            }

            if (kvPairs.containsKey("hcl")) {
                val value = kvPairs.get("hcl");
                if (!value.matches("^#[a-f0-9][a-f0-9][a-f0-9][a-f0-9][a-f0-9][a-f0-9]$")) {
                    out.println("hcl invalid");
                    return false;
                }
            } else {
                out.println("hcl missing");
                return false;
            }

            if (kvPairs.containsKey("ecl")) {
                val ecls = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
                val value = kvPairs.get("ecl");
                if (!ecls.contains(value)) {
                    out.println("ecl invalid");
                    return false;
                }
            } else {
                out.println("ecl missing");
                return false;
            }

            if (kvPairs.containsKey("pid")) {
                val value = kvPairs.get("pid");
                if (!value.matches("^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$")) {
                    out.println("pid invalid");
                    return false;
                }
            } else {
                out.println("pid missing");
                return false;
            }

            out.println("valid");
            return true;

        }).count();
        out.println("part 2: " + validCnt); // part 1 got overridden, sry ;)
    }

}
