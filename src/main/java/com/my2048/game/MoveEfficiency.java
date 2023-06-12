package com.my2048.game;

/**
 * Klasa reprezentująca efektywność danego ruchu w grze 2048.
 * Efektywność ruchu jest mierzona na podstawie liczby pustych kafelków po ruchu oraz punktów zdobytych za ruch.
 * Implementuje interfejs Comparable, dzięki czemu instancje tej klasy mogą być porównywane i sortowane.
 */
public class MoveEfficiency implements Comparable<MoveEfficiency> {
    /**
     * Reprezentuje liczbę pustych kafelków po wykonaniu ruchu. Jest to jedno z kryteriów mierzenia efektywności ruchu.
     */
    private final int numberOfEmptyTiles;
    /**
     * Reprezentuje wynik (liczbę punktów) uzyskany po wykonaniu ruchu. Jest to jedno z kryteriów mierzenia efektywności ruchu.
     */
    private final int score;
    /**
     * Reprezentuje ruch, którego efektywność jest mierzona.
     */
    private final Move move;

    /**
     * Zwraca ruch, którego efektywność reprezentuje ten obiekt.
     *
     * @return ruch reprezentowany przez ten obiekt
     */
    public Move getMove() {
        return move;
    }

    /**
     * Tworzy obiekt efektywności ruchu na podstawie podanej liczby pustych kafelków, punktów i ruchu.
     *
     * @param numberOfEmptyTiles liczba pustych kafelków po ruchu
     * @param score punkty zdobyte za ruch
     * @param move ruch, którego efektywność jest mierzona
     */
    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {

        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    /**
     * Porównuje efektywność tego ruchu z innym, na podstawie liczby pustych kafelków i zdobytych punktów.
     *
     * @param o inny obiekt MoveEfficiency do porównania
     * @return wartość ujemna, zero, lub dodatnia, jeżeli efektywność tego ruchu jest odpowiednio mniejsza, równa, lub większa niż efektywność innego ruchu
     */
    @Override
    public int compareTo(MoveEfficiency o) {
        if (numberOfEmptyTiles != o.numberOfEmptyTiles) {
            return Integer.compare(numberOfEmptyTiles, o.numberOfEmptyTiles);
        } else {
            return Integer.compare(score, o.score);
        }
    }
}

