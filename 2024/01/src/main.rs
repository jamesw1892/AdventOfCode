use hashbag::HashBag;
use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut left_list: Vec<usize> = Vec::new();
    let mut right_list: Vec<usize> = Vec::new();
    for line in file_lines {
        let nums: Vec<usize> = line
            .split("   ")
            .map(|s: &str| {
                s.parse()
                    .expect(format!("Failed to parse '{}' to usize", s).as_str())
            })
            .collect();
        left_list.push(nums[0]);
        right_list.push(nums[1]);
    }
    left_list.sort();
    right_list.sort();
    let total_distance: usize = left_list
        .into_iter()
        .zip(right_list)
        .map(|(n1, n2)| n1.abs_diff(n2))
        .sum();
    println!("{}", total_distance);
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut left_list: Vec<usize> = Vec::new();
    let mut right_list: HashBag<usize> = HashBag::new();
    for line in file_lines {
        let nums: Vec<usize> = line
            .split("   ")
            .map(|s: &str| {
                s.parse()
                    .expect(format!("Failed to parse '{}' to usize", s).as_str())
            })
            .collect();
        left_list.push(nums[0]);
        right_list.insert(nums[1]);
    }
    let similarity_score: usize = left_list
        .into_iter()
        .map(|n1: usize| n1 * right_list.contains(&n1))
        .sum();
    println!("{}", similarity_score);
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
