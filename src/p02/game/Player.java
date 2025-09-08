package p02.game;

import p02.pres.PackageChangeListener;

import java.util.ArrayList;
import java.util.List;

public
    class Player {
    private int pos;
    private boolean hasPackage;
    private int pendingMove = 0;
    private int score = 0;

    private final List<PackageChangeListener> packageListeners = new ArrayList<>();

    public Player(int pos) {
        this.pos = pos;
        hasPackage = false;
    }

    // getters ---------------------------------------------------------
    public int getPos() {
        return pos;
    }
    public boolean hasPendingMove() {
        return pendingMove != 0;
    }
    public int getScore() {
        return score;
    }

    // setters ---------------------------------------------------------
    public void setPos(int pos) {
        this.pos = pos;
    }
    public void setHasPackage(boolean hasPackage) {
        this.hasPackage = hasPackage;
    }
    public void setPendingMove(int pendingMove) {
        this.pendingMove = pendingMove;
    }
    public void setScore(int score) {
        this.score = score;
    }

    // methods ---------------------------------------------------------
    public void move(Board board) {
        int newPos = this.pos + this.pendingMove;
        int val = board.getField(this.pos, 0);

        if (this.pos%2 == 0) {
            if (newPos<0 || newPos>=12) return;

            if (val == 1 || val == 3) {
                board.setField(this.pos, 0, 0);
                board.setField(newPos, 0, val+1);
            } else if (val == 2 || val == 4 ) {
                board.setField(this.pos, 0, 0);
                board.setField(newPos, 0, val-1);
            }
        } else {
            if (this.pos == 11) {
                board.setField(this.pos, 0, 0);
                board.setField(10, 0, val-1);
            } else {
                board.setField(this.pos, 0, 0);
                board.setField(newPos, 0, val-1);
            }
            this.pendingMove = 0;
        }

        // Ten if pilnuje aby pozycja gracza nie wyszła poza prawą granicę tablicy
        if (this.pos == 11) {
            this.pos = 10;
        } else {
            this.pos = newPos;
        }

        if (this.pos == 11 && !this.hasPackage) {
            this.hasPackage = true;
            board.setField(this.pos, 0, 4);
            notifyPackageDelivered();
            score++;
        } else if (this.pos == 0 && this.hasPackage) {
            this.hasPackage = false;
            board.setField(this.pos, 0, 1);
            notifyPackageDelivered();
            score++;
        }
    }

    public void addPackageChangeListener(PackageChangeListener listener) {
        packageListeners.add(listener);
    }

    private void notifyPackageDelivered() {
        for (PackageChangeListener listener : packageListeners) {
            listener.packageChanged();
        }
    }

    public static boolean hasPlayerFallenInWater(Board board) {
        for (int i = 2; i <= 10; i+=2) {
            if ((board.getField(i, 0) == 1 || board.getField(i, 0) == 3) && board.getField(i, 1) == 0) {
                return true;
            }
        }
        return false;
    }
}