package src;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Game {
    // game states
    public static final int PROCESS = 0;
    public static final int WIN     = 1;
    public static final int LOSE    = -1;

    // playerboard states
    public static final int COVERED    = 10;
    public static final int FLAG       = 11;
    public static final int QUESTION   = 12;
    public static final int MINE       = 101;
    public static final int WRONG_FLAG = 102;
    public static final int RED_MINE   = 103;
    public static final int GRAY_MINE  = 104;

    protected int state; // game state
    protected int[][] playerBoard;
    protected int row, col, mineCount;
    
    public Game(){

    }

    public int getPlayerBoard(int x, int y) {
        return this.playerBoard[x][y];
    }

    public List<Point> getSurroundingCells(int x, int y) {
        List<Point> surroundingCells = new ArrayList<>();
        for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, this.row); i++) {
            for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, this.col); j++) {
                surroundingCells.add(new Point(i, j));
            }
        }
        return surroundingCells;
    }
}
