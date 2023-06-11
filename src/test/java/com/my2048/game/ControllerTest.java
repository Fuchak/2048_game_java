package com.my2048.game;

import org.junit.jupiter.api.Test;


import javax.swing.*;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;


class ControllerTest {

    @Test
    void keyPressed() throws InterruptedException {
        Controller controller = new Controller(new Model());
        KeyEvent keyEvent = new KeyEvent(new JPanel(), 0, 0, 0, KeyEvent.VK_ESCAPE, ' ');


        controller.setModelScore(100);
        controller.keyPressed(keyEvent);
        Thread.sleep(100); // Czekamy na zakończenie SwingUtilities.invokeLater

        assertEquals(0, controller.getScore(), "Po naciśnięciu klawisza ESCAPE, gra powinna zostać zresetowana, więc liczba punktów powinna wynosić 0");
    }

    @Test
    void resetGame() throws InterruptedException {
        // Załóżmy, że mamy dostęp do instancji klasy Controller
        Controller controller = new Controller(new Model());

        // Symulacja jakiegoś działania gry
        controller.getGameTiles()[0][0].value = 2;
        controller.setModelScore(5000);
        controller.getView().isGameWon = true;

        // Reset gry
        controller.resetGame();

        // Czekamy na zakończenie operacji w SwingUtilities.invokeLater()
        Thread.sleep(100);

        // Sprawdzamy, czy wartości zostały zresetowane
        assertEquals(0, controller.getGameTiles()[0][0].value);
        assertEquals(0, controller.getScore());
        assertFalse(controller.getView().isGameWon);
    }

    @Test
    void checkGameStatus() {
        Controller controller = new Controller(new Model());
        controller.getView().isGameWon = true;
        controller.getView().hasPlayerWonBefore = false;
        controller.checkGameStatus();

        assertTrue(controller.getView().hasPlayerWonBefore);
    }
}