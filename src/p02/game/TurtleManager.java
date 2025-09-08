package p02.game;

import java.util.Random;

public class TurtleManager {
    private static final Random rand = new Random();

    public static void moveTurtles(Board board) {
        // 5 - żółw wynurzony z głową na powietrzu
        // 6 - żółw wynurzony z głową w wodzie
        // 7 - ryba
        // 8 - żółw jedzący rybę
        for (int i = 2; i <= 10; i+=2) {
            if (board.getField(i, 1) == 6 && board.getField(i, 2) == 7) {
                board.setField(i, 2, 8);
                board.setField(i, 1, 0);
                board.decrementFishCount();
            } else if (board.getField(i, 1) == 0) {
                board.setField(i, 1, 5);
                board.setField(i, 2, 0);
            }
        }

        for (int i = 2; i <= 10; i+=2) {
            if (board.getField(i, 1) == 6 || board.getField(i, 1) == 5) {
                if (rand.nextInt(10) < 2) {
                    board.setField(i, 1, 5);
                } else {
                    board.setField(i, 1, 6);
                }
            }
        }
    }
}
