package src;

import src.util.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.math.BigDecimal;

public class Solver {
    
    // AI's decisions
    public static final int UNKNOWN = 0;
    public static final int MINE = 1;
    public static final int SAFE = -1;

    /**
     * basic analysis, check if all the surrouding cells of the given cell are mines or are all safe
     * @param game
     * @param x
     * @param y
     * @return if all the surrouding cells are mines / all safe / unsure
     */
    public static int basicAnalysis(Game game, int x, int y) {
        int num = game.getPlayerBoard(x, y);
        if (num > 8 || num == 0) return UNKNOWN;
        int coveredOrQuestionCount = 0;
        int flagCount = 0;
        List<Point> surroundingCells = game.getSurroundingCells(x, y);
        for (Point p : surroundingCells) {
            switch (game.getPlayerBoard(p.x, p.y)) {
                case Game.COVERED :
                case Game.QUESTION : coveredOrQuestionCount++; break;
                case Game.FLAG : flagCount++; break;
            }
        }
        if (coveredOrQuestionCount == 0) return UNKNOWN;
        if (num == flagCount) return SAFE;
        if (num == flagCount + coveredOrQuestionCount) return MINE;
        return UNKNOWN;
    }

    /**
     * subtraction analysis, check the number of the two adjacent cells and find if we can flag or safely dig the surrouding cells
     * @param game
     * @param x1 coordinates of the first cell
     * @param y1
     * @param x2 coordinates of the second cell, must be adjacent to the first cell and these two cells must be in order
     * @param y2
     * @return null if we can't decide, else return a Pair of lists, key = list of safe cells, value = list of cells to be flagged
     */
    public static Pair<List<Point>, List<Point>> subtractionAnalysis(Game game, int x1, int y1, int x2, int y2) {
        int num1 = game.getPlayerBoard(x1, y1);
        int num2 = game.getPlayerBoard(x2, y2);
        if (num1 > 8 || num2 > 8 || num1 == 0 || num2 == 0) return null;
        List<Point> side1 = new ArrayList<>(3);
        List<Point> side2 = new ArrayList<>(3);
        if (x1 == x2) {
            for (int i = x1 - 1; i <= x1 + 1; i++) {
                if (game.inRange(i, y1 - 1)) {
                    int num = game.getPlayerBoard(i, y1 - 1);
                    if (num == Game.FLAG) num1--;
                    else if (num == Game.COVERED || num == Game.FLAG) side1.add(new Point(i, y1 - 1));
                }
                if (game.inRange(i, y2 + 1)) {
                    int num = game.getPlayerBoard(i, y2 + 1);
                    if (num == Game.FLAG) num2--;
                    else if (num == Game.COVERED || num == Game.FLAG) side2.add(new Point(i, y2 + 1));
                }
            }
        }
        else if (y1 == y2) {
            for (int j = y1 - 1; j <= y1 + 1; j++) {
                if (game.inRange(x1 - 1, j)) {
                    int num = game.getPlayerBoard(x1 - 1, j);
                    if (num == Game.FLAG) num1--;
                    else if (num == Game.COVERED || num == Game.FLAG) side1.add(new Point(x1 - 1, j));
                }
                if (game.inRange(x2 + 1, j)) {
                    int num = game.getPlayerBoard(x2 + 1, j);
                    if (num == Game.FLAG) num2--;
                    else if (num == Game.COVERED || num == Game.FLAG) side2.add(new Point(x2 + 1, j));
                }
            }
        }
        
        Pair<List<Point>, List<Point>> res = null;
        if (num2 - num1 == side2.size()) res = new Pair<>(side1, side2);
        else if (num1 - num2 == side1.size()) res = new Pair<>(side2, side1);
        return res;
    }
}
