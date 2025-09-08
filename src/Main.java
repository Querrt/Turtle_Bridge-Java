import p02.game.Board;
import p02.pres.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Board board = new Board();

        SwingUtilities.invokeLater(() -> new GameFrame(board));
    }
}