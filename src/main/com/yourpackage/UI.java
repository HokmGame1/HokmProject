package com.yourpackage;

import javax.swing.*;

public class UI {
    public void createAndShowLoginSignUpPage() {
        SwingUtilities.invokeLater(() -> new com.yourpackage.LoginSignUpPage().createAndShowGUI());
    }
}