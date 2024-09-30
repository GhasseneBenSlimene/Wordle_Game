package graphics;

import javax.swing.*;
import controllers.*;
import utilities.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements PropFrame.ButtonActionListener, GameControls.GameControlsListener {

    private MotusFrame motusFrame;
    private PropFrame propFrame;
    private JSplitPane splitPane;
    private DifficultySelection difficultySelection;
    private GameControls gameControls;
    private DatabaseManager databaseManager;
    private float score = 0;

    public MainFrame(AppDictionnary dictionnary) {
        this.gameControls = new GameControls(dictionnary);
        this.databaseManager = new DatabaseManager();
        difficultySelection = new DifficultySelection(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Initialiser les composants
        initComponents();
        // Définir le titre de la fenêtre
        setTitle("Jeu Motus");
    }

    // listener action

    @Override
    public void onRestartActionPerformed(ActionEvent evt) {
        if (askToRestartGame()) {
            restartGame();
        }
    }

    @Override
    public void onCountdownFinished() {
        propFrame.setTextField2Editable(false);
        String word = gameControls.getCurrentWord();
        int attempts = gameControls.getAttemptsCount();
        String pseudo = difficultySelection.getPlayerName();

        // Gestionnaire de base de données pour enregistrer le résultat
        databaseManager.addGameResult(word, attempts, pseudo, score);
        showTimeUpNotification();
        if (askToRestartGame()) {
            restartGame();
        }
    }

    @Override
    public void onGameWon() {
        propFrame.setTextField2Editable(false);
        saveGameResultToDatabase();
        showCongratulationsNotification();
        if (askToRestartGame()) {
            restartGame();
        }
    }

    private void saveGameResultToDatabase() {
        String word = gameControls.getCurrentWord();
        int attempts = gameControls.getAttemptsCount();
        String pseudo = difficultySelection.getPlayerName();

        score = GameControls.calculateScore(word, attempts);

        // Gestionnaire de base de données pour enregistrer le résultat
        databaseManager.addGameResult(word, attempts, pseudo, score);
    }

    @Override
    public void onGameLost() {
        propFrame.setTextField2Editable(false);
        String word = gameControls.getCurrentWord();
        int attempts = gameControls.getAttemptsCount();
        String pseudo = difficultySelection.getPlayerName();

        float score = 0;

        // Gestionnaire de base de données pour enregistrer le résultat
        databaseManager.addGameResult(word, attempts, pseudo, score);
        showOutOfAttemptsNotification(gameControls.getCurrentWord());
        if (askToRestartGame()) {
            restartGame();
        }
    }

    public void restartGame() {
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().repaint();
        gameControls.resetGame();
        difficultySelection = new DifficultySelection(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Initialiser les composants
        initComponents();
    }

    private void initComponents() {
        // Configurer la JFrame principale
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(450, 400); // Ajuster selon vos besoins
        setLocationRelativeTo(null); // Pour centrer la fenêtre
        add(difficultySelection, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void startGame() {
        // Retrieve the selected values
        int wordLength = difficultySelection.getWordLength();
        int attempts = difficultySelection.getAttempts();
        int duration = difficultySelection.getDuration();

        // Remove the difficultySelection panel
        remove(difficultySelection);

        // Initialize gameControls with the retrieved values
        gameControls.initializeGame(wordLength, attempts, duration);

        // Now create the MotusFrame and PropFrame since we have initialized the game
        this.motusFrame = new MotusFrame(gameControls);
        this.propFrame = new PropFrame(gameControls, motusFrame);

        // set listeners
        propFrame.setButtonActionListener(this);
        gameControls.setCountdownListener(this);
        gameControls.setGameWonListener(this);
        gameControls.setGameLostListener(this);

        // Configurer le JSplitPane
        int currentHeight = propFrame.getPreferredSize().height;
        propFrame.setPreferredSize(new Dimension(350, currentHeight));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, motusFrame, propFrame);
        splitPane.setDividerLocation(0.8); // Définir la position du séparateur

        // Add the JSplitPane to the JFrame
        add(splitPane, BorderLayout.CENTER);
        pack(); // Ajuster la taille du JFrame pour s'adapter à la taille des composants
        setLocationRelativeTo(null); // Pour centrer la fenêtre
    }

    // Method to show "Time's Up" notification
    public void showTimeUpNotification() {
        JOptionPane.showMessageDialog(this,
                "Temps épuisé, votre score est 0",
                "Fin",
                JOptionPane.ERROR_MESSAGE);
    }

    // Method to ask the user if they want to restart the game
    public boolean askToRestartGame() {
        int response = JOptionPane.showConfirmDialog(this,
                "Voulez-vous recommencer une autre partie",
                "Décision",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return response == JOptionPane.YES_OPTION;
    }

    // Method to show "Run out of attempts" notification with the word to be found
    public void showOutOfAttemptsNotification(String wordToFind) {
        String message = "<html>Vous avez épuisé vos essais, la chaîne recherchée est <b><font color='green'>"
                + wordToFind + "</font></b></html> Votre score est 0";
        JOptionPane.showMessageDialog(this,
                message,
                "Fin",
                JOptionPane.WARNING_MESSAGE);
    }

    // Method to show "Congratulations" notification
    public void showCongratulationsNotification() {
        JOptionPane.showMessageDialog(this,
                "Félicitation, votre score est "+score,
                "Bravo",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
