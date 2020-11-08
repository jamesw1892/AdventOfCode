"""
0,0 is the central console
Define +x as right
Define +y as up
"""

class Point:
    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y
    def translate(self, direction, distance):
        if direction == "R":
            self.x += distance
        elif direction == "L":
            self.x -= distance
        elif direction == "U":
            self.y += distance
        elif direction == "D":
            self.y -= distance
    def manhattanDistanceFromOrigin(self):
        return abs(self.x) + abs(self.y)
    def __eq__(self, other):
        return self.x == other.x and self.y == other.y
    def copy(self):
        return Point(self.x, self.y)
    def __str__(self):
        return "Point({}, {})".format(self.x, self.y)

class Segment:
    def __init__(self, start, direction, distance):
        self.start = start
        self.direction = direction
        self.distance = distance
    def __contains__(self, point):
        current = self.start.copy()
        if current == point:
            return True
        for _ in range(self.distance):
            current.translate(self.direction, 1)
            if current == point:
                return True
        return False

with open("Input.txt") as f:
    wires = [line.split(",") for line in f.readlines()]

currentPoint = Point()
wire1Segments = []
for instruction in wires[0]:
    direction = instruction[0]
    distance = int(instruction[1:])
    wire1Segments.append(Segment(currentPoint.copy(), direction, distance))
    currentPoint.translate(direction, distance)

currentPoint = Point()
pointsInCommon = []
for instruction in wires[1]:
    direction = instruction[0]
    distance = int(instruction[1:])
    for segment in wire1Segments:
        if currentPoint in segment:
            pointsInCommon.append(currentPoint.copy())
    for _ in range(distance):
        currentPoint.translate(direction, 1)
        for segment in wire1Segments:
            if currentPoint in segment:
                pointsInCommon.append(currentPoint.copy())

pointsInCommon.remove(Point())
print(min(pointsInCommon, key=lambda point: point.manhattanDistanceFromOrigin()).manhattanDistanceFromOrigin())
