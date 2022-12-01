elves = []
current_elf = 0
with open("input.txt") as f:
    for line in f:
        line = line[:-1]
        if line == "":
            elves.append(current_elf)
            current_elf = 0
        else:
            current_elf += int(line)

print(max(elves))