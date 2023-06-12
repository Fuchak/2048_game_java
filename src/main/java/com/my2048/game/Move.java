package com.my2048.game;

/**
 * Funkcjonalny interfejs reprezentujący ruch w grze 2048.
 * Zawiera jedną metodę bezargumentową, która wykonuje operację ruchu.
 */
@FunctionalInterface
public interface Move {
    /**
     * Wykonuje ruch w grze.
     */
    void move();
}

