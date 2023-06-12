package com.my2048.game;

import javax.swing.*;

/**
 * Klasa DesktopLauncher służy do uruchamiania gry 2048 na pulpicie.
 * Zawiera metodę main, która jest punktem wejścia do programu.
 */
public class DesktopLauncher {

    /**
     * Metoda main służy do inicjalizacji gry 2048 i wyświetlenia jej na pulpicie.
     * Tworzy nowy model gry i kontroler, a następnie tworzy i konfiguruje nowe okno JFrame,
     * do którego dodaje widok z kontrolera.
     *
     * @param args Argumenty wiersza poleceń. Nie są używane w tej aplikacji.
     */
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        JFrame game = new JFrame();

        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(460, 580);
        game.setResizable(false);

        game.add(controller.getView());


        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}
