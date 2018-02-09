package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataSource {

    private String dbUrl;
    private String user;
    private String password;
    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static final String VEGETABLES_TABLE = "vegetables";
    public static final String MEATS_TABLE = "meats";
    public static final String ADDITIONS_TABLE = "additions";

    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String COUNTRY_COLUMN = "country";
    public static final String GRAMS_COLUMN = "grams";
    public static final String CALORIES_COLUMN = "calories";
    public static final String PROTEIN_COLUMN = "protein";
    public static final String CARB_COLUMN = "carb";
    public static final String MEAL_ID_COLUMN = "mealId";


    public static final String STM_SELECT_FROM =
            "SELECT * FROM ";

    public static final String STM_DATA_VEGETABLE =
            "SELECT * FROM " + VEGETABLES_TABLE +
                    " WHERE " + NAME_COLUMN + " = ?";

    public static final String STM_DATA_MEATS =
            "SELECT * FROM " + MEATS_TABLE +
                    " WHERE " + NAME_COLUMN + " = ?";

    public static final String STM_DATA_ADDITIONS =
            "SELECT * FROM " + ADDITIONS_TABLE +
                    " WHERE " + NAME_COLUMN + " = ?";

    public static final String VEGETABLES_CAL_PER_GRAM = "vegCalcPerGram";
    public static final String QUERY_VEGETABLES_CAL_PER_GRAM =
            VEGETABLES_TABLE + "." + CALORIES_COLUMN + " / " + VEGETABLES_TABLE + "." + GRAMS_COLUMN +
                    " AS " + VEGETABLES_CAL_PER_GRAM;

    public static final String ADDITIONS_CAL_PER_GRAM = "addCalcPerGram";
    public static final String QUERY_ADDITIONS_CAL_PER_GRAM =
            ADDITIONS_TABLE + "." + CALORIES_COLUMN + " / " + ADDITIONS_TABLE + "." + GRAMS_COLUMN +
                    " AS " + ADDITIONS_CAL_PER_GRAM;

    public static final String STM_QUERY_MEAL_BY_MEAT =
            "SELECT " + MEATS_TABLE + "." + NAME_COLUMN + ", "
                    + VEGETABLES_TABLE + "." + NAME_COLUMN + ", "
                    + ADDITIONS_TABLE + "." + NAME_COLUMN + ", "
                    + QUERY_VEGETABLES_CAL_PER_GRAM + ", "
                    + QUERY_ADDITIONS_CAL_PER_GRAM +
                    " FROM " + MEATS_TABLE +
                    " INNER JOIN " + VEGETABLES_TABLE + " ON " + MEATS_TABLE + "." + ID_COLUMN +
                    " = " + VEGETABLES_TABLE + "." + MEAL_ID_COLUMN +
                    " INNER JOIN " + ADDITIONS_TABLE + " ON " + MEATS_TABLE + "." + ID_COLUMN +
                    " = " + ADDITIONS_TABLE + "." + MEAL_ID_COLUMN +
                    " WHERE " + MEATS_TABLE + "." + NAME_COLUMN + " = ";

    public static final String QUERY_MEAL_BY_MEAT_SORT =
            " ORDER BY " + VEGETABLES_CAL_PER_GRAM + " + " + ADDITIONS_CAL_PER_GRAM + " ";
    public static final String QUERY_MEAL_BY_MEAT_LIMIT =
            " LIMIT 1";


    private PreparedStatement queryVegetableByName;
    private PreparedStatement queryMeatByName;
    private PreparedStatement queryAdditionByName;
    //private PreparedStatement queryMealByMeat;


    public boolean open() {
        Properties properties = new Properties();
        try (FileInputStream openFile = new FileInputStream("C:\\Users\\User\\Desktop\\projects\\foodDB\\FoodCalc\\foodcalc.properties")) {
            properties.load(openFile);
            dbUrl = properties.getProperty("dburl");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            try {
                connection = DriverManager.getConnection(dbUrl, user, password);
                queryVegetableByName = connection.prepareStatement(STM_DATA_VEGETABLE);
                queryMeatByName = connection.prepareStatement(STM_DATA_MEATS);
                queryAdditionByName = connection.prepareStatement(STM_DATA_ADDITIONS);
                //queryMealByMeat = connection.prepareStatement(STM_QUERY_MEAL_BY_MEAT + );

                return true;
            } catch (SQLException e) {
                System.out.println("Opening connection error: ");
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            System.out.println("Opening file error: ");
            e.printStackTrace();
            return false;
        }
    }

    public boolean close() {
        try {
            if (connection != null) connection.close();
            if (queryVegetableByName != null) queryVegetableByName.close();
            if (queryMeatByName != null) queryMeatByName.close();
            if (queryAdditionByName != null) queryAdditionByName.close();
            //if (queryMealByMeat != null) queryMealByMeat.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Closing error: ");
            e.printStackTrace();
            return false;
        }
    }

    public void getTablesNames() {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] tablesName = null;
            String tableName;
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, tablesName);
            while (resultSet.next()) {
                tableName = resultSet.getString("TABLE_NAME");
                System.out.print("\n" + tableName.substring(0, 1).toUpperCase() + tableName.substring(1));
                List<String> foodListString = selectAllFrom(connection, tableName);
                System.out.println(" contain following food: ");
                for (String food : foodListString) {
                    System.out.print("\t" + food);
                }
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> selectAllFrom(Connection connection, String tableName) {
        List<String> foodList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(STM_SELECT_FROM + tableName)) {
            while (resultSet.next()) {
                foodList.add(resultSet.getString("name").toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodList;
    }

    public Vegetables chooseVegetableByName(String name, double chosenGrams) {
        ResultSet resultSet = null;
        try {
            queryVegetableByName.setString(1, name);
            resultSet = queryVegetableByName.executeQuery();
            Vegetables vegetable = new Vegetables();
            if (resultSet == null) {
                return null;
            }
            while (resultSet.next()) {
                vegetable.setName(resultSet.getString("name"));
                vegetable.setCountry(resultSet.getString("country"));
                vegetable.setGrams(resultSet.getInt("grams"));
                vegetable.setCalories(resultSet.getInt("calories"));
                vegetable.setProtein(resultSet.getInt("protein"));
                vegetable.setCarb(resultSet.getInt("carb"));
                vegetable.setChosenGrams(chosenGrams);
            }
            return vegetable;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Meat chooseMeatByName(String name, double chosenGrams) {
        ResultSet resultSet = null;
        try {
            queryMeatByName.setString(1, name);
            resultSet = queryMeatByName.executeQuery();
            Meat meat = new Meat();
            if (resultSet == null) {
                return null;
            }
            while (resultSet.next()) {
                meat.setName(resultSet.getString("name"));
                meat.setCountry(resultSet.getString("country"));
                meat.setGrams(resultSet.getInt("grams"));
                meat.setCalories(resultSet.getInt("calories"));
                meat.setProtein(resultSet.getInt("protein"));
                meat.setCarb(resultSet.getInt("carb"));
                meat.setChosenGrams(chosenGrams);
            }
            return meat;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Additions chooseAdditionByName(String name, double chosenGrams) {
        ResultSet resultSet = null;
        try {
            queryAdditionByName.setString(1, name);
            resultSet = queryAdditionByName.executeQuery();
            Additions additions = new Additions();
            if (resultSet == null) {
                return null;
            }
            while (resultSet.next()) {
                additions.setName(resultSet.getString("name"));
                additions.setCountry(resultSet.getString("country"));
                additions.setGrams(resultSet.getInt("grams"));
                additions.setCalories(resultSet.getInt("calories"));
                additions.setProtein(resultSet.getInt("protein"));
                additions.setCarb(resultSet.getInt("carb"));
                additions.setChosenGrams(chosenGrams);
            }
            return additions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Food> chooseMealByMeat(String name, String sortOrder) {
        List<Food> foodList = new ArrayList<>();
        String chosenSortOrder = chooseSortOrder(sortOrder);
        StringBuilder queryBuilder = new StringBuilder(STM_QUERY_MEAL_BY_MEAT);
        queryBuilder.append("\"");
        queryBuilder.append(name);
        queryBuilder.append("\"");
        queryBuilder.append(QUERY_MEAL_BY_MEAT_SORT);
        queryBuilder.append(chosenSortOrder);
        queryBuilder.append(QUERY_MEAL_BY_MEAT_LIMIT);
        System.out.println(queryBuilder.toString());
        String meatName = "";
        String vegetableName = "";
        String additionName = "";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryBuilder.toString())) {
            while (resultSet.next()) {
                meatName = resultSet.getString(1);
                vegetableName = resultSet.getString(2);
                additionName = resultSet.getString(3);
            }
            System.out.println("How much " + meatName + " do you want?");
            Meat newMeat = chooseMeatByName(meatName, Food.foodWeight());

            System.out.println("How much " + vegetableName + " do you want?");
            Vegetables newVegetable = chooseVegetableByName(vegetableName, Food.foodWeight());

            System.out.println("How much " + additionName + " do you want?");
            Additions newAddition = chooseAdditionByName(additionName, Food.foodWeight());

            foodList.add(newMeat);
            foodList.add(newVegetable);
            foodList.add(newAddition);
            return foodList;
        } catch (SQLException e) {
            System.out.println("chooseMealByMeat error:");
            e.printStackTrace();
            return null;
        }
    }

    public static String chooseSortOrder(String sortOrder) {
        if (sortOrder.equalsIgnoreCase("y") || sortOrder.equalsIgnoreCase("yes")) {
            return "ASC";
        }
        return "DESC";
    }
}
