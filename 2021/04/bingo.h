#include <vector>
#include <memory>
#include <string>
#include <cstring>

#include "lib.h"

#define BOARD_SIZE 5

class BingoBoard {
    int Board[BOARD_SIZE][BOARD_SIZE];

    /**
     * Check for bingo where the entry Bingo[i][j] was the last to be inserted
     * into so the only possible places Bingo could be is in row i or col j
     */
    bool check(int i, int j);

public:

    /**
     * Save the bingo board to the class.
     * Add 1 to each entry so we always have positive values
     */
    BingoBoard(int board[BOARD_SIZE][BOARD_SIZE]);

    /**
     * Mark a number on the bingo board as found.
     * This allows for multiple of the same number on the same board.
     * If found, it checks if bingo has been obtained and returns this
     */
    bool mark(int num);

    /**
     * Calculate the sum of all unmarked cells in the board
     */
    int sumUnmarked();
};

/**
 * Return a vector of numbers to be drawn as well as a vector of all boards
 * in the input
 */
std::pair<std::vector<int>, std::vector<std::unique_ptr<BingoBoard>>> parseInput(std::string filename);