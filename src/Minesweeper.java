package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;

public class Minesweeper extends JFrame {
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
    public static final int GAME_RULE_WIN_XP = 20011025;
    public static final int GAME_RULE_WIN_7  = 20091022;
    public static final int GAME_RULE_UNKNOWN  = 20220401;
    
    // variables in game
    private int status;                                 
    private int numRows, numCols, cellSize, numMines, step;  
    private int gameRule;
    private boolean isCheatEnabled, showMine;
    private boolean[][] mineBoard;  // true if there's mine
    private boolean[] mineBoard1D;
    private int[][] infoBoard, playerBoard, lastPlayerBoard;    // infoBoard containing all information, player's perspective see line 19
    private int numClearCellsLeft;
    private int numMinesLeft;       
    
    
    public Minesweeper(int difficulty, int row, int col, int cellSize, int numMines) {
        this.initGame(row, col, numMines, isCheatEnabled, mineBoard, gameRule);
    }
    public Minesweeper(int difficulty, boolean cheat, int gameRule) {
        switch (difficulty) {
            case DIFFICULTY_BEGINNER:
                this.initGame(9, 9, 10, cheat, null, gameRule); 
                break;
            case DIFFICULTY_INTERMEDIATE:
                this.initGame(16, 16, 40, cheat, null, gameRule); 
                break;
            case DIFFICULTY_EXPERT:
                this.initGame(16, 30, 99, cheat, null, gameRule);
        }
    }
    public Minesweeper(int row, int col, int numMines, boolean cheatEnabled) {
        this(row, col, numMines, cheatEnabled, GAME_RULE_WIN_XP);
    }
    public Minesweeper(int row, int col, int numMines, boolean cheatEnabled, int gameRule) {
        this.initGame(row, col, numMines, cheatEnabled, null, gameRule);
    }

    public void initGame(int row, int col, int numMines, boolean isCheatEnabled, boolean[][] mineBoard, int gameRule) {
        this.status = STATUS_STARTED;
        this.numRows = row;
        this.numCols = col;
        //this.cellSize = cellSize;     used in GUI instead
        this.numMines = numMines;
        this.gameRule = gameRule;
        
        this.mineBoard = mineBoard;
        this.isCheatEnabled = isCheatEnabled;
        this.playerBoard = new int[this.numRows][this.numCols];
        this.infoBoard = new int[this.numRows][this.numCols];
        for(int i = 0; i < this.numRows; i++) {
            for(int j = 0; j < this.numCols; j++) {
                this.playerBoard[i][j] = UNCHECKED;
            }
        }
        this.lastPlayerBoard = null;
        this.numClearCellsLeft = this.numRows * this.numCols - this.numMines;
        this.numMinesLeft = this.numMines;
        this.step = 0;

    }

    private void initBoard(int x, int y) {  // (x,y) coordinates of the first click
        // place mine using Fisher-Yates shuffle
        this.mineBoard = new boolean[this.numRows][this.numCols];
        // this.mineBoard1D = new boolean[this.numRows * this.numCols];     1D not necessary
        if(gameRule == GAME_RULE_WIN_XP) {
            // avoid placing mine on (x,y)
            // iterate numMines times to randomly place mine
            for(int i = this.numRows * this.numCols - 1; i >= 0; i --) {   
                while(i != x + y * numCols) {
                    int iXLocation = i % this.numCols;
                    int iYLocation = i / this.numCols;
                    
                    int randNum = (int) (Math.random() * i);
                    int randXLocation = randNum % this.numCols;
                    int randYLocation = randNum / this.numCols;
                    
                    boolean temp = mineBoard[iXLocation][iYLocation];
                    mineBoard[iXLocation][iYLocation] = mineBoard[randXLocation][randYLocation];
                    mineBoard[randXLocation][randYLocation] = temp;
                } 
            }
        } else if (gameRule == GAME_RULE_WIN_7) {
            // avoid placing mine on (x,y) and its surrounding cells
            for(int i = this.numRows * this.numCols - 1; i >= 0; i --) {   
                
                int iXLocation = i % this.numCols;
                int iYLocation = i / this.numCols;
                mineBoard[iYLocation][iXLocation] = true;
                
                while((iXLocation < x - 1 || iXLocation > x + 1) 
                    &&(iYLocation < y - 1 || iYLocation > y + 1)) {
                    
                    int randNum = (int) (Math.random() * i);
                    int randXLocation = randNum % this.numCols;
                    int randYLocation = randNum / this.numCols;
                    
                    boolean temp = mineBoard[iYLocation][iXLocation];
                    mineBoard[iYLocation][iXLocation] = mineBoard[randYLocation][randXLocation];
                    mineBoard[randYLocation][randXLocation] = temp;
                } 
                
            }
        }
            // not knowing what to do with the last scenario...
        for(int j = 0; j < this.numRows * this.numCols; j++) {
            int count = 0;
            int jX = j % this.numCols;   
            int jY = j / this.numCols;
            if(mineBoard[jY][jX]) {
                infoBoard[jY][jX] = -1; // -1 if there is mine
            } else {
                // add value to each cell the number of nearby mines
                
                if (jY - 1 >= 0) {
                    if (jX - 1 >= 0 && this.mineBoard[jY - 1][jX - 1]) count++;
                    if (this.mineBoard[jY - 1][jX]) count++;
                    if (jX + 1 < this.numCols && this.mineBoard[jY - 1][jX + 1]) count++;
                }
                if (jY + 1 < this.numRows) {
                    if (jX - 1 >= 0 && this.mineBoard[jY + 1][jX - 1]) count++;
                    if (this.mineBoard[jY + 1][jX]) count++;
                    if (jX + 1 < this.numCols && this.mineBoard[jY + 1][jX + 1]) count++;
                }
                if (jX - 1 >= 0 && this.mineBoard[jY][jX - 1]) count++ ;
                if (jX + 1 < this.numCols && this.mineBoard[jY][jX + 1]) count++;
                
                infoBoard[jY][jX] = count;
            }
        }   
    }
    
    // abort method, implement directly in initBoard
    /* private int getNumNearbyMines(int x, int y) {
        int  count = 0;
        if(x - 1 >= 0) {
            
        }
    } */

    private void flagAction(int x, int y) {

    }
    
    private void questionAction(int x, int y) {

    }
    
    private void selectAction(int x, int y) {

    }




    // getters
    public int getGameStat() { return this.status; }
    public int getNumRows() { return this.numRows; }
    public int getNumCols() { return this.numCols; }
    public int getCellSize() { return this.cellSize; }
    public int getNumMines() { return this.numMines; }
    // public int getNumCellsLeft() { return this.numCellsLeft; }
    public int getNumMinesLeft() { return this.numMinesLeft; }
    public int getGameRule() { return this.gameRule; }

}
