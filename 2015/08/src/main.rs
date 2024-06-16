use std::env::args;
use std::fs::File;
use std::io::{BufRead, BufReader, Lines};
use std::iter::Flatten;

fn read_file_lines(filename: &str) -> Flatten<Lines<BufReader<File>>> {
    BufReader::new(
        File::open(filename).expect(format!("Failed to open file '{}'", filename).as_str()),
    )
    .lines()
    .flatten()
}

fn process_line_part1(line: String) -> Result<usize, String> {
    let line_len: usize = line.len();
    let line_bytes: &[u8] = line.as_bytes();
    let mut num_memory_chars: usize = 0;
    let mut index: usize = 1;
    if line_bytes[0] as char != '"' || line_bytes[line_len - 1] as char != '"' {
        return Err("Line does not start and end with '\"'".to_string());
    }
    while index < line_len - 1 {
        if line_bytes[index] as char == '\\' {
            if index + 1 == line_len - 1 {
                return Err("Backslash at end of line - escape sequence cut off".to_string());
            }
            index += match line_bytes[index + 1] as char {
                'x' => {
                    if index + 3 >= line_len - 1 {
                        return Err("\\x escape sequence cut off at end of line".to_string());
                    }
                    if !(line_bytes[index + 2] as char).is_ascii_hexdigit()
                        || !(line_bytes[index + 3] as char).is_ascii_hexdigit()
                    {
                        return Err("\\x escape sequence contains non-hex character".to_string());
                    }
                    3
                }
                '\\' => 1,
                '"' => 1,
                _ => return Err("Invalid escape sequence".to_string()),
            }
        }
        num_memory_chars += 1;
        index += 1;
    }
    Ok(line_len - num_memory_chars)
}

fn process_line_part2(line: String) -> usize {
    2 + line
        .chars()
        .filter(|chr: &char| *chr == '"' || *chr == '\\')
        .count()
}

fn run_part1(filename: &str) {
    let ans: Result<usize, String> = read_file_lines(filename).map(process_line_part1).sum();

    if filename == "test.txt" {
        assert_eq!(ans, Ok(12));
        println!("Correct");
    } else if filename.starts_with("invalid") {
        println!("Correctly identified error: {}", ans.unwrap_err());
    } else {
        println!("{}", ans.unwrap());
    }
}

fn run_part2(filename: &str) {
    let ans: usize = read_file_lines(filename).map(process_line_part2).sum();

    match filename {
        "test.txt" => Some(19),
        "invalid1.txt" => Some(5),
        "invalid2.txt" => Some(5),
        "invalid3.txt" => Some(5),
        "invalid4.txt" => Some(5),
        "invalid5.txt" => Some(3),
        "invalid6.txt" => Some(3),
        _ => None,
    }
    .map_or_else(
        || println!("{}", ans),
        |correct_ans: usize| {
            assert_eq!(
                ans, correct_ans,
                "Incorrect, expected {} but got {}",
                correct_ans, ans
            );
            println!("Correct");
        },
    );
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
