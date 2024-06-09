use regex::{Captures, Regex, RegexSet};
use std::collections::{HashMap, VecDeque};
use std::env::args;
use std::fs::File;
use std::io::{BufRead, BufReader};

fn get_val(text: &str, vals: &mut HashMap<String, u16>) -> Option<u16> {
    text.parse::<u16>().ok().or_else(|| vals.get(text).copied())
}

fn process_line_part1(
    regex_set: &RegexSet,
    regexes: &Vec<Regex>,
    vals: &mut HashMap<String, u16>,
    queue: &mut VecDeque<String>,
    line: String,
) {
    let mut set_matches_iter = regex_set.matches(line.as_str()).into_iter();
    let match_index: usize = set_matches_iter.next().unwrap();
    assert!(set_matches_iter.next().is_none(), "More matches!");
    let captures: Captures = regexes
        .get(match_index)
        .unwrap()
        .captures(line.as_str())
        .unwrap();
    let output: String = captures.name("output").unwrap().as_str().to_owned();

    // Set value
    if match_index == 0 {
        match get_val(captures.name("val").unwrap().as_str(), vals) {
            Some(val) => drop(vals.insert(output, val)),
            None => queue.push_back(line),
        }

    // NOT
    } else if match_index == 1 {
        match get_val(captures.name("input").unwrap().as_str(), vals) {
            Some(input) => drop(vals.insert(output, !input)),
            None => queue.push_back(line),
        }
    } else {
        match get_val(captures.name("input1").unwrap().as_str(), vals) {
            None => queue.push_back(line),
            Some(input1) => match get_val(captures.name("input2").unwrap().as_str(), vals) {
                None => queue.push_back(line),
                Some(input2) => match match_index {
                    // AND
                    2 => drop(vals.insert(output, input1 & input2)),
                    // OR
                    3 => drop(vals.insert(output, input1 | input2)),
                    // LSHIFT
                    4 => drop(vals.insert(output, input1 << input2)),
                    // RSHIFT
                    5 => drop(vals.insert(output, input1 >> input2)),
                    _ => panic!("Impossible: Matched regex out of range"),
                },
            },
        }
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
    let regexes: Vec<Regex> = patterns
        .iter()
        .map(|pattern: &&str| Regex::new(pattern).unwrap())
        .collect();

    let mut vals: HashMap<String, u16> = HashMap::new();
    let mut queue: VecDeque<String> = VecDeque::new();

    BufReader::new(File::open(filename).unwrap())
        .lines()
        .flatten()
        .for_each(|line: String| {
            process_line_part1(&regex_set, &regexes, &mut vals, &mut queue, line)
        });

    while !queue.is_empty() {
        let line = queue.pop_front().unwrap();
        process_line_part1(&regex_set, &regexes, &mut vals, &mut queue, line);
    }

    if filename == "test.txt" {
        assert_eq!(*vals.get("d").unwrap(), 72);
        assert_eq!(*vals.get("e").unwrap(), 507);
        assert_eq!(*vals.get("f").unwrap(), 492);
        assert_eq!(*vals.get("g").unwrap(), 114);
        assert_eq!(*vals.get("h").unwrap(), 65412);
        assert_eq!(*vals.get("i").unwrap(), 65079);
        assert_eq!(*vals.get("x").unwrap(), 123);
        assert_eq!(*vals.get("y").unwrap(), 456);
    } else {
        println!("{}", vals.get("a").unwrap());
    }
}

fn run_part2(filename: &str) {
    let _ = filename;
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
