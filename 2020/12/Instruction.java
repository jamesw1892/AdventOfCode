public class Instruction {
    private Direction dir;
    private int num;
    public Instruction(Direction dir, int num) {
        this.dir = dir;
        this.num = num;
    }
	public Direction getDir() {
		return this.dir;
	}
	public void setDir(Direction dir) {
		this.dir = dir;
	}
	public int getNum() {
		return this.num;
	}
	public void setNum(int num) {
		this.num = num;
	}
}