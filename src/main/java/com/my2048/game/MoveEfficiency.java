package com.my2048.game;

public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private final int numberOfEmptyTiles;
    private final int score;
    private final Move move;

    public Move getMove() {
        return move;
    }

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {

        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {
        if (numberOfEmptyTiles != o.numberOfEmptyTiles) {
            return Integer.compare(numberOfEmptyTiles, o.numberOfEmptyTiles);
        } else {
            return Integer.compare(score, o.score);
        }
    }
}

