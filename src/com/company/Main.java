package com.company;

import model.*;

import java.util.*;

import static model.Food.*;

public class Main {

    private static List<Food> meals = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DataSource source = new DataSource();
        if (!source.open()) {
            System.out.println("Connection not opened.");
            return;
        }
        System.out.println("=| FoodCalculator |=");

        boolean quit = false;
        int option;
        printInstructions();
        while (!quit) {
            System.out.println("Press 1 for instructions");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    printInstructions();
                    break;
                case 2:
                    System.out.println("You can choose your meal ingredients from: ");
                    source.getTablesNames();

                    System.out.println("Choose meat:");
                    String meatType = foodName(DataSource.MEATS_TABLE);
                    System.out.println("Meat grams:");
                    double meatGram = foodWeight();

                    System.out.println("Choose vegetables:");
                    String vegetableType = foodName(DataSource.VEGETABLES_TABLE);
                    System.out.println("Vegetable grams:");
                    double vegetableGram = foodWeight();

                    System.out.println("Choose additions:");
                    String additionType = foodName(DataSource.ADDITIONS_TABLE);
                    System.out.println("Addition grams:");
                    double additionsGram = foodWeight();

                    composeMeal(source, meatType, meatGram, vegetableType, vegetableGram, additionType, additionsGram);
                    System.out.println("Your meal is composed of: ");
                    showMealList(meals);
                    sumUpChosenMeal(meals);

                    break;

                case 3:
                    System.out.println("Choose meat that you want your meal with from. Do you want also meal with lowest calories? ");

                    ;
                    List<String> meatList = DataSource.selectAllFrom(DataSource.getConnection(), DataSource.MEATS_TABLE);
                    for (String meat : meatList) {
                        System.out.println("\t" + meat);
                    }
                    String chosenMeal = Food.foodName(DataSource.MEATS_TABLE);
                    scanner.nextLine();
                    System.out.println("Do you choose lowest calories?");
                    String sortOrder = scanner.nextLine();
                    meals = source.chooseMealByMeat(chosenMeal, sortOrder);
                    System.out.println("We propose meal of:");
                    showMealList(meals);
                    sumUpChosenMeal(meals);
                    break;
                case 0:
                    quit = true;
                    break;

                default:
                    printInstructions();
                    break;
            }
        }

        if (source.close()) {
            System.out.println("Closed successfully");
        }
    }

    public static void printInstructions() {
        System.out.println("\nChoose your option:\n" +
                "\t1 - Print instructions\n" +
                "\t2 - Compose your own meal\n" +
                "\t3 - Let us suggest you optimal meal with your favourite meat\n" +
                "\t4 - Add new food (TODO)\n" +
                "\t0 - Quit\n");
    }

    public static void composeMeal(DataSource source,
                                   String meat, double meatGrams,
                                   String vegetable, double vegetableGrams,
                                   String addition, double additionGram) {
        Meat newMeat = source.chooseMeatByName(meat, meatGrams);
        Vegetables newVegetable = source.chooseVegetableByName(vegetable, vegetableGrams);
        Additions newAddition = source.chooseAdditionByName(addition, additionGram);
        meals.add(newMeat);
        meals.add(newVegetable);
        meals.add(newAddition);
    }
}
