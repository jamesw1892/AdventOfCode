from sys import argv
from grid import Grid

def calc_paper_to_remove(grid: Grid) -> list[tuple[int, int]]:
    """
    Return a list of coordinates of paper in the grid that can be removed. These
    are all the cells that contain paper and less than 4 of the 8 neighbouring
    cells contain paper.
    """
    return [
        (y, x)
        for y in range(grid.height)
        for x in range(grid.width)
        if grid[y][x] == "@" and \
            grid.neighbours(grid.neighbour_indices_8, y, x).count("@") < 4
    ]

def run_part1(filename: str) -> None:
    print(len(calc_paper_to_remove(Grid(filename))))

def run_part2(filename: str) -> None:
    grid: Grid = Grid(filename)
    total_removable: int = 0
    while True:
        paper_to_remove: list[tuple[int, int]] = calc_paper_to_remove(grid)
        if len(paper_to_remove) == 0:
            break
        total_removable += len(paper_to_remove)
        for y, x in paper_to_remove:
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
