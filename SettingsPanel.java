import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class SettingsPanel extends JScrollPane {

    private final MinimalButton startButton;
    private final MinimalButton pauseButton;

    public SettingsPanel(GridPanel gridPanel) {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Theme.APP_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(22, 20, 22, 20));

        setViewportView(content);
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Theme.BORDER));
        setPreferredSize(new Dimension(268, 640));
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUI(new MinimalScrollBarUI());
        getVerticalScrollBar().setUnitIncrement(16);
        getVerticalScrollBar().setBackground(Theme.APP_BACKGROUND);
        getViewport().setBackground(Theme.APP_BACKGROUND);

        JLabel title = new JLabel("GAME OF LIFE");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT);

        JSeparator separator = new JSeparator();
        separator.setForeground(Theme.BORDER);
        separator.setBackground(Theme.BORDER);

        ParameterControl rows = new ParameterControl("Rows", gridPanel.getRows(), 5, 100);
        ParameterControl columns = new ParameterControl("Columns", gridPanel.getCols(), 5, 100);
        ParameterControl cellSize = new ParameterControl("Cell size", gridPanel.getCellSize(), 8, 48);
        ParameterControl tickInterval = new ParameterControl("Tick interval (ms)", gridPanel.getTickInterval(), 50, 2000);

        rows.addListener(v -> gridPanel.setGridSize(rows.getValue(), columns.getValue()));
        columns.addListener(v -> gridPanel.setGridSize(rows.getValue(), columns.getValue()));
        cellSize.addListener(gridPanel::setCellSize);
        tickInterval.addListener(gridPanel::setTickInterval);

        ColorPickerControl alive = new ColorPickerControl("Alive cells", gridPanel.getAliveColor());
        ColorPickerControl dead = new ColorPickerControl("Dead cells", gridPanel.getDeadColor());
        ColorPickerControl canvas = new ColorPickerControl("Canvas", gridPanel.getCanvasBackground());

        alive.addListener(gridPanel::setAliveColor);
        dead.addListener(gridPanel::setDeadColor);
        canvas.addListener(gridPanel::setCanvasBackground);

        startButton = new MinimalButton("Start", MinimalButton.Style.PRIMARY);
        pauseButton = new MinimalButton("Pause", MinimalButton.Style.NEUTRAL);
        MinimalButton clearButton = new MinimalButton("Clear", MinimalButton.Style.NEUTRAL);

        startButton.addActionListener(e -> gridPanel.startSimulation());
        pauseButton.addActionListener(e -> gridPanel.pauseSimulation());
        clearButton.addActionListener(e -> gridPanel.clearGrid());
        pauseButton.setEnabled(false);

        gridPanel.addPropertyChangeListener("simulationRunning", e -> {
            boolean running = (Boolean) e.getNewValue();
            startButton.setEnabled(!running);
            pauseButton.setEnabled(running);
        });

        JPanel secondaryButtons = new JPanel(new GridLayout(1, 2, 8, 0));
        secondaryButtons.setOpaque(false);
        secondaryButtons.add(pauseButton);
        secondaryButtons.add(clearButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addRow(content, gbc, 0, title, 0, 10);
        addRow(content, gbc, 1, separator, 0, 22);
        addRow(content, gbc, 2, rows, 0, 16);
        addRow(content, gbc, 3, columns, 0, 16);
        addRow(content, gbc, 4, cellSize, 0, 16);
        addRow(content, gbc, 5, tickInterval, 0, 24);
        addRow(content, gbc, 6, alive, 0, 8);
        addRow(content, gbc, 7, dead, 0, 8);
        addRow(content, gbc, 8, canvas, 0, 24);
        addRow(content, gbc, 9, startButton, 0, 8);
        addRow(content, gbc, 10, secondaryButtons, 0, 0);

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        gbc.gridy = 11;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(filler, gbc);
    }

    private static void addRow(JPanel content, GridBagConstraints gbc, int row,
                               JComponent component, int top, int bottom) {
        gbc.gridy = row;
        gbc.insets = new Insets(top, 0, bottom, 0);
        content.add(component, gbc);
    }
}
