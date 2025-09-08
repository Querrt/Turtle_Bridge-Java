package p02.pres;

import p02.game.Board;
import p02.game.BoardListener;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public
class MainPanel
        extends JPanel
        implements BoardListener {

    private final Board board;
    private final JTable table;
    private final SpriteTableModel model;
    private final ImageIcon[] sprites = new ImageIcon[9];

    public MainPanel(Board board) {
        this.board = board;
        setLayout(new BorderLayout());

        loadSprites();

        // Model musi byc osobnym polem, aby wywolac fireTableCellUpdated()
        this.model = new SpriteTableModel();
        this.table = new JTable(model);
        this.table.setRowHeight(40);

        table.setFocusable(false);
        table.setShowGrid(false);
        table.setEnabled(false);
        table.setOpaque(false);

        table.setDefaultRenderer(Object.class, new SpriteRenderer());

        JPanel backgroundPanel = new JPanel() {
            private final Image bg = new ImageIcon("assets\\TurtleBridgeBackground.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        add(backgroundPanel, BorderLayout.CENTER);
        backgroundPanel.add(table, BorderLayout.CENTER);
//        setFocusable(true);
        addKeyListener(board);
        requestFocusInWindow();

        board.addBoardListener(this);
    }

    private void loadSprites() {
        sprites[0] = new ImageIcon("assets\\TransparentField.png");
        sprites[1] = new ImageIcon("assets\\PlayerStanding.png");
        sprites[2] = new ImageIcon("assets\\PlayerJumping.png");
        sprites[3] = new ImageIcon("assets\\PlayerStandingWithBox.png");
        sprites[4] = new ImageIcon("assets\\PlayerJumpingWithBox.png");
        sprites[5] = new ImageIcon("assets\\TurtleHeadUp.png");
        sprites[6] = new ImageIcon("assets\\TurtleHeadDown.png");
        sprites[7] = new ImageIcon("assets\\Fish.png");
        sprites[8] = new ImageIcon("assets\\TurtleEatingFish.png");
    }

    private
    class SpriteTableModel
            extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return 5;
        }

        @Override
        public int getColumnCount() {
            return 12;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return board.getField(columnIndex, rowIndex);
        }
    }

    private class SpriteRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column
        ) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            int index = (int) value;

            if (index >= 0 && index < sprites.length) {
                label.setIcon(sprites[index]);
            }

            label.setOpaque(false);
            return label;
        }
    }

    @Override
    public void boardChanged(List<CellIndex> changedCells) {
        for (CellIndex cellIndex : changedCells) {
            model.fireTableCellUpdated(cellIndex.getRowIndex(), cellIndex.getColumnIndex());
        }
    }
}

