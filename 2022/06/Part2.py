from collections import deque

with open("input.txt") as f:
    INP = f.read()

N = 14
pos = N
queue = deque(INP[:N])

while len(set(queue)) < N:
    queue.popleft()
    queue.append(INP[pos])
    pos += 1

print(pos)