#include "lib.h"

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
 * Verify that 'got' is equal to 'expected' and if not, print their
 * values in an error message and exit with status code 1
 */
void assertEqual(int got, int expected) {
    if (got != expected) {
        printf("FAILED test: expected %d, but got %d\n", expected, got);
        exit(1);
    }
}