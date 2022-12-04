t=0
with open("input.txt") as f:
    for line in f:
        p1,p2=line.split(",")
        p11,p12=p1.split("-")
        p21,p22=p2.split("-")
        p11, p12, p21, p22 = int(p11),int(p12),int(p21),int(p22)
        
        if (p21 <= p12 and p22 >= p11):
            t+=1
print(t)