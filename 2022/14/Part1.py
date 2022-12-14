from enum import Enum
from typing import List

START = [500, 0]

class Point(Enum):
    AIR = 0
    ROCK = 1
    SAND = 2
    def __str__(self):
        if self == Point.AIR:
            return "."
        elif self == Point.ROCK:
            return "#"
        else:
            return "O"
    __repr__ = __str__

class Grid:
    def __init__(self):
        self.rows = dict()

    def setEntry(self, x: int, y: int, point: Point):
        if y not in self.rows:
            self.rows[y] = dict()

        self.rows[y][x] = point

    def getEntry(self, x: int, y: int) -> Point:
        if y not in self.rows:
            return Point.AIR

        if x not in self.rows[y]:
            return Point.AIR

        return self.rows[y][x]

    def addRock(self, points: List[List[int]]):
        xp, yp = points[0]
        for x, y in points[1:]:
            if x == xp:
                for yi in range(min(yp, y), max(yp, y) + 1):
                    self.setEntry(x, yi, Point.ROCK)
            elif y == yp:
                for xi in range(min(xp, x), max(xp, x) + 1):
                    self.setEntry(xi, y, Point.ROCK)
            else:
                print("Diagonal in input!")
                exit(1)

            xp = x
            yp = y

    def __str__(self):
        min_x = min(min(x for x in self.rows[y].keys()) for y in self.rows.keys())
        max_x = max(max(x for x in self.rows[y].keys()) for y in self.rows.keys())
        min_y = min(0, min(self.rows))
        max_y = max(self.rows)
        return "\n".join("".join(str(self.getEntry(x, y)) for x in range(min_x, max_x + 1)) for y in range(min_y, max_y + 1))

    __repr__ = __str__

    def sand(self) -> int:
        max_y_rock = max(self.rows)
        num_sand = 0
        change = True
        while change:
            sand_x, sand_y = START
            stopped = False
            while not stopped:

                # fell off map
                if sand_y > max_y_rock:
                    stopped = True
                    change = False

                
                # try to move sand down
                if self.getEntry(sand_x, sand_y + 1) == Point.AIR:
                    sand_y += 1

                # try to move sand down and left
                elif self.getEntry(sand_x - 1, sand_y + 1) == Point.AIR:
                    sand_x -= 1
                    sand_y += 1

                # try to move sand down and right
                elif self.getEntry(sand_x + 1, sand_y + 1) == Point.AIR:
                    sand_x += 1
                    sand_y += 1

                # sand can't move so mark as sand permanently
                else:
                    stopped = True
                    self.setEntry(sand_x, sand_y, Point.SAND)

            if change:
                num_sand += 1

        return num_sand

grid = Grid()

with open("input.txt") as f:
    for line in f:
        points_ = line.split(" -> ")
        grid.addRock([list(map(int, point.split(","))) for point in points_])

print(grid)
print(grid.sand())
