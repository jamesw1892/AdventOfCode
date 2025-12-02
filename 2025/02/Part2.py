total = 0
with open("input.txt") as f:
    for range_ in f.read().strip().split(","):
        start, end = map(int, range_.split("-"))
        # print(f"{start}-{end}")
        for id in range(start, end + 1):
            str_id: str = str(id)
            width: int = len(str_id)
            for possible_divisor in range(2, width + 1):
                if width % possible_divisor != 0:
                    continue
                chunk_size: int = width // possible_divisor
                sections: list[str] = [str_id[i:i + chunk_size] for i in range(0, width, chunk_size)]
                if all(sections[i] == sections[0] for i in range(1, len(sections))):
                    total += id
                    # print(id)
                    break # if works for multiple divisors, only include once
print(total)
