score = 0
with open("input.txt") as f:
    for line in f:
        opponent, us = line.split()
        if us == "X": # lose
            score += 0
            if opponent == "A": # rock - we play scissors
                score += 3
            elif opponent == "B": # paper - we play rock
                score += 1
            else: # C - scissors - we play paper
                score += 2

        elif us == "Y": # draw
            score += 3
            if opponent == "A": # rock - we play rock
                score += 1
            elif opponent == "B": # paper - we play paper
                score += 2
            else: # C - scissors - we play scissors
                score += 3

        else: # Z - win
            score += 6
            if opponent == "A": # rock - we play paper
                score += 2
            elif opponent == "B": # paper - we play scissors
                score += 3
            elif opponent == "C": # scissors - we play rock
                score += 1

print(score)