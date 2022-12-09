class Pos:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    def copy(self):
        return Pos(self.x, self.y)
    def __eq__(self, other):
        return self.x == other.x and self.y == other.y
    def __hash__(self):
        return self.x + 10000000 * self.y

def adjust_position(leader_pos, follower_pos):
    # only have to move tail if not adjacent
    if abs(leader_pos.x - follower_pos.x) > 1 or \
        abs(leader_pos.y - follower_pos.y) > 1:

        # if in line then just move orthogonally
        if leader_pos.x == follower_pos.x:
            if leader_pos.y < follower_pos.y:
                follower_pos.y -= 1
            else:
                follower_pos.y += 1
        elif leader_pos.y == follower_pos.y:
            if leader_pos.x < follower_pos.x:
                follower_pos.x -= 1
            else:
                follower_pos.x += 1

        # otherwise move diagaonally
        elif leader_pos.x < follower_pos.x:
            follower_pos.x -= 1
            if leader_pos.y < follower_pos.y:
                follower_pos.y -= 1
            else:
                follower_pos.y += 1
        else:
            follower_pos.x += 1
            if leader_pos.y < follower_pos.y:
                follower_pos.y -= 1
            else:
                follower_pos.y += 1

    return follower_pos

head_pos = Pos(0, 0)
knots = [head_pos.copy() for _ in range(9)]
positions_tail_visited = set()
positions_tail_visited.add(head_pos.copy())

with open("input_test.txt") as f:
    for line in f:
        direction, number = line.split()
        for _ in range(int(number)):
            if direction == "U":
                head_pos.y += 1
            elif direction == "D":
                head_pos.y -= 1
            elif direction == "R":
                head_pos.x += 1
            else: # L
                head_pos.x -= 1
            
            knots[0] = adjust_position(head_pos, knots[0])
            for i in range(1, 9):
                knots[i] = adjust_position(knots[i - 1], knots[i])

            positions_tail_visited.add(knots[-1].copy())

print(len(positions_tail_visited))
