use lazy_static::lazy_static;
use regex::{Captures, Regex, RegexSet};
use std::collections::{HashMap, VecDeque};
use std::env::args;
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::str::FromStr;

lazy_static! {
    static ref PATTERNS: [&'static str; 6] = [
        r"^(?P<val>\w+) -> (?P<output>\w+)$",
        r"^NOT (?P<input>\w+) -> (?P<output>\w+)$",
        r"^(?P<input1>\w+) AND (?P<input2>\w+) -> (?P<output>\w+)$",
        r"^(?P<input1>\w+) OR (?P<input2>\w+) -> (?P<output>\w+)$",
        r"^(?P<input1>\w+) LSHIFT (?P<input2>\w+) -> (?P<output>\w+)$",
        r"^(?P<input1>\w+) RSHIFT (?P<input2>\w+) -> (?P<output>\w+)$",
    ];
    static ref REGEX_SET: RegexSet = RegexSet::new(PATTERNS.iter()).unwrap();
    static ref REGEXES: Vec<Regex> = PATTERNS
        .iter()
        .map(|pattern: &&str| Regex::new(pattern).unwrap())
        .collect();
}

#[derive(Debug)]
struct ParseErr;

enum Operation {
    OpSet(String, String),
    OpNot(String, String),
    OpBinary(String, String, String, Box<dyn Fn(u16, u16) -> u16>),
}

impl FromStr for Operation {
    type Err = ParseErr;

    fn from_str(line: &str) -> Result<Self, Self::Err> {
        let mut set_matches_iter = REGEX_SET.matches(line).into_iter();
        let match_index: usize = set_matches_iter.next().ok_or(ParseErr)?;
        if set_matches_iter.next().is_some() {
            return Err(ParseErr);
        }
        let captures: Captures = REGEXES.get(match_index).unwrap().captures(line).unwrap();
        let output: String = captures.name("output").unwrap().as_str().to_string();

        Ok(if match_index == 0 {
            Operation::OpSet(captures.name("val").unwrap().as_str().to_string(), output)
        } else if match_index == 1 {
            Operation::OpNot(captures.name("input").unwrap().as_str().to_string(), output)
        } else {
            let input1: String = captures.name("input1").unwrap().as_str().to_string();
            let input2: String = captures.name("input2").unwrap().as_str().to_string();
            let func = match match_index {
                2 => |i1, i2| i1 & i2,
                3 => |i1, i2| i1 | i2,
                4 => |i1, i2| i1 << i2,
                5 => |i1, i2| i1 >> i2,
                _ => panic!("Impossible"),
            };
            Operation::OpBinary(input1, input2, output, Box::new(func))
        })
    }
}

fn get_val(text: &str, vals: &mut HashMap<String, u16>) -> Option<u16> {
    text.parse::<u16>().ok().or_else(|| vals.get(text).copied())
}

fn process_line(
    vals: &mut HashMap<String, u16>,
    queue: &mut VecDeque<String>,
    line: String,
    b_override: Option<u16>,
) {
    match Operation::from_str(line.as_str())
        .expect(format!("Error parsing line '{}'", line).as_str())
    {
        Operation::OpSet(input, output) => {
            if output == "b" && b_override.is_some() {
                vals.insert("b".to_string(), b_override.unwrap());
            } else {
                match get_val(input.as_str(), vals) {
                    Some(val) => drop(vals.insert(output, val)),
                    None => queue.push_back(line),
                }
            }
        }
        Operation::OpNot(input, output) => match get_val(input.as_str(), vals) {
            Some(input) => drop(vals.insert(output, !input)),
            None => queue.push_back(line),
        },
        Operation::OpBinary(input1, input2, output, func) => match get_val(input1.as_str(), vals) {
            None => queue.push_back(line),
            Some(input1) => match get_val(input2.as_str(), vals) {
                None => queue.push_back(line),
                Some(input2) => drop(vals.insert(output, func(input1, input2))),
            },
        },
    }
}

fn run(filename: &str, b_override: Option<u16>) -> HashMap<String, u16> {
    let mut vals: HashMap<String, u16> = HashMap::new();
    let mut queue: VecDeque<String> = VecDeque::new();

    BufReader::new(
        File::open(filename).expect(format!("Failed to open file '{}'", filename).as_str()),
    )
    .lines()
    .flatten()
    .for_each(|line: String| process_line(&mut vals, &mut queue, line, b_override));

    while !queue.is_empty() {
        let line = queue.pop_front().unwrap();
        process_line(&mut vals, &mut queue, line, b_override);
    }

    vals
}

fn run_part1(filename: &str) {
    let vals: HashMap<String, u16> = run(filename, None);

    if filename == "test.txt" {
        assert_eq!(*vals.get("d").expect("No variable 'd' set"), 72);
        assert_eq!(*vals.get("e").expect("No variable 'e' set"), 507);
        assert_eq!(*vals.get("f").expect("No variable 'f' set"), 492);
        assert_eq!(*vals.get("g").expect("No variable 'g' set"), 114);
        assert_eq!(*vals.get("h").expect("No variable 'h' set"), 65412);
        assert_eq!(*vals.get("i").expect("No variable 'i' set"), 65079);
        assert_eq!(*vals.get("x").expect("No variable 'x' set"), 123);
        assert_eq!(*vals.get("y").expect("No variable 'y' set"), 456);
    } else {
        println!("{}", vals.get("a").expect("No variable 'a' set"));
    }
}

fn run_part2(filename: &str) {
    let vals: HashMap<String, u16> = run(filename, Some(956));

    if filename == "test.txt" {
        println!("text.txt only makes sense for part 1");
    } else {
        println!("{}", vals.get("a").expect("No variable 'a' set"));
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
