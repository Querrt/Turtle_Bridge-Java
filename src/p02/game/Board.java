package p02.game;

import p02.GameLoop;
import p02.events.ResetEvent;
import p02.events.ResetListener;
import p02.events.StartEvent;
import p02.events.StartListener;
import p02.pres.CellIndex;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public
    class Board
    implements TickListener, KeyListener, ResetListener {
    private final int[][] fields;
    private final Player player;
    private int currentFishCount = 0;
    private boolean isRunning = false;

    private final List<BoardListener> boardListeners = new ArrayList<>();
    private final List<CellIndex> changedCells = new ArrayList<>();
    private final List<ResetListener> resetListeners = new ArrayList<>();
    private final List<StartListener> startListeners = new ArrayList<>();

    // 0 - puste pole
    // 1 - gracz stoi
    // 2 - gracz skacze
    // 3 - gracz stoi z paczka
    // 4 - gracz skacze z paczka
    // 5 - żółw wynurzony z głową na powietrzu
    // 6 - żółw wynurzony z głową w wodzie
    // 7 - ryba
    // 8 - żółw jedzący rybę
    public Board() {
        fields = new int[12][5];
        player = new Player(0);

        this.addResetListener(this);
        this.addResetListener(GameLoop.getInstance());
        this.addStartListener(GameLoop.getInstance());
    }

    public int getField(int x, int y) {
        return fields[x][y];
    }
    public Player getPlayer() {
        return player;
    }
    public int getCurrentFishCount() {
        return currentFishCount;
    }
    public List<CellIndex> getChangedCells() {
        return changedCells;
    }


    public void setField(int x, int y, int value) {
        fields[x][y] = value;
        changedCells.add(new CellIndex(y, x));
    }
    public void incrementFishCount() {
        currentFishCount++;
    }
    public void decrementFishCount() {
        currentFishCount--;
    }

    public void initializeBoard() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 5; j++) {
                if (j == 1 && (i == 2 || i == 4 || i == 6 || i == 8 || i == 10)) {
                    fields[i][j] = 5;
                }
                else {
                    fields[i][j] = 0;
                }
            }
        }
        setField(0, 0, 1);
        player.setPos(0);
    }

    // listeners -----------------------------------------------------------

    public void addBoardListener(BoardListener listener) {
        boardListeners.add(listener);
    }

    private void notifyBoardChanged() {
        if (!changedCells.isEmpty()) {
            for (BoardListener listener : boardListeners) {
                listener.boardChanged(changedCells);
            }
        }
        changedCells.clear();
    }

    public void addResetListener(ResetListener listener) {
        resetListeners.add(listener);
    }

    private void resetEvent() {
        ResetEvent event = new ResetEvent(this);
        for (ResetListener listener : resetListeners) {
            listener.reset(event);
        }
    }

    public void addStartListener(StartListener listener) {
        startListeners.add(listener);
    }

    private void startEvent() {
        this.initializeBoard();
        StartEvent event = new StartEvent(this);
        for (StartListener listener : startListeners) {
            listener.start(event);
        }
        isRunning = true;
        player.setHasPackage(false);
    }

    public void reset(ResetEvent e) {
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                setField(i, j, 0);
            }
        }
        this.currentFishCount = 0;
        this.player.setScore(0);
        isRunning = false;
    }

    // keyListeners --------------------------------------------------------
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Jeśli gracz jest w powietrzu, to nie można go kontrolować
        if (player.getPos()%2 == 1) return;

        if (keyCode == KeyEvent.VK_A) {
            player.setPendingMove(-1);
        } else if (keyCode == KeyEvent.VK_D) {
            player.setPendingMove(1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_S) {
            GameLoop gameLoop = GameLoop.getInstance();
            if (!gameLoop.hasTickListener(this))
                gameLoop.addTickListener(this);

            if (!isRunning) {
                startEvent();
            }
        }
    }


    // tickEvents ----------------------------------------------------------
    @Override
    public void tickEvent() {
        // Żółwie
        TurtleManager.moveTurtles(this);

        // Ryby
        FishManager.moveFishes(this);

        // Ruch gracza
        if (player.hasPendingMove()) {
            player.move(this);
        }

        // Koniec gry
        if (Player.hasPlayerFallenInWater(this)) {
            resetEvent();
        }

        notifyBoardChanged();
    }
}
