cycle = 1
X = 1
ans = 0
with open("input.txt") as f:
    for line in f:
        line = line.strip()
        if cycle in [20, 60, 100, 140, 180, 220]:
            ans += cycle * X
        if line == "noop":
            cycle += 1
        elif line != "": # addx
            if cycle + 1 in [20, 60, 100, 140, 180, 220]:
                ans += (cycle + 1) * X
            V = int(line.split()[1])
            X += V
            cycle += 2

print(ans)