# MAX = 20
MAX = 4000000
entries = []
beacons = []

def calc_d(x1: int, x2: int, y1: int, y2: int) -> int:
    return abs(x1 - x2) + abs(y1 - y2)

with open("input.txt") as f:
    for line in f:
        sensor, beacon = line.strip().split("Sensor at x=")[1].split(": closest beacon is at x=")
        sensor_x, sensor_y = map(int, sensor.split(", y="))
        beacon_x, beacon_y = map(int, beacon.split(", y="))
        beacons.append((beacon_x, beacon_y))
        entries.append((sensor_x, sensor_y, calc_d(sensor_x, beacon_x, sensor_y, beacon_y)))

for y in range(MAX + 1):
    if y % 1000 == 0:
        print(y)
    for x in range(MAX + 1):
        for entry in entries:
            if calc_d(x, entry[0], y, entry[1]) <= entry[2]:
                break
        else:
            distress_x = x
            distress_y = y
            print(4000000*distress_x + distress_y)
            exit()
