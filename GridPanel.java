
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GridPanel extends JPanel {

    // GRID SETTINGS
    private int rows = 20;
    private int cols = 20;
    private int tileSize = 25;

    // Stores which cells are alive
    private boolean[][] grid = new boolean[rows][cols];

    // SIMULATION TIMER
    private Timer timer;

    // CELL COLORS
    private Color aliveColor = Color.WHITE;
    private Color deadColor = Color.DARK_GRAY;

    // CONSTRUCTOR
    public GridPanel() {

        setBackground(Color.BLACK);

        updatePanelSize();

        // Timer ticks every 500 milliseconds
        timer = new Timer(500, e -> {
            updateGrid();
            repaint();
        });

        // Timer does NOT start automatically

        // Mouse interaction
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                toggleCell(e.getX(), e.getY());
            }
        });
    }

    // CALCULATE GRID PANEL SIZE
    private void updatePanelSize() {

        int width = cols * tileSize;
        int height = rows * tileSize;

        setPreferredSize(new Dimension(width, height));

        revalidate();
        repaint();
    }

    // CHANGE GRID DIMENSIONS
    public void setGridSize(int newRows, int newCols) {

        stopSimulation();

        rows = newRows;
        cols = newCols;

        // Reset cells when dimensions change
        grid = new boolean[rows][cols];

        updatePanelSize();
    }

    // CHANGE TILE SIZE
    public void setTileSize(int newSize) {

        tileSize = newSize;

        updatePanelSize();
    }

    // START SIMULATION
    public void startSimulation() {
        timer.start();
    }

    // STOP SIMULATION
    public void stopSimulation() {
        timer.stop();
    }

    // CHECK SIMULATION STATUS
    public boolean isSimulationRunning() {
        return timer.isRunning();
    }

    // CLEAR ALL CELLS
    public void clearGrid() {

        stopSimulation();

        grid = new boolean[rows][cols];

        repaint();
    }

    // TOGGLE A CELL WITH THE MOUSE
    private void toggleCell(int mouseX, int mouseY) {

        // Editing is allowed only when stopped
        if (timer.isRunning()) {
            return;
        }

        int col = mouseX / tileSize;
        int row = mouseY / tileSize;

        // Check valid coordinates
        if (row < 0 || row >= rows ||
            col < 0 || col >= cols) {
            return;
        }

        // Alive becomes dead, dead becomes alive
        grid[row][col] = !grid[row][col];

        repaint();
    }

    // COUNT LIVING NEIGHBORS
    private int countNeighbors(int row, int col) {

        int count = 0;

        // Check surrounding 3x3 area
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                // Don't count the cell itself
                if (dr == 0 && dc == 0) {
                    continue;
                }

                int newRow = row + dr;
                int newCol = col + dc;

                // Check boundaries
                if (newRow >= 0 && newRow < rows &&
                    newCol >= 0 && newCol < cols) {

                    if (grid[newRow][newCol]) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    // APPLY CONWAY'S GAME OF LIFE RULES
    private void updateGrid() {

        boolean[][] nextGrid =
            new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                int neighbors =
                    countNeighbors(row, col);

                if (grid[row][col]) {

                    // Living cell survives with 2 or 3
                    nextGrid[row][col] =
                        neighbors == 2 || neighbors == 3;

                } else {

                    // Dead cell becomes alive with 3
                    nextGrid[row][col] =
                        neighbors == 3;
                }
            }
        }

        // Replace old generation with new
        grid = nextGrid;
    }

    // DRAW THE GRID
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                // Choose color
                if (grid[row][col]) {
                    g.setColor(aliveColor);
                } else {
                    g.setColor(deadColor);
                }

                // Calculate position
                int x = col * tileSize;
                int y = row * tileSize;

                // Draw tile with 1px gap
                g.fillRect(
                    x,
                    y,
                    tileSize - 1,
                    tileSize - 1
                );
            }
        }
    }
}
