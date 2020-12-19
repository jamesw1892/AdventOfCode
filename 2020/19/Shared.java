import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shared {
    TreeMap<Integer, EvaluatedStr> rules;
    static final Pattern EXACT = Pattern.compile("\"(.)\"");

    public Shared(TreeMap<Integer, EvaluatedStr> rules) {
        this.rules = rules;
    }

    public String getRegex(int ruleNum) {

        EvaluatedStr evaluatedStr = rules.get(ruleNum);
        String rule = evaluatedStr.str;

        // if already been evaluated, just return it
        if (evaluatedStr.evaluated) {
            return rule;
        }

        // if it is a single character, replace it with this single character and mark as evaluated
        Matcher matcherExact = EXACT.matcher(rule);
        if (matcherExact.matches()) {
            this.rules.put(ruleNum, new EvaluatedStr(matcherExact.group(1), true));
            return matcherExact.group(1);
        }

        // if a relative rule
        String regex = "";
        for (String or: rule.split(" \\| ")) {
            for (String ruleNumStr: or.split(" ")) {
                regex += this.getRegex(Integer.parseInt(ruleNumStr));
            }
            regex += "|";
        }
        String evaluated = "(" + regex.substring(0, regex.length()-1) + ")";
        this.rules.put(ruleNum, new EvaluatedStr(evaluated, true));
        return evaluated;
    }

    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }
}