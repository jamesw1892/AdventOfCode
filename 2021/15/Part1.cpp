#include <string>
#include <vector>
#include <iostream>
#include <fstream>

/**
 * Return a vector with an element for each line of the input file
 */
std::vector<std::string> readLines(std::string filename) {
    std::ifstream F(filename);
    std::string line;
    std::vector<std::string> inp;

    while (getline(F, line)) {
        inp.push_back(line);
    }

    F.close();

    return inp;
}

/**
 * Split the string s by delim and return a vector of all the parts.
 * This includes the string after the last occurance of delim, even if
 * this is the empty string
 */
std::vector<std::string> split(std::string s, std::string delim) {
    // https://www.javatpoint.com/how-to-split-strings-in-cpp
    int pos = 0;
    std::vector<std::string> out;
    while ((pos = s.find(delim)) != std::string::npos) {
        out.push_back(s.substr(0, pos));
        s = s.erase(0, pos + delim.length());
    }
    // don't forget to add the rest - after the last delim
    out.push_back(s);
    return out;
}

/**
 * Return a string representation of a vector of strings for debugging
 */
std::string vectorToStr(std::vector<std::string> v) {
    std::string out = "{";
    for (int i = 0; i < v.size(); i++) {
        out += v.at(i) + ", ";
    }
    out = out.substr(0, out.size()-2);
    return out + "}";
}

/**
 * Return a string representation of a vector of chars for debugging
 */
std::string vectorToStr(std::vector<char> v) {
    std::string out = "{";
    for (int i = 0; i < v.size(); i++) {
        out = out + "'" + v.at(i) + "', ";
    }
    out = out.substr(0, out.size()-2);
    return out + "}";
}

/**
 * Return a string representation of a vector of ints for debugging
 */
std::string vectorToStr(std::vector<int> v) {
    std::string out = "{";
    for (int i = 0; i < v.size(); i++) {
        out += std::to_string(v.at(i)) + ", ";
    }
    out = out.substr(0, out.size()-2);
    return out + "}";
}

/**
 * Return a string representation of a vector of unsigned long longs for debugging
 */
std::string vectorToStr(std::vector<unsigned long long> v) {
    std::string out = "{";
    for (int i = 0; i < v.size(); i++) {
        out += std::to_string(v.at(i)) + ", ";
    }
    out = out.substr(0, out.size()-2);
    return out + "}";
}

/**
 * Verify that 'got' is equal to 'expected' and if not, print their
 * values in an error message and exit with status code 1.
 * For ints
 */
void assertEqual(int got, int expected) {
    if (got != expected) {
        printf("FAILED test: expected %d, but got %d\n", expected, got);
        exit(1);
    }
}

/**
 * Verify that 'got' is equal to 'expected' and if not, print their
 * values in an error message and exit with status code 1.
 * For unsigned long longs
 */
void assertEqual(unsigned long long got, unsigned long long expected) {
    if (got != expected) {
        printf("FAILED test: expected %llu, but got %llu\n", expected, got);
        exit(1);
    }
}

#define SIZE 100
#define min(a,b) (((a) < (b)) ? (a) : (b))
#define max(a,b) (((a) > (b)) ? (a) : (b))

int p1(std::string filename) {

    // short SIZE;
    // if (filename == "input_test.txt") {
    //     SIZE = 10;
    // } else {
    //     SIZE = 100;
    // }

    // parse input
    short array[SIZE][SIZE];
    short y = 0;
    for (std::string line: readLines(filename)) {
        short x = 0;
        for (char chr: line) {
            array[y][x] = chr - '0';
            x++;
        }
        y++;
    }

    // OPT[y][x] stores the min cost to get from (0,0) to entry (x, y)
    int OPT[SIZE][SIZE];
    bool PATH[SIZE][SIZE]; // PATH[y][x] stores whether PATH[y-1][x] led to it (otherwise PATH[y][x-1])
    for (short k = 0; k < 2 * SIZE - 1; k++) { // k is y + x
        for (short y = max(0, k - SIZE + 1); y <= min(k, SIZE-1); y++) {
            short x = k - y;
            if (x == 0) {
                if (y == 0) {
                    OPT[0][0] = 0;
                } else {
                    OPT[y][0] = array[y][0] + OPT[y-1][0];
                    PATH[y][0] = true;
                }
            } else if (y == 0) {
                OPT[0][x] = array[0][x] + OPT[0][x-1];
                PATH[y][0] = false;
            } else {
                int up = OPT[y-1][x];
                int left = OPT[y][x-1];
                if (up < left) {
                    OPT[y][x] = array[y][x] + up;
                    PATH[y][x] = true;
                } else {
                    OPT[y][x] = array[y][x] + left;
                    PATH[y][x] = false;
                    if (up == left) {
                        printf("Up and left equal for (%d, %d)\n", x, y);
                    }
                }
            }
        }
    }

    y = SIZE-1;
    short x = SIZE-1;
    puts("Backtracking route:");
    while (y != 0 || x != 0) {
        short ny, nx;
        std::string dir;
        if (PATH[y][x]) {
            dir = "down";
            ny = y - 1;
            nx = x;
        } else {
            dir = "right";
            ny = y;
            nx = x - 1;
        }
        printf("To get to (%d, %d), we went %s from (%d, %d) with cost %d\n", x, y, dir.c_str(), nx, ny, array[y][x]);
        x = nx;
        y = ny;
    }

    return OPT[SIZE-1][SIZE-1];
}

int main() {

    // assertEqual(p1("input_test.txt"), 40);
    printf("Ans = %d\n", p1("input.txt"));

    return 0;
}