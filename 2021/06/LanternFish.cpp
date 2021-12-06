#include <vector>

#include "lib.h"

/**
 * Sum the values in the array
 */
unsigned long long totalFish(unsigned long long fish[]) {
    unsigned long long total = 0;
    for (int i = 0; i < 9; i++) {
        total += fish[i];
    }
    return total;
}

/**
 * Print the number of lantern fish after the given number of days given
 * initial numbers in the given filename
 */
void printNumFish(int days, std::string filename) {

    // create the array to store the counts of each fish with each
    // internal timer. Initialised to 0s
    unsigned long long fish[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    // parse the input - all data is on the first line and add to array
    std::vector<std::string> nums = split(readLines(filename).at(0), ",");
    for (std::string num: nums) {
        fish[stoull(num)]++;
    }

    // to store the previous day's fish with internal timer 0
    unsigned long long prev0;

    for (int day = 1; day <= days; day++) {

        // each day, reduce each fish's internal timer but fish with internal
        // timer 0 change to internal timer 6 and create a child with internal
        // timer 8
        prev0   = fish[0];
        fish[0] = fish[1];
        fish[1] = fish[2];
        fish[2] = fish[3];
        fish[3] = fish[4];
        fish[4] = fish[5];
        fish[5] = fish[6];
        fish[6] = fish[7] + prev0;
        fish[7] = fish[8];
        fish[8] = prev0;
    }

    // output the number of fish
    printf("Fish after %d days = %llu\n", days, totalFish(fish));
}