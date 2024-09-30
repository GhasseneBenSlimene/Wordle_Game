package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class DifficultySelection extends JPanel {
    private JRadioButton easyButton;
    private JRadioButton mediumButton;
    private JRadioButton hardButton;
    private JRadioButton customButton;
    private JTextField attemptsField;
    private JTextField wordLengthField;
    private JTextField durationField;
    private JTextField playerNameField;
    private JButton startButton;
    private JButton quitButton;
    private ButtonGroup difficultyGroup;
    private JLabel customRemarkLabel;

    public DifficultySelection(ActionListener startGameListener) {
        createUI(startGameListener);
    }

    private void createUI(ActionListener startGameListener) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        customRemarkLabel = new JLabel("La longueur du mot doit être comprise entre 7 et 15.");
        customRemarkLabel.setForeground(Color.BLUE);
        customRemarkLabel.setVisible(false); // Initially not visible
        gbc.gridx = 0;
        gbc.gridy = 5; // Adjust as needed to position above the buttons
        gbc.gridwidth = 2; // Nombre de colonnes occupées
        add(customRemarkLabel, gbc);
        customRemarkLabel.setVisible(true);

        // Top Section
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel("Jeu Motus");
        topPanel.add(titleLabel);
        gbc.gridx = 0; // Colonne
        gbc.gridy = 0; // Ligne
        gbc.gridwidth = 2; // Nombre de colonnes occupées
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Marges extérieures
        add(topPanel, gbc);

        // Middle Section for difficulty selection
        easyButton = new JRadioButton("Facile", true);
        mediumButton = new JRadioButton("Medium");
        hardButton = new JRadioButton("Difficile");
        customButton = new JRadioButton("Personnalisé");

        // Coleur
        easyButton.setBackground(Color.GREEN);
        mediumButton.setBackground(Color.YELLOW);
        hardButton.setBackground(Color.RED);
        customButton.setBackground(Color.CYAN);

        // Group the radio buttons.
        difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyButton);
        difficultyGroup.add(mediumButton);
        difficultyGroup.add(hardButton);
        difficultyGroup.add(customButton);

        // Radio buttons
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(easyButton, gbc);

        gbc.gridy = 2;
        add(mediumButton, gbc);

        gbc.gridy = 3;
        add(hardButton, gbc);

        gbc.gridy = 4;
        add(customButton, gbc);

        // Game Options
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 0, 15));

        JPanel playerNamePanel = new JPanel(new BorderLayout());
        playerNamePanel.add(new JLabel("Pseudo du joueur:"), BorderLayout.NORTH);
        playerNameField = new JTextField("");
        playerNamePanel.add(playerNameField, BorderLayout.SOUTH);
        optionsPanel.add(playerNamePanel);

        JPanel attemptsPanel = new JPanel(new BorderLayout());
        attemptsPanel.add(new JLabel("Nombre de tentatives:"), BorderLayout.NORTH);
        attemptsField = new JTextField("");
        attemptsPanel.add(attemptsField, BorderLayout.SOUTH);
        optionsPanel.add(attemptsPanel);

        JPanel wordLengthPanel = new JPanel(new BorderLayout());
        wordLengthPanel.add(new JLabel("Longueur du mot:"), BorderLayout.NORTH);
        wordLengthField = new JTextField("");
        wordLengthPanel.add(wordLengthField, BorderLayout.SOUTH);
        optionsPanel.add(wordLengthPanel);

        JPanel durationPanel = new JPanel(new BorderLayout());
        durationPanel.add(new JLabel("Durée (secondes):"), BorderLayout.NORTH);
        durationField = new JTextField("");
        durationPanel.add(durationField, BorderLayout.SOUTH);
        optionsPanel.add(durationPanel);

        customButton.setSelected(true);

        gbc.gridx = 1; // Colonne
        gbc.gridy = 1; // Ligne
        gbc.gridheight = 4; // Nombre de lignes occupées
        gbc.fill = GridBagConstraints.BOTH;
        add(optionsPanel, gbc);

        // Bottom Section for action buttons
        JPanel bottomPanel = new JPanel();
        startButton = new JButton("Démarrer");
        startButton.setMargin(new Insets(10, 10, 10, 10)); // Ajoute un padding de 10 pixels de chaque côté

        quitButton = new JButton("Quitter");
        quitButton.setMargin(new Insets(10, 10, 10, 10)); // Ajoute un padding de 10 pixels de chaque côté
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text1 = wordLengthField.getText();
                String text2 = attemptsField.getText();
                String text3 = durationField.getText();
                String text4 = playerNameField.getText();
                if (!text1.isEmpty() && !text2.isEmpty() && !text3.isEmpty() && !text4.isEmpty()) {
                    try {
                        int wordLength;
                        if (easyButton.isSelected()) {
                            wordLength = getRandomNumber(7, 9);
                        } else if (mediumButton.isSelected()) {
                            wordLength = getRandomNumber(10, 12);
                        } else if (hardButton.isSelected()) {
                            wordLength = getRandomNumber(13, 15);
                        } else {
                            wordLength = Integer.parseInt(wordLengthField.getText());
                        }
                        if (wordLength > 15 || wordLength < 7) {
                            wordLengthField.setText("");
                            customRemarkLabel.setText(
                                    "<html><font color='red'>La longueur du mot doit être comprise entre 7 et 15.</font></html>");
                        } else
                            startGameListener.actionPerformed(e);
                    } catch (NumberFormatException ex) {
                        customRemarkLabel.setText(
                                "<html><font color='red'>Veuillez entrer des nombres valides.</font></html>");
                    }
                }
            }
        });
        quitButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(startButton);
        bottomPanel.add(quitButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottomPanel, gbc);

        // Action listeners for radio buttons
        ActionListener radioListener = e -> {
            boolean isCustom = customButton.isSelected();
            attemptsField.setEditable(isCustom);
            wordLengthField.setEditable(isCustom);
            durationField.setEditable(isCustom);
            playerNameField.setEditable(true);
            customRemarkLabel.setVisible(isCustom);

            if (!isCustom) {
                if (easyButton.isSelected()) {
                    attemptsField.setText("8");
                    wordLengthField.setText("7-9");
                    durationField.setText("120");
                } else if (mediumButton.isSelected()) {
                    attemptsField.setText("7");
                    wordLengthField.setText("10 - 12");
                    durationField.setText("90");
                } else if (hardButton.isSelected()) {
                    attemptsField.setText("6");
                    wordLengthField.setText("13 - 15");
                    durationField.setText("60");
                }
                playerNameField.setText("");
            } else {
                attemptsField.setText("");
                wordLengthField.setText("");
                durationField.setText("");
                playerNameField.setText("");
            }
        };

        easyButton.addActionListener(radioListener);
        mediumButton.addActionListener(radioListener);
        hardButton.addActionListener(radioListener);
        customButton.addActionListener(radioListener);
    }

    public int getWordLength() {
        if (easyButton.isSelected()) {
            return getRandomNumber(7, 9);
        } else if (mediumButton.isSelected()) {
            return getRandomNumber(10, 12);
        } else if (hardButton.isSelected()) {
            return getRandomNumber(13, 15);
        } else {
            return Integer.parseInt(wordLengthField.getText());
        }
    }

    public int getAttempts() {
        return Integer.parseInt(attemptsField.getText());
    }

    public int getDuration() {
        return Integer.parseInt(durationField.getText());
    }

    public String getPlayerName() {
        return playerNameField.getText();
    }

    private int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
