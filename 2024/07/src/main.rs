use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

use itertools::{repeat_n, Itertools};

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

#[derive(Debug)]
enum Operators {
    Add,
    Multiply,
    Concatenate,
}

fn ans_if_can_be_made(line: String, possible_operators: &Vec<Operators>) -> usize {
    let splat: Vec<&str> = line.split(": ").collect();
    let ans: usize = splat[0]
        .parse()
        .expect(format!("{} cannot be converted to usize", splat[0]).as_str());
    let operands: Vec<usize> = splat[1]
        .split(" ")
        .map(|operand: &str| {
            operand
                .parse()
                .expect(format!("{} cannot be converted to usize", operand).as_str())
        })
        .collect();
    let num_operators: usize = operands.len() - 1;

    for operators in repeat_n(possible_operators.iter(), num_operators).multi_cartesian_product() {
        let mut accumulator: usize = operands[0];
        for i in 0..num_operators {
            accumulator = match operators[i] {
                Operators::Add => accumulator + operands[i + 1],
                Operators::Multiply => accumulator * operands[i + 1],
                Operators::Concatenate => (accumulator.to_string() + &operands[i + 1].to_string())
                    .parse()
                    .expect(
                        format!(
                            "{}{} cannot be converted to usize",
                            accumulator,
                            operands[i + 1]
                        )
                        .as_str(),
                    ),
            }
        }
        if accumulator == ans {
            return ans;
        }
    }
    0
}

fn solution(filename: &str, possible_operators: Vec<Operators>) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let total: usize = file_lines
        .map(|line| ans_if_can_be_made(line, &possible_operators))
        .sum();
    println!("{}", total);
}

fn run_part1(filename: &str) {
    solution(filename, vec![Operators::Add, Operators::Multiply]);
}

fn run_part2(filename: &str) {
    solution(
        filename,
        vec![Operators::Add, Operators::Multiply, Operators::Concatenate],
    );
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
