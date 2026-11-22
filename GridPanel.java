import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public class GridPanel extends JPanel {

    private int rows = 20;
    private int cols = 20;
    private int cellSize = 25;
    private int cellGap = 2;
    private int tickInterval = 500;

    private boolean[][] grid;
    private final Timer timer;

    private Color aliveColor = Theme.ACCENT;
    private Color deadColor = Theme.DEAD_CELL;
    private Color canvasColor = Theme.CANVAS_BACKGROUND;

    private int generation = 0;
    private int livingCells = 0;

    private Point hoveredCell;
    private boolean painting;
    private boolean paintAlive;

    private Shape cellShape;

    public GridPanel() {
        grid = new boolean[rows][cols];
        setBackground(canvasColor);
        rebuildCellShape();
        updatePanelSize();

        timer = new Timer(tickInterval, e -> advanceGeneration());
        timer.setInitialDelay(tickInterval);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                beginPaint(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                continuePaint(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                painting = false;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (!timer.isRunning()) {
                    updateHover(cellAt(e));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                painting = false;
                updateHover(null);
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getTickInterval() {
        return tickInterval;
    }

    public int getGeneration() {
        return generation;
    }

    public int getLivingCellCount() {
        return livingCells;
    }

    public Color getAliveColor() {
        return aliveColor;
    }

    public Color getDeadColor() {
        return deadColor;
    }

    public Color getCanvasBackground() {
        return canvasColor;
    }

    public boolean isSimulationRunning() {
        return timer.isRunning();
    }

    public void setGridSize(int newRows, int newCols) {
        pauseSimulation();

        boolean[][] resized = new boolean[newRows][newCols];
        for (int row = 0; row < Math.min(newRows, grid.length); row++) {
            int shared = Math.min(newCols, grid[row].length);
            System.arraycopy(grid[row], 0, resized[row], 0, shared);
        }

        rows = newRows;
        cols = newCols;
        grid = resized;
        painting = false;
        updateHover(null);
        updatePanelSize();
        updateStats();
    }

    public void setCellSize(int newSize) {
        if (cellSize == newSize) {
            return;
        }
        cellSize = newSize;
        painting = false;
        updateHover(null);
        rebuildCellShape();
        updatePanelSize();
    }

    public void setTickInterval(int millis) {
        tickInterval = millis;
        timer.setDelay(millis);
        timer.setInitialDelay(millis);
    }

    public void setAliveColor(Color color) {
        if (color == null || color.equals(aliveColor)) {
            return;
        }
        aliveColor = color;
        repaint();
    }

    public void setDeadColor(Color color) {
        if (color == null || color.equals(deadColor)) {
            return;
        }
        deadColor = color;
        repaint();
    }

    public void setCanvasBackground(Color color) {
        if (color == null || color.equals(canvasColor)) {
            return;
        }
        Color previous = canvasColor;
        canvasColor = color;
        setBackground(color);
        repaint();
        firePropertyChange("canvasBackground", previous, color);
    }

    public void startSimulation() {
        if (timer.isRunning()) {
            return;
        }
        timer.start();
        updateHover(null);
        firePropertyChange("simulationRunning", false, true);
    }

    public void pauseSimulation() {
        if (!timer.isRunning()) {
            return;
        }
        timer.stop();
        repaint();
        firePropertyChange("simulationRunning", true, false);
    }

    public void clearGrid() {
        pauseSimulation();
        grid = new boolean[rows][cols];
        painting = false;
        setGeneration(0);
        updateStats();
        repaint();
    }

    private void rebuildCellShape() {
        cellGap = cellSize >= 14 ? 2 : 1;
        cellShape = Theme.squircle(0, 0, cellSize - cellGap);
    }

    private void updatePanelSize() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        revalidate();
        repaint();
    }

    private void advanceGeneration() {
        boolean[][] next = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int neighbors = countNeighbors(row, col);
                if (grid[row][col]) {
                    next[row][col] = neighbors == 2 || neighbors == 3;
                } else {
                    next[row][col] = neighbors == 3;
                }
            }
        }

        grid = next;
        setGeneration(generation + 1);
        updateStats();
        repaint();
    }

    private int countNeighbors(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int newRow = row + dr;
                int newCol = col + dc;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                        && grid[newRow][newCol]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void updateStats() {
        int count = 0;
        for (boolean[] line : grid) {
            for (boolean cell : line) {
                if (cell) {
                    count++;
                }
            }
        }
        if (count != livingCells) {
            int previous = livingCells;
            livingCells = count;
            firePropertyChange("livingCells", previous, livingCells);
        }
    }

    private void setGeneration(int value) {
        if (generation == value) {
            return;
        }
        int previous = generation;
        generation = value;
        firePropertyChange("generation", previous, generation);
    }

    private Point cellAt(MouseEvent e) {
        int col = e.getX() / cellSize;
        int row = e.getY() / cellSize;
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return new Point(col, row);
    }

    private void beginPaint(MouseEvent e) {
        if (timer.isRunning() || e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        Point cell = cellAt(e);
        if (cell == null) {
            return;
        }
        painting = true;
        paintAlive = !grid[cell.y][cell.x];
        updateHover(cell);
        applyPaint(cell);
    }

    private void continuePaint(MouseEvent e) {
        if (!painting || timer.isRunning()
                || (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0) {
            painting = false;
            return;
        }
        Point cell = cellAt(e);
        updateHover(cell);
        if (cell != null) {
            applyPaint(cell);
        }
    }

    private void applyPaint(Point cell) {
        if (grid[cell.y][cell.x] == paintAlive) {
            return;
        }
        grid[cell.y][cell.x] = paintAlive;
        updateStats();
        repaint();
    }

    private void updateHover(Point cell) {
        if (Objects.equals(cell, hoveredCell)) {
            return;
        }
        hoveredCell = cell;
        setCursor(cell == null
            ? Cursor.getDefaultCursor()
            : Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform origin = g2.getTransform();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                g2.setTransform(origin);
                g2.translate(col * cellSize + cellGap / 2f, row * cellSize + cellGap / 2f);
                g2.setColor(grid[row][col] ? aliveColor : deadColor);
                g2.fill(cellShape);
            }
        }

        if (hoveredCell != null && !timer.isRunning()) {
            g2.setTransform(origin);
            g2.translate(hoveredCell.x * cellSize + cellGap / 2f, hoveredCell.y * cellSize + cellGap / 2f);
            g2.setColor(new Color(aliveColor.getRed(), aliveColor.getGreen(), aliveColor.getBlue(), 150));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(cellShape);
        }

        g2.dispose();
    }
}
