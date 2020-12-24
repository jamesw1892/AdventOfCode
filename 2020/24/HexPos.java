/**
 * Position on a 2D hex grid. Based on normal 2D grid but every other position is invalid
 */
public class HexPos implements Comparable<HexPos> {
    private int x;
    private int y;

    /**
     * Default contructor for 0, 0
     */
    public HexPos() {
        this.x = 0;
        this.y = 0;
    }

    public HexPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * Whether this coordinate lies on the hex grid
     */
    public boolean isValid() {
        // valid if difference between coordinates is even
        return (this.x - this.y) % 2 == 0;
    }

    public void east() {
        this.x += 2;
    }

    public void west() {
        this.x -= 2;
    }

    public void southEast() {
        this.x += 1;
        this.y -= 1;
    }

    public void southWest() {
        this.x -= 1;
        this.y -= 1;
    }

    public void northEast() {
        this.x += 1;
        this.y += 1;
    }

    public void northWest() {
        this.x -= 1;
        this.y += 1;
    }

	public int compareTo(HexPos other) {
        if (this.x == other.x) {
            return Integer.compare(this.y, other.y);
        }
		return Integer.compare(this.x, other.x);
	}
}