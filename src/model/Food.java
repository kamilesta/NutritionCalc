package model;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static model.DataSource.getConnection;
import static model.DataSource.selectAllFrom;

public class Food {
    private int id;
    private String name;
    private String country;
    private int grams;
    private int calories;
    private int protein;
    private int carb;
    private double chosenGrams;

    public double getChosenGrams() {
        return chosenGrams;
    }

    public void setChosenGrams(double chosenGrams) {
        this.chosenGrams = chosenGrams;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getGrams() {
        return grams;
    }

    public void setGrams(int grams) {
        this.grams = grams;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarb() {
        return carb;
    }

    public void setCarb(int carb) {
        this.carb = carb;
    }

    @Override
    public String toString() {
        if (name == null){
            return null;
        }
        StringBuilder sb = new StringBuilder(name);
        sb.append(": \n");
        sb.append("\tComes from: ");
        sb.append(country);
        sb.append("\n\tNutritional values: ");
        sb.append("Grams: ");
        sb.append(grams);
        sb.append(" | Calories: ");
        sb.append(calories);
        sb.append(" | Protein: ");
        sb.append(protein);
        sb.append(" | Carb:" );
        sb.append(carb);
        return sb.toString();
    }

    public static void showMealList(List<Food> foodList){
        for (Food food : foodList){
            if (food.getName() != null) {
                System.out.println("\t- " + food.getName());
            }
        }
    }

    public static void sumUpChosenMeal(List<Food> foodList){
        double calories = 0;
        double protein = 0;
        double carb = 0;
        for (Food food : foodList){
            if (food.getName() != null) {
                calories += ((food.getChosenGrams() / food.getGrams()) * food.getCalories());
                protein += ((food.getChosenGrams() / food.getGrams()) * food.getProtein());
                carb += ((food.getChosenGrams() / food.getGrams()) * food.getCarb());
            }
        }
        System.out.println("Nutrition values of your meal are: ");
        System.out.print("\tCalories: ");
        System.out.format(Locale.ENGLISH, "%.2f%n", calories);
        System.out.print("\tProtein: ");
        System.out.format(Locale.ENGLISH, "%.2f%n", protein);
        System.out.print("\tCarb: " );
        System.out.format(Locale.ENGLISH, "%.2f%n", carb);
    }

    public static double foodWeight() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Wrong value.");
                scanner.nextLine();
            }
        }
    }

    public static String foodName(String tableName) {
        List<String> foodList = selectAllFrom(getConnection(), tableName);
        Scanner scanner = new Scanner(System.in);
        String input = "";
        boolean isOnList = false;
        boolean isValid = false;
        while (!(isOnList && isValid)) {
            input = scanner.nextLine();
            if (foodList.contains(input.toLowerCase())){
                isOnList = true;
            } else {
                System.out.println(input + " is not in our database. Try something else.");
            }
            try {
                Integer.parseInt(input);
                System.out.println("Wrong value.");
            } catch (NumberFormatException e) {
                isValid = true;
            }
        }
        return input;
    }
}
