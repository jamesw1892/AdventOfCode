use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;
use std::collections::HashSet;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

struct Grid {
    grid: Vec<Vec<usize>>,
    trailheads: Vec<(usize, usize)>,
    height: usize,
    width: usize,
    trailends: HashSet<(usize, usize)>,
}

impl Grid {
    fn new(lines: Flatten<Lines<BufReader<File>>>) -> Grid {
        let mut grid: Vec<Vec<usize>> = Vec::new();
        let mut trailheads: Vec<(usize, usize)> = Vec::new();
        for (row_num, line) in lines.enumerate() {
            let mut row: Vec<usize> = Vec::new();
            for (col_num, cell) in line.chars().enumerate() {
                let height: usize = cell as usize - '0' as usize;
                if height == 0 {
                    trailheads.push((col_num, row_num));
                }
                row.push(height);
            }
            grid.push(row);
        }
        let height: usize = grid.len();
        let width: usize = grid[0].len();
        Grid { grid, trailheads, height, width, trailends: HashSet::new() }
    }
    fn score(&mut self, x: usize, y: usize, next_height: usize) {
        if next_height == 10 {
            self.trailends.insert((x, y));
        }
        // up
        if y != 0 && self.grid[y - 1][x] == next_height {
            self.score(x, y - 1, next_height + 1);
        }
        // right
        if x + 1 != self.width && self.grid[y][x + 1] == next_height {
            self.score(x + 1, y, next_height + 1);
        }
        // down
        if y + 1 != self.height && self.grid[y + 1][x] == next_height {
            self.score(x, y + 1, next_height + 1);
        }
        // left
        if x != 0 && self.grid[y][x - 1] == next_height {
            self.score(x - 1, y, next_height + 1);
        }
    }
    fn total_score(&mut self) -> usize {
        self.trailheads.clone().iter().map(|(x, y)| {
            self.score(*x, *y, 1);
            let num_trailends: usize = self.trailends.len();
            self.trailends.clear();
            num_trailends
        }).sum()
    }
    fn rating(&self, x: usize, y: usize, next_height: usize) -> usize {
        if next_height == 10 {
            return 1;
        }
        let mut total: usize = 0;
        // up
        if y != 0 && self.grid[y - 1][x] == next_height {
            total += self.rating(x, y - 1, next_height + 1);
        }
        // right
        if x + 1 != self.width && self.grid[y][x + 1] == next_height {
            total += self.rating(x + 1, y, next_height + 1);
        }
        // down
        if y + 1 != self.height && self.grid[y + 1][x] == next_height {
            total += self.rating(x, y + 1, next_height + 1);
        }
        // left
        if x != 0 && self.grid[y][x - 1] == next_height {
            total += self.rating(x - 1, y, next_height + 1);
        }
        total
    }
    fn total_rating(&self) -> usize {
        self.trailheads.iter().map(|(x, y)| self.rating(*x, *y, 1)).sum()
    }
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut grid: Grid = Grid::new(file_lines);
    let total_score: usize = grid.total_score();
    println!("{total_score}");
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let grid: Grid = Grid::new(file_lines);
    let total_rating: usize = grid.total_rating();
    println!("{total_rating}");
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
