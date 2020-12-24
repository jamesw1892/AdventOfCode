import java.util.ArrayList;

public class Circle {
    ArrayList<Integer> circle;
    int currentCupLabel;
    int size;

    public Circle(String nums, int numNums) {
        this(nums);
        int max = Character.getNumericValue(nums.charAt(0));
        for (int index = 1; index < nums.length(); index++) {
            int value = Character.getNumericValue(nums.charAt(index));
            if (value > max) {
                max = value;
            }
        }

        for (int num = max + 1; num <= numNums; num++) {
            this.circle.add(num);
        }
        this.size = this.circle.size();
    }

    public Circle(String nums) {
        this.circle = new ArrayList<>();
        for (char num: nums.toCharArray()) {
            this.circle.add(Character.getNumericValue(num));
        }
        this.currentCupLabel = this.circle.get(0);
        this.size = this.circle.size();
    }

    public String toString() {
        String out = "";
        int oneIndex = this.circle.indexOf(1);
        for (int index = oneIndex + 1; index % this.size != oneIndex; index++) {
            out += this.circle.get(index % this.size).toString();
        }
        return out;
    }

    public long product() {
        int oneIndex = this.circle.indexOf(1);
        int index1 = (oneIndex + 1) % this.size;
        int index2 = (oneIndex + 2) % this.size;
        return ((long) this.circle.get(index1)) * ((long) this.circle.get(index2));
    }

    public void move(int numMoves) {
        int pct = 1;
        for (int moveNum = 1; moveNum <= numMoves; moveNum++) {
            this.move();
            if (moveNum % 10000 == 0) {
                System.out.println(pct + "%");
                pct++;
            }
        }
    }

    public void move() {

        // remove 3 cups clockwise from current cup
        int pickedUp1 = this.circle.remove((this.circle.indexOf(this.currentCupLabel) + 1) % this.circle.size());
        int pickedUp2 = this.circle.remove((this.circle.indexOf(this.currentCupLabel) + 1) % this.circle.size());
        int pickedUp3 = this.circle.remove((this.circle.indexOf(this.currentCupLabel) + 1) % this.circle.size());

        // destination cup
        int destinationCupLabel = this.currentCupLabel;
        do {
            if (destinationCupLabel == 1) {
                destinationCupLabel = this.size;
            } else {
                destinationCupLabel--;
            }
        } while (destinationCupLabel == pickedUp1 || destinationCupLabel == pickedUp2 || destinationCupLabel == pickedUp3);

        int destinationCupIndex = this.circle.indexOf(destinationCupLabel);

        // add picked up cups after destination cup
        this.circle.add(destinationCupIndex + 1, pickedUp1);
        this.circle.add(destinationCupIndex + 2, pickedUp2);
        this.circle.add(destinationCupIndex + 3, pickedUp3);

        // select new current cup
        this.currentCupLabel = this.circle.get((this.circle.indexOf(this.currentCupLabel) + 1) % this.size);
    }
}
