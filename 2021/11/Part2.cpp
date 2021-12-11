#include <string>
#include <vector>
#include <deque>

#include "lib.h"
#include "Flash.h"

int solve(std::string filename) {

    // parse input into array
    unsigned short a[SIZE][SIZE];
    unsigned short y = 0;
    for (std::string line: readLines(filename)) {
        unsigned short x = 0;
        for (char n: line) {
            a[y][x] = n - '0';
            x++;
        }
        y++;
    }

    int stepNum = 0;

    while (true) {

        stepNum++;

        // store boolean array of whether each point has been flashed
        bool flashed[SIZE][SIZE];

        // add all points to the queue (and initialise flashed to false)
        std::deque<std::pair<short, short>> d;
        for (short y = 0; y < SIZE; y++) {
            for (short x = 0; x < SIZE; x++) {
                d.push_back(std::pair<short, short>(y, x));
                flashed[y][x] = false;
            }
        }

        while (!d.empty()) {

            // dequeue
            std::pair<short, short> p = d.front();
            short y = p.first;
            short x = p.second;
            d.pop_front();

            // increment the point
            a[y][x]++;

            // if it should flash but hasn't already flashed (=10)
            if (a[y][x] == 10) {
                flashed[y][x] = true;

                // add all neighbours to the queue
                for (short adjY = y-1; adjY <= y+1; adjY++) {
                    for (short adjX = x-1; adjX <= x+1; adjX++) {

                        // don't add out of range or self
                        if (adjY >= 0 && adjX >= 0 && adjX < SIZE && adjY < SIZE && (adjX != x || adjY != y)) {
                            d.push_back(std::pair<short, short>(adjY, adjX));
                        }
                    }
                }
            }
        }

        // set flashed to 0 before next round and check if all flashed
        bool allFlashed = true;
        for (unsigned short y = 0; y < SIZE; y++) {
            for (unsigned short x = 0; x < SIZE; x++) {
                allFlashed = allFlashed && flashed[y][x];
                if (a[y][x] > 9) {
                    a[y][x] = 0;
                }
            }
        }

        // carry on for another round until all flashed
        if (allFlashed) {
            break;
        }
    }

    return stepNum;
}

int main() {
    assertEqual(solve("input_test.txt"), 195);
    printf("Ans = %d\n", solve("input.txt"));
    return 0;
}