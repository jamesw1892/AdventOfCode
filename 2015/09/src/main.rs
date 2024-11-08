use itertools::Itertools;
use lazy_static::lazy_static;
use regex::{Captures, Regex};
use std::collections::HashMap;
use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;
use std::num::ParseIntError;
use std::str::FromStr;

lazy_static! {
    static ref REGEX: Regex =
        Regex::new(r"^(?P<from>\w+) to (?P<to>\w+) = (?P<distance>\d+)$").unwrap();
}

struct Line {
    from: String,
    to: String,
    distance: u16,
}

impl FromStr for Line {
    type Err = String;

    fn from_str(line: &str) -> Result<Self, Self::Err> {
        let captures: Captures = REGEX.captures(line).ok_or(format!(
            "Failed to parse line '{}' - does not match regex",
            line
        ))?;
        let from: String = captures.name("from").unwrap().as_str().to_string();
        let to: String = captures.name("to").unwrap().as_str().to_string();
        let distance: &str = captures.name("distance").unwrap().as_str();
        let distance: u16 = distance.parse().map_err(|err: ParseIntError| {
            format!(
                "Failed to parse distance from string '{}' into u16: {}",
                distance, err
            )
        })?;
        return Ok(Line { from, to, distance });
    }
}

struct Graph {
    adjacency_list: HashMap<String, HashMap<String, u16>>,
}

impl Graph {
    fn new() -> Graph {
        Graph {
            adjacency_list: HashMap::new(),
        }
    }

    fn add(&mut self, line: Line) {
        match self.adjacency_list.get_mut(&line.from) {
            Some(inner) => drop(inner.insert(line.to.clone(), line.distance)),
            None => {
                let mut inner: HashMap<String, u16> = HashMap::new();
                inner.insert(line.to.clone(), line.distance);
                self.adjacency_list.insert(line.from.clone(), inner);
            }
        }
        match self.adjacency_list.get_mut(&line.to) {
            Some(inner) => drop(inner.insert(line.from, line.distance)),
            None => {
                let mut inner: HashMap<String, u16> = HashMap::new();
                inner.insert(line.from, line.distance);
                self.adjacency_list.insert(line.to, inner);
            }
        }
    }

    fn path_distance(&self, path: Vec<&String>) -> u16 {
        let mut prev: Option<&String> = None;
        path.iter()
            .map(|to: &&String| {
                let dis: &u16 = match prev {
                    None => &0,
                    Some(from) => self.adjacency_list.get(from).unwrap().get(*to).unwrap(),
                };
                prev = Some(*to);
                dis
            })
            .sum()
    }

    fn min_path_distance(&self) -> Option<u16> {
        self.adjacency_list
            .keys()
            .permutations(self.adjacency_list.len())
            .map(|path: Vec<&String>| self.path_distance(path))
            .min()
    }

    fn max_path_distance(&self) -> Option<u16> {
        self.adjacency_list
            .keys()
            .permutations(self.adjacency_list.len())
            .map(|path: Vec<&String>| self.path_distance(path))
            .max()
    }
}

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

fn read_graph(file_lines: Flatten<Lines<BufReader<File>>>) -> Result<Graph, String> {
    let mut graph: Graph = Graph::new();
    for line in file_lines {
        graph.add(line.parse::<Line>()?);
    }
    Ok(graph)
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let graph: Graph = read_graph(file_lines).unwrap();
    let ans: u16 = graph.min_path_distance().expect("Graph empty");

    if filename == "test.txt" {
        assert_eq!(ans, 605);
        println!("Correct");
    } else {
        println!("{}", ans);
    }
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let graph: Graph = read_graph(file_lines).unwrap();
    let ans: u16 = graph.max_path_distance().expect("Graph empty");

    if filename == "test.txt" {
        assert_eq!(ans, 982);
        println!("Correct");
    } else {
        println!("{}", ans);
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
