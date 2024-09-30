package utilities;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "scores";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "0000";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + DATABASE_NAME, USERNAME, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement()) {

            // Create the database if it doesn't exist
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);

            // Now connect to the newly created database and check for tables
            try (Connection dbConnection = getConnection()) {
                if (!tableExists(dbConnection, "results")) {
                    createResultsTable(dbConnection);
                }

                if (!tableExists(dbConnection, "mots")) {
                    createMotsTable(dbConnection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });
        boolean exists = resultSet.next();
        resultSet.close();
        return exists;
    }

    public static boolean isTableEmpty(String tableName) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
    
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
            resultSet.next();
            int count = resultSet.getInt(1);
            return count == 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // En cas d'erreur, considérez que la table n'est pas vide pour éviter des problèmes
        }
    }
    

    private static void createResultsTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sqlCreate = "CREATE TABLE IF NOT EXISTS results (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "playerName VARCHAR(255)," +
                    "word VARCHAR(255)," +
                    "attempts INT," +
                    "score FLOAT)";
            statement.executeUpdate(sqlCreate);
            System.out.println("Table 'results' created successfully.");
        }
    }

    private static void createMotsTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sqlCreate = "CREATE TABLE mots (" +
                    "mot VARCHAR(255) PRIMARY KEY)";
            statement.executeUpdate(sqlCreate);
            System.out.println("Table 'mots' created successfully.");
        }
    }

    public void addGameResult(String word, int attempts, String playerName, float score) {
        String query = "INSERT INTO results (playerName, word, attempts, score) VALUES (?, ?, ?, ?)";
        System.out.println("score: " + score);
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, playerName);
            preparedStatement.setString(2, word);
            preparedStatement.setInt(3, attempts);
            preparedStatement.setFloat(4, score);

            preparedStatement.executeUpdate();
            System.out.println("Game result added to the database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
