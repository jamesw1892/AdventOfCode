score = 0
with open("input.txt") as f:
    for line in f:
        half1 = line[:len(line)//2]
        half2 = line[len(line)//2:]
        for letter in half1:
            if letter in half2:
                if letter in "abcdefghijklmnopqrstuvwxyz":
                    score += ord(letter) - ord("a") + 1
                else:
                    score += ord(letter) - ord("A") + 27
                break
print(score)