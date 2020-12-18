from Calc import calculate
from Datatypes import BinaryOperator, op_add, op_mul

def main(tokens):
    total = 0
    with open("input.txt") as f:
        for line in f:
            total += int(calculate(line, tokens))
    print(total)

def test(tokens, answers):
    assert calculate("1 + 2 * 3 + 4 * 5 + 6",                           tokens) == answers.pop(0), "Failed test input 1"
    assert calculate("1 + (2 * 3) + (4 * (5 + 6))",                     tokens) == answers.pop(0), "Failed test input 2"
    assert calculate("2 * 3 + (4 * 5)",                                 tokens) == answers.pop(0), "Failed test input 3"
    assert calculate("5 + (8 * 3 + 9 + 3 * 4 * 3)",                     tokens) == answers.pop(0), "Failed test input 4"
    assert calculate("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))",       tokens) == answers.pop(0), "Failed test input 5"
    assert calculate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", tokens) == answers.pop(0), "Failed test input 6"
    print("All tests passed!")

if __name__ == "__main__":
    from sys import argv
    assert len(argv) > 1 and argv[1] == "1" or argv[1] == "2", "Must specify which part: 1 or 2"

    # same presedence in part 1
    if argv[1] == "1":
        tokens = {
            "+": BinaryOperator("Addition (+)", op_add, 3, True),
            "*": BinaryOperator("Multiplication (*)", op_mul, 3, True)
        }
        answers = ["71", "51", "26", "437", "12240", "13632"]

    # in part 2, addition has a higher precendence than multiplication
    else:
        tokens = {
            "+": BinaryOperator("Addition (+)", op_add, 2, True),
            "*": BinaryOperator("Multiplication (*)", op_mul, 3, True)
        }
        answers = ["231", "51", "46", "1445", "669060", "23340"]

    if len(argv) > 2 and argv[2] == "test":
        test(tokens, answers)
    else:
        main(tokens)
