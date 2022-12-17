from typing import List

WIDTH = 7

def print_cave(cave: List[List[str]], current_rock_positions: List[List[str]]) -> str:

    # deep copy
    c = []
    for line in cave:
        c.append(line.copy())
    crps = []
    for line in current_rock_positions:
        crps.append(line.copy())

    for x, y in crps:
        while y >= len(c):
            c.append(["."] * WIDTH)
        c[y][x] = "@"

    lines = ["+" + "-" * WIDTH + "+"]
    for line in c:
        str_line = "".join(line)
        lines.append(f"|{str_line}|")

    print("\n".join(lines[::-1]))

def place_rock(type: int, max_y: int) -> List[List[str]]:
    low = max_y + 3
    if type == 0: # -
        return [
            [2, low],
            [3, low],
            [4, low],
            [5, low],
        ]
    elif type == 1: # +
        return [
            [3, low + 2],
            [2, low + 1],
            [3, low + 1],
            [4, low + 1],
            [3, low]
        ]
    elif type == 2: # mirrored L
        return [
            [4, low + 2],
            [4, low + 1],
            [2, low],
            [3, low],
            [4, low]
        ]
    elif type == 3: # |
        return [
            [2, low + 3],
            [2, low + 2],
            [2, low + 1],
            [2, low],
        ]
    else: # 4 -  square
        return [
            [2, low + 1],
            [3, low + 1],
            [2, low],
            [3, low]
        ]

with open("input.txt") as f:
    jets = f.readline().strip()

cave = []
jet_index = 0
rock_index = 0
current_rock_positions = place_rock(0, 0)
while True:
    jet = jets[jet_index]

    # move due to jet if possible
    if jet == "<":
        for x, y in current_rock_positions:
            if x - 1 < 0 or (y < len(cave) and cave[y][x - 1] == "#"):
                break
        else:
            for position in current_rock_positions:
                position[0] -= 1
    else: # ">"
        for x, y in current_rock_positions:
            if x + 1 >= WIDTH or (y < len(cave) and cave[y][x + 1] == "#"):
                break
        else:
            for position in current_rock_positions:
                position[0] += 1

    # try to fall, otherwise stop falling and add new rock
    can_fall = True
    for x, y in current_rock_positions:
        if y <= 0 or (y - 1 < len(cave) and cave[y - 1][x] == "#"):
            can_fall = False
            break

    if can_fall:
        for position in current_rock_positions:
            position[1] -= 1
    else:
        # add current rock to cave
        for x, y in current_rock_positions:
            while y >= len(cave):
                cave.append(["."] * WIDTH)
            cave[y][x] = "#"

        # check if done
        rock_index += 1
        if rock_index == 2022: # this is actually the 2023rd rock as just incremented and start from 0
            break

        # add new rock
        current_rock_positions = place_rock(rock_index % 5, len(cave))

        # print_cave(cave, current_rock_positions)
        # input(">")

    jet_index = (jet_index + 1) % len(jets)

print(len(cave))
