package com.my2048.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


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
        return model.getScore();
    }

    /**
     * Metoda zwraca najlepszy wynik.
     *
     * @return Najlepszy wynik.
     */
    public int getbestScore() {
        return model.getBestScore();
    }

    /**
     * Setter służący do ustawiania wyniku modelu gry, głównie używany w testach.
     *
     * @param score Nowy wynik gry.
     */
    public void setModelScore(int score) {this.model.setScore(score);}

    /**
     * Metoda resetująca stan gry do stanu początkowego.
     */
    public void resetGame() {
        SwingUtilities.invokeLater(() -> {
            model.setScore(0);
            model.clearHistory();
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
            System.exit(0);
        if (!model.canMove()) view.isGameLost = true;
        if (!view.isGameLost) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) model.left();
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) model.right();
            if (e.getKeyCode() == KeyEvent.VK_UP) model.up();
            if (e.getKeyCode() == KeyEvent.VK_DOWN) model.down();
            if (e.getKeyCode() == KeyEvent.VK_Z) model.rollback();
            if (e.getKeyCode() == KeyEvent.VK_R) resetGame();
            if (e.getKeyCode() == KeyEvent.VK_A) model.autoMove();
        }
        else if(view.currentState == View.GameState.LOST){
            if (e.getKeyCode() == KeyEvent.VK_Z) {
                model.rollback();
                view.isGameLost = false;
                view.currentState = View.GameState.PLAYING;
            }

            if (e.getKeyCode() == KeyEvent.VK_R) {
                resetGame();
                view.isGameLost = false;
                view.currentState = View.GameState.PLAYING;
            }
        }

        if (model.maxTile == WINNING_TILE) view.isGameWon = true;

        view.repaint();
        model.saveGame();
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

    /**
     * Obsługuje zdarzenia związane z kliknięciem myszy w interfejsie użytkownika.
     * <p>
     * Jeżeli stan gry to MENU, ta metoda sprawdza, który przycisk został naciśnięty (3x3, 4x4, czy 5x5)
     * i uruchamia nową grę z wybranym rozmiarem planszy. Każdemu rozmiarowi planszy odpowiada inna wielkość kafelka.
     * <p>
     * Jeżeli stan gry to PLAYING, metoda sprawdza, czy użytkownik kliknął w obszar "MENU".
     * Jeżeli tak, zmienia stan gry na MENU i odświeża widok.
     *
     * @param e obiekt zdarzenia myszy, który zawiera informacje o zdarzeniu, takie jak położenie kliknięcia
     */
    @Override public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (view.currentState == View.GameState.MENU) {
            if (view.get3x3ButtonArea().contains(p)) {
                view.setTileSize(132);
                model.setFieldWidth(3);
                view.setBoardSize(3);
                handleNewGame(3);
            } else if (view.get4x4ButtonArea().contains(p)) {
                view.setTileSize(96);
                model.setFieldWidth(4);
                view.setBoardSize(4);
                handleNewGame(4);
            } else if (view.get5x5ButtonArea().contains(p)) {
                view.setTileSize(74);
                model.setFieldWidth(5);
                view.setBoardSize(5);
                handleNewGame(5);
            }
        }
        else if (view.currentState == View.GameState.PLAYING) {
            // Sprawdzamy, czy punkt kliknięcia znajduje się w obszarze "MENU"
            if (new Rectangle(15, 70, 50, 20).contains(p)) {
                // Przełączamy stan gry na MENU
                view.repaint();
                view.currentState = View.GameState.MENU;
            }
        }
        else if (view.currentState == View.GameState.WON && new Rectangle(0, 0, 460, 580).contains(p)) {
            view.repaint();
            view.currentState = View.GameState.PLAYING;
            // Opcjonalnie możemy zresetować lub zaktualizować stan gry tu
        }
    }

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
     * Obsługuje rozpoczęcie nowej gry o podanym rozmiarze.
     * <p>
     * Metoda sprawdza, czy istnieje zapis gry dla danego rozmiaru. Jeżeli tak, wczytuje go.
     * W przeciwnym razie tworzy nowy plik zapisu gry i resetuje stan gry.
     * Po wykonaniu tych działań, odświeża widok i ustawia stan gry na PLAYING.
     *
     * @param size rozmiar planszy gry, dla którego rozpoczyna się nowa gra
     */
    private void handleNewGame(int size) {
        File file = new File("gameState" + size + ".json");
        if (file.exists() && !file.isDirectory()) {
            model.loadGame();
        } else {
            try {
                if (file.createNewFile()) {
                    model.resetGameTiles();
                    model.saveGame();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        view.repaint();
        view.currentState = View.GameState.PLAYING;
    }


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
            // Przełączamy stan gry na WON zamiast wywoływać JOptionPane
            view.currentState = View.GameState.WON;
        } else if (view.isGameLost) {
            // Przełączamy stan gry na LOST zamiast wywoływać JOptionPane
            view.currentState = View.GameState.LOST;
        }
    }
}

