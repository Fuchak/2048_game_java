package com.my2048.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {



    @Test
    void autoMove() {
        Model model = new Model();

        // Wywołaj metodę autoMove()
        model.autoMove();

        // Sprawdź, czy plansza gry uległa zmianie po wywołaniu autoMove().
        // Zakładamy, że metoda autoMove() działa poprawnie, jeżeli plansza gry
        // uległa zmianie (tzn. jeżeli wykonano jakiś ruch).
        boolean boardChanged = model.hasBoardChanged();

        assertTrue(boardChanged, "Po wywołaniu metody autoMove() powinna nastąpić zmiana na planszy gry.");
    }

    @Test
    void hasBoardChanged() {
        Model model = new Model();

        // Zapiszemy stan początkowy gry
        model.saveState(model.getGameTiles());

        // Sprawdź, czy hasBoardChanged() zwraca false na nieruchomej planszy
        assertFalse(model.hasBoardChanged(), "Na nieruchomej planszy, metoda hasBoardChanged() powinna zwrócić false");

        // Teraz wykonajmy ruch
        model.left();

        // I sprawdźmy, czy hasBoardChanged() zwraca true po wykonaniu ruchu
        assertTrue(model.hasBoardChanged(), "Po wykonaniu ruchu, metoda hasBoardChanged() powinna zwrócić true");
    }

    @Test
    void randomMove(){

    }

    @Test
    void saveState() {
        Model model = new Model();

        // Zmieniamy stan gry.
        model.left();

        // Tworzymy kopię stanu gry.
        Tile[][] originalState = new Tile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (model.gameTiles[i][j] != null) {
                    Tile copyTile = new Tile();
                    copyTile.value = model.gameTiles[i][j].value;
                    originalState[i][j] = copyTile;
                }
            }
        }

        // Zapisujemy stan gry.
        model.saveState(model.gameTiles);

        // Zmieniamy stan gry.
        model.left();


        // Sprawdzamy, czy poprzedni stan został poprawnie zapisany.
        Tile[][] savedState = model.getPreviousStates().peek();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (originalState[i][j] != null) {
                    assertEquals(originalState[i][j].value, savedState[i][j].value);
                } else {
                    assertNull(savedState[i][j]);
                }
            }
        }
    }

    @Test
    void rollback() {
    }

    @Test
    void resetGameTiles() {
        Model model = new Model();
        model.resetGameTiles();
        Tile[][] gameTiles = model.getGameTiles();

        // Po restarcie gry, powinniśmy mieć 16 pustych kafelków
        int emptyTiles = 0;
        for (int i = 0; i < Model.FIELD_WIDTH; i++) {
            for (int j = 0; j < Model.FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty()) {
                    emptyTiles++;
                }
            }
        }

        assertEquals(14, emptyTiles);
    }

    @Test
    void canMove() {
    }

    @Test
    void left() {
    }

    @Test
    void right() {
    }

    @Test
    void getGameTiles() {
    }

    @Test
    void up() {
    }

    @Test
    void down() {
    }
}