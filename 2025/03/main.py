from sys import argv
from itertools import combinations

def largest_joltage_brute_force(bank: str, num_batteries: int) -> int:
    """
    Brute force by using `combination` to generate all in-order sequences of
    `num_batteries` batteries from bank, concatenating the digits in each
    sequence together to get integers, and finding the maximum one. This becomes
    infeasible pretty quickly.
    """
    return max(int("".join(joltages)) for joltages in combinations(bank, r=num_batteries))

def largest_joltage_custom(bank: str, num_batteries: int) -> int:
    """
    Return the largest joltage obtainable by turning on num_batteries batteries
    of the batteries in the given bank.

    Bank is a string of digits where each digit is between 1 and 9. Select
    num_batteries of them such that the integer created by concatenating their
    digits together (in their original order) is maximised.

    Notice that since the output always has num_batteries digits, and there is
    no carry when concatenating the digits, to maximise the output, each digit
    must be maximised in order from most to least significant (left to right).
    But remember we need to leave enough room to still be able to select all the
    remaining digits (so we must choose from the first `len(bank)-num_batteries`
    digits).

    So this algorithm is recursive where each iteration chooses the first digit
    and recursive calls select the batteries AFTER the selected battery and
    decrement num_batteries. The base case is when num_batteries is 1 where we
    just return the maximum digit.

    My first thought was that if there are multiple indices with the same max
    value then we must make multiple recursive calls and see which is largest.
    But then I realised that it's always at least as good to choose the first
    max value since subsequent iterations will have at least as much choice for
    subsequent values as choosing a later max value.

    It is tail-recursive so it could be made iterative.
    """

    first_index_with_max: int = max(
        range(len(bank) - num_batteries + 1),
        key=lambda i: int(bank[i])
    )
    max_digit: int = int(bank[first_index_with_max])

    if num_batteries == 1:
        return max_digit

    # To concatenate two integers together, multiply by 10 to a power and add
    return largest_joltage_custom(bank[first_index_with_max+1:], num_batteries-1) + \
        max_digit * 10 ** (num_batteries - 1)

def day3(filename: str, num_batteries: int) -> None:
    with open(filename) as f:
        print(sum(largest_joltage_custom(line.strip(), num_batteries) for line in f))

def run_part1(filename: str) -> None:
    day3(filename, 2)

def run_part2(filename: str) -> None:
    day3(filename, 12)

def main() -> None:
    part: str = argv[1] if len(argv) > 1 else "part1"
    filename: str = argv[2] if len(argv) > 2 else "input.txt"
    if part == "part1":
        run_part1(filename)
    elif part == "part2":
        run_part2(filename)
    else:
        raise Exception("Unknown part")

if __name__ == "__main__":
    main()
