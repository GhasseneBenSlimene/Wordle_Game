package test;

import controllers.*;
import utilities.*;
import graphics.*;
import javax.swing.SwingUtilities;

public class AppMain {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        AppDictionnary dictionnary = new AppDictionnary();
        
        if (DatabaseManager.isTableEmpty("mots")) {
            dictionnary.FileToDb("./words.txt");
        }

        dictionnary.loadWords();
        SwingUtilities.invokeLater(() -> new MainFrame(dictionnary).setVisible(true));
    }
}
