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

head_pos = Pos(0, 0)
tail_pos = head_pos.copy()
positions_tail_visited = set()
positions_tail_visited.add(tail_pos.copy())

with open("input.txt") as f:
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
            
            # only have to move tail if not adjacent
            if abs(head_pos.x - tail_pos.x) > 1 or \
                abs(head_pos.y - tail_pos.y) > 1:

                # if in line then just move orthogonally
                if head_pos.x == tail_pos.x:
                    if head_pos.y < tail_pos.y:
                        tail_pos.y -= 1
                    else:
                        tail_pos.y += 1
                elif head_pos.y == tail_pos.y:
                    if head_pos.x < tail_pos.x:
                        tail_pos.x -= 1
                    else:
                        tail_pos.x += 1
                
                # otherwise move diagaonally
                elif head_pos.x < tail_pos.x:
                    tail_pos.x -= 1
                    if head_pos.y < tail_pos.y:
                        tail_pos.y -= 1
                    else:
                        tail_pos.y += 1
                else:
                    tail_pos.x += 1
                    if head_pos.y < tail_pos.y:
                        tail_pos.y -= 1
                    else:
                        tail_pos.y += 1

            positions_tail_visited.add(tail_pos.copy())

print(len(positions_tail_visited))
