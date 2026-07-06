import java.util.ArrayList;
import java.util.List;

public class DeadlockDetector {

    //check if a move causes deadlock
    static boolean isDeadlock(char[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                if (i <= 0 || i >= board.length - 1 || j <= 0 || j >= board[0].length - 1) {
                    continue;
                }


                if (board[i][j] != '0') continue;
                if (board[i][j] == '*') continue;

                if (board[i][j] == '0') {

                    //Corridor deadlock check
                    if (isCorridorDeadlock(i, j, board)) {
                        return true;
                    }

                    List<int[]> wallDirections = checkFourDirections(i, j, board);

                    if (wallDirections.isEmpty()) {
                        continue;

                    } else if (wallDirections.size() == 2) {
                        if (isItCorner(board, i, j) && board[i][j] != '$') {
                            return true;
                        }

                    } else if (wallDirections.size() > 2) {
                        return true;
                    } else {
                        int[] coordinates = wallDirections.getFirst();
                        int identifier = coordinates[0];
                        boolean targetExistsUp = false;
                        boolean targetExistsdown = false;


                        if (identifier == 1) {//column case
                            boolean UpBlocked = false;
                            boolean downBlocked = false;

                            //scan row to left
                            for (int k = j; k >= 0; k--) {
                                if (board[i][k] == '#') {
                                    UpBlocked = true;
                                    break;
                                }
                                if (board[i][k] == '$') {
                                    targetExistsUp = true;
                                    break;
                                }
                            }

                            //scan row to right
                            for (int k = j; k < board[0].length; k++) {
                                if (board[i][k] == '#') {
                                    downBlocked = true;
                                    break;
                                }
                                if (board[i][k] == '$') {
                                    targetExistsdown = true;
                                    break;
                                }
                            }

                            boolean fullWalls = true;
                            for (int k = 0; k < board[0].length; k++) {
                                if (board[i][k] != '#') {
                                    fullWalls = false;
                                    break;
                                }
                            }

                            if (!targetExistsUp && !targetExistsdown && downBlocked && UpBlocked) {
                                if (fullWalls) {
                                    return true;
                                }
                            }

                        } else if (identifier == 0) {//row case
                            boolean leftBlocked = false;
                            boolean rightBlocked = false;
                            boolean targetExistsleft = false;
                            boolean targetExistsright = false;

                            //scan column left
                            for (int k = i; k >= 0; k--) {
                                if (board[k][j] == '#') {
                                    leftBlocked = true;
                                    break;
                                }
                                if (board[k][j] == '$') {
                                    targetExistsleft = true;
                                    break;
                                }
                            }
                            boolean fullWalls = true;
                            for (char[] chars : board) {
                                if (chars[j] != '#') {
                                    fullWalls = false;
                                    break;
                                }
                            }

                            //scan column right
                            for (int k = i; k < board.length; k++) {
                                if (board[k][j] == '#') {
                                    rightBlocked = true;
                                    break;
                                }
                                if (board[k][j] == '$') {
                                    targetExistsright = true;
                                    break;
                                }
                            }

                            if (!targetExistsright && rightBlocked && !targetExistsleft && leftBlocked) {
                                if (fullWalls) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    //deadlock tunnel case
    private static boolean isCorridorDeadlock(int i, int j, char[][] board) {
        int rows = board.length;
        int cols = board[0].length;
        boolean horizontal = (j - 1 >= 0 && j + 1 < cols && board[i][j - 1] == '#' && board[i][j + 1] == '#');
        boolean vertical = (i - 1 >= 0 && i + 1 < rows && board[i - 1][j] == '#' && board[i + 1][j] == '#');

        if (!horizontal && !vertical) return false;

        int holes = 0;

        if (vertical) {
            //Check for box or goal in this tunnel
            boolean boxExists = false;
            boolean goalExists = false;
            for (char[] value : board) {
                if (value[j] == '0') {
                    boxExists = true;
                }
                if (value[j] == '$') {
                    goalExists = true;
                }
            }
            //check left and right
            for (char[] chars : board) {
                if (j - 1 >= 0 && j + 1 < board[0].length) {
                    if (chars[j - 1] != '#' || chars[j + 1] != '#') {
                        holes++;
                    }
                }
            }
            return holes == 0  && !boxExists && !goalExists;
        }
        if(horizontal) {
            //check for box or goal in this tunnel
            boolean boxExists = false;
            boolean goalExists = false;

            for(int m = 0; m<board[0].length; m++) {
                if(board[i][m] == '0') {
                    boxExists = true;
                }
                if(board[i][m] == '$') {
                    goalExists = true;
                }
            }
            //check up and down
            for (int c = 0; c < board[0].length; c++) {
                if (i - 1 >= 0 && i + 1 < board.length) {
                    if (board[i - 1][c] != '#' || board[i + 1][c] != '#') {
                        holes++;
                    }
                }
            }
            return holes == 0 &&  !boxExists && !goalExists;
        }

        //check up and down (holes in these columns)
        for (int c = 0; c < board[0].length; c++) {
            if (i - 1 >= 0 && i + 1 < board.length) {
                if (board[i - 1][c] != '#' && board[i + 1][c] != '#') {
                    holes++;
                }
            }
        }
        return holes < 1;

    }


    private static boolean isItCorner(char[][] board, int i, int j) {
        int rows = board.length;
        int cols = board[0].length;
        boolean isCorner = false;

        //check boundaries before each access
        if (i - 1 >= 0 && j - 1 >= 0 && board[i - 1][j] == '#' && board[i][j - 1] == '#') {
            isCorner = true;
        } else if (i - 1 >= 0 && j + 1 < cols && board[i - 1][j] == '#' && board[i][j + 1] == '#') {
            isCorner = true;
        } else if (i + 1 < rows && j - 1 >= 0 && board[i + 1][j] == '#' && board[i][j - 1] == '#') {
            isCorner = true;
        } else if (i + 1 < rows && j + 1 < cols && board[i + 1][j] == '#' && board[i][j + 1] == '#') {
            isCorner = true;
        }

        return isCorner;
    }


    private static List<int[]> checkFourDirections(int row, int col, char[][] board) {
        List<int[]> wallDirections = new ArrayList<>();

        int rows = board.length;
        int cols = board[0].length;

        //up
        if (row - 1 >= 0 && board[row - 1][col] == '#') {
            wallDirections.add(new int[]{1});
        }

        //down
        if (row + 1 < rows && board[row + 1][col] == '#') {
            wallDirections.add(new int[]{1});
        }

        //left
        if (col - 1 >= 0 && board[row][col - 1] == '#') {
            wallDirections.add(new int[]{0});
        }

        //right
        if (col + 1 < cols && board[row][col + 1] == '#') {
            wallDirections.add(new int[]{0});
        }

        return wallDirections;
    }

}
