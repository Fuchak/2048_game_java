package com.my2048.game;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa reprezentująca graficzny interfejs użytkownika gry 2048.
 * Dziedziczy z JPanel, który umożliwia wyświetlanie i interakcję.
 */
public class View extends JPanel {
    /**
     * Nazwa czcionki używanej w grze.
     */
    private static final String FONT_NAME = "Arial";
    /**
     * Rozmiar kafelka w grze.
     */
    private static final int TILE_SIZE = 96;
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
        int width = this.getSize().width;
        int height = this.getSize().height;

        Color color1 = Color.BLUE;
        Color color2 = Color.MAGENTA;

        GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        g.setColor(new Color(0xFFFFFF));
        g.setFont(new Font(FONT_NAME, Font.BOLD, 50));
        g.drawString("2048", 10, 50);

        g2d.setColor(new Color(0xcd, 0xc1, 0xb4, 128));
        g2d.fillRoundRect(280, 15, 110, 60, 10, 10);
        g.setFont(new Font(FONT_NAME, Font.BOLD, 24));
        g.drawString("Wynik ", 300, 40);
        g.drawString(String.valueOf(controller.getScore()), 300, 65);

        g.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        g.drawString("Cofnij ruch - Z,",30, 90);
        g.drawString("Randomowy ruch - R,", 130, 90);
        g.drawString("Automatyczny ruch - A", 270, 90);
        g.drawString("Reset - ESC", 150, 75);

        g2d.fillRoundRect(3, 100,
                (TILE_SIZE + TILE_MARGIN+2) * 4,
                (TILE_SIZE + TILE_MARGIN+2) * 4,
                10, 10);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                drawTile(g, controller.getGameTiles()[y][x], x, y );
            }
        }

        g2d.dispose();
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
    private static int offsetCoors(int arg) {
        return arg * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN;
    }
}

