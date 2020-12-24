import java.util.TreeSet;

public class HexGrid {
    private TreeSet<HexPos> blackTiles;

    // keep track of how far the black tiles are spreading so
    // we know which white tiles to check - otherwise we would
    // have to check the infinite grid. We update them as we go
    // so we don't have to recompute them at the start of each day
    private int minX = 0;
    private int maxX = 0;
    private int minY = 0;
    private int maxY = 0;

    private static final int[][] ADJACENT_OFFSETS = new int[][] {
        { 1,  1}, // north east
        { 2,  0}, //       east
        { 1, -1}, // south east
        {-1, -1}, // south west
        {-2,  0}, //       west
        {-1,  1}  // north west
    };

    public HexGrid() {
        this.blackTiles = new TreeSet<>();
    }

    /**
     * Parse the input instructions to get a HexPos
     * that they lead to
     */
    private HexPos getPos(String instructions) {

        // active determines whether the last character was 'n' or 's'
        // so the next character, 'e' or 'w' actually means 'ne', 'nw', 'se' or 'sw'
        Active active = Active.NONE;

        // start the position at the origin (the reference point)
        HexPos pos = new HexPos();

        // move the position the direction depending on the characters in the instructions
        for (char chr: instructions.toCharArray()) {
            switch (active) {
                case NONE:
                    switch (chr) {
                        case 'n': active = Active.NORTH; break;
                        case 'e': pos.east(); break;
                        case 's': active = Active.SOUTH; break;
                        case 'w': pos.west(); break;
                        default: Part1.myAssert(false, "Invalid character while not active");
                    }
                    break;
                case NORTH:
                    switch (chr) {
                        case 'e': pos.northEast(); break;
                        case 'w': pos.northWest(); break;
                        default: Part1.myAssert(false, "Invalid character while NORTH active");
                    }
                    active = Active.NONE;
                    break;
                case SOUTH:
                    switch (chr) {
                        case 'e': pos.southEast(); break;
                        case 'w': pos.southWest(); break;
                        default: Part1.myAssert(false, "Invalid character while SOUTH active");
                    }
                    active = Active.NONE;
                    break;
                default:
                    Part1.myAssert(false, "Invalid Active");
            }
        }

        Part1.myAssert(active == Active.NONE, "Should not end as active");

        return pos;
    }

    /**
     * Get the number of adjacent tiles to the one given
     */
    private int getNumAdjacentBlackTiles(HexPos pos) {
        int numAdjacentTiles = 0;

        // for each offset, add it to the position and if
        // that is a black tile, increment the counter
        for (int[] offset: ADJACENT_OFFSETS) {
            if (this.blackTiles.contains(new HexPos(pos.getX() + offset[0], pos.getY() + offset[1]))) {
                numAdjacentTiles++;
            }
        }

        return numAdjacentTiles;
    }

    /**
     * Update the bounds to track how far black tiles reach
     */
    private void updateBounds(HexPos pos) {
        if (pos.getX() > this.maxX) {
            this.maxX = pos.getX();
        } else if (pos.getX() < this.minX) {
            this.minX = pos.getX();
        }

        if (pos.getY() > this.maxY) {
            this.maxY = pos.getY();
        } else if (pos.getY() < this.minY) {
            this.minY = pos.getY();
        }
    }

    /**
     * Flip the tile pointed to by the instructions
     */
    public void flip(String instructions) {
        HexPos pos = this.getPos(instructions);

        // add if not already there, else remove
        if (this.blackTiles.contains(pos)) {
            this.blackTiles.remove(pos);
        } else {
            this.blackTiles.add(pos);
            this.updateBounds(pos);
        }
    }

    /**
     * Get the number of black tiles currently in the grid
     */
    public int getNumBlackTiles() {
        return this.blackTiles.size();
    }

    /**
     * Flip tiles according to the rules for a single day
     */
    private void day() {

        // only apply the changes at the end of the day so add to here first
        TreeSet<HexPos> toRemove = new TreeSet<>();
        TreeSet<HexPos> toAdd = new TreeSet<>();
        
        // we adjust the bounds as we go so shouldn't keep using these values
        // as they may change so make a copy before we start and use these
        int maxXToCheck = this.maxX + 2;
        int maxYToCheck = this.maxY + 2;

        HexPos pos;
        int numAdjacentTiles;

        // iterate through every normal 2D grid position in the range
        for (int x = this.minX - 2; x <= maxXToCheck; x++) {
            for (int y = this.minY - 2; y <= maxYToCheck; y++) {
                pos = new HexPos(x, y);

                // for those that are valid positions on a hex grid
                if (pos.isValid()) {

                    // get the number of black tiles adjacent to them
                    numAdjacentTiles = this.getNumAdjacentBlackTiles(pos);

                    // if the tile is black and it has 0 or more than 2 adjacent
                    // black tiles, it should be flipped to white
                    if (this.blackTiles.contains(pos)) {
                        if (numAdjacentTiles == 0 || numAdjacentTiles > 2) {
                            toRemove.add(pos);
                        }

                    // if the tile is white and it has 2 adjacent black tiles,
                    // it should be flipped to black
                    } else {
                        if (numAdjacentTiles == 2) {
                            toAdd.add(pos);
                            this.updateBounds(pos);
                        }
                    }
                }
            }
        }

        // apply changes
        this.blackTiles.removeAll(toRemove);
        this.blackTiles.addAll(toAdd);
    }

    /**
     * Perform the changes for the given number of days
     */
    public void days(int numDays) {
        for (int dayNum = 1; dayNum <= numDays; dayNum++) {
            this.day();
        }
    }
}