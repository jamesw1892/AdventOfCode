use std::collections::HashSet;
use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

use itertools::Itertools;
use multimap::MultiMap;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn is_valid(antinode: (i8, i8), width: usize, height: usize) -> bool {
    antinode.0 >= 0 && antinode.0 < width as i8 && antinode.1 >= 0 && antinode.1 < height as i8
}

fn add_antinodes(antinodes: &mut HashSet<(usize, usize)>, antenna: Vec<(usize, usize)>, width: usize, height: usize, is_part1: bool) {
    for pair in antenna.iter().combinations(2) {
        let antenna1: (usize, usize) = *pair[0];
        let antenna2: (usize, usize) = *pair[1];
        // println!("Processing {antenna1:?} and {antenna2:?}");
        let x_off: i8 = antenna1.0 as i8 - antenna2.0 as i8;
        let y_off: i8 = antenna1.1 as i8 - antenna2.1 as i8;
        if is_part1 {
            let antinode1: (i8, i8) = (antenna1.0 as i8 + x_off, antenna1.1 as i8 + y_off);
            let antinode2: (i8, i8) = (antenna2.0 as i8 - x_off, antenna2.1 as i8 - y_off);
            // println!("Possible antinodes at {antinode1:?} and {antinode2:?}");
            if is_valid(antinode1, width, height) {
                antinodes.insert((antinode1.0 as usize, antinode1.1 as usize));
            }
            if is_valid(antinode2, width, height) {
                antinodes.insert((antinode2.0 as usize, antinode2.1 as usize));
            }
        } else {
            let mut scalar: i8 = 0;
            loop {
                let antinode: (i8, i8) = (antenna1.0 as i8 + x_off * scalar, antenna1.1 as i8 + y_off * scalar);
                if is_valid(antinode, width, height) {
                    antinodes.insert((antinode.0 as usize, antinode.1 as usize));
                    // println!("Adding antinode {antinode:?} from {scalar} times distance between {antenna1:?} and {antenna2:?}");
                    scalar += 1;
                } else {
                    break;
                }
            }
            scalar = 1;
            loop {
                let antinode: (i8, i8) = (antenna1.0 as i8 - x_off * scalar, antenna1.1 as i8 - y_off * scalar);
                if is_valid(antinode, width, height) {
                    antinodes.insert((antinode.0 as usize, antinode.1 as usize));
                    // println!("Adding antinode {antinode:?} from -{scalar} times distance between {antenna1:?} and {antenna2:?}");
                    scalar += 1;
                } else {
                    break;
                }
            }
        }
    }
}

fn solution(filename: &str, is_part1: bool) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut antenna: MultiMap<char, (usize, usize)> = MultiMap::new();
    let mut width: usize = 0;
    let mut height: usize = 0;
    for (y, row) in file_lines.enumerate() {
        for (x, value) in row.chars().enumerate() {
            if value != '.' {
                antenna.insert(value, (x, y));
            }
            width = width.max(x);
        }
        height = height.max(y);
    }
    height += 1;
    width += 1;
    // println!("Antenna: {antenna:?}");
    let mut antinodes: HashSet<(usize, usize)> = HashSet::new();
    for (_, antenna) in antenna {
        add_antinodes(&mut antinodes, antenna, width, height, is_part1);
    }
    // println!("Final antinodes: {antinodes:?}");
    let num_unique_antinodes: usize = antinodes.len();
    println!("{}", num_unique_antinodes);
}

fn run_part1(filename: &str) {
    solution(filename, true);
}

fn run_part2(filename: &str) {
    solution(filename, false);
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
