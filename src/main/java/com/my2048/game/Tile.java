package com.my2048.game;

import java.awt.*;

/**
 * Klasa reprezentująca kafelek w grze 2048.
 */
public class Tile {
    /**
     * Wartość liczbową przechowywaną przez kafelek.
     */
    public int value;

    /**
     * Konstruktor tworzący pusty kafelek (wartość równa zero).
     */
    public Tile() {
        this.value = 0;
    }

    /**
     * Sprawdza, czy kafelek jest pusty (wartość równa zero).
     *
     * @return true jeśli kafelek jest pusty, false w przeciwnym wypadku
     */
    public boolean isEmpty() {
        return (this.value == 0);
    }

    /**
     * Zwraca kolor czcionki dla kafelka na podstawie jego wartości.
     *
     * @return obiekt Color reprezentujący kolor czcionki
     */
    public Color getFontColor() {
        if (value < 16) return new Color(0x000000);
        else return new Color(0xFFFFFF);
    }
    /**
     * Zwraca kolor tła kafelka na podstawie jego wartości.
     *
     * @return obiekt Color reprezentujący kolor tła
     */
    public Color getTileColor() {
        switch (value) {
            case 0:
                return new Color(0xcd, 0xc1, 0xb4, 128);
            case 2:
                return new Color(0xE0FFFF);
            case 4:
                return new Color(0xAFEEEE);
            case 8:
                return new Color(0x48D1CC);
            case 16:
                return new Color(0x0B5FE6);
            case 32:
                return new Color(0x1914A6);
            case 64:
                return new Color(0x8A2BE2);
            case 128:
                return new Color(0x9400D3);
            case 256:
                return new Color(0x8827A8);
            case 512:
                return new Color(0x9E0849);
            case 1024:
                return new Color(0x8B0000);
            case 2048:
                return new Color(0xDE0235);
            case 4096:
                return new Color(0xC2105F);
            case 8192:
                return new Color(0xBD0F5D);
            case 16384:
                return new Color(0x831BA8);
            default:
                return new Color(0x000000);
        }
    }
}

