#include <string>
#include <vector>
#include <map>
#include <algorithm>

#include "lib.h"

/**
 * Calculate the syntax errors in the file:
 * - for lines that have incorrect matching brackets, calculate the syntax score
 * - for incomplete lines, calculate the completion score
 * Return the sum of the syntax scores and the median completion score
 */
std::pair<int, unsigned long long> syntax(std::string filename) {

    // lookup tables for scores
    std::map<char, int>                    syntaxScoreTable = {{')', 3}, {']', 57}, {'}', 1197}, {'>', 25137}};
    std::map<char, unsigned long long> completionScoreTable = {{')', 1}, {']',  2}, {'}',    3}, {'>',     4}};

    // store the brackets on a stack where we push opening brackets and pop
    // corresponding closing brackets. For part 1, we don't care if the stack
    // is empty at the end but part 2 we do.
    std::vector<char> stack;

    // score the total syntax score
    int syntaxScore = 0;

    // store each completion score here to sort later
    std::vector<unsigned long long> completionScores;

    for (std::string line: readLines(filename)) {

        // empty the stack because for lines with syntax errors, this will probably
        // have elements in it that we don't want to keep for the next line
        stack.clear();

        // store whether there has been a syntax error
        bool syntaxError = false;

        for (char chr: line) {
            switch (chr) {

                // if we see an opening bracket, push it (actually push its
                // corresponding closing bracket so we can compare easily later)
                case '(':
                    stack.push_back(')');
                    break;
                case '[':
                    stack.push_back(']');
                    break;
                case '{':
                    stack.push_back('}');
                    break;
                case '<':
                    stack.push_back('>');
                    break;

                // if it's a closing bracket, compare to what we expected
                default: {
                    char expected = stack.back();
                    stack.pop_back();

                    // if it's not what we expected, there is a syntax error
                    // so add to syntax score
                    if (expected != chr) {
                        syntaxScore += syntaxScoreTable[chr];
                        syntaxError = true;
                    }
                }
            }
            // if we have seen a syntax error on a line,
            // don't carry on with the rest of the line
            if (syntaxError) {
                break;
            }
        }

        // if the line is incomplete rather than a syntax error:
        if (!syntaxError) {

            // calculate the completion score with the brackets still on the stack
            unsigned long long completionScore = 0;
            while (stack.size() > 0) {
                char top = stack.back();
                stack.pop_back();
                completionScore *= 5;
                completionScore += completionScoreTable[top];
            }

            // add the completion score to the list
            completionScores.push_back(completionScore);
        }
    }

    // sort the completion scores
    std::sort(completionScores.begin(), completionScores.end());

    // the median element is the element at this index once sorted
    int indexMiddle = (completionScores.size() - 1) / 2;
    unsigned long long completionScore = completionScores.at(indexMiddle);

    // return the scores
    return std::pair<int, unsigned long long>(syntaxScore, completionScore);
}

int main() {

    // check the tests pass
    std::pair<int, unsigned long long> test = syntax("input_test.txt");
    assertEqual(test.first, 26397);
    assertEqual(test.second, 288957);

    // print the results for the real input
    std::pair<int, unsigned long long> real = syntax("input.txt");
    printf("Syntax score = %d\n", real.first);
    printf("Completion score = %llu\n", real.second);

    return 0;
}