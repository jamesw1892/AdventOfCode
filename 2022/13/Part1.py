def compare_sides(left, right) -> int:
    """
    Returns 0 if the same so should compare the next thing,
    Returns >0 if in the correct order
    Returns <0 if in the wrong order
    """
    # print(f"Compare {left} vs {right}")
    if isinstance(left, int):
        if isinstance(right, int):
            return right - left
        else: # left int, right list - convert left to singleton
            left = [left]
    elif isinstance(right, int): # left list, right int - convert right to singleton
        right = [right]

    # both lists
    for left_item, right_item in zip(left, right):
        ans = compare_sides(left_item, right_item)
        if ans != 0:
            return ans

    # see which list returned first if any
    return len(right) - len(left)

is_left = True
left = None
right = None
pair_num = 1
total = 0
with open("input.txt") as f:
    for line in f:
        line = line.strip()
        if line != "":
            if is_left:
                is_left = False
                exec(f"left = {line}")
            else:
                exec(f"right = {line}")
                is_left = True
                ans = compare_sides(left, right)
                if ans == 0:
                    print("ZERO!")
                elif ans > 0:
                    total += pair_num
                pair_num += 1
print(total)