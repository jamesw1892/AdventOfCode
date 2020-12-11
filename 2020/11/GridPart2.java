import java.util.ArrayList;

class GridPart2 {
    private ArrayList<ArrayList<Seat>> grid;
    private ArrayList<ArrayList<Seat>> newGrid;
    private static final int[][] DIRS = {
        {1, 1},
        {1, 0},
        {1, -1},
        {0, 1},
        {0, -1},
        {-1, 1},
        {-1, 0},
        {-1, -1}
    };
    private int width;
    private int height;

    public GridPart2(ArrayList<ArrayList<Seat>> grid) {
        this.grid = grid;
        this.newGrid = new ArrayList<ArrayList<Seat>>();
        for (ArrayList<Seat> inner: grid) {
            @SuppressWarnings("unchecked")
            ArrayList<Seat> temp = (ArrayList<Seat>) inner.clone();
            this.newGrid.add(temp);
        }
        this.height = this.grid.size();
        Shared.myAssert(this.height > 0, "Must have at least 1 row");
        this.width = this.grid.get(0).size();
    }

    public Seat get(int x, int y) {
        return this.grid.get(y).get(x);
    }

    public void set(int x, int y, Seat seat) {
        this.newGrid.get(y).set(x, seat);
    }

    private boolean noneVisibleOccupied(int x, int y) {

        int i, newX, newY;
        boolean done;
        for (int[] dir: DIRS) {
            i = 1;
            done = false;
            while (!done && isValidX((newX = x + i*dir[0])) && isValidY((newY = y + i*dir[1]))) {
                switch (this.get(newX, newY)) {
                    case OCCUPIED:
                        return false;
                    case EMPTY:
                        done = true;
                        break;
                    case FLOOR:
                        break;
                }
                i++;
            }
        }
        return true;
    }

    private boolean isValidX(int x) {
        return x >= 0 && x < this.width;
    }

    private boolean isValidY(int y) {
        return y >= 0 && y < this.height;
    }

    private boolean atLeast5VisibleOccupied(int x, int y) {
        
        int occupiedCount = 0;
        int i;
        int newX, newY;
        boolean done;
        for (int[] dir: DIRS) {
            i = 1;
            done = false;
            while (!done && isValidX((newX = x + i*dir[0])) && isValidY((newY = y + i*dir[1]))) {
                switch (this.get(newX, newY)) {
                    case OCCUPIED:
                        occupiedCount++;
                        done = true;
                        break;
                    case EMPTY:
                        done = true;
                        break;
                    case FLOOR:
                        break;
                }
                i++;
            }
        }

        return occupiedCount >= 5;
    }

    private boolean applyRulesSeat(int x, int y) {
        switch (this.get(x, y)) {
            case EMPTY:
                if (this.noneVisibleOccupied(x, y)) {
                    this.set(x, y, Seat.OCCUPIED);
                    return true;
                }
                break;
            case OCCUPIED:
                if (this.atLeast5VisibleOccupied(x, y)) {
                    this.set(x, y, Seat.EMPTY);
                    return true;
                }
                break;
			case FLOOR:
				break;
        }
        return false;
    }

    public boolean applyRules() {
        boolean somethingHasChanged = false;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (applyRulesSeat(x, y)) somethingHasChanged = true;
            }
        }
        this.grid.clear();
        for (ArrayList<Seat> inner: this.newGrid) {
            @SuppressWarnings("unchecked")
            ArrayList<Seat> temp = (ArrayList<Seat>) inner.clone();
            this.grid.add(temp);
        }
        return somethingHasChanged;
    }

    public int getNumOccupied() {
        int numOccupied = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (get(x, y) == Seat.OCCUPIED) numOccupied++;
            }
        }
        return numOccupied;
    }
}