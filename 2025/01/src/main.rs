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
    let mut times_pointing_zero: usize = 0;
    let mut pointing: i16 = 50;
    for line in file_lines {
        let (dir, num) = line.split_at(1);
        let clicks: i16 = num.parse().expect(format!("Failed to parse '{}' into i16", num).as_str());
        if dir == "L" {
            pointing -= clicks;
        } else {
            pointing += clicks;
        }
        pointing = pointing.rem_euclid(100);
        if pointing == 0 {
            times_pointing_zero += 1;
        }
    }
    println!("{}", times_pointing_zero);
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut times_pointing_zero: usize = 0;
    let mut pointing: i16 = 50;
    for line in file_lines {
        let (dir, num) = line.split_at(1);
        let clicks: i16 = num.parse().expect(format!("Failed to parse '{}' into i16", num).as_str());
        if dir == "L" {
            if clicks >= pointing {
                // println!("L Activated");
                times_pointing_zero += ((clicks - pointing) as usize) / 100;
                if pointing > 0 {
                    times_pointing_zero += 1;
                }
            }
            pointing -= clicks;
        } else {
            if clicks >= 100 - pointing {
                // println!("R Activated");
                times_pointing_zero += ((clicks + pointing) as usize) / 100;
            }
            pointing += clicks;
        }
        pointing = pointing.rem_euclid(100);
        // println!("line {} pointing {} times pointing zero {}", line, pointing, times_pointing_zero);
    }
    println!("{}", times_pointing_zero);
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
