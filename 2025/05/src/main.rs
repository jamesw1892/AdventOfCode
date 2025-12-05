use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn run_part1(filename: &str) {
    // Initially I created a set and added every value in each range to it, then
    // checked whether each ingredient ID was in it. But this takes too long, I
    // think because the set gets very big. The input has few lines of fresh ID
    // ranges but each range is quite big, so it's quicker to just store the
    // start and end points of each range and iterate through them each time,
    // checking if the ingredient ID is between them

    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());

    let mut fresh_id_ranges: Vec<(usize, usize)> = Vec::new();
    let mut num_fresh_ingredients: usize = 0;
    let mut seen_empty_line: bool = false;

    for line in file_lines {

        // The second section of input is after the empty line and each line is
        // an int so we parse into usize and check if it's in any of the fresh
        // ID ranges
        if seen_empty_line {
            let id: usize = line.parse::<usize>().expect(format!("Failed to parse id to int: {}", line).as_str());
            for (start, end) in fresh_id_ranges.iter() {
                if *start <= id && id <= *end {
                    num_fresh_ingredients += 1;
                    break;
                }
            }

        // When we see the empty line we toggle to the other mode
        } else if line == "" {
            seen_empty_line = true;

        // The first section of input is before the empty line and each line is
        // <int>-<int> so we split on '-', parse into usize and then push to vec
        } else {
            let (start_str, end_str): (&str, &str) = line.split_once('-').expect(format!("Incorrectly formatted line: {}", line).as_str());
            let start: usize = start_str.parse::<usize>().expect(format!("Failed to parse start to int: {}", start_str).as_str());
            let end: usize = end_str.parse::<usize>().expect(format!("Failed to parse end to int: {}", end_str).as_str());
            fresh_id_ranges.push((start, end));
        }
    }

    println!("{}", num_fresh_ingredients);
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());

    let mut fresh_id_ranges: Vec<(usize, usize)> = Vec::new();

    for line in file_lines {

        // If we see an empty line, ignore everything afterwards
        if line == "" {
            break;

        // The first section of input is before the empty line and each line is
        // <int>-<int> so we split on '-', parse into usize and then push to vec
        } else {
            let (start_str, end_str): (&str, &str) = line.split_once('-').expect(format!("Incorrectly formatted line: {}", line).as_str());
            let start: usize = start_str.parse::<usize>().expect(format!("Failed to parse start to int: {}", start_str).as_str());
            let end: usize = end_str.parse::<usize>().expect(format!("Failed to parse end to int: {}", end_str).as_str());
            fresh_id_ranges.push((start, end));
        }
    }

    // Sort by start of range
    fresh_id_ranges.sort_unstable_by_key(|range| range.0);

    // Merge overlapping ranges. Since sorted, the current range always overlaps
    // or is entirely to the right of the last one. If it's entirely to the
    // right then because it's sorted, the next cannot overlap with the previous
    let mut merged: Vec<(usize, usize)> = Vec::new();
    for (start, end) in fresh_id_ranges {
        if let Some(last) = merged.last_mut() {
            if start <= last.1 + 1 {
                last.1 = last.1.max(end);
            } else {
                merged.push((start, end));
            }
        } else {
            merged.push((start, end));
        }
    }

    // Count up the number of elements in the non-overlapping ranges
    let mut num_fresh_ingredients: usize = 0;
    for (start, end) in merged.iter() {
        num_fresh_ingredients += end - start + 1;
    }

    println!("{}", num_fresh_ingredients);
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
