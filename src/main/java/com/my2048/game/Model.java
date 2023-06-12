package com.my2048.game;

import java.util.*;

/**
 * Klasa Model reprezentuje logikę gry 2048. Przechowuje aktualny stan gry
 * i zawiera metody do manipulowania i badania tego stanu.
 */
public class Model {
    /**
     * Stale określająca szerokość i wysokość planszy gry. Domyślnie ustawiona na 4.
     */
    private static final int FIELD_WIDTH = 4;
    /**
     * Dwuwymiarowa tablica przechowująca kafelki (płytki) gry.
     */
    private Tile[][] gameTiles;
    /**
     * Przechowuje aktualny wynik gry.
     */
    public int score = 0;
    /**
     * Przechowuje wartość najwyższego kafelka w grze.
     */
    public int maxTile = 2;
    /**
     * Stos przechowujący poprzednie stany gry. Używane do cofania ruchów.
     */
    private final Stack<Tile[][]> previousStates = new Stack<>();
    /**
     * Stos przechowujący poprzednie wyniki. Używane do cofania ruchów.
     */
    private final Stack<Integer> previousScores = new Stack<>();
    /**
     * Flaga określająca, czy trzeba zapisać stan gry. Ustawiane na true po każdym ruchu.
     */
    private boolean isSaveNeeded = true;

    /**
     * Getter dla poprzednich stanów planszy. Używany w testach
     *
     * @return Stack zawierający poprzednie stany planszy.
     */
    public Stack<Tile[][]> getPreviousStates() {
        return previousStates;
    }

    /**
     * Tworzy nowy model gry, inicjalizując planszę gry.
     */
    public Model() {
        resetGameTiles();
    }

    /**
     * Dodaje losowo nowy kafelek do planszy.
     * Kafelek o wartości 2 jest dodawany z 90% prawdopodobieństwem,
     * a kafelek o wartości 4 z 10% prawdopodobieństwem.
     */
    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty()) {
            int randomTileIndex = (int) (Math.random() * emptyTiles.size());
            emptyTiles.get(randomTileIndex).value = (Math.random() < 0.9) ? 2 : 4;
        }
    }

    /**
     * Wykonuje automatyczny ruch wybierając najbardziej efektywny kierunek.
     */
    public void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        priorityQueue.offer(getMoveEfficiency(this::left));
        priorityQueue.offer(getMoveEfficiency(this::right));
        priorityQueue.offer(getMoveEfficiency(this::up));
        priorityQueue.offer(getMoveEfficiency(this::down));
        Objects.requireNonNull(priorityQueue.poll()).getMove().move();
    }

    /**
     * Sprawdza, czy plansza gry uległa zmianie w porównaniu do poprzedniego stanu.
     *
     * @return true jeśli plansza się zmieniła, false w przeciwnym wypadku.
     */
    public boolean hasBoardChanged() {
        Tile[][] clone = previousStates.peek();
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                if (gameTiles[i][j].value != clone[i][j].value)
                    return true;
            }
        }
        return false;
    }

    /**
     * Oblicza i zwraca efektywność ruchu.
     * Efektywność jest obliczana na podstawie liczby pustych kafelków i wyniku po wykonaniu ruchu.
     *
     * @param move Ruch do oceny.
     * @return Obiekt MoveEfficiency reprezentujący efektywność ruchu.
     */
    public MoveEfficiency getMoveEfficiency(Move move) {
        move.move();
        MoveEfficiency moveEfficiency;
        if (!hasBoardChanged()) {
            moveEfficiency = new MoveEfficiency(-1, 0, move);
        } else {
            int EmptyTiles = 0;
            for (Tile[] gameTile : gameTiles) {
                for (int j = 0; j < gameTiles.length; j++) {
                    if (gameTile[j].isEmpty())
                        EmptyTiles++;
                }
            }
            moveEfficiency = new MoveEfficiency(EmptyTiles, score, move);
        }
        rollback();
        return moveEfficiency;
    }

    /**
     * Wykonuje losowy ruch spośród dostępnych: w górę, w dół, w lewo, w prawo.
     */
    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0:
                up();
                break;
            case 1:
                right();
                break;
            case 2:
                down();
                break;
            case 3:
                left();
                break;
            default:
                break;
        }
    }

    /**
     * Zapisuje aktualny stan gry, w tym planszę gry i wynik.
     * Po zapisie stanu, flaga isSaveNeeded jest ustawiana na false.
     *
     * @param tiles Dwuwymiarowa tablica reprezentująca stan planszy gry do zapisania.
     */
    public void saveState(Tile[][] tiles) {
        Tile[][] clone = new Tile[4][4];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile a = new Tile();
                a.value = tiles[i][j].value;
                clone[i][j] = a;
            }
        }
        previousStates.push(clone);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    /**
     * Przywraca ostatni zapisany stan gry z stosu previousStates i stosu previousScores.
     */
    public void rollback() {
        if (!previousStates.isEmpty() && !previousScores.isEmpty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    /**
     * Zwraca listę wszystkich pustych kafelków na planszy gry.
     *
     * @return Listę pustych kafelków.
     */
    private List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty())
                    emptyTiles.add(gameTiles[i][j]);
            }
        }
        return emptyTiles;
    }
    /**
     * Resetuje stan gry, tworząc nową, pustą planszę gry i dodając dwa kafelki.
     */
    public void resetGameTiles() {
        this.gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }
    /**
     * Sprawdza, czy są dostępne jakiekolwiek dozwolone ruchy na planszy gry.
     *
     * @return true, jeśli jest dostępny przynajmniej jeden dozwolony ruch, w przeciwnym razie false.
     */
    public boolean canMove() {
        if (!getEmptyTiles().isEmpty()) return true;

        for (Tile[] gameTile : gameTiles) {
            for (int j = 1; j < gameTiles.length; j++) {
                if (gameTile[j].value == gameTile[j - 1].value)
                    return true;
            }
        }

        for (int j = 0; j < gameTiles.length; j++) {
            for (int i = 1; i < gameTiles.length; i++) {
                if (gameTiles[i][j].value == gameTiles[i - 1][j].value)
                    return true;
            }
        }
        return false;
    }

    /**
     * Kompresuje tablicę kafelków, przesuwając wszystkie puste kafelki na koniec.
     *
     * @param tiles Tablica kafelków do skompresowania.
     * @return true jeśli tablica została zmieniona, false w przeciwnym wypadku.
     */
    private boolean compressTiles(Tile[] tiles) {
        Tile[] clone = tiles.clone();
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].value == 0 && i < tiles.length - 1 && tiles[i + 1].value != 0) {
                Tile temp = tiles[i];
                tiles[i] = tiles[i + 1];
                tiles[i + 1] = temp;
                i = -1;
            }
        }
        for (int i = 0; i < clone.length; i++) {
            if (clone[i].value != tiles[i].value) return true;
        }
        return false;
    }
    /**
     * Łączy sąsiadujące kafelki o tych samych wartościach. Po połączeniu, puste kafelki są przesuwane na koniec.
     *
     * @param tiles Tablica kafelków do połączenia.
     * @return true jeśli tablica została zmieniona, false w przeciwnym wypadku.
     */
    private boolean mergeTiles(Tile[] tiles) {
        Tile[] clone = tiles.clone();
        for (int i = 1; i < tiles.length; i++) {
            if ((tiles[i - 1].value == tiles[i].value) && !tiles[i - 1].isEmpty() && !tiles[i].isEmpty()) {

                tiles[i - 1].value *= 2;
                if (tiles[i - 1].value > maxTile) {
                    maxTile = tiles[i - 1].value;
                }
                score += tiles[i - 1].value;
                tiles[i] = new Tile();

                compressTiles(tiles);
            }
        }
        for (int i = 0; i < clone.length; i++) {
            if (clone[i].value != tiles[i].value) return true;
        }
        return false;
    }

    /**
     * Przesuwa wszystkie kafelki na planszy gry w lewo. Jeśli przesunięcie powoduje zmianę na planszy, dodaje nowy kafelek.
     */
    public void left() {
        if (isSaveNeeded) {
            saveState(gameTiles);
        }
        boolean isChanged = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                isChanged = true;
            }
        }
        if (isChanged) addTile();
        isSaveNeeded = true;
    }

    /**
     * Przesuwa wszystkie kafelki na planszy gry w prawo. W tym celu zapisuje stan gry, obraca planszę dwa razy przeciwnie do ruchu wskazówek zegara,
     * wykonuje ruch w lewo, a następnie obraca planszę dwa razy zgodnie z ruchem wskazówek zegara.
     */
    public void right() {
        saveState(gameTiles);
        rotateleft();
        rotateleft();
        left();
        rotateleft();
        rotateleft();
    }

    /**
     * Obraca planszę gry w lewo (przeciwnie do ruchu wskazówek zegara).
     */
    private void rotateleft() {
        // rotate
        for (int k = 0; k < FIELD_WIDTH / 2; k++) // border -> center
        {
            for (int j = k; j < FIELD_WIDTH - 1 - k; j++) // left -> right
            {

                Tile tmp = gameTiles[k][j];
                gameTiles[k][j] = gameTiles[j][FIELD_WIDTH - 1 - k];
                gameTiles[j][FIELD_WIDTH - 1 - k] = gameTiles[FIELD_WIDTH - 1 - k][FIELD_WIDTH - 1 - j];
                gameTiles[FIELD_WIDTH - 1 - k][FIELD_WIDTH - 1 - j] = gameTiles[FIELD_WIDTH - 1 - j][k];
                gameTiles[FIELD_WIDTH - 1 - j][k] = tmp;
            }
        }
    }

    /**
     * Getter dla tablicy kafelków gry.
     *
     * @return Dwuwymiarową tablicę zawierającą kafelki gry.
     */
    public Tile[][] getGameTiles() { return gameTiles; }


    /**
     * Przesuwa wszystkie kafelki na planszy gry do góry. W tym celu zapisuje stan gry, obraca planszę w lewo,
     * wykonuje ruch w lewo, a następnie obraca planszę trzy razy zgodnie z ruchem wskazówek zegara.
     */
    public void up() {
        saveState(gameTiles);
        rotateleft();
        left();
        rotateleft();
        rotateleft();
        rotateleft();
    }

    /**
     * Przesuwa wszystkie kafelki na planszy gry w dół. W tym celu zapisuje stan gry, obraca planszę trzy razy w lewo,
     * wykonuje ruch w lewo, a następnie obraca planszę zgodnie z ruchem wskazówek zegara.
     */
    public void down() {
        saveState(gameTiles);
        rotateleft();
        rotateleft();
        rotateleft();
        left();
        rotateleft();
    }

}