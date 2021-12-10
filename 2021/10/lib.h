#include <string>
#include <vector>
#include <iostream>
#include <fstream>

/**
 * Return a vector with an element for each line of the input file
 */
std::vector<std::string> readLines(std::string filename);

/**
 * Split the string s by delim and return a vector of all the parts.
 * This includes the string after the last occurance of delim, even if
 * this is the empty string
 */
std::vector<std::string> split(std::string s, std::string delim);

/**
 * Return a string representation of a vector of strings for debugging
 */
std::string vectorToStr(std::vector<std::string> v);

/**
 * Return a string representation of a vector of chars for debugging
 */
std::string vectorToStr(std::vector<char> v);

/**
 * Return a string representation of a vector of ints for debugging
 */
std::string vectorToStr(std::vector<int> v);

/**
 * Return a string representation of a vector of unsigned long longs for debugging
 */
std::string vectorToStr(std::vector<unsigned long long> v);

/**
 * Verify that 'got' is equal to 'expected' and if not, print their
 * values in an error message and exit with status code 1
 */
void assertEqual(int got, int expected);