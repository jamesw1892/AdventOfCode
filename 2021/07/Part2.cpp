#include <string>
#include <vector>

#include "Crabs.h"

int main() {
    assertEqual(bruteForce("input_test.txt", true), 168);
    printf("Ans = %d\n", bruteForce("input.txt", true));
    return 0;
}