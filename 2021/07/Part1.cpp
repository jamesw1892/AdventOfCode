#include <string>
#include <vector>

#include "Crabs.h"

int main() {
    assertEqual(bruteForce("input_test.txt", false), 37);
    printf("Ans = %d\n", bruteForce("input.txt", false));
    return 0;
}