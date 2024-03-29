package com.my2048.game;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ModelTest {


    @Test
    void testGetAndSetScore() {
        Model model = new Model();
        model.setScore(500);
        assertEquals(500, model.getScore());
    }

    @Test
    void testGetAndSetBestScore() {
        Model model = new Model();
        model.setBestScore(1000);
        assertEquals(1000, model.getBestScore());
    }

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
    void saveState() {
        Model model = new Model();
        Controller controller = new Controller(new Model());

        // Zmieniamy stan gry.
        model.left();

        // Tworzymy kopię stanu gry.
        Tile[][] originalState = new Tile[model.getFieldWidth()][model.getFieldWidth()];

        for (int i = 0; i < model.getFieldWidth(); i++) {
            for (int j = 0; j < model.getFieldWidth(); j++) {
                if (controller.getGameTiles()[i][j] != null) {
                    Tile copyTile = new Tile();
                    copyTile.value = controller.getGameTiles()[i][j].value;
                    originalState[i][j] = copyTile;
                }
            }
        }

        // Zapisujemy stan gry.
        model.saveState(controller.getGameTiles());

        // Zmieniamy stan gry.
        model.left();


        // Sprawdzamy, czy poprzedni stan został poprawnie zapisany.
        Tile[][] savedState = model.getPreviousStates().peek();
        for (int i = 0; i < model.getFieldWidth(); i++) {
            for (int j = 0; j < model.getFieldWidth(); j++) {
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
        Model model = new Model();

        // Wykonajmy ruch
        model.up();

        int expectedScore = model.getScore();

        // Wykonajmy dodatkowy ruch, który zmieni stan gry
        model.up();

        // Przywróćmy poprzedni stan gry
        model.rollback();

        assertEquals(expectedScore, model.getScore(), "Wynik powinien zostać przywrócony do poprzedniego stanu");
    }


    @Test
    void resetGameTiles() {
        Model model = new Model();
        model.resetGameTiles();
        Tile[][] gameTiles = model.getGameTiles();

        // Po restarcie gry, powinniśmy mieć 7 pustych kafelków bo mamy statycznie field_width=3
        int emptyTiles = 0;
        for (int i = 0; i < model.getFieldWidth(); i++) {
            for (int j = 0; j < model.getFieldWidth(); j++) {
                if (gameTiles[i][j].isEmpty()) {
                    emptyTiles++;
                }
            }
        }

        assertEquals(14, emptyTiles);
    }

    @Test
    void canMove() {
        Model model = new Model();

        // Test dla pustego pola gry - powinno zwrócić true
        assertTrue(model.canMove(), "Metoda canMove powinna zwrócić true dla pustego pola gry");

        // Wypełnij pole gry losowymi kafelkami
        for(int i=0; i<1000; i++) {
            model.autoMove();
        }
        // Test dla pełnego pola gry - powinno zwrócić false
        assertFalse(model.canMove(), "Metoda canMove powinna zwrócić false dla pełnego pola gry");

    }

    @Test
    void left() {
        Model model = new Model();

        // Przesuń kafelki do lewej
        model.left();

        // Sprawdź, czy wszystkie kafelki są przesunięte do lewej
        Tile[][] afterLeft = model.getGameTiles();
        for (Tile[] tiles : afterLeft) {
            int lastNonEmptyTileIndex = -1;
            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i].value != 0) {
                    assertTrue(i >= lastNonEmptyTileIndex + 1, "kafelki nie są przesunięte lewej");
                    lastNonEmptyTileIndex = i;
                }
            }
        }
    }

    @Test
    void right() {
        Model model = new Model();

        // Przesuń kafelki w prawo
        model.right();

        // Sprawdź, czy wszystkie kafelki są przesunięte do prawej
        Tile[][] afterRight = model.getGameTiles();
        for (Tile[] tiles : afterRight) {
            int lastNonEmptyTileIndex = tiles.length;
            for (int i = tiles.length - 1; i >= 0; i--) {
                if (tiles[i].value != 0) {
                    assertTrue(i <= lastNonEmptyTileIndex - 1, "kafelki nie są przesunięte do prawej");
                    lastNonEmptyTileIndex = i;
                }
            }
        }
    }

    @Test
    void up() {
        Model model = new Model();

        // Przesuń kafelki do góry
        model.up();

        // Sprawdź, czy wszystkie kafelki są przesunięte do góry
        Tile[][] afterUp = model.getGameTiles();
        for (int j = 0; j < afterUp.length; j++) {
            int lastNonEmptyTileIndex = -1;
            for (int i = 0; i < afterUp[j].length; i++) {
                if (afterUp[i][j].value != 0) {
                    assertTrue(i >= lastNonEmptyTileIndex + 1, "kafelki nie są przesunięte do góry");
                    lastNonEmptyTileIndex = i;
                }
            }
        }
    }

    @Test
    void down() {
        Model model = new Model();

        // Przesuń kafelki w dół
        model.down();

        // Sprawdź, czy wszystkie kafelki są przesunięte w dół
        Tile[][] afterDown = model.getGameTiles();
        for (int j = 0; j < afterDown.length; j++) {
            int lastNonEmptyTileIndex = afterDown.length;
            for (int i = afterDown[j].length - 1; i >= 0; i--) {
                if (afterDown[i][j].value != 0) {
                    assertTrue(i <= lastNonEmptyTileIndex - 1, "kafelki nie są przesunięte w dół");
                    lastNonEmptyTileIndex = i;
                }
            }
        }
    }

    @Test
    void saveAndLoadGameTest() {
        Model model = new Model();
        model.setScore(5000);
        model.setBestScore(6000);
        // ustawiamy wartości na planszy gry
        // (właściwe wartości zależą od twojego konkretnego rozwiązania)
        model.getGameTiles()[0][0].value = 2;

        model.saveGame();

        // teraz resetujemy model
        model.setScore(0);
        model.setBestScore(0);
        model.getGameTiles()[0][0].value = 0;

        // teraz wczytujemy stan gry
        model.loadGame();

        assertEquals(5000, model.getScore());
        assertEquals(6000, model.getBestScore());
        assertEquals(2, model.getGameTiles()[0][0].value);
    }

}