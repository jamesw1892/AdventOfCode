use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn increment(password: String) -> String {
    password.chars().rev().scan(true, |carry: &mut bool, chr: char| {
        Some(
            if *carry {
                if chr == 'z' { 'a' } else {
                    *carry = false;
                    ((chr as u8) + 1) as char
                }
            } else { chr }
        )
    }).collect::<Vec<_>>().into_iter().rev().collect()
}

fn is_valid(password: &String) -> bool {
    let mut prev_chr_option: Option<char> = None;
    let mut straight_index: u8 = 0;
    let mut seen_straight: bool = false;
    let mut num_pairs: u8 = 0;
    let mut prev_pair: Option<char> = None;
    for chr in password.chars() {

        // Forbidden letters
        if ['i', 'o', 'l'].contains(&chr) {
            return false;
        }

        if let Some(prev_chr) = prev_chr_option {

            // Straight
            let last_chr_incremented: char = ((prev_chr as u8) + 1) as char;
            if chr == last_chr_incremented && prev_chr != 'z' {
                straight_index += 1;
                if straight_index == 2 {
                    seen_straight = true;
                }
            } else {
                straight_index = 0;
            }

            // Pairs
            if chr == prev_chr && prev_pair.map_or(true, |prev_pair_chr: char| prev_pair_chr != chr) {
                num_pairs += 1;
                prev_pair = Some(chr);
            }
        }
        prev_chr_option = Some(chr);
    }
    seen_straight && num_pairs >= 2
}

fn next_valid(mut password: String) -> String {
    password = increment(password);
    while !is_valid(&password) {
        password = increment(password);
    };
    password
}

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    for line in file_lines {
        match filename {
            "input.txt" => println!("{}", next_valid(line)), // There's only one line
            "test-valid.txt" => println!("{}: {}", line, is_valid(&line)),
            "test-next.txt" => println!("{}: {}", line, next_valid(line.clone())),
            "test-increment.txt" => println!("{}: {}", line, increment(line.clone())),
            _ => panic!("Invalid file")
        }
    }
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    for line in file_lines {
        match filename {
            "input.txt" => println!("{}", next_valid(next_valid(line))), // There's only one line
            "test-valid.txt" => println!("{}: {}", line, is_valid(&line)),
            "test-next.txt" => println!("{}: {}", line, next_valid(line.clone())),
            "test-increment.txt" => println!("{}: {}", line, increment(line.clone())),
            _ => panic!("Invalid file")
        }
    }
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
