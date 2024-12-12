use std::collections::HashMap;
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

#[allow(dead_code)]
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

#[allow(dead_code)]
fn depth_first(input: String, num_iterations: usize) -> usize {
    input.split(" ").map(|s: &str| s.parse().expect(format!("Failed to parse {s} into usize").as_str())).map(|stone: usize| depth_first_recursive(stone, num_iterations)).sum()
}

struct Transformer {
    // stone value -> num iterations -> end stones
    cache: HashMap<usize, HashMap<usize, Vec<usize>>>,
}

impl Transformer {
    fn new() -> Transformer {
        Transformer { cache: HashMap::new() }
    }

    fn recurse(&mut self, result: Vec<usize>, num_iterations: usize) -> Vec<usize> {
        result.into_iter().flat_map(|result_stone: usize| self.depth_first_recursive(result_stone, num_iterations - 1)).collect()
    }

    fn depth_first_recursive(&mut self, stone: usize, num_iterations: usize) -> Vec<usize> {
        if num_iterations <= 0 {
            return vec![stone];
        }

        // Try to shortcut as many iterations as possible using the cache
        if let Some(mapping) = self.cache.get(&stone) {
            for num_iterations_in_cache in (1..=num_iterations).rev() {
                if let Some(result) = mapping.get(&num_iterations_in_cache) {
                    return self.recurse(result.clone(), num_iterations - num_iterations_in_cache);
                }
            }
        }

        // Otherwise transform once and recurse
        let transformed_stone: Vec<usize> = transform_stone(&stone);
        let result: Vec<usize> = self.recurse(transformed_stone, num_iterations);
        self.add_to_cache(&stone, num_iterations, result.clone());
        result
    }

    fn add_to_cache(&mut self, stone: &usize, num_iterations: usize, result: Vec<usize>) {
        let mut mapping: HashMap<usize, Vec<usize>> = match self.cache.get(stone) {
            None => HashMap::new(),
            Some(mapping) => mapping.clone(),
        };
        mapping.insert(num_iterations, result);
        self.cache.insert(*stone, mapping);
    }
}

fn caching(input: String, num_iterations: usize) -> usize {
    let mut transformer: Transformer = Transformer::new();
    input.split(" ")
        .map(|s: &str| s.parse().expect(format!("Failed to parse {s} into usize").as_str()))
        .map(|stone: usize| transformer.depth_first_recursive(stone, num_iterations).len())
        .sum()
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());

    // Only one line
    for line in file_lines {
        let ans: usize = caching(line, 25);
        println!("{ans}");
    }
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());

    // Only one line
    for line in file_lines {
        let ans: usize = caching(line, 75);
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
