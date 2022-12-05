from typing import List
from collections import deque

def parse_arrangement(lines: List[str]) -> List[deque]:
    stacks = []
    for _ in lines[-1].split():
        stacks.append(deque())

    # reversed and ignoring the (now) first which was the last with stack numbers
    for line in lines[::-1][1:]:
        for stack_num, stack in enumerate(stacks):
            letter = line[4 * stack_num + 1]
            if letter != " ":
                stack.append(letter)

    return stacks

parsing_arrangement = True
arrangement_lines = []
stacks = []
with open("input.txt") as f:
    for line in f:
        if line == "\n":
            parsing_arrangement = False
            stacks = parse_arrangement(arrangement_lines)
        elif parsing_arrangement:
            arrangement_lines.append(line)
        else:
            _, num_to_move, _, from_, _, to = line.split()
            num_to_move, from_, to = int(num_to_move), int(from_), int(to)
            # moving multiple crates at once and retaining order is equivalent
            # to moving to a temporary stack and then back to reverse order twice
            temp = deque()
            for _ in range(num_to_move):
                temp.append(stacks[from_ - 1].pop())
            for _ in range(num_to_move):
                stacks[to - 1].append(temp.pop())

for stack in stacks:
    print(stack.pop(), end="")
