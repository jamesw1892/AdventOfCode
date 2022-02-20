floor = 0
pos = 1
with open("input.txt") as f:
    for line in f:
        for char in line:
            if char == "(":
                floor += 1
            elif char == ")":
                floor -= 1
            if floor == -1:
                print(pos)
                exit()
            pos += 1
