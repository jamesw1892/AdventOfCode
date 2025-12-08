"""
Minimum Spanning Tree (MST) of 3D points using Kruskal's algorithm. I initially
wrote this blind, half remembering algorithms I learnt at uni.
"""

from __future__ import annotations
from collections.abc import Callable
from sys import argv
from math import sqrt, prod
from sortedcontainers import SortedDict

class Point3D:
    """
    Represent a point in 3 dimensions with x, y, z coordinates and corresponding
    attributes.
    """
    def __init__(self, x: int, y: int, z: int):
        self.x: int = x
        self.y: int = y
        self.z: int = z

    def distance(self, other: Point3D) -> float:
        """
        Find the Euclidean (straight line, Pythagoras) distance between this
        point and the other given point.
        """
        return sqrt(
            (self.x - other.x) ** 2 +
            (self.y - other.y) ** 2 +
            (self.z - other.z) ** 2
        )

    # Format the points coordinates
    def __str__(self) -> str:
        return f"({self.x},{self.y},{self.z})"
    __repr__ = __str__

def kruskals_until_condition(filename: str, break_condition: Callable[[list[set[Point3D]], list[Point3D], Point3D, Point3D, int], bool]):
    """
    Run Kruskal's algorithm to find a Minimal Spanning Tree on the 3D points
    stored in the file with given name. Each time two connected components are
    connected, call the break_condition function with the list of current
    connected components, the list of points, the two points that have just been
    connected, and the iteration number. If the function returns True then return.

    Note the iteration number is the number of shortest distances between two
    points considered, not the number of unions between connected components
    made. Whereas the break_condition only runs when a union is performed. This
    works for my inputs but I've just realised this could theoretically fail
    for part 1.

    Assuming there are n points, the algorithm cannot be better than O(n^2)
    since in order to know the pairs of points with the shortest distances, the
    distance between every pair of points must be calculated.
    """

    # Read all the points into a list
    with open(filename) as f:
        points: list[Point3D] = [Point3D(*map(int, line.strip().split(","))) for line in f]

    # Calculate the distance between every pair of points and maintain a sorted
    # store of these so we can pop them shortest first. O(n^2) if maintaining
    # the SortedDict is O(1) - which thinking about it, it's probably not
    distances: SortedDict[float, (Point3D, Point3D)] = SortedDict()
    for i in range(len(points)):
        for j in range(i):
            point1: Point3D = points[i]
            point2: Point3D = points[j]
            distance: float = point1.distance(point2)
            distances[distance] = (point1, point2)

    # My improvised version of a union-find data structure is a list of sets but
    # we only store non-singleton connected components
    connected_components: list[set[Point3D]] = []

    # Always increment the iteration number even if the points closest together
    # are already connected
    iteration_num: int = 0
    while True:
        iteration_num += 1

        # Pop the shortest distance
        point1, point2 = distances.popitem(index=0)[1]
        #print(f"Two closest points are {point1} and {point2}")

        # Check which connected components they're in (reference a connected
        # component by its index in the connected_components list) or -1 if it's
        # not connected to any other points yet
        point1_cc_index: int = -1
        point2_cc_index: int = -1
        for index, cc in enumerate(connected_components):
            if point1 in cc:
                point1_cc_index = index
            if point2 in cc:
                point2_cc_index = index

        # If already connected, skip this pair and continue round loop
        if point1_cc_index != -1 and point1_cc_index == point2_cc_index:
            #print("Already connected")
            continue

        # Otherwise union their connected components
        if point1_cc_index == point2_cc_index == -1:
            connected_components.append({point1, point2})
        elif point2_cc_index == -1:
            connected_components[point1_cc_index].add(point2)
        elif point1_cc_index == -1:
            connected_components[point2_cc_index].add(point1)
        else:
            connected_components[point1_cc_index].update(connected_components[point2_cc_index])
            connected_components.pop(point2_cc_index)
        #print(f"Connected, now connected components are {connected_components}")

        # Break if required
        if break_condition(connected_components, points, point1, point2, iteration_num):
            break

def run_part1(filename: str) -> None:
    num_iterations: int = 10 if filename == "test.txt" else 1000

    def break_condition(connected_components: list[set[Point3D]],
                        points: list[Point3D],
                        point1: Point3D,
                        point2: Point3D,
                        iteration_num: int) -> bool:

        # Stop when we've run for the correct number of iterations
        if iteration_num != num_iterations:
            return False

        # Count up circuits by size
        largest_3_circuits: list[set[Point3D]] = sorted(connected_components, key=lambda cc: len(cc), reverse=True)[:3]

        #print(largest_3_circuits)
        print(prod(map(len, largest_3_circuits)))

        return True

    kruskals_until_condition(filename, break_condition)

def run_part2(filename: str) -> None:

    def break_condition(connected_components: list[set[Point3D]],
                        points: list[Point3D],
                        point1: Point3D,
                        point2: Point3D,
                        iteration_num: int) -> bool:

        # Stop when all points are in one component
        if len(connected_components[0]) != len(points):
            return False

        # Output the product of the x ordinates of the last two points to be
        # connected
        print(point1.x * point2.x)
        return True

    kruskals_until_condition(filename, break_condition)

def main() -> None:
    part: str = argv[1] if len(argv) > 1 else "part1"
    filename: str = argv[2] if len(argv) > 2 else "input.txt"
    if part == "part1":
        run_part1(filename)
    elif part == "part2":
        run_part2(filename)
    else:
        raise Exception("Unknown part")

if __name__ == "__main__":
    main()
