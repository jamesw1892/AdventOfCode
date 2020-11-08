from math import floor

with open("Input.txt") as f:
    contents = f.readlines()

total = 0
for line in contents:
    total += floor(int(line) / 3) - 2

print(total)
