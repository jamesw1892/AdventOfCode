public class EvaluatedStr {
    String str;
    boolean evaluated;

	public EvaluatedStr(String str, boolean evaluated) {
		this.str = str;
		this.evaluated = evaluated;
    }

    public String toString() {
        String out = "";
        if (evaluated) out += "Evaluated";
        else out += "Unevaluated";
        return out + "(" + str + ")";
    }
}