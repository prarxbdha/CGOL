package com.cgol.model;

public class GameOfLife {

    private int rows;
    private int cols;
    private boolean[][] cells;
    private int generation;

    public GameOfLife(int rows, int cols) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException("Grid dimensions must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.cells = new boolean[rows][cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getGeneration() {
        return generation;
    }

    public boolean isAlive(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && cells[row][col];
    }

    public void setAlive(int row, int col, boolean alive) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            cells[row][col] = alive;
        }
    }

    public void toggle(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            cells[row][col] = !cells[row][col];
        }
    }

    public int getLivingCellCount() {
        int count = 0;
        for (boolean[] line : cells) {
            for (boolean cell : line) {
                if (cell) {
                    count++;
                }
            }
        }
        return count;
    }

    public void clear() {
        cells = new boolean[rows][cols];
        generation = 0;
    }

    public void resize(int newRows, int newCols) {
        if (newRows < 1 || newCols < 1) {
            throw new IllegalArgumentException("Grid dimensions must be positive");
        }
        boolean[][] resized = new boolean[newRows][newCols];
        for (int row = 0; row < Math.min(newRows, rows); row++) {
            int shared = Math.min(newCols, cols);
            System.arraycopy(cells[row], 0, resized[row], 0, shared);
        }
        rows = newRows;
        cols = newCols;
        cells = resized;
    }

    public int countNeighbors(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                if (isAlive(row + dr, col + dc)) {
                    count++;
                }
            }
        }
        return count;
    }

    public void nextGeneration() {
        boolean[][] next = new boolean[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int neighbors = countNeighbors(row, col);
                if (cells[row][col]) {
                    next[row][col] = neighbors == 2 || neighbors == 3;
                } else {
                    next[row][col] = neighbors == 3;
                }
            }
        }
        cells = next;
        generation++;
    }
}
