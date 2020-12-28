package aoc.day24;

import lombok.val;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HexagonUtils {

    static List<HexagonDirection> parsePath(String pathString) {
        val path = new ArrayList<HexagonDirection>();
        for (int i = 0; i < pathString.length(); i++) {
            switch (pathString.charAt(i)) {
                case 'n':
                    i++;
                    switch (pathString.charAt(i)) {
                        case 'e':
                            path.add(HexagonDirection.NORTH_EAST);
                            break;
                        case 'w':
                            path.add(HexagonDirection.NORTH_WEST);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid input.");
                    }
                    break;
                case 's':
                    i++;
                    switch (pathString.charAt(i)) {
                        case 'e':
                            path.add(HexagonDirection.SOUTH_EAST);
                            break;
                        case 'w':
                            path.add(HexagonDirection.SOUTH_WEST);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid input.");
                    }
                    break;
                case 'e':
                    path.add(HexagonDirection.EAST);
                    break;
                case 'w':
                    path.add(HexagonDirection.WEST);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid input.");
            }
        }
        return path;
    }

    static Set<HexagonCoordinate> neighborsOf(HexagonCoordinate c) {
        val neighbors = new HashSet<HexagonCoordinate>();
        for (val dir : HexagonDirection.values()) {
            neighbors.add(c.in(dir));
        }
        return neighbors;
    }

}
