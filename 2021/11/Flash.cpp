#include <string>
#include "Flash.h"

/**
 * Print the grid of natural numbers. Any numbers >9 are printed as letters
 * starting from A. The indices are included on the axes for easy finding
 */
void printGrid(unsigned short a[][SIZE]) {
    std::string out = "\n  0123456789\n  ----------\n";
    for (unsigned short y = 0; y < SIZE; y++) {
        out += y + '0';
        out += "|";
        for (unsigned short x = 0; x < SIZE; x++) {
            unsigned short v = a[y][x];

            // use hex but can go through whole alphabet so can deal with numbers
            // up to 36 before going to other characters
            if (v <= 9) {
                out += a[y][x] + '0';
            } else {
                out += a[y][x] - 10 + 'A';
            }
        }
        out += "\n";
    }
    puts(out.c_str());
}