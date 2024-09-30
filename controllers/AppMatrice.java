package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AppMatrice {
    private int attemptsCount;
    private String randomChar;
    private int randomIndex;

    // Il pourrait être utile de conserver une référence à la matrice générée
    private List<List<String>> matrix;

    // Constructeur
    public AppMatrice() {
    }

    public AppMatrice(int numRows, int numCols) {
        matrix = new ArrayList<>();
        attemptsCount = 0;
        for (int i = 0; i < numRows; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < numCols; j++) {
                row.add(""); // Initialisez avec une chaîne vide ou un espace
            }
            matrix.add(row);
        }
    }

    // Méthodes
    public List<List<String>> generateMatrix(int numRows, int numCols) {
        matrix = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < numCols; j++) {
                row.add(""); // Initialisez avec une chaîne vide ou un espace
            }
            matrix.add(row);
        }
        return matrix;
    }

    public int getAttemptsCount() {
        return attemptsCount;
    }

    // Méthode pour compter le nombre de tentatives utilisées
    public void incrementAttemptsCount() {
        attemptsCount++;
    }

    public void updateMatrix(String word, int rowIndex) {
        String wordUpperCase = word.toUpperCase();

        // Vérifier que l'index de ligne est dans les limites de la matrice
        if (rowIndex < 0 || rowIndex >= matrix.size()) {
            throw new IllegalArgumentException("Row index out of bounds");
        }

        List<String> row = matrix.get(rowIndex);

        // Mettre à jour la rangée avec le mot
        for (int i = 0; i < row.size(); i++) {
            if (i < wordUpperCase.length()) {
                row.set(i, String.valueOf(wordUpperCase.charAt(i)));
            } else {
                row.set(i, ""); // Remplir le reste de la rangée avec des chaînes vides
            }
        }
    }

    public void setFirstRow(String word) {
        if (word.length() > matrix.get(0).size()) {
            throw new IllegalArgumentException("Word is too long for the matrix");
        }

        List<String> row = matrix.get(0); // Récupération de la première rangée

        // Placer la première lettre du mot
        row.set(0, String.valueOf(word.charAt(0)));

        // Choisir aléatoirement un autre caractère du mot (autre que le premier)
        Random random = new Random();
        randomIndex = random.nextInt(word.length() - 1) + 1; // +1 pour exclure la première lettre

        // Placer le caractère choisi dans sa position correspondante
        randomChar = String.valueOf(word.charAt(randomIndex));
        if (randomIndex < row.size()) {
            row.set(randomIndex, randomChar);
        }

        // Remplir le reste de la rangée avec des chaînes vides
        for (int i = 1; i < row.size(); i++) {
            if (i != randomIndex) {
                row.set(i, "");
            }
        }
    }

    public void setNextRow(String word) {
        int nextRow = findFirstEmptyRow();
        if (nextRow != -1) {
            if (word.length() > matrix.get(nextRow).size()) {
                throw new IllegalArgumentException("Word is too long for the matrix");
            }

            List<String> row = matrix.get(nextRow); // Récupération de la première rangée

            // Placer la première lettre du mot
            row.set(0, String.valueOf(word.charAt(0)));

            // Placer le caractère choisi dans sa position correspondante
            randomChar = String.valueOf(word.charAt(randomIndex));
            if (randomIndex < row.size()) {
                row.set(randomIndex, randomChar);
            }

            // Remplir le reste de la rangée avec des chaînes vides
            for (int i = 1; i < row.size(); i++) {
                if (i != randomIndex) {
                    row.set(i, "");
                }
            }
        }
    }

    public int findFirstEmptyRow() {
        for (int i = 0; i < matrix.size(); i++) {
            List<String> row = matrix.get(i);
            if (isRowEmpty(row)) {
                return i; // Retourne l'indice de la première ligne vide trouvée
            }
        }
        return -1; // Aucune ligne vide trouvée
    }

    private boolean isRowEmpty(List<String> row) {
        for (String cell : row) {
            if (cell.isEmpty()) {
                return true; // La ligne n'est pas vide
            }
        }
        return false; // La ligne est vide
    }

    public void clearMatrix() {
        for (List<String> row : matrix) {
            for (int i = 0; i < row.size(); i++) {
                row.set(i, "");
            }
        }
    }

    public List<List<String>> getMatrix() {
        return matrix;
    }

    public void setMatrix(List<List<String>> matrix) {
        this.matrix = matrix;
    }

    public List<String> getRow(int rowIndex) {
        return matrix.get(rowIndex);
    }

    public int getLines() {
        return matrix.size();
    }

    public int getRows() {
        if (!matrix.isEmpty()) {
            return matrix.get(0).size();
        }
        return 0;
    }

    public int getRandomIndex() {
        return randomIndex;
    }

    public String getRandomChar() {
        return randomChar;
    }
}
