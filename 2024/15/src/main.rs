use std::convert::From;
use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

#[derive(Debug, Clone, PartialEq)]
enum Cell {
    SPACE,
    BOX,
    ROBOT,
    WALL,
}

impl From<char> for Cell {
    fn from(value: char) -> Self {
        match value {
            '.' => Cell::SPACE,
            'O' => Cell::BOX,
            '@' => Cell::ROBOT,
            '#' => Cell::WALL,
            _ => panic!("Unknown cell {value}"),
        }
    }
}

#[derive(Debug)]
struct Grid {
    grid: Vec<Vec<Cell>>,
    robot_x: usize,
    robot_y: usize,
    height: usize,
    width: usize,
}

impl Grid {
    fn new() -> Self {
        Grid { grid: Vec::new(), robot_x: 0, robot_y: 0, height: 0, width: 0 }
    }
    fn get(&self, x: usize, y: usize) -> &Cell {
        &self.grid[y][x]
    }
    fn set(&mut self, x: usize, y: usize, new_cell: Cell) {
        if new_cell == Cell::ROBOT {
            self.robot_x = x;
            self.robot_y = y;
        }
        let row: &mut Vec<Cell> = self.grid.get_mut(y).unwrap();
        row[x] = new_cell;
    }
    fn add_line(&mut self, line: String) {
        let mut x: usize = 0;
        self.grid.push(line.chars().map(|chr: char| {
            if chr == '@' {
                self.robot_y = self.height;
                self.robot_x = x;
            }
            x += 1;
            chr
        }).map(Cell::from).collect());
        self.height += 1;
        self.width = x;
    }
    fn try_move(&mut self, xs: Vec<usize>, ys: Vec<usize>) {

        // See if can move
        let mut done: bool = false;
        for x in xs.clone() {
            for y in ys.clone() {
                match self.get(x, y) {
                    Cell::SPACE => {
                        // Can move as seen space before wall
                        done = true;
                        break;
                    },
                    Cell::BOX => {}, // Do nothing
                    Cell::ROBOT => panic!("Shouldn't see robot at ({x}, {y}) when seeing if can move!"),
                    Cell::WALL => return, // Can't move as seen wall before space
                }
            }
            if done {
                break;
            }
        }

        // If get to this point, can move so move
        let mut current_cell: Cell = Cell::ROBOT;
        self.set(self.robot_x, self.robot_y, Cell::SPACE);
        for x in xs {
            for y in ys.clone() {
                match self.get(x, y) {
                    Cell::SPACE => {
                        self.set(x, y, current_cell.clone());
                        return;
                    },
                    Cell::BOX => {
                        self.set(x, y, current_cell.clone());
                        current_cell = Cell::BOX;
                    },
                    Cell::ROBOT => panic!("Shouldn't see robot at ({x}, {y}) when moving!"),
                    Cell::WALL => panic!("Shouldn't see wall at ({x}, {y}) when moving!"),
                }
            }
        }
    }
    fn try_moves(&mut self, line: String) {
        for dir in line.chars() {
            match dir {
                '^' => self.try_move(vec![self.robot_x], (0..self.robot_y).rev().collect()),
                '>' => self.try_move((self.robot_x + 1..self.width).collect(), vec![self.robot_y]),
                'v' => self.try_move(vec![self.robot_x], (self.robot_y + 1..self.height).collect()),
                '<' => self.try_move((0..self.robot_x).rev().collect(), vec![self.robot_y]),
                _ => panic!("Invalid direction {dir}"),
            }
        }
    }
    fn total_gps(&self) -> usize {
        let mut total: usize = 0;
        for x in 0..self.width {
            for y in 0..self.height {
                if *self.get(x, y) == Cell::BOX {
                    total += 100 * y + x;
                }
            }
        }
        total
    }
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut grid: Grid = Grid::new();
    let mut seen_empty_line: bool = false;
    for line in file_lines {
        if line == "" {
            seen_empty_line = true;
        } else if seen_empty_line {
            grid.try_moves(line);
        } else {
            grid.add_line(line);
        }
    }
    let total_gps: usize = grid.total_gps();
    println!("{total_gps}");
}

fn run_part2(filename: &str) {
    let _file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
}

fn main() {
    let args: Vec<String> = args().collect();
    let part: &str = if args.len() > 1 { &args[1] } else { "part1" };
    let filename: &str = if args.len() > 2 {
        &args[2]
    } else {
        "input.txt"
    };
    match part {
        "part1" => run_part1(filename),
        "part2" => run_part2(filename),
        _ => panic!("Unknown part"),
    }
}
