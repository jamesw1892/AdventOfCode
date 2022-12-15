# ROW = 10
ROW = 2000000

xs = set()
beacon_xs_in_ROW = set()

with open("input.txt") as f:
    for line in f:
        sensor, beacon = line.strip().split("Sensor at x=")[1].split(": closest beacon is at x=")
        sensor_x, sensor_y = map(int, sensor.split(", y="))
        beacon_x, beacon_y = map(int, beacon.split(", y="))
        if beacon_y == ROW:
            beacon_xs_in_ROW.add(beacon_x)

        # calc distance between sensor and beacon so the distress
        # signal can't be less than or equal to this distance away
        # from the sensor
        d = abs(sensor_x - beacon_x) + abs(sensor_y - beacon_y)

        # calculate all x positions with y=ROW that are <= d away
        # from sensor
        y_dist = abs(ROW - sensor_y)
        x_dist_available = d - y_dist
        # negative x_dist_available means this sensor reading has
        # no bearing on the distress signal because too far away
        # 0 means it can't be in 1 position in the same x position
        # as the sensor. 1 means can't be in 3 positions around the
        # x position, etc
        if x_dist_available >= 0:
            xs.add(sensor_x)
        for delta in range(1, x_dist_available + 1):
            xs.add(sensor_x + delta)
            xs.add(sensor_x - delta)

print(len(xs.difference(beacon_xs_in_ROW)))
