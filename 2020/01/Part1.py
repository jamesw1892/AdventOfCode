from typing import List

def brute_force(l: List[int]) -> int:

    for index1 in range(len(l)):
        for index2 in range(index1+1, len(l)):
            if l[index1] + l[index2] == 2020:
                return l[index1] * l[index2]
    return 0

if __name__ == "__main__":
    l = []
    with open("input.txt") as f:
        for line in f:
            if line.strip() != "":
                l.append(int(line.strip()))
    print(brute_force(l))
