package controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.sql.*;
import utilities.*;
import java.io.*;

public class AppDictionnary {

    // Attributs
    private List<String> wordsList;
    private Connection connection;

    // Constructeur
    public AppDictionnary() {
        wordsList = new ArrayList<>();
        try {
            connection = DatabaseManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthodes
    public void loadWords() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT mot FROM mots");

            while (resultSet.next()) {
                wordsList.add(resultSet.getString("mot"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.closeConnection(connection); // Fermer la connexion après utilisation
        }
    }

    public void FileToDb(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String word;
            connection.setAutoCommit(false); // Démarrer une transaction

            // Prepare the SQL statement to insert words
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO mots (mot) VALUES (?)");

            while ((word = reader.readLine()) != null) {
                // Ajouter chaque mot dans la base de données
                preparedStatement.setString(1, word);
                preparedStatement.addBatch();
            }

            // Execute batch insert
            preparedStatement.executeBatch();
            connection.commit(); // Commit la transaction
            connection.setAutoCommit(true); // Reset the auto-commit to true
            System.out.println("Mots chargés dans la base de données depuis le fichier.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback en cas d'erreur
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback: " + ex.getMessage());
            }
            System.err.println("Erreur SQL: " + e.getMessage());
        }
    }

    public String getRandomWord(int size) {
        // Filtre les mots qui ont la taille demandée
        List<String> filteredWords = wordsList.stream()
                .filter(word -> word.length() == size)
                .toList();
        if (filteredWords.isEmpty()) {
            return null; // ou une gestion d'erreur appropriée
        } else {
            // Sélectionne un mot aléatoire parmi ceux filtrés
            Random rand = new Random();
            return filteredWords.get(rand.nextInt(filteredWords.size()));
        }
    }
}
