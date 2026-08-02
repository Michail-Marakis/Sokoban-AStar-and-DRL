import java.util.*;

public class AstarAlgorithm {

    public static SolveResult solve(int choice) {
        long startTime = System.currentTimeMillis();

        char[][] level = GameLevels.levels(choice);
        level = BoardUtils.makeRectangularWithBorder(level);

        int countBoxes = 0;
        int countGoals = 0;
        int countPlayers = 0;

        for (char[] row : level) {
            for (char tile : row) {
                if (tile == '0') {
                    countBoxes++;
                } else if (tile == '$') {
                    countGoals++;
                } else if (tile == '1') {
                    countPlayers++;
                } else if (tile == '*') {
                    countBoxes++;
                    countGoals++;
                } else if (tile == '+') {
                    countPlayers++;
                    countGoals++;
                }
            }
        }

        if (countBoxes != countGoals) {
            return SolveResult.failure(
                    "The number of boxes and targets does not match."
                            + "\nBoxes: " + countBoxes
                            + "\nTargets: " + countGoals,
                    System.currentTimeMillis() - startTime
            );
        }

        if (countPlayers > 1) {
            return SolveResult.failure(
                    "There is more than one player on the board.",
                    System.currentTimeMillis() - startTime
            );
        }

        if (countPlayers == 0) {
            return SolveResult.failure(
                    "There is no player on the board.",
                    System.currentTimeMillis() - startTime
            );
        }

        int[] startPosition = BoardUtils.findPlayer(level);

        Node start = new Node(
                BoardUtils.copyGrid(level),
                startPosition[0],
                startPosition[1],
                null,
                0,
                HeuristicEvaluator.heuristic(level)
        );

        PriorityQueue<Node> open = new PriorityQueue<>(
                Comparator.comparingInt(node -> node.f)
        );

        Set<Node> visited = new HashSet<>();

        open.add(start);
        visited.add(start);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (BoardUtils.isGoal(current)
                    && BoardUtils.noMoneyOrBox(current.grid)) {

                List<Node> path = buildSolutionPath(current);

                return SolveResult.success(
                        path,
                        System.currentTimeMillis() - startTime,
                        visited.size()
                );
            }

            for (int direction = 0;
                 direction < Main.DIRECTIONS.length;
                 direction++) {

                int newRow =
                        current.playerRow
                                + Main.DIRECTIONS[direction][0];

                int newColumn =
                        current.playerCol
                                + Main.DIRECTIONS[direction][1];

                if (!BoardUtils.isValidMove(
                        newRow,
                        newColumn,
                        current.grid,
                        direction
                )) {
                    continue;
                }

                char[][] newGrid = BoardUtils.updateGrid(
                        newRow,
                        newColumn,
                        BoardUtils.copyGrid(current.grid),
                        direction
                );

                /*
                 * Check the resulting state, not current.grid.
                 */
                if (DeadlockDetector.isDeadlock(newGrid)) {
                    continue;
                }

                Node child = new Node(
                        newGrid,
                        newRow,
                        newColumn,
                        current,
                        current.g + 1,
                        HeuristicEvaluator.heuristic(newGrid)
                );

                if (visited.add(child)) {
                    open.add(child);
                }
            }
        }

        return SolveResult.failure(
                "No solution was found.",
                System.currentTimeMillis() - startTime
        );
    }

    private static List<Node> buildSolutionPath(Node goal) {
        List<Node> path = new ArrayList<>();

        Node current = goal;

        while (current != null) {
            path.add(current);
            current = current.parent;
        }

        Collections.reverse(path);

        return path;
    }
}