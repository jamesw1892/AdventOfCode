# for each name, stores the Node or Leaf instance
nodes = dict()

class Leaf:
    def __init__(self, name: str, val: int):
        self.name = name

        # do some algebra and make this be 'x'
        if name == "humn":
            self.val = "x"
        else:
            self.val = val
    def calc(self):
        return self.val

class Node:
    def __init__(self, name: str, op: str, left: str, right: str):
        self.name = name
        self.op = op
        self.left = left
        self.right = right
        self.val = None

    def calc(self):

        # don't recalculate if already calculated
        if self.val is None:

            left_ans = nodes[self.left].calc()
            right_ans = nodes[self.right].calc()

            if self.name == "root":
                return left_ans, right_ans

            # keep simplifying as ints as long as possible
            if isinstance(left_ans, int) and isinstance(right_ans, int):
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

            # if includes a string (caused by humn being x) then
            # wrap in brackets and construct string
            else:
                if self.op == "+":
                    self.val = f"({str(left_ans)}) + ({str(right_ans)})"
                elif self.op == "-":
                    self.val = f"({str(left_ans)}) - ({str(right_ans)})"
                elif self.op == "*":
                    self.val = f"({str(left_ans)}) * ({str(right_ans)})"
                elif self.op == "/":
                    self.val = f"({str(left_ans)}) / ({str(right_ans)})"
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

lhs, rhs = nodes["root"].calc()

# lhs is either (<num>) <op> (<more stuff>) or (<more stuff>) <op> (<num>)
# so keep peeling back the number and operation and perform
# the inverse operation on the rhs until the lhs is x
while lhs != "x":
    # print(f"{lhs} = {rhs}")

    index_first_close_bracket = lhs.find(")")
    index_last_open_bracket = -lhs[::-1].find("(") - 1

    # if all digits between first open and close brackets (ie no more nested
    # open brackets) then we can peel the left side away
    if lhs[1:index_first_close_bracket].isdigit():
        operand = int(lhs[1:index_first_close_bracket])
        op = lhs[index_first_close_bracket + 2]
        lhs = lhs[index_first_close_bracket + 5:-1]

        # perform inverse operation on rhs
        if op == "+":
            rhs -= operand
        elif op == "-":
            rhs = operand - rhs
        elif op == "*":
            if rhs % operand != 0:
                print(f"Fraction created: {rhs}/{operand} at end")
                exit(1)
            rhs //= operand
        elif op == "/":
            if operand % rhs != 0:
                print(f"Fraction created: {operand}/{rhs} at end")
                exit(1)
            rhs = operand // rhs
        else:
            print(f"Invalid operation: {op} at end")
            exit(1)

    # if all digits between last open and close brackets (ie no more nested
    # brackets) then we can peel the right side away
    elif lhs[index_last_open_bracket + 1:-1].isdigit():
        operand = int(lhs[index_last_open_bracket + 1: -1])
        op = lhs[index_last_open_bracket - 2]
        lhs = lhs[1:index_last_open_bracket - 4]

        # perform inverse operation on rhs
        if op == "+":
            rhs -= operand
        elif op == "-":
            rhs += operand
        elif op == "*":
            if rhs % operand != 0:
                print(f"Fraction created: {rhs}/{operand} at end")
                exit(1)
            rhs //= operand
        elif op == "/":
            rhs *= operand
        else:
            print(f"Invalid operation: {op} at end")
            exit(1)
    else:
        print(f"Neither side can be peeled: {lhs}")
        exit(1)

# now that lhs = "x", x = rhs
print(rhs)
