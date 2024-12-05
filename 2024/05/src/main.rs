use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;
use multimap::MultiMap;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut parsing_rules: bool = true;
    let mut cant_be_before: MultiMap<usize, usize> = MultiMap::new();
    let mut total: usize = 0;
    for line in file_lines {
        if line == "" {
            parsing_rules = false;
            // println!("multimap: {:?}", cant_be_before);
        } else if parsing_rules {
            let nums: Vec<usize> = line.split('|').map(|n| n.parse().expect(format!("Couldn't parse '{}' into usize", n).as_str())).collect();
            cant_be_before.insert(nums[0], nums[1]);
        } else {
            let order: Vec<usize> = line.split(',').map(|n| n.parse().expect(format!("Couldn't parse '{}' into usize", n).as_str())).collect();
            // println!("order: {:?}", order);
            let mut valid: bool = true;
            for i in 1..order.len() {
                let pages_before: &[usize] = &order[..i];
                if let Some(cant_be_before_this) = cant_be_before.get_vec(&order[i]) {
                    if pages_before.iter().any(|page_before: &usize| cant_be_before_this.contains(page_before)) {
                        valid = false;
                        // println!("Invalid, current: {:?}\npages_before: {:?}\ncant_be_before_this: {:?}", order[i], pages_before, cant_be_before_this);
                        break;
                    }
                }
            }
            if valid {
                let middle_val: usize = order[order.len() / 2];
                total += middle_val;
            }
        }
    }
    println!("{}", total);
}

fn run_part2(filename: &str) {
    let _file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    
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
