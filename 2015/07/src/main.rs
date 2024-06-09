use std::env;
use std::fs;
use std::io;
use std::io::BufRead;
use std::str;
use std::collections::HashMap;
use regex::{Captures, Regex, RegexSet};

fn get_val(text: &str, vals: &mut HashMap<String, u16>) -> u16 {
    let keys = vals.keys().map(|s| s.clone()).collect::<Vec<_>>().join(", ");
    let err_msg = format!("Failed to parse value {} as number and isn't in hashmap (keys: {})", text, keys);
    text.parse::<u16>().unwrap_or_else(|_| *vals.get(text).expect(err_msg.as_str()))
}

fn process_line_part1(regex_set: &RegexSet, regexes: &Vec<Regex>, vals: &mut HashMap<String, u16>, line: String) {
    let mut set_matches_iter = regex_set.matches(line.as_str()).into_iter();
    let match_index: usize = set_matches_iter.next().unwrap();
    assert!(set_matches_iter.next().is_none(), "More matches!");
    let captures: Captures = regexes.get(match_index).unwrap().captures(line.as_str()).unwrap();
    let output: String = captures.name("output").unwrap().as_str().to_owned();

    // println!("Matching patterns for {}: {}", line, regex_set.matches(line.as_str()).iter().map(|s| s.to_string()).collect::<Vec<_>>().join(", "));

    // Set value
    if match_index == 0 {
        let val: u16 = get_val(captures.name("val").unwrap().as_str(), vals);
        vals.insert(output, val);

    // NOT
    } else if match_index == 1 {
        let input: u16 = get_val(captures.name("input").unwrap().as_str(), vals);
        vals.insert(output, !input);

    } else {
        let input1: u16 = get_val(captures.name("input1").unwrap().as_str(), vals);
        let input2: u16 = get_val(captures.name("input2").unwrap().as_str(), vals);

        match match_index {
            // AND
            2 => vals.insert(output, input1 & input2),
            // OR
            3 => vals.insert(output, input1 | input2),
            // LSHIFT
            4 => {
                // println!("output: {}, input1: {}, input2: {}", output, input1, input2);
                vals.insert(output, input1 << input2)},
            // RSHIFT
            5 => {
                // println!("output: {}, input1: {}, input2: {}", output, input1, input2);
                vals.insert(output, input1 >> input2)},
            _ => panic!("Impossible: Matched regex out of range"),
        };
    }
}

fn run_part1(filename: &str) {
    let patterns: [&str; 6] = [
        r"^(?P<val>\d+) -> (?P<output>\w+)$",
        r"^NOT (?P<input>\w+) -> (?P<output>\w+)$",
        r"^(?P<input1>\w+) AND (?P<input2>\w+) -> (?P<output>\w+)$",
        r"^(?P<input1>\w+) OR (?P<input2>\w+) -> (?P<output>\w+)$",
        r"^(?P<input1>\w+) LSHIFT (?P<input2>\w+) -> (?P<output>\w+)$",
        r"^(?P<input1>\w+) RSHIFT (?P<input2>\w+) -> (?P<output>\w+)$",
    ];
    let regex_set: RegexSet = RegexSet::new(patterns).unwrap();
    let regexes: Vec<Regex> = patterns.iter().map(|pattern: &&str| Regex::new(pattern).unwrap()).collect();

    let file = fs::File::open(filename).unwrap();
    let mut vals: HashMap<String, u16> = HashMap::new();
    io::BufReader::new(file)
        .lines()
        .flatten()
        .for_each(|line: String| process_line_part1(&regex_set, &regexes, &mut vals, line));
    if filename == "test.txt" {
        vals.iter().for_each(|(k, v)| println!("{}: {}", k, v));
    } else {
        println!("{}", vals.get("a").unwrap());
    }
}

fn run_part2(filename: &str) {
    let _ = filename;
    // let file = fs::File::open(filename).unwrap();
    // io::BufReader::new(file)
    //     .lines()
    //     .flatten()
    //     .for_each(|s: String| process_line_part2(&mut grid, s));
    // println!(
    //     "{}",
    //     grid.into_iter()
    //         .map(|inner_arr: [u32; 1000]| inner_arr.into_iter().sum::<u32>())
    //         .sum::<u32>()
    // );

    println!("{}", 4 << 2);
    println!("{}", 4 >> 2);
    println!("{}", 123 << 2);
    println!("{}", 456 >> 2);
}

fn main() {
    let args: Vec<String> = env::args().collect();
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
