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

#include <memory>
#include <map>

class Cave {
public:
    bool Big;
    bool Visited = false;
    std::string Name;
    std::vector<std::shared_ptr<Cave>> Neighbours;
    Cave(bool big, std::string name) {
        Big = big;
        Name = name;
    }
    void addNeighbour(std::shared_ptr<Cave> neighbour) {
        Neighbours.push_back(neighbour);
    }
};

/**
 * Return whether 'm' contains 'key'
 */
bool mapContains(std::map<std::string, std::shared_ptr<Cave>> m, std::string key) {
    return m.find(key) != m.end();
}

/**
 * Depth first search so can mark visited if small cave and won't visit again.
 * Return 0 if we are forced to revisit a small cave as no paths.
 * Return num paths from start given the configurations.
 * Make sure to mark unvisited when we exit visiting a small cave for the first time
 */
int explore(std::shared_ptr<Cave> start, std::map<std::string, std::shared_ptr<Cave>> caves, std::string pathSoFar) {

    // successful path
    if (start->Name == "end") {
        // printf("Finished path: %s\n", pathSoFar.c_str());
        return 1;
    }

    int numPaths = 0;
    for (std::shared_ptr<Cave> neighbour: start->Neighbours) {

        // don't visit small caves already visited
        if (neighbour->Big || !neighbour->Visited) {
            neighbour->Visited = true;
            numPaths += explore(neighbour, caves, pathSoFar + "," + neighbour->Name);
            neighbour->Visited = false;
        }
    }
    return numPaths;
}

int numPaths(std::string filename) {

    std::map<std::string, std::shared_ptr<Cave>> caves;

    for (std::string line: readLines(filename)) {
        std::vector<std::string> splitLine = split(line, "-");
        std::string from = splitLine.at(0);
        std::string to   = splitLine.at(1);
        std::shared_ptr<Cave> caveFrom;
        if (mapContains(caves, from)) {
            caveFrom = caves[from];
        } else {
            caveFrom = std::make_shared<Cave>(isupper(from.at(0)), from);
        }
        std::shared_ptr<Cave> caveTo;
        if (mapContains(caves, to)) {
            caveTo = caves[to];
        } else {
            caveTo = std::make_shared<Cave>(isupper(to.at(0)), to);
        }
        caveFrom->addNeighbour(caveTo);
        caveTo->addNeighbour(caveFrom);
        caves[from] = caveFrom;
        caves[to] = caveTo;
    }

    std::shared_ptr<Cave> caveStart = caves.at("start");
    caveStart->Visited = true;
    return explore(caveStart, caves, "start");
}

int main() {
    assertEqual(numPaths("input_test.txt"), 10);
    assertEqual(numPaths("input_test2.txt"), 19);
    assertEqual(numPaths("input_test3.txt"), 226);
    assertEqual(numPaths("input.txt"), 4186);
    printf("Ans = %d\n",  numPaths("input.txt"));
    return 0;
}