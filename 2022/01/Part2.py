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

top_elf = max(elves)
elves.remove(top_elf)
second_elf = max(elves)
elves.remove(second_elf)
third_elf = max(elves)
print(top_elf + second_elf + third_elf)