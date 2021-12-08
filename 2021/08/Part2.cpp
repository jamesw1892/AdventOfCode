#include <string>
#include <vector>
#include <memory>
#include <map>

#include "lib.h"
#include "strOps.h"

/**
 * Given an unsorted string representing a 7 segment display digit containing
 * some of the characters a-g (the correct ones - after they've been converted),
 * return the digit that it represents as a string
 */
std::string toDigit(std::string s) {

    // we can decide most by the number of segments required
    switch (s.length()) {
        case 2:
            return "1";
        case 3:
            return "7";
        case 4:
            return "4";
        case 7:
            return "8";

        // 2, 3, and 5 require 5 segments
        case 5:

            // 2 requires segment e but 3 and 5 don't
            if (strContains(s, 'e')) {
                return "2";

            // 3 requires segment c but 5 doesn't
            } else if (strContains(s, 'c')) {
                return "3";
            } else {
                return "5";
            }

        // 0, 6 and 9 require 6 segments
        default:

            // 6 and 9 require segment d but 0 doesn't
            if (!strContains(s, 'd')) {
                return "0";

            // 6 requires segment e but 9 doesn't
            } else if (strContains(s, 'e')) {
                return "6";
            } else {
                return "9";
            }
    }
}

/**
 * Return a lookup table from fake segments to real segments
 */
std::map<char, char> deduceConversion(std::vector<std::string> digits) {

    // create lookup table from the fake segments to the real segments
    std::map<char, char> fake2real;

    // empty std::string rather than char*
    // so can concatenate characters by concatenating this to the start
    std::string empty = "";

    // store the fake segments representing these numbers
    std::string poss1s, poss4s, poss7s, poss8s;

    // store the list of 3 fake segments representing the numbers that
    // require 5 and 6 segments respectively
    std::vector<std::string> possLen5s, possLen6s;

    // populate the above based on the number of segments
    for (std::string digit: digits) {
        switch (digit.length()) {
            case 2: // 1
                poss1s = digit;
                break;
            case 3: // 7
                poss7s = digit;
                break;
            case 4: // 4
                poss4s = digit;
                break;
            case 7: // 8
                poss8s = digit;
                break;
            case 5: // 2 or 3 or 5
                possLen5s.push_back(digit);
                break;
            case 6: // 0 or 6 or 9
                possLen6s.push_back(digit);
                break;
        }
    }

    // PROCESS OF ELIMINATION TO DETERMINE THE
    // MAPPING FROM FAKE SEGMENTS TO REAL SEGMENTS

    // the single char that's in 7 but not 1 is 'a'
    char fakeA = strDiff(poss7s, poss1s).at(0);
    fake2real[fakeA] = 'a';

    // the intersection of the segments in all the digits that have 6
    // segments (0, 6 and 9) and the segments in 1 is 'f'
    std::string size6int = strIntersection(strIntersection(possLen6s.at(0), possLen6s.at(1)), possLen6s.at(2));
    char fakeF = strIntersection(poss1s, size6int).at(0);
    fake2real[fakeF] = 'f';

    // the other segment in 1 is 'c'
    char fakeC = strDiff(poss1s, empty + fakeF).at(0);
    fake2real[fakeC] = 'c';

    // the intersection of the segments in all the digits that have 6
    // segments (0, 6 and 9) and the segments in 4 is 'b' and 'f'
    // so take off 1 (segments c, f) to get 'b'
    char fakeB = strDiff(strIntersection(size6int, poss4s), poss1s).at(0);
    fake2real[fakeB] = 'b';

    // the other segment in 4 is 'd'
    char fakeD = strDiff(poss4s, poss1s + fakeB).at(0);
    fake2real[fakeD] = 'd';

    // size6int is the fake versions of 'a', 'b', 'f', 'g'
    // so take off 'a', 'b', 'f' which we already know to get 'g'
    char fakeG = strDiff(size6int, empty + fakeA + fakeB + fakeF).at(0);
    fake2real[fakeG] = 'g';

    // whichever character hasn't been determined yet
    char fakeE = strDiff("abcdefg", empty + fakeA + fakeB + fakeC + fakeD + fakeF + fakeG).at(0);
    fake2real[fakeE] = 'e';

    return fake2real;
}

/**
 * Return the number represented by the 4 digits in 'outputs' given 'digits'
 */
int deduceNumber(std::vector<std::string> digits, std::vector<std::string> outputs) {

    // deduce a lookup table from fake segments to real segments using the
    // digits at the start of the input
    std::map<char, char> fake2real = deduceConversion(digits);

    std::string out = "";
    for (std::string output: outputs) {
        std::string real = "";
        for (char segment: output) {

            // convert the fake segment to its real version
            real += fake2real[segment];
        }

        // convert the digit the real segments represents
        out += toDigit(real);
    }

    // convert the string of 4 digits to an int
    return stoi(out);
}

/**
 * Parse the input, calculate the value represented by each 4-digit 7-segment
 * display and return the sum of these digits
 */
int p2(std::string filename) {
    int total = 0;
    for (std::string line: readLines(filename)) {
        std::vector<std::string> parts = split(line, " | ");
        std::vector<std::string> digits = split(parts.at(0), " ");
        std::vector<std::string> outputs = split(parts.at(1), " ");
        total += deduceNumber(digits, outputs);
    }
    return total;
}

int main() {
    assertEqual(p2("input_test.txt"), 61229);
    int ans = p2("input.txt");
    printf("Ans = %d\n", ans);
    return 0;
}