package com.my2048.game;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa reprezentująca graficzny interfejs użytkownika gry 2048.
 * Dziedziczy z JPanel, który umożliwia wyświetlanie i interakcję.
 */
public class View extends JPanel {

    /**
     * Enum reprezentujący stany gry.
     */
    public enum GameState {
        MENU,
        PLAYING,
        WON,
        LOST
    }
    /**
     * Bieżący stan gry, domyślnie ustawiony na MENU.
     */
    public GameState currentState = GameState.MENU;

    /**
     * Rozmiar kafelka w grze.
     */
    private int TILE_SIZE = 96;

    /**
     * Rozmiar planszy gry, domyślnie ustawiony na 3.
     */
    private int size=3;
    /**
     * Zwraca rozmiar planszy gry.
     * Rozmiar planszy to liczba kafelków w jednym rzędzie (lub kolumnie, ponieważ plansza jest kwadratem).
     *
     * @return rozmiar planszy gry.
     */
    public int getBoardSize() {
        return size;
    }
    /**
     * Zwraca rozmiar kafelka na planszy gry.
     * Jest to długość (lub szerokość) kafelka, ponieważ kafelek jest kwadratem.
     *
     * @return rozmiar kafelka na planszy gry.
     */
    public int getTileSize() {
        return TILE_SIZE;
    }


    /**
     * Ustawia rozmiar planszy gry.
     *
     * @param size nowy rozmiar planszy gry
     */
    public void setBoardSize(int size) {
        this.size = size;
    }

    /**
     * Margines kafelka w grze.
     */
    private static final int TILE_MARGIN = 12;
    /**
     * Kontroler gry, który obsługuje interakcje z użytkownikiem i logikę gry.
     */
    private final Controller controller;
    /**
     * Zmienna śledząca, czy gracz wygrał grę.
     */
    public boolean isGameWon = false;
    /**
     * Zmienna śledząca, czy gracz przegrał grę.
     */
    public boolean isGameLost = false;
    /**
     * Zmienna śledząca, czy gracz wygrał grę wcześniej.
     */
    public boolean hasPlayerWonBefore = false;
    /**
     * Nazwa czcionki używanej w grze.
     */
    private static final String FONT_NAME = "Arial";

    /**
     * Ustawia rozmiar kafelka (komórki) na planszy gry.
     *
     * @param tileSize nowy rozmiar kafelka
     */
    public void setTileSize(int tileSize) {
        this.TILE_SIZE = tileSize;
    }

    /**
     * Zwraca prostokąt reprezentujący obszar przycisku 3x3 na interfejsie użytkownika.
     *
     * @return prostokąt reprezentujący obszar przycisku 3x3
     */
    public Rectangle get3x3ButtonArea() {
        return new Rectangle(50, 200, 110, 60);  // współrzędne i wymiary przycisku 3x3
    }
    /**
     * Zwraca prostokąt reprezentujący obszar przycisku 4x4 na interfejsie użytkownika.
     *
     * @return prostokąt reprezentujący obszar przycisku 4x4
     */
    public Rectangle get4x4ButtonArea() {
        return new Rectangle(170, 200, 110, 60);  // współrzędne i wymiary przycisku 4x4
    }
    /**
     * Zwraca prostokąt reprezentujący obszar przycisku 5x5 na interfejsie użytkownika.
     *
     * @return prostokąt reprezentujący obszar przycisku 5x5
     */
    public Rectangle get5x5ButtonArea() {
        return new Rectangle(290, 200, 110, 60);  // współrzędne i wymiary przycisku 5x5
    }


    /**
     * Konstruktor klasy View, który ustawia kontroler gry, dodaje do niego nasłuchiwaczy klawiatury i myszy.
     *
     * @param controller Kontroler gry.
     */
    public View(Controller controller) {
        setFocusable(true);
        this.controller = controller;
        addKeyListener(controller);

        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
    }
    /**
     * Metoda rysowania komponentów gry na panelu.
     *
     * @param g Obiekt graficzny do rysowania komponentów.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = this.getSize().width;
        int height = this.getSize().height;

        Color color1 = Color.BLUE;
        Color color2 = Color.MAGENTA;

        GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        switch (currentState) {
            case MENU:
                drawMenu(g);
                break;
            case PLAYING:
                drawGame(g);
                break;
            case WON:
                drawGame(g);
                drawGameWon(g);
                break;
            case LOST:
                drawGame(g);
                drawGameOver(g);
                break;
        }

        g2d.dispose();
    }

    /**
     * Metoda drawGame służy do rysowania planszy gry. Wyświetla planszę z kafelkami,
     * a także informacje o grze, takie jak aktualny wynik i najlepszy wynik.
     * @param g Obiekt Graphics, który dostarcza kontekst graficzny.
     */
    private void drawGame(Graphics g) {
        // rysujemy grę
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0xFFFFFF));
        g.setFont(new Font(FONT_NAME, Font.BOLD, 50));
        g.drawString("2048", 10, 50);

        g2d.setColor(new Color(0xcd, 0xc1, 0xb4, 128));
        g2d.fillRoundRect(15, 70, 50, 20, 10, 10);
        g.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        g.setColor(new Color(0xFFFFFF));
        g.drawString("MENU", 23, 85);

        g2d.setColor(new Color(0xcd, 0xc1, 0xb4, 128));
        g2d.fillRoundRect(160, 10, 110, 60, 10, 10);
        g.setFont(new Font(FONT_NAME, Font.BOLD, 24));
        g.drawString("Wynik ", 180, 35);
        g.drawString(String.valueOf(controller.getScore()), 180, 60);

        g2d.setColor(new Color(0xcd, 0xc1, 0xb4, 128));
        g2d.fillRoundRect(280, 10, 150, 60, 10, 10);
        g.setFont(new Font(FONT_NAME, Font.BOLD, 24));
        g.drawString("Najlepszy ", 300, 35);
        g.drawString(String.valueOf(controller.getbestScore()), 300, 60);


        g.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        // Cofnij ruch
        g.setColor(Color.BLACK);
        g.drawString("Cofnij ruch - ",100, 90);
        g2d.fillRoundRect(181, 75, 15, 20, 10, 10);
        g.setColor(Color.WHITE);
        g.drawString("Z", 185, 90);
        // Reset gry
        g.setColor(Color.BLACK);
        g.drawString("Reset - ", 200, 90);
        g2d.fillRoundRect(252, 75, 15, 20, 10, 10);
        g.setColor(Color.WHITE);
        g.drawString("R", 255, 90);
        // Automatyczny ruch
        g.setColor(Color.BLACK);
        g.drawString("Automatyczny ruch - ", 270, 90);
        g2d.fillRoundRect(401, 75, 15, 20, 10, 10);
        g.setColor(Color.WHITE);
        g.drawString("A", 405, 90);

        g2d.fillRoundRect(3, 100,
                (96 + TILE_MARGIN+2) * 4,
                (96 + TILE_MARGIN+2) * 4,
                10, 10);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                drawTile(g, controller.getGameTiles()[y][x], x, y );
            }
        }
    }
    /**
     * Metoda drawMenu służy do rysowania menu głównego gry. Wyświetla opcje wyboru
     * rozmiaru planszy (3x3, 4x4, 5x5).
     * @param g Obiekt Graphics, który dostarcza kontekst graficzny.
     */
    private void drawMenu(Graphics g) {
        // rysujemy menu główne
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.setFont(new Font(FONT_NAME, Font.BOLD, 70));
        g.drawString("2048", 140, 100);

        g.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        g.drawString("Wybierz rozmiar planszy", 100, 170);

        g.setFont(new Font(FONT_NAME, Font.PLAIN, 30));
        g2d.setColor(new Color(0xcd, 0xc1, 0xb4, 128));
        g2d.fillRoundRect(50, 200, 110, 60, 10, 10);
        g.drawString("3x3", 80, 240);

        g2d.setColor(new Color(0xcd, 0xc1, 0xb4, 128));
        g2d.fillRoundRect(170, 200, 110, 60, 10, 10);
        g.drawString("4x4", 200, 240);

        g2d.setColor(new Color(0xcd, 0xc1, 0xb4, 128));
        g2d.fillRoundRect(290, 200, 110, 60, 10, 10);
        g.drawString("5x5", 320, 240);
    }
    /**
     * Metoda drawGameWon służy do rysowania ekranu po wygranej grze. Wyświetla komunikat
     * o wygranej oraz informację o możliwości kontynuowania gry.
     * @param g Obiekt Graphics, który dostarcza kontekst graficzny.
     */
    private void drawGameWon(Graphics g) {
        // Przyciemniamy planszę gry
        g.setColor(new Color(0, 0, 0, 127));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Wyświetlamy napis "Wygrałeś" na środku planszy
        g.setColor(Color.WHITE);
        g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
        g.drawString("Wygrałeś", getWidth() / 4, getHeight() / 2);

        // Wyświetlamy napis "Stuknij, aby kontynuować" poniżej
        g.setFont(new Font(FONT_NAME, Font.BOLD, 24));
        g.drawString("Stuknij, aby kontynuować", 80, (getHeight() / 2) + 40);
    }
    /**
     * Metoda drawGameOver służy do rysowania ekranu po przegranej grze. Wyświetla komunikat
     * o przegranej oraz informacje o możliwości cofnięcia ruchu i resetu gry.
     * @param g Obiekt Graphics, który dostarcza kontekst graficzny.
     */
    private void drawGameOver(Graphics g) {
        // Przyciemniamy planszę gry
        g.setColor(new Color(0, 0, 0, 127));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Wyświetlamy napis "Koniec Gry!" na środku planszy
        g.setColor(Color.WHITE);
        g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
        g.drawString("Koniec Gry!", getWidth() / 4, getHeight() / 2);

        // Wyświetlamy informacje o możliwości cofnięcia ruchu, resetu gry lub powrotu do menu
        g.setFont(new Font(FONT_NAME, Font.BOLD, 24));
        g.drawString("Cofnij ruch - Z, Reset - R", 90, (getHeight() / 2) + 40);
    }


    /**
     * Prywatna metoda do rysowania kafelka na planszy gry.
     *
     * @param g2 Obiekt graficzny do rysowania komponentów.
     * @param tile Kafelek do narysowania.
     * @param x Pozycja x kafelka na planszy.
     * @param y Pozycja y kafelka na planszy.
     */
    private void drawTile(Graphics g2, Tile tile, int x, int y) {
        Graphics2D g = ((Graphics2D) g2);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int value = tile.value;
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y) + 100;


        g.setColor(tile.getTileColor());
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 8, 8);
        g.setColor(tile.getFontColor());
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
        final Font font = new Font(FONT_NAME, Font.BOLD, size);
        g.setFont(font);

        String s = String.valueOf(value);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        if (value != 0)
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
    }
    /**
     * Prywatna metoda do obliczania przesunięcia koordynat kafelka na planszy gry.
     *
     * @param arg Pozycja x lub y kafelka na planszy.
     * @return Przesunięcie koordynatu kafelka.
     */
    private int offsetCoors(int arg) {
        return arg * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN;
    }



}

