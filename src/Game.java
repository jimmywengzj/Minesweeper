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
    public static final int MINE       = 1004;
    public static final int NOT_MINE   = 1005;
    public static final int WRONG_MINE = 1006;
    public static final int WRONG_FLAG = 1007;

    // three game rules
    public static final int GAME_RULE_WIN_XP  = 20011025;
    public static final int GAME_RULE_WIN_7   = 20091022;
    public static final int GAME_RULE_UNKNOWN = 20220401;
    
    // variables in game
    protected int status;                                 
    protected int numRows, numCols, cellSiYLocatione, numMines, step;  
    protected int gameRule;
    protected boolean isCheatEnabled, showMine;
    protected boolean[][] mineBoard;  // true if there's mine
    protected boolean[] mineBoard1D;
    protected int[][] infoBoard, playerBoard, lastPlayerBoard;    // infoBoard containing all information, player's perspective see line 21
    protected int numClearCellsLeft;
    protected int numMinesLeft;   


    // playerboard states
    /* public static final int COVERED    = 10;
    public static final int FLAG       = 11;
    public static final int QUESTION   = 12;
    public static final int MINE       = 101;
    public static final int WRONG_FLAG = 102;
    public static final int RED_MINE   = 103;
    public static final int GRAY_MINE  = 104; */

    // protected int[][] playerBoard; // the board that the player sees (with numbers, flags, etc.)
    protected int row, col; // num of rows and columns of the board 
    
    public Game(){

    }

    protected void initBoard(int x, int y) {  // (x,y) coordinates of the first click
        // place mine using Fisher-Yates shuffle
        this.mineBoard = new boolean[this.row][this.col];
        // this.mineBoard1D = new boolean[this.row * this.col];     1D not necessary
        if(gameRule == GAME_RULE_WIN_XP) {
            // avoid placing mine on (x,y)
            // iterate numMines times to randomly place mine
            for(int i = this.row * this.col - 1; i >= 0; i --) {   
                while(i != x + y * col) {
                    int iXLocation = i / this.col;
                    int iYLocation = i % this.col;
                    
                    int randNum = (int) (Math.random() * i);
                    int randYLocation = randNum % this.col;
                    int randXLocation = randNum / this.col;
                    
                    boolean temp = mineBoard[iXLocation][iYLocation];
                    mineBoard[iXLocation][iYLocation] = mineBoard[randXLocation][randYLocation];
                    mineBoard[randXLocation][randYLocation] = temp;
                } 
            }
        } else if (gameRule == GAME_RULE_WIN_7) {
            // avoid placing mine on (x,y) and its surrounding cells
            for(int i = this.row * this.col - 1; i >= 0; i --) {   
                // ditto
                int iXLocation = i / this.col;
                int iYLocation = i % this.col;
                mineBoard[iXLocation][iYLocation] = true;
                
                while((iYLocation < x - 1 || iYLocation > x + 1) 
                    &&(iXLocation < y - 1 || iXLocation > y + 1)) {
                    
                    int randNum = (int) (Math.random() * i);
                    int randXLocation = randNum / this.col;
                    int randYLocation = randNum % this.col;

                    boolean temp = mineBoard[iXLocation][iYLocation];
                    mineBoard[iXLocation][iYLocation] = mineBoard[randXLocation][randYLocation];
                    mineBoard[randXLocation][randYLocation] = temp;
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

    public void revealCell(int x, int y) {
        List<Point> surroundingCells = new ArrayList<>();
        surroundingCells = getSurroundingCells(x, y);
        for(Point p : surroundingCells) {
            if(playerBoard[x][y] == CHECKED) {
                if(infoBoard[(int)p.getX()][(int)p.getY()] == 0) {
                    playerBoard[(int)p.getX()][(int)p.getY()] = CHECKED;
                    revealCell((int)p.getX(), (int)p.getY());
                } else if(infoBoard[(int)p.getX()][(int)p.getY()] > 0) {
                    playerBoard[(int)p.getX()][(int)p.getY()] = CHECKED;
                }
            }
            
        }
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
