#include "lib.h"
#include "Map.h"

/**
 * Initialise a 2D map of length and width size and store whether to
 * calculate diagonals or not
 */
Map::Map(bool diagonals, int size) {
    Diagonals = diagonals;
    SIZE = size;

    // allocate the memory as a single dimensional array
    // which we access with the idx function
    Grid = (int*) calloc(sizeof(int), size * size);
    if (!Grid) exit(1);
}

/**
 * Destroy the map and free the grid
 */
Map::~Map() {
    free(Grid);
    Grid = nullptr;
}

/**
 * Return the index in the grid of the item at point (x,y). This can be
 * used for getting or setting a value: `int a = Grid[idx(x,y)]` or
 * `Grid[idx(x,y)] = 4`
 */
int Map::idx(int x, int y) {

    // the index of point (x, y) since we store it as a single dimensional array
    return y * SIZE + x;
}

/**
 * Add the line segment (x1, y1) -> (x2, y2) to the map
 */
void Map::add(int x1, int y1, int x2, int y2) {

    // vertical line
    if (x1 == x2) {

        // swap ys as necessary
        if (y2 < y1) {
            int tmp = y2;
            y2 = y1;
            y1 = tmp;
        }

        // increment the count for every point on the line
        for (int i = y1; i <= y2; i++) {
            Grid[idx(x1, i)]++;
        }

    // horizontal line
    } else if (y1 == y2) {

        // swap xs as necessary
        if (x2 < x1) {
            int tmp = x2;
            x2 = x1;
            x1 = tmp;
        }

        // increment the count for every point on the line
        for (int j = x1; j <= x2; j++) {
            Grid[idx(j, y1)]++;
        }
    } else if (Diagonals) {

        // both going same direction (leading diagonal)
        if ((x1 <= x2 && y1 <= y2) || (x1 > x2 && y1 > y2)) {

            // swap xs and ys as necessary
            if (x2 < x1) {
                int tmp = x2;
                x2 = x1;
                x1 = tmp;
            }
            if (y2 < y1) {
                int tmp = y2;
                y2 = y1;
                y1 = tmp;
            }

            // increment the count for every point on the line
            for (int offset = 0; offset <= y2 - y1; offset++) {
                Grid[idx(x1 + offset, y1 + offset)]++;
            }

        // going opposite directions (trailing diagonal)
        } else {

            // swap xs and ys as necessary
            if (x2 < x1) {
                int tmp = x2;
                x2 = x1;
                x1 = tmp;
            }
            if (y2 < y1) {
                int tmp = y2;
                y2 = y1;
                y1 = tmp;
            }

            // increment the count for every point on the line
            // subtract from x2 and add to y1 to go the opposite direction
            for (int offset = 0; offset <= y2 - y1; offset++) {
                Grid[idx(x2 - offset, y1 + offset)]++;
            }
        }
    }
    // otherwise do nothing - ignore diagonals
}

/**
 * Return a string representation of the map
 */
std::string Map::to_string() {
    std::string out = "";

    for (int y = 0; y < SIZE; y++) {
        for (int x = 0; x < SIZE; x++) {

            // print a dot for 0s so easier to see
            if (Grid[idx(x, y)] == 0) {
                out += ".";

            // print the number for anything else
            } else {
                out += std::to_string(Grid[idx(x, y)]);
            }
        }
        out += "\n";
    }
    return out;
}

/**
 * Return the number of dangerous points on the map - the number of points
 * where 2 or more lines cross
 */
int Map::numDangerousPoints() {
    int total = 0;
    for (int y = 0; y < SIZE; y++) {
        for (int x = 0; x < SIZE; x++) {
            if (Grid[idx(x, y)] >= 2) {
                total++;
            }
        }
    }
    return total;
}

/**
 * Parse the input file with given name with or without processing diagonals,
 * add all lines to the map and print the number of dangerous points
 */
void printNumDangerousPoints(std::string filename, bool diagonals) {
    std::vector<std::string> lines = readLines(filename);

    // for the test we only need a grid of size 10 but for the real
    // input we need a grid of size 1000
    int size = 1000;
    if (filename.compare("input_test.txt") == 0) {
        size = 10;
    }

    Map* map = new Map(diagonals, size);

    // parse the line to get x1, y1, x2 and y2
    for (std::string line: lines) {
        std::vector<std::string> points = split(line, " -> ");
        std::vector<std::string> p1 = split(points.at(0), ",");
        std::vector<std::string> p2 = split(points.at(1), ",");
        int x1 = stoi(p1.at(0));
        int y1 = stoi(p1.at(1));
        int x2 = stoi(p2.at(0));
        int y2 = stoi(p2.at(1));

        // add the line to the map
        map->add(x1, y1, x2, y2);
    }

    // print the map for the test input
    if (size == 10) {
        puts(map->to_string().c_str());
    }

    // print the number of dangerous points
    printf("Dangerous points = %d\n", map->numDangerousPoints());
}