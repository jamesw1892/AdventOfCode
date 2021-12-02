#include <string>
#include <vector>

#include "lib.h"

int main() {
    std::vector<std::string> lines = readLines("input.txt");

    int depth = 0;
    int horpos = 0;

    for (std::string line: lines) {
        std::vector<std::string> splitLine = split(line, " ");
        if (splitLine.size() != 2) {
            puts("Incorrect size");
            return -1;
        }
        std::string dir = splitLine.at(0);
        int x = std::stoi(splitLine.at(1));
        if (dir.compare("forward") == 0) {
            horpos += x;
        } else if (dir.compare("down") == 0) {
            depth += x;
        } else if (dir.compare("up") == 0) {
            depth -= x;
        }
    }

    printf("%d\n", depth * horpos);

    return 0;
}