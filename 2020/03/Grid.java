import java.util.ArrayList;

public class Grid {
    private int width;
    private int height;
    private ArrayList<ArrayList<Boolean>> grid;

    public Grid(ArrayList<ArrayList<Boolean>> grid) {
        this.height = grid.size();
        this.width = grid.get(0).size();
        this.grid = grid;
    }

    /**
     * Start at 0
     * @param x
     * @param y
     * @return
     */
    public boolean get(int x, int y) {
        assert y < height: "y out of bounds";
        if (x >= width) {
            x = x % width;
        }
        return this.grid.get(y).get(x);
    }

    public int getHeight() {
        return this.height;
    }
}
