score = 0
rem = 0
with open("input.txt") as f:
    for line in f:
        rem += 1
        if rem % 3 == 0:
            line3 = line
            for letter in line1:
                if letter in line2 and letter in line3:
                    if letter in "abcdefghijklmnopqrstuvwxyz":
                        score += ord(letter) - ord("a") + 1
                    else:
                        score += ord(letter) - ord("A") + 27
                    break
        elif rem % 3 == 1:
            line1 = line
        else:
            line2 = line
print(score)