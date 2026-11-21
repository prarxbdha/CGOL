
import javax.swing.*;
import java.awt.*;

public class GridApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Create the application window
            JFrame frame = new JFrame("Conway's Game of Life");

            // Create the grid
            GridPanel gridPanel = new GridPanel();

            // Create the settings sidebar
            SettingsPanel settingsPanel =
                new SettingsPanel(gridPanel);

            // Put grid inside a scrollable area
            JScrollPane scrollPane =
                new JScrollPane(gridPanel);

            scrollPane.getViewport().setBackground(Color.BLACK);

            // Arrange the application
            frame.setLayout(new BorderLayout());

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(settingsPanel, BorderLayout.EAST);

            // Window settings
            frame.setSize(900, 650);
            frame.setResizable(true);
            frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
            );

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
