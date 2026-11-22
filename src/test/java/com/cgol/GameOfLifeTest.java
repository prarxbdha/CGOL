package com.cgol;

import com.cgol.model.GameOfLife;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameOfLifeTest {

    @Test
    void startsEmpty() {
        GameOfLife game = new GameOfLife(20, 20);
        assertEquals(20, game.getRows());
        assertEquals(20, game.getCols());
        assertEquals(0, game.getGeneration());
        assertEquals(0, game.getLivingCellCount());
    }

    @Test
    void rejectsInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new GameOfLife(0, 10));
        assertThrows(IllegalArgumentException.class, () -> new GameOfLife(10, 0));
    }

    @Test
    void toggleAndBoundsAreSafe() {
        GameOfLife game = new GameOfLife(4, 4);
        game.toggle(1, 1);
        assertTrue(game.isAlive(1, 1));
        assertEquals(1, game.getLivingCellCount());
        game.toggle(1, 1);
        assertFalse(game.isAlive(1, 1));

        game.setAlive(-1, 0, true);
        game.setAlive(0, 99, true);
        game.toggle(4, 4);
        assertEquals(0, game.getLivingCellCount());
    }

    @Test
    void underpopulationKillsIsolatedCells() {
        GameOfLife game = new GameOfLife(10, 10);
        game.setAlive(1, 1, true);
        game.setAlive(5, 5, true);
        game.nextGeneration();
        assertEquals(0, game.getLivingCellCount());
        assertEquals(1, game.getGeneration());
    }

    @Test
    void blinkerOscillates() {
        GameOfLife game = new GameOfLife(10, 10);
        game.setAlive(5, 4, true);
        game.setAlive(5, 5, true);
        game.setAlive(5, 6, true);

        game.nextGeneration();
        assertFalse(game.isAlive(5, 4));
        assertTrue(game.isAlive(4, 5));
        assertTrue(game.isAlive(5, 5));
        assertTrue(game.isAlive(6, 5));
        assertEquals(3, game.getLivingCellCount());

        game.nextGeneration();
        assertTrue(game.isAlive(5, 4));
        assertTrue(game.isAlive(5, 5));
        assertTrue(game.isAlive(5, 6));
        assertFalse(game.isAlive(4, 5));
        assertEquals(3, game.getLivingCellCount());
        assertEquals(2, game.getGeneration());
    }

    @Test
    void blockIsStillLife() {
        GameOfLife game = new GameOfLife(10, 10);
        game.setAlive(2, 2, true);
        game.setAlive(2, 3, true);
        game.setAlive(3, 2, true);
        game.setAlive(3, 3, true);

        game.nextGeneration();
        assertEquals(4, game.getLivingCellCount());
        assertTrue(game.isAlive(2, 2) && game.isAlive(2, 3)
            && game.isAlive(3, 2) && game.isAlive(3, 3));
    }

    @Test
    void overpopulationKillsCrowdedCells() {
        GameOfLife game = new GameOfLife(10, 10);
        for (int row = 4; row <= 6; row++) {
            for (int col = 4; col <= 6; col++) {
                game.setAlive(row, col, true);
            }
        }

        game.nextGeneration();
        assertFalse(game.isAlive(5, 5));
        assertFalse(game.isAlive(4, 5));
        assertTrue(game.isAlive(4, 4) && game.isAlive(4, 6)
            && game.isAlive(6, 4) && game.isAlive(6, 6));
        assertTrue(game.isAlive(3, 5) && game.isAlive(5, 3)
            && game.isAlive(5, 7) && game.isAlive(7, 5));
        assertEquals(8, game.getLivingCellCount());
    }

    @Test
    void resizePreservesOverlapAndAddsDeadCells() {
        GameOfLife game = new GameOfLife(4, 4);
        game.setAlive(2, 2, true);
        game.setAlive(3, 3, true);

        game.resize(6, 3);
        assertEquals(6, game.getRows());
        assertEquals(3, game.getCols());
        assertTrue(game.isAlive(2, 2));
        assertFalse(game.isAlive(3, 3));
        assertEquals(1, game.getLivingCellCount());
    }

    @Test
    void clearResetsCellsAndGeneration() {
        GameOfLife game = new GameOfLife(8, 8);
        game.setAlive(1, 1, true);
        game.nextGeneration();
        game.clear();
        assertEquals(0, game.getGeneration());
        assertEquals(0, game.getLivingCellCount());
    }

    @Test
    void neighborCountUsesFiniteBoundaries() {
        GameOfLife game = new GameOfLife(3, 3);
        game.setAlive(0, 1, true);
        game.setAlive(1, 0, true);
        game.setAlive(1, 2, true);
        assertEquals(2, game.countNeighbors(0, 0));
        assertEquals(1, game.countNeighbors(2, 2));
    }
}
