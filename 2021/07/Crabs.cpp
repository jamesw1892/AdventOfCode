#include "Crabs.h"

/**
 * Parse the input with given filename and return a vector of ints representing
 * the crabs horizontal positions
 */
std::vector<int> parseInput(std::string filename) {

    // all data is on the first line separated by commas
    std::vector<std::string> line = split(readLines(filename).at(0), ",");

    // convert to ints
    std::vector<int> nums;
    for (std::string num: line) {
        nums.push_back(stoi(num));
    }

    return nums;
}

/**
 * Calculate the total fuel use of the given target position on all crabs.
 * Triangular indicates whether to calculate fuel using triangular numbers - 
 * the fuel used increases with each step, or otherwise to just take the fuel
 * as the number of steps.
 */
int calcFuel(int target, std::vector<int> nums, bool triangular) {
    int total = 0;

    for (int num: nums) {
        int n = abs(num - target);

        // use the formula for triangular numbers
        if (triangular) {
            total += n*(n+1)/2;

        // just use the distance travelled
        } else {
            total += n;
        }
    }

    return total;
}

/**
 * Solves the problem by brute force by parsing the input with given filename,
 * calculating the fuel use of all candidate positions between the min and
 * max positions of the crabs, and returning the min fuel use.
 * Triangular indicates whether to calculate fuel using triangular numbers - 
 * the fuel used increases with each step, or otherwise to just take the fuel
 * as the number of steps.
 */
int bruteForce(std::string filename, bool triangular) {

    std::vector<int> nums = parseInput(filename);

    // calculate minimum and maximum horizontal positions of the crabs
    // to give the range of candidate positions to try
    int min = nums.at(0);
    int max = nums.at(0);
    for (int i = 1; i < nums.size(); i++) {
        if (nums.at(i) < min) {
            min = nums.at(i);
        }
        if (nums.at(i) > max) {
            max = nums.at(i);
        }
    }

    // for every candidate position, calculate the total fuel required to
    // send all crabs there and find the minimum
    int minFuel = calcFuel(min, nums, triangular);
    for (int pos = min + 1; pos <= max; pos++) {
        int fuel = calcFuel(pos, nums, triangular);
        if (fuel < minFuel) {
            minFuel = fuel;
        }
    }

    return minFuel;
}