#include "strOps.h"

/**
 * Return whether the string contains the character
 */
bool strContains(std::string s, char c) {
    for (char sc: s) {
        if (sc == c) {
            return true;
        }
    }
    return false;
}

/**
 * Return a string with all characters in the first string but not in the second
 */
std::string strDiff(std::string s1, std::string s2) {
    std::string out = "";
    for (char c: s1) {
        if (!strContains(s2, c)) {
            out += c;
        }
    }
    return out;
}

/**
 * Return a string with all characters in both strings
 */
std::string strIntersection(std::string s1, std::string s2) {
    std::string out = "";
    for (char c: s1) {
        if (strContains(s2, c)) {
            out += c;
        }
    }
    return out;
}