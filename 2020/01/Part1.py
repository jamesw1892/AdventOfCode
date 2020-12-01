from typing import List

def read_input() -> List[int]:
    l = []
    with open("input.txt") as f:
        for line in f:
            if line.strip() != "":
                l.append(int(line.strip()))
    return l

def brute_force(l: List[int]) -> int:
    """
    Runs in quadratic time
    """

    for index1 in range(len(l)):
        for index2 in range(index1+1, len(l)):
            if l[index1] + l[index2] == 2020:
                return l[index1] * l[index2]
    return 0

def binary_search(l: List[int], target: int) -> bool:

    start = 0
    end = len(l) - 1
    while start <= end:
        mid = (start + end) // 2
        if l[mid] == target:
            return True
        elif l[mid] < target:
            start = mid + 1
        else:
            end = mid - 1

    return False

def sort_search(l: List[int]) -> int:
    """
    Idea is to sort a copy of the list. Then we can iterate through the
    first list in order and binary search for 2020- that element in the 
    sorted list. That should bring the asymptotic running time down to
    linearithmic
    """

    s = sorted(l) # sort into ascending order

    for item in l:
        opposite = 2020 - item
        if binary_search(s, opposite):
            return opposite * item
    return 0

if __name__ == "__main__":
    l = read_input()
    print(sort_search(l))
