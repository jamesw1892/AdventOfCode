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

    int numFlashes = 0;

    for (unsigned short stepNum = 1; stepNum <= 100; stepNum++) {

        // add all points to the queue
        std::deque<std::pair<short, short>> d;
        for (short y = 0; y < SIZE; y++) {
            for (short x = 0; x < SIZE; x++) {
                d.push_back(std::pair<short, short>(y, x));
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
                numFlashes++;

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

        // set flashed to 0 before next round
        for (unsigned short y = 0; y < SIZE; y++) {
            for (unsigned short x = 0; x < SIZE; x++) {
                if (a[y][x] > 9) {
                    a[y][x] = 0;
                }
            }
        }
    }

    return numFlashes;
}

int main() {
    assertEqual(solve("input_test.txt"), 1656);
    printf("Ans = %d\n", solve("input.txt"));
    return 0;
}