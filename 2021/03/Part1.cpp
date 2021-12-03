#include <string>
#include <vector>

#include "lib.h"

int main() {
    std::vector<std::string> lines = readLines("input.txt");

    std::string gamma = "";
    std::string epsilon = "";
    for (int i = 0; i < lines.at(0).length(); i++) {
        int num_zeros = 0;
        int num_ones = 0;
        for (std::string line: lines) {
            if (line.at(i) == '0') {
                num_zeros += 1;
            } else {
                num_ones += 1;
            }
        }
        if (num_zeros > num_ones) {
            gamma += "0";
            epsilon += "1";
        } else {
            gamma += "1";
            epsilon += "0";
        }
    }

    int g = strtoull(gamma.c_str(), nullptr, 2);
    int e = strtoull(epsilon.c_str(), nullptr, 2);
    printf("%d\n", g * e);

    return 0;
}