import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.Timer;


public class Board {

    public void DrawGrid() {
        JFrame jFrame = new JFrame("Board");
        jFrame.setSize(600, 400);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(jFrame.getSize());
        jFrame.add(new MultiDraw(jFrame.getSize()));
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Board b = new Board();
        b.DrawGrid();
    }

    private static class MultiDraw extends JPanel implements MouseListener {
        int startX = 10;
        int startY = 10;
        int cellWidth = 40;
        int turn = 2;
        int rows = 6;
        int cols = 7;
        boolean winner = false;

        String cColor = "";

        Color[][] grid = new Color[rows][cols];

        public MultiDraw(Dimension size) {
            setSize(size);
            setPreferredSize(size);
            addMouseListener(this);

            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    grid[row][col] = new Color(255, 255, 255);
                }
            }
        }


        /** @noinspection SuspiciousNameCombination*/
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, d.width, d.height);
            startX = 0;
            startY = 0;

            for (Color[] colors : grid) {
                for (int col = 0; col < grid[0].length; col++) {
                    g2.setColor(colors[col]);
                    //g2.setColor(new Color(255, 255, 255));
                    g2.fillOval(startX, startY, 40, 40);
                    startX += cellWidth;

                }
                startX = 0;
                startY += cellWidth;
            }

            g2.setColor(new Color(255, 255, 255));
            if (!winner) {
                if (turn % 2 == 0) {
                    g2.drawString("Red's Turn", 400, 20);
                } else {
                    g2.drawString("Yellow's Turn", 400, 20);
                }
            } else {
                g2.drawString("The winner is " + cColor, 400, 20);
                g2.drawString("A new game will begin after 5 seconds",350,40);
                Timer timer=new Timer(5000,e->reset());
                timer.setRepeats(false);
                timer.start();
            }


        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            //int y = e.getY();
            if (!winner) {
                int xSpot = x / cellWidth;
                int ySpot = gravity(xSpot);
                if (ySpot < 0) {
                    System.out.println("not valid");

                } else {

                    if (turn % 2 == 0) {
                        grid[ySpot][xSpot] = new Color(255, 0, 0);
                        cColor = "Red";
                    }
                    if (checkForWinner(xSpot, ySpot, grid[ySpot][xSpot])) {
                        winner = true;
                    }
                    turn++;
                }
                repaint();

                Timer timer = new Timer(2000,e1->aiMove());
                timer.setRepeats(false);
                timer.start();

            }
        }
        public void aiMove(){
            int bestMove = getBestMove();
            int ySpotForAI = gravity(bestMove);

            if (ySpotForAI >= 0) {
                grid[ySpotForAI][bestMove] = new Color(255, 255, 0);
                cColor = "Yellow";

                // Check for winner after AI's move
                if (checkForWinner(bestMove, ySpotForAI, Color.yellow)) {
                    winner = true;
                }

                turn++;
                repaint();
            }
        }

        public int gravity(int xSpot) {
            int ySpot = grid.length - 1;

            while (ySpot >= 0) {

                if (grid[ySpot][xSpot].equals(Color.white)) {
                    return ySpot;
                }
                ySpot--;
            }

            return -1;
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {


        }

        public boolean checkForWinner(int x, int y, Color c) {
            int xStart = x;
            int count = 1;

            //left
            xStart--;
            while (xStart >= 0) {
                if (grid[y][xStart].equals(c)) {
                    count++;
                } else {
                    break;
                }
                if (count == 4) {
                    return true;
                }
                xStart--;
            }

            //right
            xStart = x;
            xStart++;
            while (xStart < grid[0].length) {
                if (grid[y][xStart].equals(c)) {
                    count++;
                } else {
                    break;
                }
                if (count == 4) {
                    return true;
                }
                xStart++;
            }

            //up
            count = 1;
            int yStart = y;
            yStart--;
            while (yStart > 0) {
                if (grid[yStart][x].equals(c)) {
                    count++;
                } else {
                    break;
                }
                if (count == 4) {
                    return true;
                }

                yStart--;
            }

            //down
            yStart = y;
            yStart++;
            while (yStart < grid.length) {

                if (grid[yStart][x].equals(c)) {

                    count++;
                } else {
                    break;
                }
                if (count == 4) {
                    return true;
                }

                yStart++;
            }

            //diagonal up-left

            count = 1;
            yStart = y;
            xStart = x;
            xStart--;
            yStart--;
            while (yStart >= 0 && xStart >= 0) {
                if (grid[yStart][xStart].equals(c)) {
                    count++;
                } else {
                    break;
                }
                if (count == 4) {
                    return true;
                }

                yStart--;
                xStart--;
            }

            //diagonal down-right
            yStart = y;
            xStart = x;
            yStart++;
            xStart++;
            while (yStart < grid.length && xStart < grid[0].length) {

                if (grid[yStart][xStart].equals(c)) {

                    count++;
                } else {
                    break;
                }
                if (count == 4) {
                    return true;
                }

                yStart++;
                xStart++;
            }

            //diagonal down-left
            count = 1;
            yStart = y;
            xStart = x;
            xStart--;
            yStart++;
            while (yStart < grid.length && xStart >= 0) {
                if (grid[yStart][xStart].equals(c)) {
                    count++;
                } else {
                    break;
                }
                if (count == 4) {
                    return true;
                }

                yStart++;
                xStart--;
            }

            //diagonal up-right
            yStart = y;
            xStart = x;
            yStart--;
            xStart++;
            while (yStart >= 0 && xStart < grid[0].length) {

                if (grid[yStart][xStart].equals(c)) {

                    count++;
                } else {
                    break;
                }
                if (count == 4)
                    return true;

                yStart--;
                xStart++;
            }

            return false;
        }


        public int MiniMaxAlgorithm(Color[][] board, int depth, boolean isMaximizing, int alpha, int beta) {
            if (checkForWinnerByColor(Color.yellow)) {
                return 1000; // AI wins
            } else if (checkForWinnerByColor(Color.red)) {
                return -1000; // Opponent wins
            } else if (isFullBoard()) {
                return 0; // Tie
            }

            if (depth == 0) {
                return evaluateBoard(board); // Return the evaluation of the board
            }

            if (isMaximizing) {
                int maxEval = Integer.MIN_VALUE;
                for (int col : getAvailableMoves()) {
                    int row = gravity(col); // Get row for gravity drop
                    if (row != -1) {
                        board[row][col] = Color.yellow;
                        int eval = MiniMaxAlgorithm(board, depth - 1, false, alpha, beta);
                        board[row][col] = Color.white; // Undo move
                        maxEval = Math.max(eval, maxEval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break; // Alpha-beta pruning
                    }
                }
                return maxEval;
            } else {
                int minEval = Integer.MAX_VALUE;
                for (int col : getAvailableMoves()) {
                    int row = gravity(col); // Get row for gravity drop
                    if (row != -1) {
                        board[row][col] = Color.red;
                        int eval = MiniMaxAlgorithm(board, depth - 1, true, alpha, beta);
                        board[row][col] = Color.white; // Undo move
                        minEval = Math.min(eval, minEval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break; // Alpha-beta pruning
                    }
                }
                return minEval;
            }
        }

        public boolean checkForWinnerByColor(Color color) {
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    if (grid[row][col].equals(color)) {
                        if (checkForWinner(col, row, color)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public boolean isFullBoard() {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[0][col].equals(new Color(255, 255, 255))) {
                    return false; // There's at least one empty space
                }
            }
            return true;
        }

        public int evaluateBoard(Color[][] board) {
            int score = 0;
            // Add evaluation heuristics like counting rows, columns, and diagonals for possible wins
            // Positive scores for Yellow pieces, negative for Red
            score += evaluateDirection(board, 1, 0); // Horizontal
            score += evaluateDirection(board, 0, 1); // Vertical
            score += evaluateDirection(board, 1, 1); // Diagonal up-right
            score += evaluateDirection(board, 1, -1); // Diagonal up-left
            return score;
        }

        public int evaluateDirection(Color[][] board, int dx, int dy) {
            int score = 0;
            // Traverse the entire grid
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    Color current = board[row][col];

                    // Evaluate only non-empty cells
                    if (!current.equals(new Color(255, 255, 255))) {
                        // Check consecutive pieces in the specified direction (dx, dy)
                        int count = 0;
                        boolean blocked = false;

                        // Traverse in the direction dx, dy
                        int r = row, c = col;
                        while (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length && board[r][c].equals(current)) {
                            count++;
                            r += dy;
                            c += dx;
                        }

                        // Check if the sequence is blocked by an opponent's piece or the edge of the board
                        if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length) {
                            if (!board[r][c].equals(new Color(255, 255, 255)) && !board[r][c].equals(current)) {
                                blocked = true;
                            }
                        }

                        // Now apply scoring logic
                        if (current.equals(Color.yellow)) { // AI (Yellow)
                            if (count == 4) {
                                score += 1000; // Winning sequence
                            } else if (count == 3 && !blocked) {
                                score += 50; // High chance of winning
                            } else if (count == 2 && !blocked) {
                                score += 10; // Good opportunity
                            }
                        } else if (current.equals(Color.red)) { // Opponent (Red)
                            if (count == 4) {
                                score -= 1000; // Opponent winning sequence
                            } else if (count == 3 && !blocked) {
                                score -= 50; // High chance of opponent winning
                            } else if (count == 2 && !blocked) {
                                score -= 10; // Opponent building a sequence
                            }
                        }
                    }
                }
            }

            return score;
        }

        public int[] getAvailableMoves() {
            java.util.List<Integer> availableMoves = new java.util.ArrayList<>();
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[0][col].equals(Color.white)) {
                    availableMoves.add(col); // Add column if not full
                }
            }
            return availableMoves.stream().mapToInt(i -> i).toArray(); // Convert to int array
        }

        public int getBestMove() {
            int bestScore = Integer.MIN_VALUE;
            int bestCol = -1;

            for (int col : getAvailableMoves()) {
                int row = gravity(col);
                if (row != -1) {
                    grid[row][col] = Color.yellow; // AI plays
                    int score = MiniMaxAlgorithm(grid, 6, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    grid[row][col] = Color.white; // Undo move
                    if (score > bestScore) {
                        bestScore = score;
                        bestCol = col;
                    }
                }
            }
            return bestCol;
        }

        public void reset() {
            winner = false;
            turn = 2;
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    grid[row][col] = new Color(255, 255, 255);
                    repaint();

                }
            }
        }

    }
}