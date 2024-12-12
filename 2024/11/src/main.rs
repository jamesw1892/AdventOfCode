use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn transform_stone(stone: &usize) -> Vec<usize> {
    if *stone == 0 {
        return vec![1];
    }

    let stone_str: String = stone.to_string();
    let stone_len: usize = stone_str.len();
    if stone_len % 2 == 0 {
        return vec![stone_str[..stone_len / 2].parse().unwrap(), stone_str[stone_len / 2..].parse().unwrap()];
    }

    return vec![*stone * 2024];
}

fn simple(input: String, num_iterations: usize) -> usize {
    let mut stones: Vec<usize> = input.split(" ").map(|s: &str| s.parse().expect(format!("Failed to parse {s} into usize").as_str())).collect();
    for _ in 0..num_iterations {
        stones = stones.iter().flat_map(|stone: &usize| transform_stone(stone)).collect();
    }
    stones.len()
}

fn depth_first_recursive(stone: usize, num_iterations: usize) -> usize {
    let result: Vec<usize> = transform_stone(&stone);
    if num_iterations == 1 {
        return result.len();
    }
    result.into_iter().map(|result_stone: usize| depth_first_recursive(result_stone, num_iterations - 1)).sum()
}

fn depth_first(input: String, num_iterations: usize) -> usize {
    input.split(" ").map(|s: &str| s.parse().expect(format!("Failed to parse {s} into usize").as_str())).map(|stone: usize| depth_first_recursive(stone, num_iterations)).sum()
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());

    // Only one line
    for line in file_lines {
        let ans: usize = depth_first(line, 25);
        println!("{ans}");
    }
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());

    // Only one line
    for line in file_lines {
        let ans: usize = depth_first(line, 75);
        println!("{ans}");
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
