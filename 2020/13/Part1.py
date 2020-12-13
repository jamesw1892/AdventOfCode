def parse_input(filename):

    with open(filename) as f:
        exp = int(f.readline().strip("\n"))
        times = []
        for busS in f.readline().strip("\n").split(","):
            if busS != "x":
                times.append(int(busS))

    return exp, times

def main(exp, times):

    minID = times[0]
    minTime = ((exp // times[0]) + 1) * times[0]

    for time in times[1:]:
        mod = ((exp // time) + 1) * time
        if mod < minTime:
            minID = time
            minTime = mod

    return minID * (minTime - exp)

def test():

    exp, times = parse_input("input_test.txt")
    assert main(exp, times) == 295, "Failed test input"

    print("All tests passed")

if __name__ == "__main__":
    from sys import argv
    if len(argv) >= 2 and argv[1] == "test":
        test()
    else:
        exp, times = parse_input("input.txt")
        print(main(exp, times))
