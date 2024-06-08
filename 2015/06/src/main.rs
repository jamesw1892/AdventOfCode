use std::env;
use std::fs;
use std::io;
use std::io::BufRead;
use std::str;
use std::str::FromStr;

struct Point {
    x: usize,
    y: usize,
}

impl str::FromStr for Point {
    type Err = ParseLineErr;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let (x_str, y_str): (&str, &str) = s.split_once(',').ok_or(ParseLineErr)?;
        let x: usize = x_str.parse::<usize>().map_err(|_| ParseLineErr)?;
        let y: usize = y_str.parse::<usize>().map_err(|_| ParseLineErr)?;
        Ok(Point { x, y })
    }
}

enum Instruction {
    TurnOn,
    TurnOff,
    Toggle,
}

struct Line {
    instruction: Instruction,
    start: Point,
    end: Point,
}

#[derive(Debug)]
struct ParseLineErr;

impl str::FromStr for Line {
    type Err = ParseLineErr;

    fn from_str(line: &str) -> Result<Self, Self::Err> {
        let (instruction, rest_of_line) = if line.starts_with("turn on ") {
            (Instruction::TurnOn, line.strip_prefix("turn on ").unwrap())
        } else if line.starts_with("turn off ") {
            (
                Instruction::TurnOff,
                line.strip_prefix("turn off ").unwrap(),
            )
        } else if line.starts_with("toggle ") {
            (Instruction::Toggle, line.strip_prefix("toggle ").unwrap())
        } else {
            return Err(ParseLineErr);
        };
        let (start_str, end_str): (&str, &str) =
            rest_of_line.split_once(" through ").ok_or(ParseLineErr)?;
        let start: Point = Point::from_str(start_str)?;
        let end: Point = Point::from_str(end_str)?;
        Ok(Line {
            instruction,
            start,
            end,
        })
    }
}

fn process_line_part1(grid: &mut [[bool; 1000]; 1000], line: String) {
    let line: Line = Line::from_str(line.as_str()).unwrap();
    for (x, y) in itertools::iproduct!(line.start.x..=line.end.x, line.start.y..=line.end.y) {
        match line.instruction {
            Instruction::TurnOn => grid[x][y] = true,
            Instruction::TurnOff => grid[x][y] = false,
            Instruction::Toggle => grid[x][y] = !grid[x][y],
        }
    }
}

fn process_line_part2(grid: &mut [[u32; 1000]; 1000], line: String) {
    let line: Line = Line::from_str(line.as_str()).unwrap();
    for (x, y) in itertools::iproduct!(line.start.x..=line.end.x, line.start.y..=line.end.y) {
        match line.instruction {
            Instruction::TurnOn => grid[x][y] += 1,
            Instruction::TurnOff => grid[x][y] = if grid[x][y] == 0 { 0 } else { grid[x][y] - 1 },
            Instruction::Toggle => grid[x][y] += 2,
        }
    }
}

fn run_part1(filename: &str) {
    let file = fs::File::open(filename).unwrap();
    let mut grid: [[bool; 1000]; 1000] = [[false; 1000]; 1000];
    io::BufReader::new(file)
        .lines()
        .flatten()
        .for_each(|s: String| process_line_part1(&mut grid, s));
    println!(
        "{}",
        grid.into_iter()
            .map(|inner_arr: [bool; 1000]| inner_arr
                .into_iter()
                .map(|el: bool| if el { 1 } else { 0 })
                .sum::<u32>())
            .sum::<u32>()
    );
}

fn run_part2(filename: &str) {
    let file = fs::File::open(filename).unwrap();
    let mut grid: [[u32; 1000]; 1000] = [[0; 1000]; 1000];
    io::BufReader::new(file)
        .lines()
        .flatten()
        .for_each(|s: String| process_line_part2(&mut grid, s));
    println!(
        "{}",
        grid.into_iter()
            .map(|inner_arr: [u32; 1000]| inner_arr.into_iter().sum::<u32>())
            .sum::<u32>()
    );
}

fn main() {
    let args: Vec<String> = env::args().collect();
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
