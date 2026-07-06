import java.util.ArrayList;
import java.util.List;

public class HeuristicEvaluator {

    static int heuristic(char[][] grid) {
        List<int[]> boxes = new ArrayList<>();
        List<int[]> goals = new ArrayList<>();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == '0' || grid[r][c] == '*') boxes.add(new int[]{r, c});
                if (grid[r][c] == '$' || grid[r][c] == '*') goals.add(new int[]{r, c});
            }
        }

        int totalDist = 0;

        for (int[] box : boxes) {
            int minDist = Integer.MAX_VALUE;
            for (int[] goal : goals) {
                int dist = Math.abs(box[0] - goal[0]) + Math.abs(box[1] - goal[1]); //manhattan h1
                if (dist < minDist) minDist = dist;
            }
            totalDist += minDist;
        }
        int playerDist = IDSPlayertobox(grid);
        totalDist += playerDist;    //IDS h2

        return totalDist;
    }

    //Movement directions: up, right, down, left
    static int[] dRow = {-1, 0, 1, 0};
    static int[] dCol = {0, 1, 0, -1};


    static boolean isValid(char[][] level, boolean[][] visited, int row, int col) {
        return row >= 0 && col >= 0 &&
                row < level.length && col < level[0].length &&
                level[row][col] != '#' &&
                !visited[row][col];
    }

    //IDS
    public static int IDSPlayertobox(char[][] level) {
        int rows = level.length;
        int cols = level[0].length;

        int[] start = BoardUtils.findPlayer(level);

        int maxDepth = rows * cols;

        for (int depth = 0; depth <= maxDepth; depth++) {
            boolean[][] visited = new boolean[rows][cols];
            if (DLS(level, start[0], start[1], depth, visited))
                return depth;
        }
        return 0;
    }

    //Depth Limited Search
    private static boolean DLS(char[][] level, int row, int col, int limit, boolean[][] visited) {
        if (limit < 0) return false;
        if (level[row][col] == '0') return true;

        visited[row][col] = true;

        if (limit == 0) return false;

        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];
            if (isValid(level, visited, newRow, newCol)) {
                if (DLS(level, newRow, newCol, limit - 1, visited))
                    return true;
            }
        }
        return false;
    }

}
