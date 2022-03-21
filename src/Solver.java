package src;

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
     * @return all the surrouding cells are mines / all safe / unsure
     */
    public static int basicAnalysis(Game game, int x, int y) {
        int num = game.getPlayerBoard(x, y);
        if (num > 8) return UNKNOWN;
        if (num == 0) return UNKNOWN;
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

}
