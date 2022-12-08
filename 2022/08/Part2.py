GRID = []

with open("input_test.txt") as f:
    for line in f:
        LINE = []
        for char in line.strip():
            LINE.append(int(char))
        GRID.append(LINE)

HEIGHT = len(GRID)
WIDTH = len(GRID[0])

best_scenic_score = -1
best_row_num = -1
best_col_num = -1
for row_num, row in enumerate(GRID):
    for col_num, tree in enumerate(row):

        north = 0
        for new_row_num in range(row_num - 1, -1, -1):
            north += 1
            if GRID[new_row_num][col_num] >= tree:
                break # view blocked

        south = 0
        for new_row_num in range(row_num + 1, HEIGHT):
            south += 1
            if GRID[new_row_num][col_num] >= tree:
                break # view blocked

        east = 0
        for new_col_num in range(col_num + 1, WIDTH):
            east += 1
            if GRID[row_num][new_col_num] >= tree:
                break # view blocked

        west = 0
        for new_col_num in range(col_num - 1, -1, -1):
            west += 1
            if GRID[row_num][new_col_num] >= tree:
                break # view blocked

        scenic_score = north * south * east * west
        if scenic_score > best_scenic_score:
            best_scenic_score = scenic_score
            best_col_num = col_num
            best_row_num = row_num

print(best_scenic_score)