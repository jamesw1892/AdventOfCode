public class Pos4D {
    int x;
    int y;
	int z;
	int w;
    public Pos4D(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
		this.z = z;
		this.w = w;
    }
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		result = prime * result + w;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pos4D other = (Pos4D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		if (w != other.w)
			return false;
		return true;
	}
}