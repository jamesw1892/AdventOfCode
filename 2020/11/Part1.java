import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class GridPart1 {
    private ArrayList<ArrayList<Seat>> grid;
    private ArrayList<ArrayList<Seat>> newGrid;
    private int width;
    private int height;

    public GridPart1(ArrayList<ArrayList<Seat>> grid) {
        this.grid = grid;
        this.newGrid = new ArrayList<ArrayList<Seat>>();
        for (ArrayList<Seat> inner: grid) {
            @SuppressWarnings("unchecked")
            ArrayList<Seat> temp = (ArrayList<Seat>) inner.clone();
            this.newGrid.add(temp);
        }
        this.height = this.grid.size();
        Part1.myAssert(this.height > 0, "Must have at least 1 row");
        this.width = this.grid.get(0).size();
    }

    public Seat get(int x, int y) {
        return this.grid.get(y).get(x);
    }

    public void set(int x, int y, Seat seat) {
        this.newGrid.get(y).set(x, seat);
    }

    private int[][] getAdjacentPairs(int x, int y) {
        return new int[][] {
            {x + 1, y + 1},
            {x + 1, y    },
            {x + 1, y - 1},
            {x    , y + 1},
            {x    , y - 1},
            {x - 1, y + 1},
            {x - 1, y    },
            {x - 1, y - 1},
        };
    }

    private boolean noneAdjacentOccupied(int x, int y) {

        for (int[] pair: getAdjacentPairs(x, y)) {
            if (pair[0] >= 0 && pair[0] < this.width && pair[1] >= 0 && pair[1] < this.height) {
                if (this.get(pair[0], pair[1]) == Seat.OCCUPIED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean atLeast4AdjacentOccupied(int x, int y) {

        int occupiedCount = 0;
        for (int[] pair: getAdjacentPairs(x, y)) {
            if (pair[0] >= 0 && pair[0] < this.width && pair[1] >= 0 && pair[1] < this.height) {
                if (this.get(pair[0], pair[1]) == Seat.OCCUPIED) {
                    occupiedCount++;
                }
            }
        }
        return occupiedCount >= 4;
    }

    private boolean applyRulesSeat(int x, int y) {
        switch (this.get(x, y)) {
            case EMPTY:
                if (this.noneAdjacentOccupied(x, y)) {
                    this.set(x, y, Seat.OCCUPIED);
                    return true;
                }
                break;
            case OCCUPIED:
                if (this.atLeast4AdjacentOccupied(x, y)) {
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

public class Part1 {

    private static int ans(GridPart1 grid) {

        while (grid.applyRules());

        return grid.getNumOccupied();
    }

    private static int solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<ArrayList<Seat>> g = new ArrayList<>();
        
        while (scanner.hasNextLine()) {
            ArrayList<Seat> inner = new ArrayList<>();
            for (char chr: scanner.nextLine().toCharArray()) {
                switch (chr) {
                    case '.': inner.add(Seat.FLOOR); break;
                    case 'L': inner.add(Seat.EMPTY); break;
                    case '#': inner.add(Seat.OCCUPIED); break;
                    default: myAssert(false, "Invalid seat");
                }
            }
            g.add(inner);
        }
        scanner.close();

        return ans(new GridPart1(g));
    }

    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 37, "Failed test input");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(solve("input.txt"));
        }
    }
}