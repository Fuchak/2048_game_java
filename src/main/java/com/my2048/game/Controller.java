package com.my2048.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class Controller extends KeyAdapter implements MouseListener, MouseMotionListener {
    private final Model model;
    private final View view;
    private static final int WINNING_TILE = 2048;

    private Point initialClick;

    private boolean moveMade;
    private boolean isMouseDragging;

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }

    //setter dla testów
    public void setModelScore(int score) {
        this.model.score = score;
    }


    //Wykonujemy operację resetu na EDT by odświeżało interfejs
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
            if (e.getKeyCode() == KeyEvent.VK_Z) model.rollback();//cofniecie ruchu
            if (e.getKeyCode() == KeyEvent.VK_R) model.randomMove();//randomowy krok
            if (e.getKeyCode() == KeyEvent.VK_A) model.autoMove();//najbardziej dochodowy ruch automatyczny
        }

        if (model.maxTile == WINNING_TILE) view.isGameWon = true;

        view.repaint();
        checkGameStatus();
    }

    // Dodane metody do obsługi myszki
    @Override
    public void mousePressed(MouseEvent e) {
        initialClick = e.getPoint();
        isMouseDragging = true;
        moveMade = false;
    }

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
    @Override public void mouseReleased(MouseEvent e) {
        isMouseDragging = false;
    }
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    public View getView() {
        return view;
    }
    public void checkGameStatus() {
        if (view.isGameWon && !view.hasPlayerWonBefore) {
            view.hasPlayerWonBefore = true;
            int result = JOptionPane.showOptionDialog(view,
                    "You've won!\nWould you like to continue, restart the game or exit?",
                    "Victory!",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Continue", "Restart", "Exit"},
                    null);

            if (result == JOptionPane.NO_OPTION) {
                resetGame();
            } else if (result == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            }
        } else if (view.isGameLost) {
            int result = JOptionPane.showOptionDialog(view,
                    "You've lost :(\nWould you like to restart the game or exit?",
                    "Game over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Restart", "Exit"},
                    null);

            if (result == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

}

