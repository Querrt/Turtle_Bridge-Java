package p02.pres;

import p02.GameLoop;
import p02.game.Board;

import javax.swing.*;
import java.awt.*;

public
    class GameFrame
    extends JFrame {

    private final MainPanel mainPanel;
    private final SevenSegmentDigit digit1;
    private final SevenSegmentDigit digit2;
    private final SevenSegmentDigit digit3;

    public GameFrame(Board board) {
        super("Turtle Bridge");

        this.mainPanel = new MainPanel(board);
        add(mainPanel, BorderLayout.CENTER);

        this.digit1 = new SevenSegmentDigit();
        this.digit2 = new SevenSegmentDigit();
        this.digit3 = new SevenSegmentDigit();

        board.addResetListener(digit1);
        board.addResetListener(digit2);
        board.addResetListener(digit3);
        board.addStartListener(digit3);

        digit3.addPlusOneListener(digit2);
        digit2.addPlusOneListener(digit1);
        digit1.addResetListener(digit1);
        digit1.addResetListener(digit2);
        digit1.addResetListener(digit3);
        digit1.addResetListener(board);
        digit1.addResetListener(GameLoop.getInstance());


        JPanel digitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        digitPanel.add(digit1);
        digitPanel.add(digit2);
        digitPanel.add(digit3);

        board.getPlayer().addPackageChangeListener(digit3::plusOne);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(digitPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        mainPanel.requestFocusInWindow();
    }

}
