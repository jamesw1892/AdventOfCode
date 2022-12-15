from typing import Tuple, Union

MAX = 20
# MAX = 4000000
lines = []

class Line:
    def __init__(self, m: int, c: int, min_y: int, max_y: int):
        self.m = m
        self.c = c
        self.min_y = min_y
        self.max_y = max_y

    @staticmethod
    def fromStartAndEnd(x1: int, y1: int, x2: int, y2: int):

        if (x1 < x2 and y1 < y2) or (x1 > x2 and y1 > y2):
            m = 1
        else:
            m = -1

        c = y2 - m*x2

        return Line(
            m,
            c,
            min(y1, y2),
            max(y1, y2)
        )

    def intersection(self, other) -> Union[None, Tuple[int]]:
        assert isinstance(other, Line)

        if self.m != other.m:
            x = (other.c - self.c) / (self.m - other.m)
            y = self.m * x + self.c
            if self.min_y <= y <= self.max_y and other.min_y <= y <= other.max_y:
                return (x, y)
        return None

def calc_d(x1: int, x2: int, y1: int, y2: int) -> int:
    return abs(x1 - x2) + abs(y1 - y2)

with open("input_test.txt") as f:
    for line in f:
        sensor, beacon = line.strip().split("Sensor at x=")[1].split(": closest beacon is at x=")
        sensor_x, sensor_y = map(int, sensor.split(", y="))
        beacon_x, beacon_y = map(int, beacon.split(", y="))
        d = calc_d(sensor_x, beacon_x, sensor_y, beacon_y)

        lines.append(Line.fromStartAndEnd(sensor_x + d, sensor_y, sensor_x, sensor_y + d))

intersections = []

for line1 in lines:
    for line2 in lines:
        intersection = line1.intersection(line2)
        if intersection is not None:
            intersections.append(intersection)
            

print(4000000*distress_x + distress_y)
