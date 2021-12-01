def num_increases(l: list) -> int:

    prev = None
    count = 0
    for line in l:
        current = int(line)
        if prev is not None and current > prev:
            count += 1
        prev = current

    return count

if __name__ == "__main__":
    sums = []
    i = 0
    with open("input.txt") as f:
        for line in f:
            current = int(line)
            sums.append(current)
            if i > 0:
                sums[i-1] += current
                if i > 1:
                    sums[i-2] += current
            i += 1

    # remove last two sums where only 1 or 2 numbers are in the sum
    sums = sums[:-2]

    print(num_increases(sums))
