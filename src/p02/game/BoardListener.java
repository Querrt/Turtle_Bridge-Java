package p02.game;

import p02.pres.CellIndex;

import java.util.List;

public interface BoardListener {
    void boardChanged(List<CellIndex> changedCells);
}
