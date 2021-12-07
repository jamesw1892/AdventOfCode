#include "lib.h"

/**
 * Solves the problem by brute force by parsing the input with given filename,
 * calculating the fuel use of all candidate positions between the min and
 * max positions of the crabs, and returning the min fuel use.
 * Triangular indicates whether to calculate fuel using triangular numbers - 
 * the fuel used increases with each step, or otherwise to just take the fuel
 * as the number of steps.
 */
int bruteForce(std::string filename, bool triangular);