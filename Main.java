package Controleur;

import Vue.FenetrePrincipale;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FenetrePrincipale::new);
    }
}
