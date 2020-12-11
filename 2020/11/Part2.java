import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Grid2 {
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

    public Grid2(ArrayList<ArrayList<Seat>> grid) {
        this.grid = grid;
        this.newGrid = new ArrayList<ArrayList<Seat>>();
        for (ArrayList<Seat> inner: grid) {
            @SuppressWarnings("unchecked")
            ArrayList<Seat> temp = (ArrayList<Seat>) inner.clone();
            this.newGrid.add(temp);
        }
    }

    public int height() {
        return this.grid.size();
    }

    public int width() {
        return this.grid.get(0).size();
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
        return x >= 0 && x < width();
    }

    private boolean isValidY(int y) {
        return y >= 0 && y < height();
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
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
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
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (get(x, y) == Seat.OCCUPIED) numOccupied++;
            }
        }
        return numOccupied;
    }
}

public class Part2 {

    private static int ans(Grid2 grid) {

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

        return ans(new Grid2(g));
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 26, "Failed test input");

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