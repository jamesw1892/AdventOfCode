public class Field {
    String name;
    int aMin;
    int aMax;
    int bMin;
    int bMax;
    int pos = -1;
    public Field(String name, int aMin, int aMax, int bMin, int bMax) {
        this.name = name;
        this.aMin = aMin;
        this.aMax = aMax;
        this.bMin = bMin;
        this.bMax = bMax;
    }
}