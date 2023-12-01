import { readFileSync } from 'fs';

function getNum(line: string): number {
    let groups = line.match(/^\D*(?<digit>\d)/)?.groups;
    if (groups === undefined) {
        throw new Error("No first digit. Line: '" + line + "'");
    }
    let digit1: string = groups.digit;

    groups = line.match(/(?<digit>\d)\D*$/)?.groups;
    if (groups === undefined) {
        throw new Error("No last digit. Line: '" + line + "'");
    }
    let digit2: string = groups.digit;

    let num: string = digit1 + digit2;
    return Number(num);
}

let total: number = 0;
for (let line of readFileSync("input.txt", "utf-8").split("\n")) {
    if (line.length > 0) {
        total += getNum(line);
    }
}
console.log(total);
