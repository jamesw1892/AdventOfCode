#include <string>
#include <vector>

#include "lib.h"

/**
 * Simply parse the input and count the number of strings after the '|' that
 * have length 2, 3, 4, or 7, representing numbers 1, 7, 4, and 8 respectively
 */
int p1(std::string filename) {
    int total = 0;
    for (std::string line: readLines(filename)) {
        std::vector<std::string> parts = split(line, " | ");
        std::vector<std::string> outputs = split(parts.at(1), " ");
        for (std::string output: outputs) {
            switch (output.length()) {
                case 2:
                case 3:
                case 4:
                case 7:
                    total++;
                    break;
            }
        }
    }
    return total;
}

int main() {
    assertEqual(p1("input_test.txt"), 26);
    int ans = p1("input.txt");
    printf("Ans = %d\n", ans);
    return 0;
}