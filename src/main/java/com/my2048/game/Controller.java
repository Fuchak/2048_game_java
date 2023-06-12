package com.my2048.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Klasa Controller odpowiada za interakcję użytkownika z grą 2048.
 * Kontroler reaguje na zdarzenia klawiatury i myszy, kontroluje stan gry
 * i wywołuje odpowiednie metody na modelu i widoku.
 */
public class Controller extends KeyAdapter implements MouseListener, MouseMotionListener {

    /**
     * Model gry 2048. Przechowuje stan gry i zawiera logikę operacji gry.
     */
    private final Model model;

    /**
     * Widok gry 2048. Odpowiada za wizualizację stanu gry na ekranie.
     */
    private final View view;

    /**
     * Stała określająca wartość kafelka, który po osiągnięciu oznacza wygraną grę.
     */
    private static final int WINNING_TILE = 2048;

    /**
     * Punkt początkowy, w którym nastąpiło naciśnięcie myszy. Używane do obsługi przeciągania myszą.
     */
    private Point initialClick;

    /**
     * Flaga określająca, czy podczas przeciągania myszą został wykonany ruch.
     */
    private boolean moveMade;

    /**
     * Flaga określająca, czy przeciąganie myszą jest aktualnie w trakcie.
     */
    private boolean isMouseDragging;

    /**
     * Konstruktor klasy Controller.
     *
     * @param model Obiekt modelu gry 2048.
     */
    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    /**
     * Metoda zwraca aktualny stan gry w postaci tablicy kafelków.
     *
     * @return Dwuwymiarowa tablica kafelków.
     */
    public Tile[][] getGameTiles() {return model.getGameTiles();}

    /**
     * Metoda zwraca aktualny wynik gry.
     *
     * @return Wynik gry.
     */
    public int getScore() {
        return model.score;
    }

    /**
     * Setter służący do ustawiania wyniku modelu gry, głównie używany w testach.
     *
     * @param score Nowy wynik gry.
     */
    public void setModelScore(int score) {
        this.model.score = score;
    }


    /**
     * Metoda resetująca stan gry do stanu początkowego.
     */
    public void resetGame() {
        SwingUtilities.invokeLater(() -> {
            model.score = 0;
            view.isGameLost = false;
            view.isGameWon = false;
            view.hasPlayerWonBefore = false;
            model.maxTile = 0;
            model.resetGameTiles();
            view.repaint();
        });
    }

    /**
     * Metoda obsługująca naciśnięcia klawiszy na klawiaturze.
     *
     * @param e Zdarzenie klawiatury.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            resetGame();
        if (!model.canMove()) view.isGameLost = true;
        if (!view.isGameLost) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) model.left();
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) model.right();
            if (e.getKeyCode() == KeyEvent.VK_UP) model.up();
            if (e.getKeyCode() == KeyEvent.VK_DOWN) model.down();
            if (e.getKeyCode() == KeyEvent.VK_Z) model.rollback();
            if (e.getKeyCode() == KeyEvent.VK_R) model.randomMove();
            if (e.getKeyCode() == KeyEvent.VK_A) model.autoMove();
        }

        if (model.maxTile == WINNING_TILE) view.isGameWon = true;

        view.repaint();
        checkGameStatus();
    }

    /**
     * Metoda obsługująca zdarzenie naciśnięcia przycisku myszy.
     * Przy naciśnięciu przycisku myszy zapisuje miejsce kliknięcia, ustawia flagi isMouseDragging i moveMade.
     *
     * @param e Zdarzenie myszy.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        initialClick = e.getPoint();
        isMouseDragging = true;
        moveMade = false;
    }

    /**
     * Metoda obsługująca zdarzenie przeciągnięcia myszy.
     * W zależności od kierunku przeciągnięcia, wywołuje odpowiednią metodę modelu gry
     * (up, down, left, right).
     *
     * @param e Zdarzenie myszy.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (isMouseDragging && !moveMade) {
            int dx = e.getX() - initialClick.x;
            int dy = e.getY() - initialClick.y;

            int minDist = 20;
            boolean moved = false;

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > minDist) {
                    model.right();
                    moved = true;
                } else if (dx < -minDist) {
                    model.left();
                    moved = true;
                }
            } else {
                if (dy > minDist) {
                    model.down();
                    moved = true;
                } else if (dy < -minDist) {
                    model.up();
                    moved = true;
                }
            }

            if (moved) {
                initialClick = e.getPoint();
                moveMade = true;
            }
            if (model.maxTile == WINNING_TILE) view.isGameWon = true;
            view.repaint();
            checkGameStatus();
        }
    }

    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}

    /**
     * Metoda obsługująca zdarzenie zwolnienia przycisku myszy.
     * Przy zwolnieniu przycisku myszy resetuje flagę isMouseDragging.
     *
     * @param e Zdarzenie myszy.
     */
    @Override public void mouseReleased(MouseEvent e) {
        isMouseDragging = false;
    }
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    /**
     * Metoda zwraca obiekt widoku gry.
     *
     * @return Obiekt widoku gry.
     */
    public View getView() {
        return view;
    }

    /**
     * Metoda sprawdzająca aktualny stan gry i wyświetlająca odpowiednie dialogi w przypadku wygranej lub przegranej.
     */
    public void checkGameStatus() {
        if (view.isGameWon && !view.hasPlayerWonBefore) {
            view.hasPlayerWonBefore = true;
            int result = JOptionPane.showOptionDialog(view,
                    "Wygrałeś!\n Chcesz kontynuować, zrestartować gre czy wyjść?",
                    "Wygrana!",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Kontynuuj", "Restart", "Wyjście"},
                    null);

            if (result == JOptionPane.NO_OPTION) {
                resetGame();
            } else if (result == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            }
        } else if (view.isGameLost) {
            int result = JOptionPane.showOptionDialog(view,
                    "Przegrałeś :(\nChcesz zrestartować gre czy wyjść?",
                    "Game over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Restart", "Wyjście"},
                    null);

            if (result == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

}

