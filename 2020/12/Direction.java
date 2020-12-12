public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    LEFT,
    RIGHT,
    FORWARD;

    public static Direction fromChar(char chr) {
        switch (chr) {
            case 'N': return Direction.NORTH;
            case 'S': return Direction.SOUTH;
            case 'E': return Direction.EAST;
            case 'W': return Direction.WEST;
            case 'L': return Direction.LEFT;
            case 'R': return Direction.RIGHT;
            case 'F': return Direction.FORWARD;
            default: Shared.myAssert(false, "Invalid direction");
        }
        return null;
    }
}