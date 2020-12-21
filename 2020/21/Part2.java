import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;

public class Part2 {
    private static String solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        HashMap<String, HashSet<String>> ingredientsOfAllergens = new HashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(" \\(contains ");
            String[] ingredients = splitLine[0].split(" ");
            String[] allergens = splitLine[1].split("\\)")[0].split(", ");

            // populate set of ingredients on this line
            HashSet<String> ingredientsSet = new HashSet<>();
            for (String ingredient: ingredients) {
                ingredientsSet.add(ingredient);
            }

            // add ingredients to ingredientsOfAllergen
            for (String allergen: allergens) {
                if (ingredientsOfAllergens.containsKey(allergen)) {
                    ingredientsOfAllergens.get(allergen).retainAll(ingredientsSet);
                } else {
                    @SuppressWarnings("unchecked")
                    HashSet<String> copy = (HashSet<String>) ingredientsSet.clone();
                    ingredientsOfAllergens.put(allergen, copy);
                }
            }
        }
        scanner.close();

        TreeMap<String, String> allergensIngredients = new TreeMap<>();

        /*
        Repeatedly try and find an allergen that can only be 1 ingredient - 
        the intersection of all the ingredients it could be has size 1 - 
        and then remove it from ingredientsOfAllergens so it can help narrow down
        other ingredients too. Once we have found a match, also add it to
        allergensIngredients which we need for the final answer. Any that do
        not have a match should not be included anyway so loop until there is no
        longer a change.
        */
        boolean hasChanged = true;
        HashSet<String> ingredients;
        String ingredient = "";
        String allergen = "";
        while (hasChanged) {
            hasChanged = false;
            for (String loopAllergen: ingredientsOfAllergens.keySet()) {
                ingredients = ingredientsOfAllergens.get(loopAllergen);
                if (ingredients.size() == 1) {
                    ingredient = ingredients.iterator().next();
                    allergen = loopAllergen;
                    allergensIngredients.put(allergen, ingredient);
                    hasChanged = true;
                    break;
                }
            }
            if (hasChanged) {
                ingredientsOfAllergens.remove(allergen);
                for (String allergenInner: ingredientsOfAllergens.keySet()) {
                    ingredientsOfAllergens.get(allergenInner).remove(ingredient);
                }
            }
        }

        // form the string to output
        String out = "";
        for (String allergenNewWowThisCodeIsAMess: allergensIngredients.keySet()) {
            out += "," + allergensIngredients.get(allergenNewWowThisCodeIsAMess);
        }
        return out.substring(1); // remove leading comma
    }

    private static void test() throws IOException {

        Shared.myAssert(solve("input_test.txt").equals("mxmxvkd,sqjhc,fvjkl"), "Failed test input");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(solve("input.txt"));
        }
    }
}