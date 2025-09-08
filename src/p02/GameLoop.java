package p02;

import p02.events.ResetEvent;
import p02.events.ResetListener;
import p02.events.StartEvent;
import p02.events.StartListener;
import p02.game.TickListener;

import java.util.ArrayList;
import java.util.List;

public
    class GameLoop
    implements Runnable, ResetListener, StartListener {

    private static final int startInterval = 1000;
    private int interval = startInterval;

    private static Thread thread;
    private static GameLoop instance;

    private volatile boolean running = false;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    private final List<TickListener> tickListeners = new ArrayList<>();

    private GameLoop() {}

    public static synchronized GameLoop getInstance() {
        if (instance == null || thread == null || !thread.isAlive()) {
            instance = new GameLoop();
            thread = new Thread(instance);
            thread.start();
        }
        return instance;
    }

    public void addTickListener(TickListener tickListener) {
        tickListeners.add(tickListener);
    }

    public boolean hasTickListener(TickListener listener) {
        return tickListeners.contains(listener);
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public void run() {
        running = true;
        while (running) {
            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            for (TickListener tickListener : tickListeners) {
                tickListener.tickEvent();
            }

            if (interval > 200) {
                interval -= 5;
            }
        }
    }

    @Override
    public void reset(ResetEvent e) {
        interval = startInterval;
        pause();
    }

    @Override
    public void start(StartEvent e) {
        if (paused)
            resume();
    }
}
