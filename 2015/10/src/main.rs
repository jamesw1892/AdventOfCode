use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn look_and_see(s: String) -> String {
    let mut last_char: Option<char> = None;
    let mut count: usize = 1;
    let mut out: String = "".to_owned();
    for c in s.chars() {
        match last_char {
            None => {
                last_char = Some(c);
            }
            Some(last_c) => {
                if last_c == c {
                    count += 1;
                } else {
                    out.push_str(&count.to_string());
                    out.push(last_c);
                    count = 1;
                    last_char = Some(c);
                }
            }
        }
    }
    if let Some(last_c) = last_char {
        out.push_str(&count.to_string());
        out.push(last_c);
    }
    out
}

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    for mut line in file_lines {
        if filename == "input.txt" {
            // There's only one line
            for _ in 0..40 {
                line = look_and_see(line);
            }
            println!("{}", line.len());
        } else {
            println!("{} -> {}", line, look_and_see(line.clone()));
        }
    }
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    for mut line in file_lines {
        if filename == "input.txt" {
            // There's only one line
            for _ in 0..50 {
                line = look_and_see(line);
            }
            println!("{}", line.len());
        } else {
            println!("{} -> {}", line, look_and_see(line.clone()));
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
