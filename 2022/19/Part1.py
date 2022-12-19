import re
import logging

# logging.basicConfig(level=logging.INFO, format=None)
logging.basicConfig(level=logging.WARNING, format=None)

class Blueprint:
    def __init__(self, ore_robot_cost_ore: int, clay_robot_cost_ore: int,
                 obsidian_robot_cost_ore: int, obsidian_robot_cost_clay: int,
                 geode_robot_cost_ore: int, geode_robot_cost_obsidian: int):

        # robot costs
        self.ore_robot_cost_ore = ore_robot_cost_ore
        self.clay_robot_cost_ore = clay_robot_cost_ore
        self.obsidian_robot_cost_ore = obsidian_robot_cost_ore
        self.obsidian_robot_cost_clay = obsidian_robot_cost_clay
        self.geode_robot_cost_ore = geode_robot_cost_ore
        self.geode_robot_cost_obsidian = geode_robot_cost_obsidian

    def max_geodes(self) -> int:
        """
        Return the maximum number of geodes that can be collected in 24 minutes
        with this blueprint starting with 1 ore robot
        """

        # robots
        ore_robots = 1
        clay_robots = 0
        obsidian_robots = 0
        geode_robots = 0

        # resources
        ore = 0
        clay = 0
        obsidian = 0
        geodes = 0

        return self.brute_force(1, ore_robots, clay_robots, obsidian_robots, geode_robots, ore, clay, obsidian, geodes, "")

    def brute_force(self, minute: int, ore_robots: int, clay_robots: int, obsidian_robots: int, geode_robots: int,
                    ore: int, clay: int, obsidian: int, geodes: int, decision: str) -> int:
        """
        Starting from the given minute, brute force all possibilities until we get to 24 minutes and return the best
        """

        most_geodes = -1

        while minute <= 19:
            logging.info(f"\n== Minute {minute} ==")

            # if decision not made for us by other instance of the recursion,
            # spawn new instances to build new robots of each type we can
            # but keep this instance to not buy anything
            if decision == "":
                if ore >= self.ore_robot_cost_ore:
                    most_geodes = max(most_geodes, self.brute_force(minute, ore_robots, clay_robots, obsidian_robots, geode_robots,
                                                                    ore - self.ore_robot_cost_ore, clay, obsidian, geodes, "ore"))
                if ore >= self.clay_robot_cost_ore:
                    most_geodes = max(most_geodes, self.brute_force(minute, ore_robots, clay_robots, obsidian_robots, geode_robots,
                                                                    ore - self.clay_robot_cost_ore, clay, obsidian, geodes, "clay"))
                if ore >= self.obsidian_robot_cost_ore and clay >= self.obsidian_robot_cost_clay:
                    most_geodes = max(most_geodes, self.brute_force(minute, ore_robots, clay_robots, obsidian_robots, geode_robots,
                                                                    ore - self.obsidian_robot_cost_ore, clay - self.obsidian_robot_cost_clay, obsidian, geodes, "obsidian"))
                if ore >= self.geode_robot_cost_ore and obsidian >= self.geode_robot_cost_obsidian:
                    most_geodes = max(most_geodes, self.brute_force(minute, ore_robots, clay_robots, obsidian_robots, geode_robots,
                                                                    ore - self.geode_robot_cost_ore, clay, obsidian - self.geode_robot_cost_obsidian, geodes, "geode"))

            # robots collect
            ore += ore_robots
            clay += clay_robots
            obsidian += obsidian_robots
            geodes += geode_robots
            logging.info(f"{ore_robots} ore-collecting robots collect {ore_robots} ore; you now have {ore} ore.")
            if clay_robots > 0:
                logging.info(f"{clay_robots} clay-collecting robots collect {clay_robots} clay; you now have {clay} clay.")
            if obsidian_robots > 0:
                logging.info(f"{obsidian_robots} obsidian-collecting robots collect {obsidian_robots} obsidian; you now have {obsidian} obsidian.")
            if geode_robots > 0:
                logging.info(f"{geode_robots} geode-collecting robots collect {geode_robots} geode; you now have {geodes} geodes.")

            # bought robots complete
            if decision == "ore":
                ore_robots += 1
                logging.info(f"The new ore-collecting robot you spent {self.ore_robot_cost_ore} ore on is ready; you now have {ore_robots} of them")
            elif decision == "clay":
                clay_robots += 1
                logging.info(f"The new clay-collecting robot you spent {self.clay_robot_cost_ore} ore on is ready; you now have {clay_robots} of them")
            elif decision == "obsidian":
                obsidian_robots += 1
                logging.info(f"The new obsidian-collecting robot you spent {self.obsidian_robot_cost_ore} ore and {self.obsidian_robot_cost_clay} clay on is ready; you now have {obsidian_robots} of them")
            elif decision == "geode":
                geode_robots += 1
                logging.info(f"The new geode-collecting robot you spent {self.geode_robot_cost_ore} ore and {self.geode_robot_cost_obsidian} obsidian on is ready; you now have {geode_robots} of them")

            decision = ""
            minute += 1

        return max(geodes, most_geodes)

blueprints = dict()
with open("input_test.txt") as f:
    for line in f:
        line = line.strip()
        if line != "":
            blueprint_num, *others = map(int, re.findall(r"\b\d+\b", line))
            blueprints[blueprint_num] = Blueprint(*others)

print(sum(blueprint_num * blueprints[blueprint_num].max_geodes() for blueprint_num in blueprints))
