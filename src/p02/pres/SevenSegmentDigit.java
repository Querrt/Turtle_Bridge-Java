package p02.pres;

import p02.events.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public
    class SevenSegmentDigit
    extends JPanel
    implements DigitEventListener, ResetListener, StartListener {

    private int digit = -1;
    private final List<DigitEventListener> plusOneListeners = new ArrayList<>();
    private final List<ResetListener> resetListeners = new ArrayList<>();

    public SevenSegmentDigit() {
        setPreferredSize(new Dimension(40, 80));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDigit(g, digit);
    }

    public void addPlusOneListener(DigitEventListener listener) {
        plusOneListeners.add(listener);
    }

    public void start(StartEvent event) {
        this.digit = 0;
        repaint();
    }

    public void plusOne() {
        if (!plusOneListeners.isEmpty()) {
            if (this.digit == -1) {
                this.digit++;
            }
            if (digit < 9) {
                digit++;
            } else {
                digit = 0;
                for (DigitEventListener listener : plusOneListeners) {
                    listener.plusOneEvent(new PlusOneEvent(this));
                }
            }
            repaint();
        } else {
            resetEvent();
        }
    }

    public void reset(ResetEvent event) {
        this.digit = -1;
        repaint();
    }

    @Override
    public void plusOneEvent(PlusOneEvent e) {
        plusOne();
    }

    public void resetEvent() {
        ResetEvent e = new ResetEvent(this);
        for (ResetListener listener : resetListeners) {
            listener.reset(e);
        }
    }

    public void addResetListener(ResetListener listener) {
        resetListeners.add(listener);
    }

    private void drawDigit(Graphics g, int digit) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(5));

        boolean[] segments = getSegments(digit);

        int w = getWidth();
        int h = getHeight();
        int x = w / 5;
        int y = h / 10;
        int segW = w * 3 / 5;
        int segH = h * 2 / 5;

        if (segments[0]) g2d.drawLine(x, y, 2*x + segW, 2*y);
        if (segments[1]) g2d.drawLine(x, y + segH, 2*x + segW, 2*y + segH);
        if (segments[2]) g2d.drawLine(x, y + 2 * segH, 2*x + segW, 2*y + 2 * segH);
        if (segments[3]) g2d.drawLine(x, y, 2*x, 2*y + segH);
        if (segments[4]) g2d.drawLine(x + segW, y, 2*x + segW, 2*y + segH);
        if (segments[5]) g2d.drawLine(x, y + segH, 2*x, 2*y + 2 * segH);
        if (segments[6]) g2d.drawLine(x + segW, y + segH, 2*x + segW, 2*y + 2 * segH);
    }

    // 0 - górny poziomy
    // 1 - środkowy poziomy
    // 2 - dolny poziomy
    // 3 - lewy górny
    // 4 - prawy gorny
    // 5 - lewy dolny
    // 6 - prawy dolny
    private boolean[] getSegments(int digit) {
        return switch (digit) {
            case 0 -> new boolean[]{true, false, true, true, true, true, true};
            case 1 -> new boolean[]{false, false, false, false, true, false, true};
            case 2 -> new boolean[]{true, true, true, false, true, true, false};
            case 3 -> new boolean[]{true, true, true, false, true, false, true};
            case 4 -> new boolean[]{false, true, false, true, true, false, true};
            case 5 -> new boolean[]{true, true, true, true, false, false, true};
            case 6 -> new boolean[]{true, true, true, true, false, true, true};
            case 7 -> new boolean[]{true, false, false, false, true, false, true};
            case 8 -> new boolean[]{true, true, true, true, true, true, true};
            case 9 -> new boolean[]{true, true, false, true, true, false, true};
            default -> new boolean[]{false, false, false, false, false, false, false,};
        };
    }
}
