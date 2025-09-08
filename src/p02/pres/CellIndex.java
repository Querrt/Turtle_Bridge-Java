package p02.pres;

public class CellIndex {
    private final int rowIndex;
    private final int columnIndex;

    public CellIndex(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }
    public int getColumnIndex() {
        return columnIndex;
    }
}
