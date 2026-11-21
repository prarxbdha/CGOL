
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    public SettingsPanel(GridPanel gridPanel) {

        setPreferredSize(new Dimension(230, 650));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBackground(new Color(35, 40, 45));

        setBorder(
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        );

        // TITLE
        JLabel title = new JLabel("GAME OF LIFE");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(30));

        // ROWS
        addLabel("Rows");

        JSpinner rowsSpinner = new JSpinner(
            new SpinnerNumberModel(20, 1, 100, 1)
        );

        rowsSpinner.setMaximumSize(new Dimension(190, 30));
        rowsSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(rowsSpinner);

        add(Box.createVerticalStrut(15));

        // COLUMNS
        addLabel("Columns");

        JSpinner colsSpinner = new JSpinner(
            new SpinnerNumberModel(20, 1, 100, 1)
        );

        colsSpinner.setMaximumSize(new Dimension(190, 30));
        colsSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(colsSpinner);

        add(Box.createVerticalStrut(20));

        // TILE SIZE
        addLabel("Tile Size (pixels)");

        JSpinner tileSizeSpinner = new JSpinner(
            new SpinnerNumberModel(25, 5, 60, 1)
        );

        tileSizeSpinner.setMaximumSize(new Dimension(190, 30));
        tileSizeSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(tileSizeSpinner);

        // START BUTTON
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // STOP BUTTON
        JButton stopButton = new JButton("Stop");
        stopButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // GRID SIZE LISTENERS
        rowsSpinner.addChangeListener(e -> {
            gridPanel.setGridSize(
                (int) rowsSpinner.getValue(),
                (int) colsSpinner.getValue()
            );

            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });

        colsSpinner.addChangeListener(e -> {
            gridPanel.setGridSize(
                (int) rowsSpinner.getValue(),
                (int) colsSpinner.getValue()
            );

            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });

        // TILE SIZE LISTENER
        tileSizeSpinner.addChangeListener(e -> {
            gridPanel.setTileSize(
                (int) tileSizeSpinner.getValue()
            );
        });

        add(Box.createVerticalStrut(25));

        // SIMULATION CONTROLS
        addLabel("Simulation");

        startButton.addActionListener(e -> {
            gridPanel.startSimulation();

            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });

        stopButton.addActionListener(e -> {
            gridPanel.stopSimulation();

            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });

        stopButton.setEnabled(false);

        add(startButton);
        add(Box.createVerticalStrut(10));
        add(stopButton);

        add(Box.createVerticalStrut(20));

        // CLEAR BUTTON
        JButton clearButton = new JButton("Clear Grid");
        clearButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        clearButton.addActionListener(e -> {
            gridPanel.clearGrid();

            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });

        add(clearButton);

        add(Box.createVerticalStrut(25));

        // TIMER INFORMATION
        JLabel timerLabel = new JLabel("Tick interval: 500 ms");
        timerLabel.setForeground(Color.LIGHT_GRAY);
        timerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(timerLabel);
    }

    // HELPER FOR LABELS
    private void addLabel(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(label);
        add(Box.createVerticalStrut(5));
    }
}
