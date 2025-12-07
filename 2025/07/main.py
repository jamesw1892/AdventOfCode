from grid import Grid
from sys import argv

def run_part1(filename: str) -> None:
    """
    Iterate through each row of the grid in turn. Draw pipe characters
    corresponding to where the beams are and count the number of splits.
    """
    grid: Grid = Grid(filename)
    num_splits: int = 0

    # On the first row just replace the S with |
    grid[0][grid[0].index("S")] = "|"

    for y in range(1, grid.height):
        for x in range(grid.width):

            # Only pay attention if the previous row has a beam in this column
            if grid[y-1][x] != "|":
                continue

            # If this cell is also a splitter then increment the number of
            # splits and draw beams either side of the splitter
            if grid[y][x] == "^":
                num_splits += 1
                grid[y][x-1] = grid[y][x+1] = "|"

            # If this cell isn't a splitter then just continue the beam down
            else:
                grid[y][x] = "|"

    print(num_splits)

def run_part2(filename: str) -> None:
    """
    Iterate through each row of the grid in turn and we only need to store the
    number of timelines where the beam is in each column. Then we only need to
    store this for the previous row to help us calculate it for the next.
    """
    grid: Grid = Grid(filename)

    # Initially we just have 1 timeline where the S is
    num_timelines_by_col: list[int] = [0 for _ in range(grid.width)]
    num_timelines_by_col[grid[0].index("S")] = 1

    # Go through the subsequent rows one by one
    for y in range(1, grid.height):
        #print(num_timelines_by_col)

        # Store the previous timelines and reset the current timelines to 0
        prev_timelines_by_col: list[int] = num_timelines_by_col
        num_timelines_by_col: list[int] = [0 for _ in range(grid.width)]

        # Go through each cell in the row. We don't need to explicitly check
        # whether the number of timelines in the cell above was 0 because we're
        # only adding so if it was 0 then nothing will be added.
        for x in range(grid.width):
            prev_timelines: int = prev_timelines_by_col[x]

            # If it's a splitter, add to the cells either side
            if grid[y][x] == "^":
                num_timelines_by_col[x-1] += prev_timelines
                num_timelines_by_col[x+1] += prev_timelines

            # If it's not a splitter, just add to the one below
            else:
                num_timelines_by_col[x] += prev_timelines

    # Add up the number of timelines over each column
    print(sum(num_timelines_by_col))

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
