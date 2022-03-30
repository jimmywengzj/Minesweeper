package src;

import src.util.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Game {

    // game status 
    public static final int STATUS_STARTED  = 0;
    public static final int STATUS_WON      = 1;
    public static final int STATUS_LOST     = -1;

    // difficulty setting
    public static final int DIFFICULTY_BEGINNER     = 101;
    public static final int DIFFICULTY_INTERMEDIATE = 102;
    public static final int DIFFICULTY_EXPERT       = 103;
    public static final int DIFFICULTY_CUSTOM       = 104;
    
    // status of each cell (player perspective)
    public static final int CHECKED   = 1001;
    public static final int UNCHECKED = 1002;
    public static final int FLAG      = 1003; 
    public static final int QUESTION  = 1004;
    
    // status when game over
    public static final int MINE       = 1005;
    public static final int NOT_MINE   = 1006;
    public static final int WRONG_MINE = 1007;
    public static final int WRONG_FLAG = 1008;

    // three game rules
    public static final int GAME_RULE_WIN_XP  = 20011025;
    public static final int GAME_RULE_WIN_7   = 20091022;
    public static final int GAME_RULE_UNKNOWN = 20220401;
    
    // variables in game
    protected int status;                                 
    protected int row, col, numMines, step;  
    protected int gameRule;
    protected boolean isCheatEnabled, showMine;
    protected boolean[][] mineBoard;  // true if there's mine
    protected boolean[] mineBoard1D;
    protected int[][] infoBoard, playerBoard, lastPlayerBoard;    // infoBoard containing all information, player's perspective see line 21
    protected int numClearCellsLeft;
    protected int numMinesLeft;   





    /**
     * initialize the board on the first click
     * @param x
     * @param y
     */


    public void initBoard(int x, int y) {  // (x,y) coordinates of the first click
        // place mine using Fisher-Yates shuffle
        this.mineBoard = new boolean[this.row][this.col];
        
        if(gameRule == GAME_RULE_WIN_XP) {
            // initialize mine starting from the last position
            this.mineBoard1D = new boolean[this.row * this.col - 1];     // 1D necessary ! It doesn't take too much...
            for(int i = mineBoard1D.length - 2; i >= mineBoard1D.length - numMines - 2; i++) {
                mineBoard1D[i] = true;
            }
            
            for(int i = mineBoard1D.length - 2; i >= mineBoard1D.length - numMines - 2; i++) {
                int randLocation = (int) (Math.random() * i);
                boolean temp = mineBoard1D[i];
                mineBoard1D[i] = mineBoard1D[randLocation];
                mineBoard1D[randLocation] = temp;
            }

            for(int i = 0; i < this.row * this.col; i++) {
                if(i != x * col + y) {
                    mineBoard[i / this.col][i % this.row] = mineBoard1D[i];
                }
            }
            // avoid placing mine on (x,y), which means excluding the position
            // iterate numMines times to randomly place mine
            
        } else if (gameRule == GAME_RULE_WIN_7) {
            // avoid placing mine on (x,y) and its surrounding cells
            this.mineBoard1D = new boolean[this.row * this.col - 9];

            for(int i = mineBoard1D.length - 10; i >= mineBoard1D.length - numMines - 10; i++) {
                mineBoard1D[i] = true;
            }
            
            for(int i = mineBoard1D.length - 10; i >= mineBoard1D.length - numMines - 10; i++) {
                int randLocation = (int) (Math.random() * i);
                boolean temp = mineBoard1D[i];
                mineBoard1D[i] = mineBoard1D[randLocation];
                mineBoard1D[randLocation] = temp;
            }

            for(int i = 0; i < this.row * this.col; i++) {
                int iXLocation = i / this.col;
                int iYLocation = i % this.col;
                if((iYLocation >= x - 1 || iYLocation <= x + 1) 
                 &&(iXLocation >= y - 1 || iXLocation <= y + 1)) {
                    mineBoard[i / this.col][i % this.row] = mineBoard1D[i];
                }
            }
        }
            // not knowing what to do with the last scenario...
        for(int j = 0; j < this.row * this.col; j++) {
            int count = 0;
            int jXLocation = j / this.col;
            int jYLocation = j % this.col; 

            if(mineBoard[jXLocation][jYLocation]) {
                infoBoard[jXLocation][jYLocation] = -1; // -1 if there is mine
            } else {
                // add value to each cell the number of nearby mines
                
                if (jXLocation - 1 >= 0) {
                    if (jYLocation - 1 >= 0 && this.mineBoard[jXLocation - 1][jYLocation - 1]) count++;
                    if (this.mineBoard[jXLocation - 1][jYLocation]) count++;
                    if (jYLocation + 1 < this.col && this.mineBoard[jXLocation - 1][jYLocation + 1]) count++;
                }
                if (jXLocation + 1 < this.row) {
                    if (jYLocation - 1 >= 0 && this.mineBoard[jXLocation + 1][jYLocation - 1]) count++;
                    if (this.mineBoard[jXLocation + 1][jYLocation]) count++;
                    if (jYLocation + 1 < this.col && this.mineBoard[jXLocation + 1][jYLocation + 1]) count++;
                }
                if (jYLocation - 1 >= 0 && this.mineBoard[jXLocation][jYLocation - 1]) count++ ;
                if (jYLocation + 1 < this.col && this.mineBoard[jXLocation][jYLocation + 1]) count++;
                
                infoBoard[jXLocation][jYLocation] = count;
            }
        }   
    }
    
    /**
     * reveal available cells using recursions
     * @param x 
     * @param y
     * @return true if not mine
     * 
     */
    public boolean revealCell(int x, int y) {
        if(mineBoard[x][y]) {
            return false;
        }
        playerBoard[x][y] = infoBoard[x][y];
        if(playerBoard[x][y] == 0) {
            for(Point p : getSurroundingCells(x, y)) {
                revealCell(p.x, p.y);
            }
        }
        return true;
    }


    public int getUncheckedCellLeft() { return this.coveredCellLeft + this.mineLeft; }


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
