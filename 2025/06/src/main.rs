use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn run_part1(filename: &str) {
    let file_lines: Vec<String> = read_file_lines(filename)
        .expect(format!("Failed to open file '{}'", filename).as_str())
        .collect();

    // Transform the file into a 2D array of numbers (separated by whitespace)
    let grid: Vec<Vec<usize>> = file_lines[..file_lines.len() - 1]
        .iter()
        .map(|line| {
            line.split_ascii_whitespace()
                .map(|word| word.parse().unwrap())
                .collect()
        })
        .collect();

    // Keep the last row of ops separate
    let ops_row: Vec<&str> = file_lines
        .last()
        .unwrap()
        .split_ascii_whitespace()
        .collect();

    // For each column (containing values and an operation), add to the total
    // the result of applying the operation to all the values
    let mut total: usize = 0;
    for col_index in 0..ops_row.len() {
        let op: &str = ops_row[col_index];
        let values = grid.iter().map(|row| row[col_index]);
        //println!("Solving problem values {values:?} op {op}");
        let ans: usize = if op == "+" {
            values.sum::<usize>()
        } else {
            values.product::<usize>()
        };
        //println!("Ans = {ans}");
        total += ans;
    }

    println!("{total}");
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());

    // Transform the file into a 2D array of characters
    let mut grid: Vec<Vec<char>> = file_lines.map(|line| line.chars().collect()).collect();

    // Split off the last row into its own separate Vec to keep ops separate
    let ops_row: Vec<char> = grid.pop().unwrap();

    // Store the current problem answer and op. Iterate through each column and
    // update the current problem answer by the number it holds. Each time we
    // change problem, update the current op and add the answer to the grand
    // total
    let mut current_problem_ans: usize = 0;
    let mut current_op: char = ' ';
    let mut total: usize = 0;
    for col_index in 0..grid[0].len() {
        // The ops are always in the first column of a problem so if that column
        // of the ops row isn't whitespace then change problem
        let op: char = ops_row[col_index];
        if !op.is_ascii_whitespace() {
            current_op = op;
            total += current_problem_ans;

            // Reset the current problem answer to the identity of the op
            current_problem_ans = if current_op == '+' { 0 } else { 1 };
        }

        // Concatenate all the digits in a column together and trim whitespace
        // on both sides to leave only digits. If it's empty then ignore and go
        // back round the loop. If not then convert to an int and add or
        // multiply the current problem's total by it
        let value_str: String = grid
            .iter()
            .map(|row| row[col_index])
            .collect::<String>()
            .trim()
            .to_string();
        if !value_str.is_empty() {
            let value: usize = value_str.parse().unwrap();
            if current_op == '+' {
                current_problem_ans += value;
            } else {
                current_problem_ans *= value;
            }
        }
    }

    // Remember to add the last problem's answer
    total += current_problem_ans;

    println!("{total}");
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
