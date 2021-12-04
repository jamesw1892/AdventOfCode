#include "bingo.h"

int main() {

    std::pair<std::vector<int>, std::vector<std::unique_ptr<BingoBoard>>> pair = parseInput("input.txt");
    std::vector<int> nums = pair.first;
    std::vector<std::unique_ptr<BingoBoard>> boards = std::move(pair.second);

    for (int numDrawn: nums) {
        for (int i = 0; i < boards.size(); i++) {
            if (boards.at(i)->mark(numDrawn)) {
                int sumUnmarked = boards.at(i)->sumUnmarked();
                printf("Ans = %d = %d * %d\n", sumUnmarked * numDrawn, sumUnmarked, numDrawn);
                return 0;
            }
        }
    }

    return 0;
}