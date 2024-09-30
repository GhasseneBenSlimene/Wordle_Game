package graphics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import controllers.AppMatrice;
import controllers.GameControls;

public class MotusFrame extends JPanel {
    private static final long serialVersionUID = 1L;
    private JPanel gamePanel;
    private GameControls gameControls;

    public MotusFrame(GameControls gameControls) {
        this.gameControls = gameControls;
        initializeComponents();
        layoutComponents();
        updateGrid(); // Met à jour la grille au démarrage
    }

    private void initializeComponents() {
        // Initialisation des composants
        gamePanel = new JPanel();
    }

    private void layoutComponents() {
        // Disposition des composants dans le JFrame
        setLayout(new BorderLayout());

        gamePanel.setLayout(new GridLayout(gameControls.getMatrice().getLines(), gameControls.getMatrice().getRows()));

        // Créer un JScrollPane contenant le gamePanel
        JScrollPane scrollPane = new JScrollPane(gamePanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateGrid() {
        gamePanel.removeAll(); // Supprimer tous les composants précédents
        AppMatrice matrice = gameControls.getMatrice();
        List<List<String>> matrixData = matrice.getMatrix();
        int cellSize = 50; // Taille de chaque cellule

        for (int rowIndex = 0; rowIndex < matrixData.size(); rowIndex++) {
            List<String> row = matrixData.get(rowIndex);
            for (String cell : row) {
                JLabel label = new JLabel(cell, SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setPreferredSize(new Dimension(cellSize, cellSize));
                label.setFont(new Font("Comic Sans MS", Font.CENTER_BASELINE, 40));

                // Appliquer la couleur bleu ciel pour la première ligne
                if ((rowIndex == 0) && !cell.isEmpty()) {
                    label.setBackground(Color.CYAN);
                    label.setOpaque(true);
                }

                gamePanel.add(label);
            }
        }

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    public void updateRow(int rowIndex) {
        AppMatrice matrice = gameControls.getMatrice();
        if (rowIndex < 0 || rowIndex >= matrice.getLines()) {
            throw new IllegalArgumentException("Row index out of bounds");
        }

        List<String> rowData = matrice.getRow(rowIndex);
        Component[] components = gamePanel.getComponents();

        int start = rowIndex * matrice.getRows();
        int end = start + matrice.getRows();

        for (int i = start; i < end; i++) {
            JLabel label = (JLabel) components[i];
            String cell = rowData.get(i - start);
            label.setText(cell);
        }

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    public void updateRowColors(int rowIndex, List<String> colors) {
        AppMatrice matrice = gameControls.getMatrice();
        if (rowIndex < 0 || rowIndex >= matrice.getLines()) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        if (colors.size() != matrice.getRows()) {
            throw new IllegalArgumentException("Colors list size does not match the number of columns");
        }

        // Mettre à jour les couleurs de la ligne spécifiée
        Component[] components = gamePanel.getComponents();
        int start = rowIndex * matrice.getRows();
        int end = start + matrice.getRows();

        for (int i = start; i < end; i++) {
            JLabel label = (JLabel) components[i];
            String color = colors.get(i - start);
            switch (color.toLowerCase()) {
                case "green":
                    label.setBackground(Color.GREEN);
                    break;
                case "yellow":
                    label.setBackground(Color.YELLOW);
                    break;
                case "red":
                    label.setBackground(Color.RED);
                    break;
                case "cyan":
                    label.setBackground(Color.cyan);
                    break;
                default:
                    label.setBackground(null); // Aucune couleur ou couleur non reconnue
            }
            label.setOpaque(true); // Nécessaire pour que la couleur de fond soit visible
        }
    }

    public void updateRowContentAndColors(String word) {
        // Trouver la première ligne vide
        String currentWord = gameControls.getCurrentWord();
        int rowIndex = gameControls.getMatrice().findFirstEmptyRow();
        if (rowIndex == -1) {
            return;
        }

        // Ajouter le mot à la première ligne vide
        gameControls.getMatrice().updateMatrix(word, rowIndex);

        // Mise à jour de la ligne dans l'interface utilisateur
        updateRow(rowIndex);

        // Obtenir la comparaison entre le mot proposé et le mot à deviner
        List<String> colors = gameControls.compareWords(word);

        // Mise à jour des couleurs de la ligne
        updateRowColors(rowIndex, colors);
        if (!word.equalsIgnoreCase(currentWord)){
            setNextLine();
        }
    }

    public void setNextLine() {
        String word = gameControls.getCurrentWord();
        int rowIndex = gameControls.getMatrice().findFirstEmptyRow();
        if (rowIndex != -1) {
            gameControls.getMatrice().setNextRow(word);
            updateRow(rowIndex);
            List<String> colors = new ArrayList<>();
            colors.add("cyan");
            for (int i = 1; i < word.length(); i++) {
                if (i == gameControls.getMatrice().getRandomIndex()) {
                    colors.add("cyan");
                    continue;
                }
                colors.add("");
            }
            updateRowColors(rowIndex, colors);
        }
    }

    public void showNotification(String message) {
        // Afficher une notification ou un message
        JOptionPane.showMessageDialog(this, message);
    }

}
