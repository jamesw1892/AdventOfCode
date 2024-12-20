use std::env::args;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Lines};
use std::iter::Flatten;

fn read_file_lines(filename: &str) -> io::Result<Flatten<Lines<BufReader<File>>>> {
    Ok(BufReader::new(File::open(filename)?).lines().flatten())
}

#[derive(Debug, PartialEq, Clone)]
enum Block {
    Space,
    File(usize),
}

fn move_all(blocks: &mut Vec<Block>) {
    let mut first_space: usize = 0;
    let mut last_file: usize = blocks.len() - 1;
    while first_space < last_file {

        // Adjust first_space and last_file
        while first_space < last_file && blocks[first_space] != Block::Space {
            first_space += 1;
        }
        while first_space < last_file && blocks[last_file] == Block::Space {
            last_file -= 1;
        }
        if first_space < last_file {
            blocks.swap(first_space, last_file);
        }
    }
}

struct Region {
    type_: Block,
    size: usize,
}

impl core::fmt::Debug for Region {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        if let Block::File(file_id) = self.type_ {
            write!(f, "{}", file_id.to_string().repeat(self.size))
        } else {
            write!(f, "{}", ".".repeat(self.size))
        }
        // write!(f, )
        // f.debug_tuple("").field(&self.type_).field(&self.size).finish()
    }
}

fn pprint(regions: &Vec<Region>) {
    for region in regions {
        for _ in 0..region.size {
            if let Block::File(file_id) = region.type_ {
                print!("{}", file_id);
            } else {
                print!(".");
            }
        }
    }
    println!("");
}

fn move_without_fragmenting(regions: &mut Vec<Region>) {
    // println!("Starting move without fragementaing, regions: ");
    // pprint(regions);

    // Try once per region, starting from the last, but only decrement the index
    // if we actually move it - since otherwise the same index points to the
    // next region.
    let mut file_index: usize = regions.len() - 1;
    for _ in 0..regions.len() {
        // println!("On region index {file_index} which is {:?}", regions[file_index]);

        // Get the next one that's a file
        while regions[file_index].type_ == Block::Space {
            file_index -= 1;
            // println!("Adjusting, now on region index {file_index} which is {:?}", regions[file_index]);
        }

        // Find a space to move it into if there is one
        let mut moved: bool = false;
        for space_index in 0..regions.len() {

            // Stop when we're not moving leftwards anymore
            if space_index >= file_index {
                break;
            }

            if regions[space_index].type_ == Block::Space && regions[space_index].size >= regions[file_index].size {
                if regions[space_index].size > regions[file_index].size {
                    // Break up space region first

                    // Insert new space region with leftover space after the current space region
                    let space_left: Region = Region { type_: Block::Space, size: regions[space_index].size - regions[file_index].size };
                    regions.insert(space_index + 1, space_left);

                    // Now we've just shuffled elements down, we need to increment file_index
                    file_index += 1;

                    // Shrink the size of the current space region
                    regions[space_index].size = regions[file_index].size;
                }
                // Move
                regions.swap(file_index, space_index);
                moved = true;

                // Now we've swapped, we need to combine adjacent space regions around file_index (where the spaces are now)
                if regions[file_index - 1].type_ == Block::Space {
                    regions[file_index - 1].size += regions[file_index].size;
                    regions.remove(file_index);
                    file_index -= 1;
                }
                if file_index + 1 < regions.len() && regions[file_index + 1].type_ == Block::Space {
                    regions[file_index].size += regions[file_index + 1].size;
                    regions.remove(file_index + 1);
                }

                // println!("Swapped indices {file_index} and {space_index}, regions: ");
                // pprint(regions);
                break;
            }
        }

        // If we didn't move it, decrement file index so we don't try to move it again
        if !moved {
            // println!("Couldn't move");
            if file_index == 0 {
                break;
            }
            file_index -= 1;
        }
    }
    // println!("Final regions:");
    // pprint(regions);
}

fn checksum(blocks: &Vec<Block>) -> usize {
    let mut checksum: usize = 0;
    for index in 0..blocks.len() {
        if let Block::File(file_id) = blocks[index] {
            checksum += index * file_id;
        }
    }
    checksum
}

fn regions_to_blocks(regions: &Vec<Region>) -> Vec<Block> {
    let mut blocks: Vec<Block> = Vec::new();
    for region in regions {
        for _ in 0..region.size {
            blocks.push(region.type_.clone());
        }
    }
    blocks
}

fn run_part1(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut blocks: Vec<Block> = Vec::new();
    let mut dense_format: Vec<usize> = Vec::new();
    for line in file_lines {
        // Only one line
        let mut is_file: bool = true;
        let mut file_id: usize = 0;
        for chr in line.chars() {
            let size: usize = chr as usize - '0' as usize;
            dense_format.push(size);
            if is_file {
                for _ in 0..size {
                    blocks.push(Block::File(file_id));
                }
                file_id += 1;
            } else {
                for _ in 0..size {
                    blocks.push(Block::Space);
                }
            }
            is_file = !is_file;
        }
    }
    // println!("{blocks:?}");
    move_all(&mut blocks);
    let the_checksum: usize = checksum(&blocks);
    println!("{the_checksum}");
}

fn run_part2(filename: &str) {
    let file_lines: Flatten<Lines<BufReader<File>>> =
        read_file_lines(filename).expect(format!("Failed to open file '{}'", filename).as_str());
    let mut regions: Vec<Region> = Vec::new();
    for line in file_lines {
        // Only one line
        let mut is_file: bool = true;
        let mut file_id: usize = 0;
        for chr in line.chars() {
            let size: usize = chr as usize - '0' as usize;
            if is_file {
                if size > 0 {
                    regions.push(Region {size, type_: Block::File(file_id)});
                }
                file_id += 1;
            } else {
                if size > 0 {
                    regions.push(Region {size, type_: Block::Space});
                }
            }
            is_file = !is_file;
        }
    }
    move_without_fragmenting(&mut regions);
    let blocks: Vec<Block> = regions_to_blocks(&regions);
    // println!("{blocks:?}");
    let the_checksum: usize = checksum(&blocks);
    println!("{the_checksum}");
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
