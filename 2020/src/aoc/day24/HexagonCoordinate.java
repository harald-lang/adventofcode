package aoc.day24;

import lombok.Value;

// A (offset, odd-row) coordinate in a hexagonal grid.
// The orientation of the hexagons are 'pointy topped'.
//                                           /\
//                                          |  |
//                                           \/
//
@Value(staticConstructor = "of")
class HexagonCoordinate {

    int x;
    int y;

    HexagonCoordinate northEast() {
        if (y % 2 == 0) {
            return HexagonCoordinate.of(x, y - 1);
        }
        else {
            return HexagonCoordinate.of(x + 1, y - 1);
        }
    }

    HexagonCoordinate east() {
        return HexagonCoordinate.of(x + 1, y);
    }

    HexagonCoordinate southEast() {
        if (y % 2 == 0) {
            return HexagonCoordinate.of(x, y + 1);
        }
        else {
            return HexagonCoordinate.of(x + 1, y + 1);
        }
    }

    HexagonCoordinate southWest() {
        if (y % 2 == 0) {
            return HexagonCoordinate.of(x - 1, y + 1);
        }
        else {
            return HexagonCoordinate.of(x, y + 1);
        }
    }

    HexagonCoordinate west() {
        return HexagonCoordinate.of(x - 1, y);
    }

    HexagonCoordinate northWest() {
        if (y % 2 == 0) {
            return HexagonCoordinate.of(x - 1, y - 1);
        }
        else {
            return HexagonCoordinate.of(x, y - 1);
        }
    }

    HexagonCoordinate in(HexagonDirection direction) {
        switch (direction) {
            case NORTH_EAST: return northEast();
            case EAST:       return east();
            case SOUTH_EAST: return southEast();
            case SOUTH_WEST: return southWest();
            case WEST:       return west();
            case NORTH_WEST: return northWest();
        }
        throw new RuntimeException("unreachable");
    }

}
