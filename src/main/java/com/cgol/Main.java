package com.cgol;

import com.cgol.ui.MainWindow;

import javax.swing.SwingUtilities;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::show);
    }
}
