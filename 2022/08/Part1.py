GRID = []

with open("input.txt") as f:
    for line in f:
        LINE = []
        for char in line.strip():
            LINE.append(int(char))
        GRID.append(LINE)

HEIGHT = len(GRID)
WIDTH = len(GRID[0])

num_trees_visible = 0
for row_num, row in enumerate(GRID):
    for col_num, tree in enumerate(row):
        # North South East West
        if all(GRID[new_row_num][col_num] < tree for new_row_num in range(0, row_num)) or \
            all(GRID[new_row_num][col_num] < tree for new_row_num in range(row_num + 1, HEIGHT)) or \
            all(GRID[row_num][new_col_num] < tree for new_col_num in range(col_num + 1, WIDTH)) or \
            all(GRID[row_num][new_col_num] < tree for new_col_num in range(0, col_num)):
            num_trees_visible += 1

print(num_trees_visible)