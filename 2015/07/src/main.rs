use std::env;
use std::fs;
use std::io;
use std::io::BufRead;
use std::str;
use std::collections::{HashMap, VecDeque};
use regex::{Captures, Regex, RegexSet};

fn get_val(text: &str, vals: &mut HashMap<String, u16>, queue: &mut VecDeque<String>, line: &String) -> Option<u16> {
    text.parse::<u16>().ok().or_else(|| vals.get(text).copied()).or_else(|| {
        queue.push_back(line.clone());
        None
    })
}

fn process_line_part1(regex_set: &RegexSet, regexes: &Vec<Regex>, vals: &mut HashMap<String, u16>, queue: &mut VecDeque<String>, line: String) {
    let mut set_matches_iter = regex_set.matches(line.as_str()).into_iter();
    let match_index: usize = set_matches_iter.next().unwrap();
    assert!(set_matches_iter.next().is_none(), "More matches!");
    let captures: Captures = regexes.get(match_index).unwrap().captures(line.as_str()).unwrap();
    let output: String = captures.name("output").unwrap().as_str().to_owned();

    // Set value
    if match_index == 0 {
        get_val(captures.name("val").unwrap().as_str(), vals, queue, &line)
            .and_then(|val: u16| vals.insert(output, val));

    // NOT
    } else if match_index == 1 {
        get_val(captures.name("input").unwrap().as_str(), vals, queue, &line)
            .and_then(|input: u16| vals.insert(output, !input));

    } else {
        get_val(captures.name("input1").unwrap().as_str(), vals, queue, &line)
            .and_then(|input1: u16| get_val(captures.name("input2").unwrap().as_str(), vals, queue, &line).map(|input2| (input1, input2)))
            .and_then(|(input1, input2)| {
                match match_index {
                    // AND
                    2 => vals.insert(output, input1 & input2),
                    // OR
                    3 => vals.insert(output, input1 | input2),
                    // LSHIFT
                    4 => vals.insert(output, input1 << input2),
                    // RSHIFT
                    5 => vals.insert(output, input1 >> input2),
                    _ => panic!("Impossible: Matched regex out of range"),
                };
                Some("hi")
            });
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

    let mut vals: HashMap<String, u16> = HashMap::new();
    let mut queue: VecDeque<String> = VecDeque::new();

    io::BufReader::new(fs::File::open(filename).unwrap())
        .lines()
        .flatten()
        .for_each(|line: String| process_line_part1(&regex_set, &regexes, &mut vals, &mut queue, line));

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
