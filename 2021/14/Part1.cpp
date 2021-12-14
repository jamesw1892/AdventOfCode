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

#include <map>

/**
 * Return whether 'm' contains 'key'
 */
bool mapContains(std::map<char, int> m, char key) {
    return m.find(key) != m.end();
}

int p1(std::string filename) {

    // parse input
    bool seenTemplate = false;
    std::string templat;
    std::map<std::string, std::string> insertionRules;
    for (std::string line: readLines(filename)) {
        if (seenTemplate) {
            std::vector<std::string> splitLine = split(line, " -> ");
            insertionRules[splitLine.at(0)] = splitLine.at(1);
        } else if (line == "") {
            seenTemplate = true;
        } else {
            templat = line;
        }
    }

    // perform steps
    for (int stepNum = 1; stepNum <= 10; stepNum++) {
        std::string current = templat;
        for (int pos = 1; pos < templat.length(); pos++) {
            std::string key = templat.substr(pos - 1, 2);
            current.insert(2 * pos - 1, insertionRules[key]);
        }
        templat = current;
    }

    // count chars
    std::map<char, int> counts;
    for (char chr: templat) {
        if (mapContains(counts, chr)) {
            counts[chr]++;
        } else {
            counts[chr] = 1;
        }
    }

    // find min & max
    int min = -1;
    int max = -1;
    for (std::map<char, int>::iterator it = counts.begin(); it != counts.end(); it++) {
        if (min == -1 || it->second < min) {
            min = it->second;
        }
        if (max == -1 || it->second > max) {
            max = it->second;
        }
    }

    return max - min;
}

int main() {

    assertEqual(p1("input_test.txt"), 1588);
    printf("Ans = %d\n", p1("input.txt"));

    return 0;
}