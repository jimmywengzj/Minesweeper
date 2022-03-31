package src;

import src.util.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Solver {
    
    // AI's decisions
    public static final int UNKNOWN = 0;
    public static final int MINE = 1;
    public static final int SAFE = -1;

    // Hint category
    public static final int WRONG_FLAG = 0;
    public static final int BASIC_MINE = 1;
    public static final int BASIC_SAFE = 2;
    public static final int LOGIC_MINE = 3;
    public static final int LOGIC_SAFE = 4;
    public static final int PROBABILITY = 5;

    // Connected components
    public static final int CC_VISITED = -1; // for visited number cell while searching for CC
    public static final int CC_UNKNOWN = 0;

    // Calculation accuracy for BigDecimal and double
    public static final int DECIMAL_SCALE = 6;
    public static final double EPSILON = 1e-5;

    // mathematical combinatorics, mathCombination.get(n).get(k) = C(n, k)
    // might exeed the limit of Long so we chose BigDecimal
    public static final ArrayList<ArrayList<BigDecimal>> mathCombination;

    static {
        // initialize mathCombination
        mathCombination = new ArrayList<>(16 * 30);
        ArrayList<BigDecimal> zero = new ArrayList<>(1);
        zero.add(new BigDecimal(1));
        mathCombination.add(zero);
        getMathCombination(16 * 30, 99);
    }

    /**
     * mathematical combinatorics, getMathCombination(n, k) = C(n, k)
     * C(n, k) = C(n - 1, k) + C(n - 1, k - 1)
     * @param n cell count
     * @param k mine count
     * @return a BigDecimal, combination count for n cells and k mines (with no restrictions)
     */
    public static BigDecimal getMathCombination(int n, int k) {
        for (int i = mathCombination.size(); i <= n; i++) {
            ArrayList<BigDecimal> arrCur = new ArrayList<>(i + 1);
            ArrayList<BigDecimal> arrPre = mathCombination.get(i - 1);
            arrCur.add(arrPre.get(0));
            for (int j = 1; j < i; j++) {
                arrCur.add(arrPre.get(j - 1).add(arrPre.get(j)));
            }
            arrCur.add(arrPre.get(i - 1));
            mathCombination.add(arrCur);
        }
        if (k >= 0 && k <= n) return mathCombination.get(n).get(k);
        return new BigDecimal(0);
    }


    /**
     * basic analysis, check if all the surrounding cells of the given cell are mines or are all safe
     * @param game
     * @param x
     * @param y
     * @return if all the surrounding cells are mines / all safe / unsure
     */
    public static int basicAnalysis(Game game, int x, int y) {
        int num = game.getPlayerBoard(x, y);
        if (num > 8 || num == 0) return UNKNOWN;
        int coveredOrQuestionCount = 0;
        int flagCount = 0;
        List<Point> surroundingCells = game.getSurroundingCells(x, y);
        for (Point p : surroundingCells) {
            switch (game.getPlayerBoard(p.x, p.y)) {
                case Game.UNCHECKED :
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
     * subtraction analysis, check the number of the two adjacent cells and find if we can flag or safely dig the surrounding cells
     * @param game
     * @param x1 coordinates of the first cell
     * @param y1
     * @param x2 coordinates of the second cell, must be adjacent to the first cell and these two cells must be in order
     * @param y2
     * @return null if we can't decide, else return a Pair of lists, key = list of safe cells, value = list of cells to be flagged
     */
    public static Pair<List<Point>, List<Point>> subtractionAnalysis(Game game, int x1, int y1, int x2, int y2) {
        if (!game.inRange(x1, y1)) return null;
        if (!game.inRange(x2, y2)) return null;
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
                    else if (num == Game.UNCHECKED || num == Game.FLAG) side1.add(new Point(i, y1 - 1));
                }
                if (game.inRange(i, y2 + 1)) {
                    int num = game.getPlayerBoard(i, y2 + 1);
                    if (num == Game.FLAG) num2--;
                    else if (num == Game.UNCHECKED || num == Game.FLAG) side2.add(new Point(i, y2 + 1));
                }
            }
        }
        else if (y1 == y2) {
            for (int j = y1 - 1; j <= y1 + 1; j++) {
                if (game.inRange(x1 - 1, j)) {
                    int num = game.getPlayerBoard(x1 - 1, j);
                    if (num == Game.FLAG) num1--;
                    else if (num == Game.UNCHECKED || num == Game.FLAG) side1.add(new Point(x1 - 1, j));
                }
                if (game.inRange(x2 + 1, j)) {
                    int num = game.getPlayerBoard(x2 + 1, j);
                    if (num == Game.FLAG) num2--;
                    else if (num == Game.UNCHECKED || num == Game.FLAG) side2.add(new Point(x2 + 1, j));
                }
            }
        }
        
        Pair<List<Point>, List<Point>> res = null;
        if (num2 - num1 == side2.size()) res = new Pair<>(side1, side2);
        else if (num1 - num2 == side1.size()) res = new Pair<>(side2, side1);
        return res;
    }

    /**
     * find all the connected components (a set of Points, which indicates a connected area on the edge of numbers, in which the cells can interact with each other)
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
                        if ((game.getPlayerBoard(p.x, p.y) != Game.UNCHECKED && game.getPlayerBoard(p.x, p.y) != Game.QUESTION)) continue;
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

    /**
     * check the surrounding cells of a number cell to see if the number can be satisfied (legal)
     * @param game
     * @param board
     * @param x
     * @param y
     * @return if the number cell can be satisfied
     */
    public static boolean isNumberCellLegal(Game game, int[][] board, int x, int y) {
        if (board[x][y] > 8) return false;
        List<Point> surroundingCells = game.getSurroundingCells(x, y);
        int mineCnt = 0;
        int uncheckedCnt = 0;
        for (Point p : surroundingCells) {
            switch (board[p.x][p.y]) {
                case Game.FLAG:
                case Game.MINE:
                case Game.WRONG_MINE:
                    mineCnt++;
                    break;
                case Game.UNCHECKED:
                case Game.QUESTION:
                    uncheckedCnt++;
                    break;
                default: break;
            }
        }
        return mineCnt <= board[x][y] && mineCnt + uncheckedCnt >= board[x][y];
    }

    /**
     * check if an unknown cell can be a mine / can be safe by checking the number cells around it
     * @param game
     * @param board
     * @param x
     * @param y
     * @return if this cell can be a mine / can be safe
     */
    public static boolean isUncheckedCellLegal(Game game, int[][] board, int x, int y) {
        if (board[x][y] < 9) return false;
        for (Point p : game.getSurroundingCells(x, y)) {
            if (board[p.x][p.y] < 9 && !isNumberCellLegal(game, board, p.x, p.y)) return false;
        }
        return true;
    }

    /**
     * find all the combination count of a connected component for each different numbers of mines, using backtracking recursions
     * @param game
     * @param board we will change the board during the backtracking so we can't use the board directely from the game
     * @param points all the points in a CC
     * @param ccCombination combination count of the CC for "key" mines, as well as the combination count where a cell is a mine
     * @param curIndex current cell index of backtracking
     * @param curMine current mine count
     * @return total combination count
     */
    public static int getCCCombinationCount(Game game, int[][] board, List<Point> points, Map<Integer, int[]> ccCombination, int curIndex, int curMine) {
        // finds a combination
        if (curIndex >= points.size()) {
            int[] count;
            // the [0 .. points.size() - 1] elements stores the combination count where that cell is a mine
            if (ccCombination.containsKey(curMine)) {
                count = ccCombination.get(curMine);
            }
            else {
                count = new int[points.size() + 1];
                ccCombination.put(curMine, count);
            }
            for (int i = 0; i < points.size(); i++) {
                Point p = points.get(i);
                if (board[p.x][p.y] == Game.MINE) count[i]++;
            }
            // and the last element stores the total combination count for a given number of mines
            count[points.size()]++;
            return 1;
        }

        // backtracking the next cell, by setting current cell as mine and as safe
        Point cur = points.get(curIndex);
        int res = 0;
        board[cur.x][cur.y] = Game.MINE;
        if (curMine < game.numMinesLeft && isUncheckedCellLegal(game, board, cur.x, cur.y)) {
            res += getCCCombinationCount(game, board, points, ccCombination, curIndex + 1, curMine + 1);
        }
        board[cur.x][cur.y] = Game.SAFE;
        if (isUncheckedCellLegal(game, board, cur.x, cur.y)) {
            res += getCCCombinationCount(game, board, points, ccCombination, curIndex + 1, curMine);
        }
        board[cur.x][cur.y] = Game.UNCHECKED;
        return res;
    }


    /**
     * there are too much information to return after the probability analysis, so this class is created to store the information
     */
    public static class ProbResult {
        public List<List<Point>> ccList;                // list for connected components
        public int[][] ccGraph;                         // graph for connected components
        public List<Map<Integer, int[]>> ccCombList;    // list for CC combination counts
        public double[][] probGraph;                    // the probabilities of mines of all the cells

        public ProbResult() {}

        public ProbResult(List<List<Point>> ccList, int[][] ccGraph, List<Map<Integer, int[]>> ccCombList, double[][] probGraph) {
            this.ccList = ccList;
            this.ccGraph = ccGraph;
            this.ccCombList = ccCombList;
            this.probGraph = probGraph;
        }
    }

    /**
     * calculate the probabilities of mines of all the cells
     * we use dynamic programming for all the calculations here
     * the state transition equations can be found in the pdf
     * @param game
     * @return a ProbResult class
     */
    public static ProbResult probabilityAnalysis(Game game) {
        double[][] probGraph = new double[game.row][game.col];
        Pair<List<List<Point>>, int[][]> _ccPair = findConnectedComponents(game);
        List<List<Point>> ccList = _ccPair.getKey();
        int[][] ccGraph = _ccPair.getValue();
        List<Map<Integer, int[]>> ccCombList = new ArrayList<>(ccList.size());

        // calculate all the combination count of the connected components
        for (List<Point> points : ccList) {
            Map<Integer, int[]> comb = new HashMap<>();
            getCCCombinationCount(game, game.getPlayerBoard(), points, comb, 0, 0);
            ccCombList.add(comb);
        }
        
        // cell count for unchecked cells that aren't in a CC
        int unknownCellCnt = game.getCoveredCellsLeft();
        for (List<Point> cc : ccList) {
            unknownCellCnt -= cc.size();
        }

        int maxMineCnt = game.numMinesLeft;
        int minMineCnt = maxMineCnt - unknownCellCnt;
        double sumProb = 0;

        // in the stack we store the merged combination counts of the union of the first 0 ~ n-1 CC (to avoid double computing)
        Deque<Map<Integer, Integer>> stack = new ArrayDeque<>(ccCombList.size());
        Map<Integer, Integer> outOfRange = new HashMap<>();
        // there is one combination with 0 CC
        outOfRange.put(0, 1);
        stack.addFirst(outOfRange);
        for (Map<Integer, int[]> toMerge : ccCombList) {
            Map<Integer, Integer> pre = stack.getFirst();
            Map<Integer, Integer> cur = mergeTwoCombinations(pre, toMerge, maxMineCnt, true);
            stack.addFirst(cur);
        }

        // calculate the total combination counts of the whole board (by also considering the unknown cells)
        // also we won't be using the first element in the stack so we remove it at the same time
        BigDecimal totalCombCnt = new BigDecimal(0);
        for (Map.Entry<Integer, Integer> e : stack.removeFirst().entrySet()) {
            totalCombCnt = totalCombCnt.add(new BigDecimal(e.getValue()).multiply(getMathCombination(unknownCellCnt, maxMineCnt - e.getKey())));
        }

        // calculate the total combination counts where one specific cell in a CC is a mine
        // we can thus divide it by totalCombCnt to get the probability of mine of that cell

        // initialise the other union of CC, to store the merged combination counts of the union of the last n-i-1 CC
        Map<Integer, Integer> right = outOfRange;
        for (int i = ccList.size() - 1; i >= 0; i--) {  // traverse each CC in the reversed way
            List<Point> ccPoints = ccList.get(i);               // cells of current CC
            Map<Integer, int[]> ccCombs = ccCombList.get(i);    // combination counts of current CC
            // get the first union of CC from the stack
            Map<Integer, Integer> left = stack.removeFirst();

            // the union of the two unions is the supply of the current CC (left + right + current = all the CC)
            Map<Integer, Integer> exceptCur = mergeTwoCombinations(left, right, maxMineCnt, false);
            right = mergeTwoCombinations(right, ccCombs, maxMineCnt, true);

            for (Map.Entry<Integer, int[]> cur : ccCombs.entrySet()) {  // for each different mine count in this CC
                BigDecimal combCnt = new BigDecimal(0);
                // for each different mine count in the supply, calculate total combinations in the supply + unknown cells
                for (Map.Entry<Integer, Integer> supply : exceptCur.entrySet()) { 
                    int mineCnt = supply.getKey() + cur.getKey();
                    if (mineCnt < minMineCnt || mineCnt > maxMineCnt) continue;
                    combCnt = combCnt.add(new BigDecimal(supply.getValue()).multiply(getMathCombination(unknownCellCnt, maxMineCnt - mineCnt)));
                }
                // update the probabilities of each cell in the CC
                for (int j = 0; j < ccPoints.size(); j++) {
                    Point p = ccPoints.get(j);
                    double prob = new BigDecimal(cur.getValue()[j]).multiply(combCnt).divide(totalCombCnt, 6, RoundingMode.HALF_UP).doubleValue();
                    probGraph[p.x][p.y] += prob;
                    sumProb += prob;
                }
            }

        }

        // the expectation of the mine count in unknown cells is (total mine count - sum of the probability of cells in CC)
        // divide it by the unknown cell count and we get its probability of mine (because all the unknown cell should have the same probability)
        if (unknownCellCnt > 0) {
            double unknownCellProb = ((double) game.numMinesLeft - sumProb) / (double) (unknownCellCnt);
            // remove potential error in computing
            if (Math.abs(unknownCellProb) < 1e-5) unknownCellProb = 0.0;
            else if (Math.abs(unknownCellProb - 1.0) < 1e-5) unknownCellProb = 1.0;
            for (int i = 0; i < game.row; i++) {
                for (int j = 0; j < game.col; j++) {
                    if (ccGraph[i][j] == CC_UNKNOWN) {
                        if (game.getPlayerBoard(i, j) == Game.UNCHECKED || game.getPlayerBoard(i, j) == Game.QUESTION) {
                            probGraph[i][j] = unknownCellProb;
                        }
                        // else if (game.getPlayerBoard(i, j) == Game.FLAG) probGraph[i][j] = 1.0;
                    }
                }
            }
            
        }
        return new ProbResult(_ccPair.getKey(), _ccPair.getValue(), ccCombList, probGraph);
    }

    /**
     * calculate the combination counts of the union of two sets of cells, with different total mines
     * @param comb1 the combination counts of component 1
     * @param comb2 the combination counts of component 2, note that the Value of this Map can be either an Integer or int[]
     * @param maxMineCnt maximum mine count
     * @param objType indicates the type of the Value of comb2, true if it's int[], false if it's Integer
     * @return the combination counts of the union of two component
     */
    public static Map<Integer, Integer> mergeTwoCombinations(Map<Integer, Integer> comb1, Map<Integer, ?> comb2, int maxMineCnt, boolean objType) {
        Map<Integer, Integer> res = new HashMap<>();
        for (Map.Entry<Integer, Integer> e1 : comb1.entrySet()) {
            for (Map.Entry<Integer, ?> e2 : comb2.entrySet()) {
                int totalMineCnt = e1.getKey() + e2.getKey();
                if (totalMineCnt > maxMineCnt) continue;
                int comb = e1.getValue();
                if (objType) {
                    // for the int[] value, the total combination count is the last element
                    int[] temp = (int[]) e2.getValue();
                    comb *= temp[temp.length - 1];
                }
                else comb *= (Integer) e2.getValue();
                if(res.containsKey(totalMineCnt)) {
                    res.put(totalMineCnt, comb + res.get(totalMineCnt));
                }
                else {
                    res.put(totalMineCnt, comb);
                }
            }
        }
        return res;
    }

    public static Pair<Integer, Point> getHint(Game game, ProbResult probResult) {
        if (game.status != Game.STATUS_STARTED) return null;

        // the AI can make wrong decisions if there are wrong flags placed by the player, so we have to check it before we run the AI
        Point wrongFlag = game.checkWrongFlag();
        if (wrongFlag != null) {
            return new Pair<Integer, Point> (WRONG_FLAG, wrongFlag);
        }

        // basis analysis
        for (int x = 0; x < game.row; x++) {
            for (int y = 0; y < game.col; y++) {
                int t = basicAnalysis(game, x, y);
                int hintType = 0;
                if (t == UNKNOWN) continue;
                if (t == MINE) hintType = BASIC_MINE;
                if (t == SAFE) hintType = BASIC_SAFE;
                return new Pair<Integer, Point> (hintType, new Point(x, y));
            }
        }

        // substraction analysis
        for (int x = 0; x < game.row; x++) {
            for (int y = 0; y < game.col; y++) {
                Pair<List<Point>, List<Point>> res = null;
                res = subtractionAnalysis(game, x, y, x + 1, y);
                if (res == null) res = subtractionAnalysis(game, x, y, x, y + 1);
                if (res == null) continue;
                // have safe cells
                if (res.getKey() != null) {
                    return new Pair<Integer, Point> (LOGIC_SAFE, res.getKey().get(0));
                }
                if (res.getValue() != null) {
                    return new Pair<Integer, Point> (LOGIC_MINE, res.getValue().get(0));
                }
            }
        }

        // probability analysis
        probResult = probabilityAnalysis(game);
        double[][] probGraph = probResult.probGraph;
        for (int x = 0; x < game.row; x++) {
            for (int y = 0; y < game.col; y++) {
                if (probGraph[x][y] == 0) {
                    if (game.getPlayerBoard(x, y) == Game.UNCHECKED || game.getPlayerBoard(x, y) == Game.QUESTION) {
                        return new Pair<Integer, Point> (LOGIC_SAFE, new Point(x, y));
                    }
                }
            }
        }

        // no safe cells left
        return new Pair<Integer, Point> (PROBABILITY, null);
    }
}
