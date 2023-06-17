package com.my2048.game;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {

    @Test
    void testSetSize() {
        // given
        Controller controller = new Controller(new Model());
        View view = new View(controller);
        int newSize = 4;

        // when
        view.setBoardSize(newSize);

        // then
        assertEquals(newSize, view.getBoardSize());
    }

    @Test
    void testSetTileSize() {
        // given
        Controller controller = new Controller(new Model());
        View view = new View(controller);
        int newTileSize = 100;

        // when
        view.setTileSize(newTileSize);

        // then
        assertEquals(newTileSize, view.getTileSize());
    }

    @Test
    void testGetButtonArea() {
        // given
        Controller controller = new Controller(new Model());
        View view = new View(controller);

        // then
        assertEquals(new Rectangle(50, 200, 110, 60), view.get3x3ButtonArea());
        assertEquals(new Rectangle(170, 200, 110, 60), view.get4x4ButtonArea());
        assertEquals(new Rectangle(290, 200, 110, 60), view.get5x5ButtonArea());
    }
}