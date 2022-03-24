package src;

import src.util.*;
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
    public static final int SAFE       = 105;

    protected int state; // game state
    protected int[][] playerBoard; // the board that the player sees (with numbers, flags, etc.)
    protected int row, col; // num of rows and columns of the board 
    protected int mineCount;

    protected int coveredCellLeft, mineLeft;
    
    public Game(){

    }

    public int getUncheckedCellLeft() { return this.coveredCellLeft + this.mineLeft; }

    public int[][] getPlayerBoard() {
        return this.playerBoard;
    }


    public int getPlayerBoard(int x, int y) {
        return this.playerBoard[x][y];
    }

    /**
     * get a list of surrounding cells (8 cells if not on the edge of the board)
     * @param x
     * @param y
     * @return list of surrounding cells
     */
    public List<Point> getSurroundingCells(int x, int y) {
        List<Point> surroundingCells = new ArrayList<>();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i != x || j != y) {
                    if (inRange(x, y)) {
                        surroundingCells.add(new Point(i, j));
                    }
                }
            }
        }
        return surroundingCells;
    }

    /**
     * check if the coordinates are within the range of the board
     * @param x
     * @param y
     * @return if the coordinates is in range
     */
    public boolean inRange(int x, int y) {
        return x >= 0 && x < this.row && y >= 0 && y < this.col;
    }
}
