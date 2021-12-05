#include <string>

class Map {
    bool Diagonals;
    int SIZE;
    int* Grid;

    /**
     * Return the index in the grid of the item at point (x,y). This can be
     * used for getting or setting a value: `int a = Grid[idx(x,y)]` or
     * `Grid[idx(x,y)] = 4`
     */
    int idx(int x, int y);
public:

    /**
     * Initialise a 2D map of length and width size and store whether to
     * calculate diagonals or not
     */
    Map(bool diagonals, int size);

    /**
     * Destroy the map and free the grid
    */
    ~Map();
    
    /**
     * Add the line segment (x1, y1) -> (x2, y2) to the map
     */
    void add(int x1, int y1, int x2, int y2);
    
    /**
     * Return a string representation of the map
     */
    std::string to_string();

    /**
     * Return the number of dangerous points on the map - the number of points
     * where 2 or more lines cross
     */
    int numDangerousPoints();
};

/**
 * Parse the input file with given name with or without processing diagonals,
 * add all lines to the map and print the number of dangerous points
 */
void printNumDangerousPoints(std::string filename, bool diagonals);