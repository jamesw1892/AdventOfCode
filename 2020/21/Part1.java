import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Part1 {
    private static int solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        HashMap<String, Integer> numOccurencesIngredient = new HashMap<>();
        HashMap<String, HashSet<String>> ingredientsOfAllergens = new HashMap<>();
        HashSet<String> ingredientsNoAllergen = new HashSet<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(" \\(contains ");
            String[] ingredients = splitLine[0].split(" ");
            String[] allergens = splitLine[1].split("\\)")[0].split(", ");

            // populate set of ingredients on this line
            HashSet<String> ingredientsSet = new HashSet<>();
            for (String ingredient: ingredients) {
                ingredientsSet.add(ingredient);

                // initially add all ingredients to this set
                ingredientsNoAllergen.add(ingredient);

                // add count to numOccurencesIngredient
                if (numOccurencesIngredient.containsKey(ingredient)) {
                    numOccurencesIngredient.put(ingredient, 1 + numOccurencesIngredient.get(ingredient));
                } else {
                    numOccurencesIngredient.put(ingredient, 1);
                }
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

        /*
        Repeatedly try and find an allergen that can only be 1 ingredient - 
        the intersection of all the ingredients it could be has size 1 - 
        and then remove it from ingredientsNoAllergen as the final answer
        only wants the number of ingredients with no allergen. We also
        remove it from ingredientsOfAllergens so it can help narrow down
        other ingredients too. Loop until there is no longer a change -
        then all the ingredients left will be those that don't have a match
        */
        boolean hasChanged = true;
        HashSet<String> ingredients;
        String ingredient = "";
        String allergenToRemove = "";
        while (hasChanged) {
            hasChanged = false;
            for (String allergen: ingredientsOfAllergens.keySet()) {
                ingredients = ingredientsOfAllergens.get(allergen);
                if (ingredients.size() == 1) {
                    ingredient = ingredients.iterator().next();
                    allergenToRemove = allergen;
                    hasChanged = true;
                    break;
                }
            }
            if (hasChanged) {
                ingredientsNoAllergen.remove(ingredient);
                ingredientsOfAllergens.remove(allergenToRemove);
                for (String allergenInner: ingredientsOfAllergens.keySet()) {
                    ingredientsOfAllergens.get(allergenInner).remove(ingredient);
                }
            }
        }

        // return the number of occurences of any ingredient with no allergen
        int num = 0;
        for (String ingredientNoAllergen: ingredientsNoAllergen) {
            num += numOccurencesIngredient.get(ingredientNoAllergen);
        }
        return num;
    }

    private static void test() throws IOException {

        Shared.myAssert(solve("input_test.txt") == 5, "Failed test input");

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