# for each name, stores the Node or Leaf instance
nodes = dict()

class Leaf:
    def __init__(self, name: str, val: int):
        self.name = name
        self.val = val
    def calc(self) -> int:
        return self.val

class Node:
    def __init__(self, name: str, op: str, left: str, right: str):
        self.name = name
        self.op = op
        self.left = left
        self.right = right
        self.val = None

    def calc(self) -> int:

        # don't recalculate if already calculated
        if self.val is None:

            left_ans = nodes[self.left].calc()
            right_ans = nodes[self.right].calc()

            if self.op == "+":
                self.val = left_ans + right_ans
            elif self.op == "-":
                self.val = left_ans - right_ans
            elif self.op == "*":
                self.val = left_ans * right_ans
            elif self.op == "/":
                if left_ans % right_ans != 0:
                    print(f"Fraction created: {left_ans}/{right_ans} at node {self.name}")
                    exit(1)
                self.val = left_ans // right_ans
            else:
                print(f"Invalid operation: {self.op} at node {self.name}")
                exit(1)

        return self.val

with open("input.txt") as f:
    for line in f:
        line = line.strip()
        if line != "":
            name, other = line.split(": ")
            if other.isdigit():
                nodes[name] = Leaf(name, int(other))
            else:
                left, op, right = other.split(" ")
                nodes[name] = Node(name, op, left, right)

print(nodes["root"].calc())
