import java.util.HashMap;

public class GridInf3D {
    private HashMap<Pos3D, Boolean> cubes;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;

    public GridInf3D(HashMap<Pos3D, Boolean> initial, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        this.cubes = initial;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    private void extendCube() {

        // x
        for (int y = this.minY; y <= this.maxY; y++) {
            for (int z = this.minZ; z <= this.maxZ; z++) {
                this.cubes.put(new Pos3D(this.minX - 1, y, z), false);
                this.cubes.put(new Pos3D(this.maxX + 1, y, z), false);
            }
        }
        this.minX--;
        this.maxX++;

        // y
        for (int x = this.minX; x <= this.maxX; x++) {
            for (int z = this.minZ; z <= this.maxZ; z++) {
                this.cubes.put(new Pos3D(x, this.minY - 1, z), false);
                this.cubes.put(new Pos3D(x, this.maxY + 1, z), false);
            }
        }
        this.minY--;
        this.maxY++;

        // z
        for (int x = this.minX; x <= this.maxX; x++) {
            for (int y = this.minY; y <= this.maxY; y++) {
                this.cubes.put(new Pos3D(x, y, this.minZ - 1), false);
                this.cubes.put(new Pos3D(x, y, this.maxZ + 1), false);
            }
        }
        this.minZ--;
        this.maxZ++;
    }

    public String toString() {
        String out = "\n\n";
        for (int z = this.minZ; z <= this.maxZ; z++) {
            out += "\n\nz=" + z;
            for (int y = this.minY; y <= this.maxY; y++) {
                out += "\ny=" + y + ": ";
                for (int x = this.minX; x <= this.maxX; x++) {
                    out += this.cubes.get(new Pos3D(x, y, z)) ? '#' : '.';
                }
            }
        }
        return out;
    }

    private void cycle() {

        // if (this.minX >= -3) System.out.println(this.toString());

        this.extendCube();

        HashMap<Pos3D, Boolean> temp = new HashMap<>();

        int numNeighbours;
        Boolean neighbourActive;
        for (Pos3D pos: this.cubes.keySet()) {

            // get number of neighbours
            numNeighbours = 0;
            for (Pos3D neighbourPos: this.neighbours(pos)) {
                neighbourActive = this.cubes.get(neighbourPos);
                if (neighbourActive != null && neighbourActive) {
                    numNeighbours++;
                }
            }

            // change

            // if active
            if (this.cubes.get(pos)) {
                switch (numNeighbours) {
                    case 2:
                    case 3:
                        temp.put(pos, true);
                        break;
                    default:
                        temp.put(pos, false);
                }

            // if inactive
            } else {
                if (numNeighbours == 3) {
                    temp.put(pos, true);
                } else {
                    temp.put(pos, false);
                }
            }
        }

        this.cubes = temp;
    }

    public int getNumActive() {
        int numActive = 0;
        for (Pos3D pos: this.cubes.keySet()) {
            if (this.cubes.get(pos)) {
                numActive++;
            }
        }
        return numActive;
    }

    public void cycles(int numCycles) {
        for (; numCycles > 0; numCycles--) {
            this.cycle();
        }
    }

    private Pos3D[] neighbours(Pos3D pos) {
        Pos3D[] offsets = new Pos3D[26];
        int index = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x != 0 || y != 0 || z != 0) {
                        offsets[index] = new Pos3D(
                            pos.x + x,
                            pos.y + y,
                            pos.z + z
                        );
                        index++;
                    }
                }
            }
        }
        return offsets;
    }
}