package p02.game;

import p02.pres.CellIndex;

import java.util.Random;

public class FishManager {
    private static final Random rand = new Random();

    public static void moveFishes(Board board) {
        int maxFishCount = (board.getPlayer().getScore() > 0)
                ? String.valueOf(board.getPlayer().getScore()).length()
                : 0;

        int[][] tempArr = new int[12][5];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 5; j++) {
                tempArr[i][j] = board.getField(i, j);
            }
        }

        CellIndex moveToField;

        for (int i = 3; i <= 4; i++) {
            for (int j = 10; j >= 1; j--) {
                if (tempArr[j][i] == 7) {
                    if (j%2 == 1) {
                        moveToField = new CellIndex(j+1, i);
                    } else if (i == 4) {
                        moveToField = new CellIndex(j-1, i-1);
                    } else {
                        moveToField = new CellIndex(j, i-1);
                    }

                    if (tempArr[moveToField.getRowIndex()][moveToField.getColumnIndex()] == 0) {
                        tempArr[j][i] = 0;
                        board.getChangedCells().add(new CellIndex(j, i));
                        tempArr[moveToField.getRowIndex()][moveToField.getColumnIndex()] = 7;
                        board.getChangedCells().add(new CellIndex(moveToField.getRowIndex(), moveToField.getColumnIndex()));
                    }
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 5; j++) {
                if (board.getField(i, j) != tempArr[i][j]) {
                    board.setField(i, j, tempArr[i][j]);
                }
            }
        }

        while (maxFishCount > board.getCurrentFishCount()) {
            int fishSpawnIndex = (rand.nextInt(4))*2+1;

            if (board.getField(fishSpawnIndex, 4) == 0) {
                board.setField(fishSpawnIndex, 4, 7);
                board.incrementFishCount();
                board.getChangedCells().add(new CellIndex(fishSpawnIndex, 4));
            }
        }
    }
}
