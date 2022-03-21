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

    // Connected components
    public static final int CC_VISITED = -1; // for visited number cell while searching for CC
    public static final int CC_UNKNOWN = 0;

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

    /**
     * find all the connected components (connected area on the edge of numbers, in which the cells can interact with each other)
     * @param game
     * @return key = a List of the connected components, value = the board that indicates the id of the CC that each cell belongs
     */
    public static Pair<List<List<Point>>, int[][]> findConnectedComponents(Game game) {
        List<List<Point>> ccList = new ArrayList<>();
        int[][] ccGraph = new int[game.row][game.col];
        int id = 1;
        // find the points that belongs to a CC that haven't been registed and put it into a new CC
        for (int i = 0; i < game.row; i++) {
            for (int j = 0; j < game.col; j++) {
                /* All the covered cells or question cells around one number cell are in the same CC.
                   So we put the first number cell inside a queue, and each time we find a new cell of the CC, 
                   we put the surranding number cells of this new cell of the CC into the queue and BFS to find the entire CC.
                   The ccGraph is the board that indicates the id of the CC that each cell belongs, 
                   but we also mark the visited number cells as CC_VISITED (which is minus) in ccGraph to prevent dead loop
                   */
                if (ccGraph[i][j] != CC_UNKNOWN || game.getPlayerBoard(i, j) > 8) continue;
                Queue<Point> queue = new LinkedList<>();
                queue.add(new Point(i, j));
                List<Point> cc = new ArrayList<>();
                boolean newComponent = false;
                
                while (!queue.isEmpty()) {
                    Point nc = queue.poll(); // number cell
                    if (ccGraph[nc.x][nc.y] == CC_VISITED) continue;
                    ccGraph[nc.x][nc.y] = CC_VISITED;
                    
                    for (Point p : game.getSurroundingCells(nc.x, nc.y)) {
                        if ((game.getPlayerBoard(p.x, p.y) != Game.COVERED && game.getPlayerBoard(p.x, p.y) != Game.QUESTION)) continue;
                        if (ccGraph[p.x][p.y] == id) continue;
                        newComponent = true;
                        cc.add(new Point(p.x, p.y));
                        ccGraph[p.x][p.y] = id;
                        for (Point p2 : game.getSurroundingCells(p.x, p.y)) {
                            if (game.getPlayerBoard(p2.x, p2.y) < 9) queue.add(p2);
                        }
                    }
                }
                if (newComponent) {
                    ccList.add(cc);
                    id++;
                }
            }
        }
        
        return new Pair<>(ccList, ccGraph);
    }
}
