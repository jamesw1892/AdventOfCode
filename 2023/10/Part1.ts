import { readFileSync } from 'fs';

enum Pipe {
    GROUND,
    START,
    VERTICAL,
    HORIZONTAL,
    NE,
    NW,
    SE,
    SW,
    NOT_MAIN_LOOP,
    OFF_GRID
};

enum Dir {
    UNDEFINED,
    UP,
    RIGHT,
    DOWN,
    LEFT,
}

var charToPipe: { [char: string] : Pipe } = {
    ".": Pipe.GROUND,
    "S": Pipe.START,
    "|": Pipe.VERTICAL,
    "-": Pipe.HORIZONTAL,
    "L": Pipe.NE,
    "J": Pipe.NW,
    "F": Pipe.SE,
    "7": Pipe.SW
};

function getUp(grid: Pipe[][], x: number, y: number): Pipe {
    if (y == 0) return Pipe.OFF_GRID;
    return grid[y-1][x];
}

function getDown(grid: Pipe[][], x: number, y: number): Pipe {
    if (y == grid.length - 1) return Pipe.OFF_GRID;
    return grid[y+1][x];
}

function getLeft(grid: Pipe[][], x: number, y: number): Pipe {
    if (x == 0) return Pipe.OFF_GRID;
    return grid[y][x-1];
}

function getRight(grid: Pipe[][], x: number, y: number): Pipe {
    if (x == grid.length - 1) return Pipe.OFF_GRID;
    return grid[y][x+1];
}

function main(filename: string): number {
    let grid: Pipe[][] = [];
    let line: string;
    let start_x: number = -1;
    let start_y: number = -1;
    for (line of readFileSync(filename, "utf-8").split("\n")) {
        line = line.trim();
        if (line.length == 0) continue;
        let row: Pipe[] = [];
        for (let i = 0; i < line.length; i++) {

            // Use dictionary to convert character to enum - if not there then
            // it SHOULD error
            let here: Pipe = charToPipe[line.charAt(i)];
            if (here == Pipe.START) {
                start_x = i;
                start_y = grid.length;
            }
            row.push(here);
        }
        grid.push(row);
    }

    // Working from the start, go round the loop until come back to start
    let x: number = start_x;
    let y: number = start_y;
    let cameFrom: Dir = Dir.UNDEFINED;
    let count = 0;

    do {
        if (cameFrom != Dir.UP && [Pipe.START, Pipe.VERTICAL, Pipe.NE, Pipe.NW].includes(grid[y][x]) && [Pipe.START, Pipe.VERTICAL, Pipe.SE, Pipe.SW].includes(getUp(grid, x, y))) {
            y--;
            cameFrom = Dir.DOWN;
        } else if (cameFrom != Dir.RIGHT && [Pipe.START, Pipe.HORIZONTAL, Pipe.NE, Pipe.SE].includes(grid[y][x]) && [Pipe.START, Pipe.HORIZONTAL, Pipe.NW, Pipe.SW].includes(getRight(grid, x, y))) {
            x++;
            cameFrom = Dir.LEFT;
        } else if (cameFrom != Dir.DOWN && [Pipe.START, Pipe.VERTICAL, Pipe.SE, Pipe.SW].includes(grid[y][x]) && [Pipe.START, Pipe.VERTICAL, Pipe.NE, Pipe.NW].includes(getDown(grid, x, y))) {
            y++;
            cameFrom = Dir.UP;
        } else if (cameFrom != Dir.LEFT && [Pipe.START, Pipe.HORIZONTAL, Pipe.NW, Pipe.SW].includes(grid[y][x]) && [Pipe.START, Pipe.HORIZONTAL, Pipe.NE, Pipe.SE].includes(getLeft(grid, x, y))) {
            x--;
            cameFrom = Dir.RIGHT;
        } else {
            throw new Error("Impossible!");
        }
        count++;
        // console.log(`(${x},${y})=${count}`)
    } while (x != start_x || y != start_y);

    return count / 2;
}

console.log(main("test1.txt"));
console.log(main("test2.txt"));
console.log(main("test3.txt"));
console.log(main("test4.txt"));
console.log(main("input.txt"));
