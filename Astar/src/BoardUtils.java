import java.util.Arrays;

public class BoardUtils {


    //checking if the move of the player is valid or not
    static boolean isValidMove(int row, int col, char[][] board, int direction) {
        //checking line limits
        if (row < 0 || row >= board.length) return false;

        //checking limits of columns for this line
        if (col < 0 || col >= board[row].length) return false;

        char targetCell = board[row][col];

        if (targetCell == '#') return false;

        if (targetCell == '0' || targetCell == '*') {
            int nextRow = row + Main.DIRECTIONS[direction][0];
            int nextCol = col + Main.DIRECTIONS[direction][1];

            //checking limits for the line of the box
            if (nextRow < 0 || nextRow >= board.length) return false;

            //checking limits for the column of the box
            if (nextCol < 0 || nextCol >= board[nextRow].length) return false;

            char afterBox = board[nextRow][nextCol];

            return afterBox != '#' && afterBox != '0' && afterBox != '*';
        }

        return true;
    }


    //move player and update the board
    static char[][] updateGrid(int newRow, int newCol, char[][] grid, int direction) {

        int oldRow = newRow - Main.DIRECTIONS[direction][0];
        int oldCol = newCol - Main.DIRECTIONS[direction][1];

        //checking limits
        if (oldRow < 0 || oldRow >= grid.length || oldCol < 0 || oldCol >= grid[0].length) {
            return grid;
        }

        char oldTile = grid[oldRow][oldCol];
        char targetTile = grid[newRow][newCol];

        //calculating what the player leaves behind
        if (oldTile == '+') {
            grid[oldRow][oldCol] = '$';
        } else {
            grid[oldRow][oldCol] = ' ';
        }

        //if a player moves to an empty cell or to a goal
        if (targetTile == ' ' || targetTile == '$') {
            if (targetTile == '$') {
                grid[newRow][newCol] = '+';
            } else {
                grid[newRow][newCol] = '1';
            }
            return grid;
        }

        //if a player moves the box
        if (targetTile == '0' || targetTile == '*') {
            int boxNewRow = newRow + Main.DIRECTIONS[direction][0];
            int boxNewCol = newCol + Main.DIRECTIONS[direction][1];

            //checking limits
            if (boxNewRow < 0 || boxNewRow >= grid.length || boxNewCol < 0 || boxNewCol >= grid[0].length) {
                return grid;
            }

            char afterBoxTile = grid[boxNewRow][boxNewCol];

            //check if space exist for the box to be moved
            if (afterBoxTile == ' ' || afterBoxTile == '$') {

                //moved the box
                if (afterBoxTile == '$') {
                    grid[boxNewRow][boxNewCol] = '*';
                } else {
                    grid[boxNewRow][boxNewCol] = '0';
                }

                //player moves in the position of the box
                if (targetTile == '*') {
                    grid[newRow][newCol] = '+';
                } else {
                    grid[newRow][newCol] = '1';
                }

                return grid;
            }
        }

        if (oldTile == '+') {
            grid[oldRow][oldCol] = '+';
        } else {
            grid[oldRow][oldCol] = '1';
        }

        return grid;
    }

    static boolean noMoneyOrBox(char[][] grid) {
        for (char[] chars : grid) {
            for (char aChar : chars) {
                if (aChar == '0' || aChar == '$') {
                    return false;
                }
            }
        }
        return true;
    }

    static int[] findPlayer(char[][] grid) {
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == '1' || grid[r][c] == '+') {
                    return new int[]{r, c};
                }
            }
        return new int[]{-1, -1};
    }

    static char[][] copyGrid(char[][] grid) {
        char[][] newGrid = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            newGrid[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        return newGrid;
    }


    static char[][] makeRectangularWithBorder(char[][] grid) {
        int rows = grid.length;


        int cols = 0;
        for (char[] row : grid)
            cols = Math.max(cols, row.length);

        //New table with safe walls in case there are holes in the board
        char[][] newGrid = new char[rows + 2][cols + 2];


        for (int i = 0; i < rows + 2; i++) {
            for (int j = 0; j < cols + 2; j++) {
                if (i == 0 || j == 0 || i == rows + 1 || j == cols + 1)
                    newGrid[i][j] = '#';                  //outside safe wall
                else if (j - 1 < grid[i - 1].length)
                    newGrid[i][j] = grid[i - 1][j - 1];   //copying cells
                else
                    newGrid[i][j] = '#';                  //put walls where missing
            }
        }
        return newGrid;
    }


    static boolean isGoal(Node current) {
        return current.h == 0;
    }
}
