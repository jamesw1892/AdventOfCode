from math import floor

with open("Input.txt") as f:
    contents = f.readlines()

def calcFuel(mass):
    return max(0, floor(mass/3) - 2)

total = 0
for line in contents:
    fuel = calcFuel(int(line))
    while fuel > 0:
        total += fuel
        fuel = calcFuel(fuel)

print(total)