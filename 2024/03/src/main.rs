use std::env::args;
use std::fs;
use regex::{Regex, Captures};

fn run_part1(filename: &str) {
    let file_contents: String = fs::read_to_string(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let re: Regex = Regex::new(r"mul\(([1-9][0-9]{0,2}),([1-9][0-9]{0,2})\)").expect("Invalid regex");
    let total: usize = re.captures_iter(&file_contents).map(|caps| {
        let (_, [x, y]) = caps.extract();
        let x: usize = x.parse::<usize>().expect(format!("{} is not a usize", x).as_str());
        let y: usize = y.parse::<usize>().expect(format!("{} is not a usize", y).as_str());
        x * y
    }).sum();
    println!("{}", total);
}

fn run_part2(filename: &str) {
    let file_contents: String = fs::read_to_string(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let re: Regex = Regex::new(r"mul\(([1-9][0-9]{0,2}),([1-9][0-9]{0,2})\)|(d)(o)\(\)|(d)o(n)'t\(\)").expect("Invalid regex");
    let mut enabled: bool = true;
    let total: usize = re.captures_iter(&file_contents).map(|caps: Captures<'_>| {
        match caps.get(0).unwrap().as_str() {
            "do()" => {
                enabled = true;
                0
            }
            "don't()" => {
                enabled = false;
                0
            }
            _ => {
                if enabled {
                    let (_, [x, y]) = caps.extract();
                    let x: usize = x.parse::<usize>().expect(format!("{} is not a usize", x).as_str());
                    let y: usize = y.parse::<usize>().expect(format!("{} is not a usize", y).as_str());
                    x * y
                } else {
                    0
                }
            }
        }
    }).sum();
    println!("{}", total);
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
