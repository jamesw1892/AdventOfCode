/**
 * The bingo values can be 0 but I mark numbers by negating them so
 * in the class, I add 1 to every number which I need to adjust for
 */

#include "bingo.h"

/**
 * Check for bingo where the entry Bingo[i][j] was the last to be inserted
 * into so the only possible places Bingo could be is in row i or col j
 */
bool BingoBoard::check(int i, int j) {
    bool row = true;
    bool col = true;
    for (int k = 0; k < BOARD_SIZE; k++) {
        if (Board[i][k] > 0) { // can't be 0
            row = false;
        }
        if (Board[k][j] > 0) {
            col = false;
        }
    }
    return row || col;
}

/**
 * Save the bingo board to the class.
 * Add 1 to each entry so we always have positive values
 */
BingoBoard::BingoBoard(int board[BOARD_SIZE][BOARD_SIZE]) {
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            Board[i][j] = board[i][j] + 1;
        }
    }
}

/**
 * Mark a number on the bingo board as found.
 * This allows for multiple of the same number on the same board.
 * If found, it checks if bingo has been obtained and returns this
 */
bool BingoBoard::mark(int num) {
    num++; // adjust for having added 1
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (Board[i][j] == num) {
                Board[i][j] *= -1;
                if (check(i, j)) {
                    return true;
                }
            }
        }
    }
    return false;
}

/**
 * Calculate the sum of all unmarked cells in the board
 */
int BingoBoard::sumUnmarked() {
    int total = 0;
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (Board[i][j] > 0) {
                total += Board[i][j] - 1; // adjust for having added 1
            }
        }
    }
    return total;
}

/**
 * Return a vector of numbers to be drawn as well as a vector of all boards
 * in the input
 */
std::pair<std::vector<int>, std::vector<std::unique_ptr<BingoBoard>>> parseInput(std::string filename) {
    std::vector<std::string> lines = readLines(filename);

    // nums read out
    std::vector<std::string> line1split = split(lines.at(0), ",");
    std::vector<int> nums;
    for (std::string item: line1split) {
        nums.push_back(stoi(item));
    }

    std::vector<std::unique_ptr<BingoBoard>> boards;
    int currentLineNum = 2;
    int currentBoard[BOARD_SIZE][BOARD_SIZE];
    int rowBoard = 0;
    int colBoard;

    while (currentLineNum < lines.size()) {
        if (strcmp(lines.at(currentLineNum).c_str(), "") == 0) {
            boards.push_back(std::make_unique<BingoBoard>(currentBoard));
            rowBoard = 0;
        } else {
            std::vector<std::string> splitLine = split(lines.at(currentLineNum), " ");
            colBoard = 0;
            for (std::string item: splitLine) {
                if (strcmp(item.c_str(), "") != 0) {
                    currentBoard[rowBoard][colBoard] = stoi(item);
                    colBoard++;
                }
            }
            rowBoard++;
        }
        currentLineNum++;
    }
    boards.push_back(std::make_unique<BingoBoard>(currentBoard));

    return std::move(std::pair<std::vector<int>, std::vector<std::unique_ptr<BingoBoard>>>(nums, std::move(boards)));
}