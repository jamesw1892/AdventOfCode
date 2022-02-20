floor = 0
with open("input.txt") as f:
    for line in f:
        for char in line:
            if char == "(":
                floor += 1
            elif char == ")":
                floor -= 1

print(floor)