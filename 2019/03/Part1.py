"""
0,0 is the central console
Define +x as right
Define +y as up
"""

class Point:
    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y
    def translate(self, dx, dy):
        self.x += dx
        self.y += dy
    def manhattanDistanceFromOrigin(self):
        return abs(self.x) + abs(self.y)
    def __eq__(self, other):
        return self.x == other.x and self.y == other.y
    def copy(self):
        return Point(self.x, self.y)
    def __str__(self):
        return "Point({}, {})".format(self.x, self.y)

with open("Input.txt") as f:
    wires = [line.split(",") for line in f.readlines()]

currentPoint = Point()
wire1Visited = [currentPoint]
for instruction in wires[0]:
    direction = instruction[0]
    distance = int(instruction[1:])
    for _ in range(1, distance+1):
        if direction == "R":
            currentPoint.translate(1, 0)
        elif direction == "L":
            currentPoint.translate(-1, 0)
        elif direction == "U":
            currentPoint.translate(0, 1)
        elif direction == "D":
            currentPoint.translate(0, -1)
        wire1Visited.append(currentPoint.copy())

count = 0
pointsInCommon = []
currentPoint = Point()
for instruction in wires[1]:
    direction = instruction[0]
    distance = int(instruction[1:])
    for _ in range(1, distance+1):
        if direction == "R":
            currentPoint.translate(1, 0)
        elif direction == "L":
            currentPoint.translate(-1, 0)
        elif direction == "U":
            currentPoint.translate(0, 1)
        elif direction == "D":
            currentPoint.translate(0, -1)
        if currentPoint in wire1Visited:
            pointsInCommon.append(currentPoint.copy())
    print(count)
    count += 1

print(min(pointsInCommon, key=lambda point: point.manhattanDistanceFromOrigin()).manhattanDistanceFromOrigin())
