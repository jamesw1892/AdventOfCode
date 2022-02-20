def paper(s: str) -> int:
    l,w,h = s.split("x")
    l=int(l)
    w=int(w)
    h=int(h)
    return 2*(l*w + w*h + h*l) + min(l*w, w*h, h*l)

with open("input.txt") as f:
    print(sum(paper(line) for line in f))
