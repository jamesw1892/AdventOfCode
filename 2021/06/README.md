# Advent of Code 2021 Day 6

Fish with the same internal timer act the same so we only need to store the number of fish with each of the 9 (0-8) possible timer values. Then at each day, the number of fish that previously had timer value `1 <= n <= 8` now have timer value `n-1`, and the number of fish that previously had timer value 0 is added to the number of fish that now have timer values 6 (themselves) and 8 (their child).

This only requires an array of 9 numbers (I use unsigned long longs since part 2 requires large numbers) for the current day and a small amount of copying and adding for each day. We don't even need to copy the whole array each day because all numbers get cascaded down apart from the fish with an internal timer of 0 so we only need to copy this number.

Finally we just sum the entries in the array to get the total number of fish.