class Dir:
    def __init__(self, filename: str, prev=None):
        self.filename = filename
        self.d = dict()
        self.prev = prev
    def __setitem__(self, name, thing):
        self.d[name] = thing
    def __getitem__(self, name):
        return self.d[name]
    def __str__(self):
        return str(self.d)
    def __repr__(self):
        return repr(self.d)
    def calc_size(self, answers):
        size = 0
        for name in self.d:
            if isinstance(self.d[name], Dir):
                s = self.d[name].calc_size(answers)
                answers[self.dirname() + "/" + name] = s
                size += s
            else:
                size += self.d[name]
        return size
    def dirname(self) -> str:
        if self.prev is None:
            return self.filename
        return self.prev.dirname() + "/" + self.filename

root = Dir("")
current_directory = root
with open("input.txt") as f:
    for line in f:
        line = line.strip()
        if line.startswith("$"):
            line = line[1:].strip()
            if line.startswith("cd"):
                line = line[2:].strip()
                if line == "/":
                    current_directory = root
                elif line == "..":
                    current_directory = current_directory.prev
                else:
                    current_directory = current_directory[line]
            elif line.startswith("ls"):
                pass
        elif line != "":
            # output from ls
            if line.startswith("dir"):
                line = line[3:].strip()
                current_directory[line] = Dir(line, current_directory)
            else:
                size, name = line.split()
                current_directory[name] = int(size)

answers = dict()
s = root.calc_size(answers)
answers[""] = s

print(answers[min(answers, key=lambda answer: answers[answer] if answers[answer] >= s - 40000000 else 70000000)])
