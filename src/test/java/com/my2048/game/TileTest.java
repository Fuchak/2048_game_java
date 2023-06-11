package com.my2048.game;
import java.awt.Color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void getFontColor() {
        Tile tile = new Tile();
        tile.value = 2;
        assertEquals(new Color(0x000000), tile.getFontColor(), "Czcionka powinna być czarna dla wartości poniżej 16");

        tile.value = 16;
        assertEquals(new Color(0xFFFFFF), tile.getFontColor(), "Czcionka powinna być biała dla wartości 16 i większych");
    }

    @Test
    void getTileColor() {
        Tile tile = new Tile();
        assertEquals(new Color(0xcd, 0xc1, 0xb4, 128), tile.getTileColor(), "Kolor kafelka powinien być specyficzny dla wartości 0");

        tile.value = 2;
        assertEquals(new Color(0xE0FFFF), tile.getTileColor(), "Kolor kafelka powinien być specyficzny dla wartości 2");

        tile.value = 2048;
        assertEquals(new Color(0xDE0235), tile.getTileColor(), "Kolor kafelka powinien być specyficzny dla wartości 2048");

        tile.value = 10000;
        assertEquals(new Color(0x000000), tile.getTileColor(), "Kolor kafelka powinien być czarny dla wartości nieprzewidzianych");
    }
}