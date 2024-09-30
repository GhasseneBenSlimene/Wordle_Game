package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameControls {

    // Attributs
    private String currentWord; // Le mot actuel à deviner
    private AppDictionnary dictionnary; // Référence au dictionnaire pour obtenir des mots
    private int countdownDuration; // Durée de la minuterie en secondes
    private AppMatrice matrice;
    private volatile boolean isRunning = true; // utilisé pour le timer
    private GameControlsListener listener; // pour informer MainFrame que le timer attaint 0

    public interface GameControlsListener {
        void onCountdownFinished();

        void onGameWon();

        void onGameLost();
    }

    public void setCountdownListener(GameControlsListener listener) {
        this.listener = listener;
    }

    public void setGameWonListener(GameControlsListener listener) {
        this.listener = listener;
    }

    public void setGameLostListener(GameControlsListener listener) {
        this.listener = listener;
    }

    // Constructeur
    public GameControls(AppDictionnary dictionnary) {
        this.dictionnary = dictionnary;
        this.countdownDuration = 60; // Valeur par défaut, vous pouvez ajuster selon vos besoins
    }

    // Méthodes

    // Méthode pour démarrer la minuterie
    public void startCountdown(int initialDuration) {
        this.countdownDuration = initialDuration;
        isRunning = true;
        new Thread(() -> {
            try {
                while (countdownDuration > 0 && isRunning) {
                    TimeUnit.SECONDS.sleep(1);
                    countdownDuration--;
                }
                if (listener != null && countdownDuration == 0) {
                    listener.onCountdownFinished();
                }
            } catch (InterruptedException e) {
                // Handle exception
            }
        }).start();
    }

    public void stopCountdown() {
        isRunning = false;
    }

    public int getCountdownTime() {
        return countdownDuration;
    }

    // Méthodes
    public void generateRandomWord(int wordLength) {
        // Utiliser la méthode du dictionnaire pour obtenir un mot aléatoire de la
        // taille spécifiée
        this.currentWord = dictionnary.getRandomWord(wordLength).toUpperCase();
    }

    public boolean validateAttempt(String attempt) {
        int randomIndex = matrice.getRandomIndex();
        return attempt.matches("[a-zA-Z]{" + this.currentWord.length() + "}") &&
                attempt.charAt(randomIndex) == currentWord.charAt(randomIndex) &&
                attempt.charAt(0) == currentWord.charAt(0);
    }

    public List<String> compareWords(String attempt) {
        List<String> feedback = new ArrayList<>();
        int lc = currentWord.length();
        int la = attempt.length();
        boolean[] usedInWord = new boolean[lc]; // Utilisé dans le mot actuel
        boolean[] usedInAttempt = new boolean[lc]; // Utilisé dans la tentative

        // Première itération pour les caractères bien placés (green)
        int greenCount = 0; // compter les valeurs vert
        for (int i = 0; i < Math.min(lc, la); i++) {
            char c = attempt.charAt(i);
            if (Character.toLowerCase(c) == Character.toLowerCase(currentWord.charAt(i))) {
                feedback.add("green");
                greenCount++;
                usedInWord[i] = true;
                usedInAttempt[i] = true;
            } else {
                feedback.add(""); // Ajouter une chaîne vide pour les indices non vérifiés
            }
        }

        // Deuxième itération pour les caractères mal placés (yellow) et non trouvés
        // (red)
        for (int i = 0; i < Math.min(lc, la); i++) {
            if (!usedInAttempt[i]) { // Si pas déjà utilisé pour un "green"
                char c = attempt.charAt(i);
                boolean found = false;
                for (int j = 0; j < lc; j++) {
                    if (!usedInWord[j] && Character.toLowerCase(c) == Character.toLowerCase(currentWord.charAt(j))) {
                        feedback.set(i, "yellow");
                        usedInWord[j] = true;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    feedback.set(i, "red");
                }
            }
        }

        // Si la tentative est plus courte que le mot, ajouter du "red" pour les indices
        // manquants
        while (feedback.size() < lc) {
            feedback.add("red");
        }

        if (greenCount == currentWord.length()) {
            new Thread(() -> listener.onGameWon()).start();
            stopCountdown();
        }

        if (matrice.findFirstEmptyRow() == -1 && greenCount != currentWord.length()) {
            new Thread(() -> listener.onGameLost()).start();
            stopCountdown();
        }

        matrice.incrementAttemptsCount(); // Incremente le nombre de tentatives

        return feedback;
    }

    public int getAttemptsCount() {
        return matrice.getAttemptsCount();
    }

    // Méthode pour initialiser le jeu et la matrice
    public void initializeGame(int wordLength, int attemptsNb, int seconds) {
        startCountdown(seconds);
        generateRandomWord(wordLength);
        String word = getCurrentWord();
        System.out.println(currentWord);

        this.matrice = new AppMatrice(attemptsNb, wordLength);
        this.matrice.setFirstRow(word.toUpperCase());
    }

    // Méthode pour ajouter un mot dans la première ligne vide
    public void addWordToFirstEmptyRow(String word) {
        int emptyRowIndex = this.matrice.findFirstEmptyRow();
        if (emptyRowIndex != -1) {
            this.matrice.updateMatrix(word, emptyRowIndex);
        } else {

        }
    }

    public static float calculateScore(String word, int attempts) {
        // score = (wordLength * someFactor) + (attemptsRemaining * someOtherFactor)
        int wordLength = word.length();
        int someFactor = 3;
        int someOtherFactor = 2;
        return (float) (wordLength * someFactor) / (attempts * someOtherFactor);
    }

    public void resetGame() {
        stopCountdown();
        matrice.clearMatrix();
    }

    // Getter pour obtenir la matrice (si nécessaire)
    public AppMatrice getMatrice() {
        return this.matrice;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public AppDictionnary getDictionnary() {
        return dictionnary;
    }

    public void setDictionnary(AppDictionnary dictionnary) {
        this.dictionnary = dictionnary;
    }
}
