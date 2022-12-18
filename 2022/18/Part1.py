class Cube:
    cubes = []
    def __init__(self, x: int, y: int, z: int):
        self.x = x
        self.y = y
        self.z = z
        Cube.cubes.append(self)

    def calcSurfaceArea(self) -> int:
        surface_area = 6
        for cube in Cube.cubes:
            if (cube.x == self.x and cube.y == self.y and abs(cube.z - self.z) == 1) or \
               (cube.x == self.x and cube.z == self.z and abs(cube.y - self.y) == 1) or \
               (cube.y == self.y and cube.z == self.z and abs(cube.x - self.x) == 1):
                surface_area -= 1
        return surface_area

    @classmethod
    def calcSurfaceAreaAll(cls) -> int:
        return sum(cube.calcSurfaceArea() for cube in cls.cubes)

with open("input.txt") as f:
    for line in f:
        Cube(*map(int, line.strip().split(",")))

print(Cube.calcSurfaceAreaAll())
