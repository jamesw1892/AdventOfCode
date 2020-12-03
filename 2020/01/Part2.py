from typing import List

def read_input(name="input.txt") -> List[int]:
    l = []
    with open(name) as f:
        for line in f:
            if line.strip() != "":
                l.append(int(line.strip()))
    return l

def brute_force(l: List[int]) -> int:

    for index1 in range(len(l)):
        for index2 in range(index1+1, len(l)):
            for index3 in range(index2+1, len(l)):
                if l[index1] + l[index2] + l[index3] == 2020:
                    return l[index1] * l[index2] * l[index3]
    return 0

def main():
    l = read_input()
    print(brute_force(l))

def test():

    l = read_input("input_test.txt")

    assert brute_force(l) == 241861950, "Brute force failed on test input"

    print("All tests passed")

if __name__ == "__main__":
    from sys import argv
    if len(argv) >= 2 and argv[1] == "test":
        test()
    else:
        main()
