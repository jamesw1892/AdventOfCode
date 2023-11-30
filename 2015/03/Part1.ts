import { readFileSync } from 'fs';

let x = 0;
let y = 0;
let num_times_visited: { [key: number]: { [key: number]: number } } = {0: {0: 1}};

for (let char of readFileSync("input.txt", "utf-8")) {
    switch (char) {
        case "^": y++; break;
        case "v": y--; break;
        case ">": x++; break;
        case "<": x--; break;
        default: throw new Error("Invalid direction");
    }
    if (num_times_visited[x] === undefined) {
        num_times_visited[x] = {};
    }
    if (num_times_visited[x][y] === undefined) {
        num_times_visited[x][y] = 1;
    } else {
        num_times_visited[x][y]++;
    }
}

let num_repeated: number = 0;
for (let i in num_times_visited) {
    for (let j in num_times_visited[i]) {
        if (num_times_visited[i][j] >= 1) {
            num_repeated++;
        }
    }
}
console.log(num_repeated);