#include "bingo.h"

int main() {

    std::pair<std::vector<int>, std::vector<std::unique_ptr<BingoBoard>>> pair = parseInput("input_test.txt");
    std::vector<int> nums = pair.first;
    std::vector<std::unique_ptr<BingoBoard>> boards = std::move(pair.second);

    for (int numDrawn: nums) {

        int i = 0;
        while (i < boards.size()) {

            if (boards.at(i)->mark(numDrawn)) {
                if (boards.size() == 1) {
                    int sumUnmarked = boards.at(i)->sumUnmarked();
                    printf("Ans = %d = %d * %d\n", sumUnmarked * numDrawn, sumUnmarked, numDrawn);
                    return 0;
                }
                boards.erase(boards.begin() + i);
            } else {
                i++;
            }
        }
    }

    return 0;
}