score = 0
with open("input.txt") as f:
    for line in f:
        opponent, us = line.split()
        if us == "X": # rock
            score += 1
            if opponent == "C": # scissors - win
                score += 6
            elif opponent == "A": # rock - draw
                score += 3
        elif us == "Y": # paper
            score += 2
            if opponent == "A": # rock - win
                score += 6
            elif opponent == "B": # paper - draw
                score += 3

        else: # Z - scissors
            score += 3
            if opponent == "B": # paper - win
                score += 6
            elif opponent == "C": # scissors - draw
                score += 3

print(score)