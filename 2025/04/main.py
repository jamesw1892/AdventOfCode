from sys import argv

def can_be_accessed(grid: list[list[str]], x: int, y: int) -> int:
    """
    Given the grid and the coordinates of one cell, return whether the number of
    neighbours (8 total) of grid[y][x] that contain paper (are '@' rather than
    '.') is less than 4.
    """

    num = 0
    for neighbour_y in (y-1, y, y+1):
        for neighbour_x in (x-1, x, x+1):
            if 0 <= neighbour_x < len(grid[0]) and \
                0 <= neighbour_y < len(grid) and \
                (neighbour_x != x or neighbour_y != y):
                # print(f"Checking ({neighbour_x}, {neighbour_y})")
                val: str = grid[neighbour_y][neighbour_x]
                if val == "@":
                    num += 1
    # print(f"({x}, {y}) = {grid[y][x]} = {num}")
    return num < 4

def run_part1(filename: str) -> None:
    with open(filename) as f:
        grid: list[str] = [[s for s in line.strip()] for line in f.readlines()]
    print(sum(
        int(grid[y][x] == "@" and can_be_accessed(grid, x, y))
        for y in range(len(grid))
        for x in range(len(grid[0]))
    ))

def run_part2(filename: str) -> None:
    with open(filename) as f:
        grid: list[list[str]] = [[s for s in line.strip()] for line in f.readlines()]
    total_removable: int = 0
    while True:
        can_be_removed: list[tuple[int, int]] = []
        for y in range(len(grid)):
            for x in range(len(grid[0])):
                if grid[y][x] == "@" and can_be_accessed(grid, x, y):
                    can_be_removed.append((x, y))
        if len(can_be_removed) == 0:
            break
        total_removable += len(can_be_removed)
        for x, y in can_be_removed:
            grid[y][x] = "."
    print(total_removable)

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
