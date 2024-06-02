use std::env;
use std::fs;
use std::str;

fn min_num(initial: String) -> u64 {
    let mut i: u64 = 0;
    loop {
        i += 1;
        let digest = md5::compute(format!("{initial}{i}"));
        if format!("{digest:x}").starts_with("00000") {
            break i;
        }
    }
}

fn run(filename: &str) {
    let initial: String = fs::read_to_string(filename).unwrap();
    let ans: u64 = min_num(initial.trim().to_string());
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
