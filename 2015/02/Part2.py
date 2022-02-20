def ribbon(s: str) -> int:
    l,w,h = s.split("x")
    l=int(l)
    w=int(w)
    h=int(h)
    return 2*min(l+w, w+h, h+l) + l*w*h

with open("input.txt") as f:
    print(sum(ribbon(line) for line in f))
