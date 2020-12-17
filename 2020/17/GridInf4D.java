import java.util.HashMap;

public class GridInf4D {
    private HashMap<Pos4D, Boolean> cubes;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;
    private int minW;
    private int maxW;

    public GridInf4D(HashMap<Pos4D, Boolean> initial, int minX, int maxX, int minY, int maxY, int minZ, int maxZ, int minW, int maxW) {
        this.cubes = initial;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.minW = minW;
        this.maxW = maxW;
    }

    private void extendCube() {

        // x
        for (int y = this.minY; y <= this.maxY; y++) {
            for (int z = this.minZ; z <= this.maxZ; z++) {
                for (int w = this.minW; w <= this.maxW; w++) {
                    this.cubes.put(new Pos4D(this.minX - 1, y, z, w), false);
                    this.cubes.put(new Pos4D(this.maxX + 1, y, z, w), false);
                }
            }
        }
        this.minX--;
        this.maxX++;

        // y
        for (int x = this.minX; x <= this.maxX; x++) {
            for (int z = this.minZ; z <= this.maxZ; z++) {
                for (int w = this.minW; w <= this.maxW; w++) {
                    this.cubes.put(new Pos4D(x, this.minY - 1, z, w), false);
                    this.cubes.put(new Pos4D(x, this.maxY + 1, z, w), false);
                }
            }
        }
        this.minY--;
        this.maxY++;

        // z
        for (int x = this.minX; x <= this.maxX; x++) {
            for (int y = this.minY; y <= this.maxY; y++) {
                for (int w = this.minW; w <= this.maxW; w++) {
                    this.cubes.put(new Pos4D(x, y, this.minZ - 1, w), false);
                    this.cubes.put(new Pos4D(x, y, this.maxZ + 1, w), false);
                }
            }
        }
        this.minZ--;
        this.maxZ++;

        // w
        for (int x = this.minX; x <= this.maxX; x++) {
            for (int y = this.minY; y <= this.maxY; y++) {
                for (int z = this.minZ; z <= this.maxZ; z++) {
                    this.cubes.put(new Pos4D(x, y, z, this.minW - 1), false);
                    this.cubes.put(new Pos4D(x, y, z, this.maxW + 1), false);
                }
            }
        }
        this.minW--;
        this.maxW++;
    }

    public String toString() {
        String out = "\n\n\n";
        for (int w = this.minW; w <= this.maxW; w++) {
            for (int z = this.minZ; z <= this.maxZ; z++) {
                out += "\n\nz=" + z + ", w=" + w;
                for (int y = this.minY; y <= this.maxY; y++) {
                    out += "\n";
                    for (int x = this.minX; x <= this.maxX; x++) {
                        out += this.cubes.get(new Pos4D(x, y, z, w)) ? '#' : '.';
                    }
                }
            }
        }
        return out;
    }

    private void cycle() {

        // if (this.minX >= -2) System.out.println(this.toString());
        this.extendCube();

        HashMap<Pos4D, Boolean> temp = new HashMap<>();

        int numNeighbours;
        Boolean neighbourActive;
        for (Pos4D pos: this.cubes.keySet()) {

            // get number of neighbours
            numNeighbours = 0;
            for (Pos4D neighbourPos: this.neighbours(pos)) {
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
        for (Pos4D pos: this.cubes.keySet()) {
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

    private Pos4D[] neighbours(Pos4D pos) {
        Pos4D[] offsets = new Pos4D[80];
        int index = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    for (int w = -1; w <= 1; w++) {
                        if (x != 0 || y != 0 || z != 0 || w != 0) {
                            offsets[index] = new Pos4D(
                                pos.x + x,
                                pos.y + y,
                                pos.z + z,
                                pos.w + w
                            );
                            index++;
                        }
                    }
                }
            }
        }
        return offsets;
    }
}