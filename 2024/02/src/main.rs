use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn is_report_safe(report: &String) -> bool {
    let levels: Vec<usize> = report
        .split(" ")
        .map(|l: &str| l.parse::<usize>().expect("Level not u32!"))
        .collect();
    let mut is_increasing_option: Option<bool> = None;
    for i in 1..levels.len() {
        let diff: usize = levels[i].abs_diff(levels[i - 1]);
        if levels[i] == levels[i - 1] || diff < 1 || diff > 3 {
            return false;
        }
        match is_increasing_option {
            None => is_increasing_option = Some(levels[i] > levels[i - 1]),
            Some(is_increasing) => {
                if (is_increasing && levels[i] < levels[i - 1])
                    || (!is_increasing && levels[i] > levels[i - 1])
                {
                    return false;
                }
            }
        }
    }
    true
}

fn can_work(report: &String) -> bool {
    if is_report_safe(report) {
        return true;
    }
    let levels: Vec<&str> = report.split(" ").collect();
    for i in 0..levels.len() {
        let mut levels_copy: Vec<&str> = levels.clone();
        levels_copy.remove(i);
        let report_reconstructed: String = levels_copy.join(" ");
        if is_report_safe(&report_reconstructed) {
            return true;
        }
    }
    false
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let num_safe: usize = file_lines.filter(is_report_safe).count();
    println!("{}", num_safe);
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let num_safe: usize = file_lines.filter(can_work).count();
    println!("{}", num_safe);
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
