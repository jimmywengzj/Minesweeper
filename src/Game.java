package src;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Game {
    // game status 
    public static final int STATUS_NOT_STARTED = -2;
    public static final int STATUS_STARTED  = 0;
    public static final int STATUS_WON      = 1;
    public static final int STATUS_LOST     = -1;

    // status of each cell (player perspective)
    public static final int UNCHECKED = 1002;
    public static final int FLAG      = 1003; 
    public static final int QUESTION  = 1004;
    
    // status when game over
    public static final int MINE       = 1005;
    public static final int SAFE       = 1006;
    public static final int WRONG_MINE = 1007;
    public static final int WRONG_FLAG = 1008;

    // three game rules
    public static final int GAME_RULE_WIN_XP  = 20011025;
    public static final int GAME_RULE_WIN_7   = 20091022;
    
    // variables in game
    protected int status;                                 
    protected int row, col, numMines;  
    protected int gameRule;
    protected boolean[][] mineBoard;  // true if there's mine
    protected boolean[] mineBoard1D;
    protected int[][] infoBoard, playerBoard;    // infoBoard containing all information, player's perspective see line 21
    protected int numCoveredCellsLeft;  // cells including unchecked, flag, and question.
    protected int numMinesLeft;     // depending on the number of mines initially planted and the number of flags

    public Game () {    // initialization of the game
        status = STATUS_NOT_STARTED;
        this.col = Options.col;
        this.row = Options.row;
        this.numMines = Options.nbBomb;
        this.numMinesLeft = this.numMines;
        this.gameRule = Options.rule;
        this.playerBoard = new int[this.row][this.col];
        for(int i = 0; i < this.row; i++) {
            for(int j = 0; j < this.col; j++) {
                playerBoard[i][j] = UNCHECKED;
            }
        }
        this.infoBoard = new int[this.row][this.col];
    }

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
            for(int i = 0; i < numMines; i++) {
                mineBoard1D[i] = true;
            }
            
            for(int i = 0; i < mineBoard1D.length; i++) {
                int randLocation = (int) (Math.random()*(mineBoard1D.length-i)) + i;
                boolean temp = mineBoard1D[i];
                mineBoard1D[i] = mineBoard1D[randLocation];
                mineBoard1D[randLocation] = temp;
            }

            for(int i = 0; i < this.row * this.col - 1; i++) {
                int iXLocation = i / this.col;
                int iYLocation = i % this.col;
                mineBoard[iXLocation][iYLocation] = mineBoard1D[i];
            }
            mineBoard[this.row - 1][this.col - 1] = mineBoard[x][y];
            mineBoard[x][y] = false;
            numMinesLeft = numMines;
            numCoveredCellsLeft = this.row * this.col;
            // avoid placing mine on (x,y), which means excluding the position
            // iterate numMines times to randomly place mine
            
        } else if (gameRule == GAME_RULE_WIN_7) {
            // avoid placing mine on (x,y) and its surrounding cells
            int numSurroundingCells = getSurroundingCells(x, y).size();
            int numClearCells = numSurroundingCells + 1;
            this.mineBoard1D = new boolean[this.row * this.col - numClearCells];     // 1D necessary ! It doesn't take too much...
            for(int i = 0; i < numMines; i++) {
                mineBoard1D[i] = true;
            }
            
            for(int i = 0; i < mineBoard1D.length; i++) {
                int randLocation = (int) (Math.random()*(mineBoard1D.length-i)) + i;
                boolean temp = mineBoard1D[i];
                mineBoard1D[i] = mineBoard1D[randLocation];
                mineBoard1D[randLocation] = temp;
            }

            for(int i = 0; i < this.row * this.col - numClearCells; i++) {
                int iXLocation = i / this.col;
                int iYLocation = i % this.col;
                mineBoard[iXLocation][iYLocation] = mineBoard1D[i];
            }
            for(Point p : getSurroundingCells(x, y)) {
                int i = this.row * this.col - numClearCells;
                int iXLocation = i / this.col;
                int iYLocation = i % this.col;
                mineBoard[iXLocation][iYLocation] = mineBoard[p.x / this.col][p.y % this.col];
                mineBoard[p.x / this.col][p.y % this.col] = false;
                i++;
            }
            mineBoard[this.row - 1][this.col - 1] = mineBoard[x][y];
            mineBoard[x][y] = false;
            numMinesLeft = numMines;
            numCoveredCellsLeft = this.row * this.col;
        }
        for(int i = 0; i < this.row; i++) {
            for(int j = 0; j < this.col; j++) {
                if (mineBoard[i][j]) {
                    infoBoard[i][j] = MINE;
                    continue;
                }
                int count = 0;
                for(Point p : getSurroundingCells(i, j)) {
                    if(mineBoard[p.x][p.y]) count++;
                }
                infoBoard[i][j] = count;
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
    public boolean revealCell(int x, int y, HashSet<Point> revealedCells) {
        if(mineBoard[x][y]) {
            return false;
        }
        if (playerBoard[x][y] == infoBoard[x][y]) return false;
        playerBoard[x][y] = infoBoard[x][y];
        this.numCoveredCellsLeft --;
        revealedCells.add(new Point(x,y));
        if(playerBoard[x][y] == 0) {
            for(Point p : getSurroundingCells(x, y)) {
                if(!revealedCells.contains(p) && playerBoard[p.x][p.y] == UNCHECKED) {
                    revealCell(p.x, p.y, revealedCells);
                }
            }
        }
        return true;
    }
    
    /**
     * get playerBoard information of the selected cell[x][y]
     * @param x
     * @param y
     * @return an int containing playerBoard information of this cell 
     */
    public int getPlayerBoard(int x, int y) {
        return this.playerBoard[x][y];
    }
    
    /**
     * get playerBoard information in a 2D table
     * @return int[][]
     */
    public int[][] getPlayerBoard() {
        return this.playerBoard;
    }
    
    /**
     * get number of covered cells left
     * @return int
     */
    public int getCoveredCellsLeft() {
        return this.numCoveredCellsLeft;
    }

    /**
     * return the coordinates of the cell that is wrong-flagged (used solely for Solver)
     * @return Point containing x and y information
     */
    public Point checkWrongFlag(){
        for (int x = 0; x < this.row; x++) {
            for (int y = 0; y < this.col; y++) {
                if (this.playerBoard[x][y] == FLAG) {
                    if(!this.mineBoard[x][y]) {
                        return new Point(x, y);
                    }
                }
            }
        }
        return null;
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
                    if (inRange(i, j)) {
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
     * @return if the coordinates are in range
     */
    public boolean inRange(int x, int y) {
        return x >= 0 && x < this.row && y >= 0 && y < this.col;
    }
}

