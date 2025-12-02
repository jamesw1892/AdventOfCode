total = 0
with open("input.txt") as f:
    for range_ in f.read().strip().split(","):
        start, end = map(int, range_.split("-"))
        # print(f"{start}-{end}")
        for id in range(start, end + 1):
            str_id: str = str(id)
            width: int = len(str_id)
            if width % 2 == 0 and str_id[:width//2] == str_id[width//2:]:
                total += id
                # print(f"Adding {str_id}")
            # else:
            #     print(f"Rejected {str_id}")
print(total)
