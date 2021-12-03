#include <string>
#include <vector>

#include "lib.h"

int main() {
    std::vector<std::string> oxyLines = readLines("input.txt");
    std::vector<std::string> co2Lines = readLines("input.txt");

    std::string oxy = "";

    for (int j = 0; j < oxyLines.at(0).length(); j++) {

        int num_zeros = 0;
        int num_ones = 0;
        for (std::string line: oxyLines) {
            if (line.at(j) == '0') {
                num_zeros += 1;
            } else {
                num_ones += 1;
            }
        }

        int i = 0;
        while (i < oxyLines.size()) {

            if ((num_ones >= num_zeros && oxyLines.at(i).at(j) == '1') || (num_ones < num_zeros && oxyLines.at(i).at(j) == '0')) {
                i++;
            } else {
                oxyLines.erase(oxyLines.begin() + i);
            }
        }

        if (oxyLines.size() == 1) {
            oxy = oxyLines.at(0);
            break;
        }
    }

    std::string co2 = "";

    for (int j = 0; j < co2Lines.at(0).length(); j++) {

        int num_zeros = 0;
        int num_ones = 0;
        for (std::string line: co2Lines) {
            if (line.at(j) == '0') {
                num_zeros += 1;
            } else {
                num_ones += 1;
            }
        }

        int i = 0;
        while (i < co2Lines.size()) {

            if ((num_ones < num_zeros && co2Lines.at(i).at(j) == '1') || (num_ones >= num_zeros && co2Lines.at(i).at(j) == '0')) {
                i++;
            } else {
                co2Lines.erase(co2Lines.begin() + i);
            }
        }


        if (co2Lines.size() == 1) {
            co2 = co2Lines.at(0);
            break;
        }
    }

    int o = strtoull(oxy.c_str(), nullptr, 2);
    int c = strtoull(co2.c_str(), nullptr, 2);
    printf("%d = %d * %d\n", o * c, o, c);

    return 0;
}