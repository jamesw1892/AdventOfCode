use std::env;
use std::fs;
use std::io;
use std::io::BufRead;
use std::str;
use unicode_segmentation::UnicodeSegmentation;

fn is_nice_part1(s: &String) -> bool {
    if s.contains("ab") || s.contains("cd") || s.contains("pq") || s.contains("xy") {
        return false;
    }
    if s.chars().filter(|c: &char| "aeiou".contains(*c)).count() < 3 {
        return false;
    }
    let mut it: std::iter::Peekable<str::Chars> = s.chars().peekable();
    loop {
        match it.next() {
            Some(c1) => {
                match it.peek() {
                    Some(c2) => {
                        if c1 == *c2 {
                            return true;
                        }
                    },
                    None => break,
                }
            },
            None => break,
        }
    }
    false
}

fn has_repeating_pair(chars: &Vec<&str>) -> bool {
    for i in 1..chars.len()-2 {
        for j in i+2..chars.len() {
            if chars[i-1] == chars[j-1] && chars[i] == chars[j] {
                return true;
            }
        }
    }
    false
}

fn has_repeating_char_one_apart(chars: &Vec<&str>) -> bool {
    for i in 0..chars.len()-2 {
        if chars[i] == chars[i+2] {
            return true;
        }
    }
    false
}

fn is_nice_part2(s: &String) -> bool {
    let chars: Vec<&str> = UnicodeSegmentation::graphemes(s.as_str(), true).collect::<Vec<&str>>();
    has_repeating_pair(&chars) && has_repeating_char_one_apart(&chars)
}

fn run(filename: &str) {
    let file = fs::File::open(filename).unwrap();
    let ans: usize = io::BufReader::new(file).lines().flatten().filter(is_nice_part2).count();
    println!("{}", ans);
}

fn main() {
    let args: Vec<String> = env::args().collect();
    if args.len() > 1 {
        run(&args[1]);
    } else {
        run("input.txt");
    }
}
