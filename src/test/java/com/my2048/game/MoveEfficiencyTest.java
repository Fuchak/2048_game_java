package com.my2048.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveEfficiencyTest {
    Move move = () -> {};
    @Test
    void compareTo() {
        MoveEfficiency moveEfficiency1 = new MoveEfficiency(5, 10, move);
        MoveEfficiency moveEfficiency2 = new MoveEfficiency(3, 12, move);
        assertTrue(moveEfficiency1.compareTo(moveEfficiency2) > 0, "5 pustych kafelków jest więcej niż 3");

        moveEfficiency1 = new MoveEfficiency(3, 15, move);
        assertTrue(moveEfficiency1.compareTo(moveEfficiency2) > 0, "Kiedy liczba pustych kafelków jest równa, decyduje wynik");

        moveEfficiency1 = new MoveEfficiency(3, 12, move);
        assertEquals(0, moveEfficiency1.compareTo(moveEfficiency2), "Kiedy zarówno liczba pustych kafelków, jak i wynik są równe, compareTo powinien zwrócić 0");
    }

    @Test
    void getMove() {
        MoveEfficiency moveEfficiency = new MoveEfficiency(5, 10, move);
        assertEquals(move, moveEfficiency.getMove(), "getMove powinien zwrócić poprawny ruch");
    }
}