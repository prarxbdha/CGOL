import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class GridApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GridApp::createWindow);
    }

    private static void createWindow() {
        JFrame frame = new JFrame("Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Theme.APP_BACKGROUND);

        GridPanel gridPanel = new GridPanel();
        SettingsPanel settingsPanel = new SettingsPanel(gridPanel);

        CenteringPanel canvas = new CenteringPanel(gridPanel);
        canvas.setBackground(gridPanel.getCanvasBackground());

        JScrollPane canvasScroll = new JScrollPane(canvas);
        canvasScroll.setBorder(null);
        canvasScroll.getViewport().setBackground(gridPanel.getCanvasBackground());
        canvasScroll.getVerticalScrollBar().setUI(new MinimalScrollBarUI());
        canvasScroll.getHorizontalScrollBar().setUI(new MinimalScrollBarUI());
        canvasScroll.getVerticalScrollBar().setUnitIncrement(24);
        canvasScroll.getHorizontalScrollBar().setUnitIncrement(24);
        canvasScroll.getVerticalScrollBar().setBackground(gridPanel.getCanvasBackground());
        canvasScroll.getHorizontalScrollBar().setBackground(gridPanel.getCanvasBackground());

        gridPanel.addPropertyChangeListener("canvasBackground", e -> {
            Color color = (Color) e.getNewValue();
            canvas.setBackground(color);
            canvasScroll.getViewport().setBackground(color);
            canvasScroll.getVerticalScrollBar().setBackground(color);
            canvasScroll.getHorizontalScrollBar().setBackground(color);
            canvas.repaint();
            canvasScroll.getViewport().repaint();
        });

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(Theme.APP_BACKGROUND);
        mainArea.add(canvasScroll, BorderLayout.CENTER);
        mainArea.add(createStatsBar(gridPanel), BorderLayout.SOUTH);

        frame.setLayout(new BorderLayout());
        frame.add(mainArea, BorderLayout.CENTER);
        frame.add(settingsPanel, BorderLayout.EAST);

        frame.setSize(1100, 720);
        frame.setMinimumSize(new Dimension(760, 520));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JComponent createStatsBar(GridPanel gridPanel) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bar.setBackground(Theme.APP_BACKGROUND);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        JLabel generation = statLabel("GENERATION  " + gridPanel.getGeneration());
        JLabel living = statLabel("LIVING CELLS  " + gridPanel.getLivingCellCount());
        gridPanel.addPropertyChangeListener("generation",
            e -> generation.setText("GENERATION  " + e.getNewValue()));
        gridPanel.addPropertyChangeListener("livingCells",
            e -> living.setText("LIVING CELLS  " + e.getNewValue()));

        bar.add(generation);
        bar.add(Box.createHorizontalStrut(24));
        bar.add(living);
        return bar;
    }

    private static JLabel statLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_SMALL);
        label.setForeground(Theme.MUTED_TEXT);
        return label;
    }
}
