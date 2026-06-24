import java.util.*;

public class AstarAlgorithm {

    static void solve(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose the level you want: 0(very easy) 1(easy) 2(medium) 3(hard) 4(very hard) 5(No solution case)");
        System.out.println("Levels 0-4 have solutions, 5th doesn't");

        int choice = sc.nextInt();
        char[][] level = GameLevels.levels(choice);

        level = BoardUtils.makeRectangularWithBorder(level);

        System.out.println("Searching for a solution...");

        int countBox = 0;
        int countGoals = 0;
        int countPerson = 0;
        for (char[] chars : level) {
            for (int j = 0; j < level[0].length; j++) {
                if (chars[j] == '0') countBox++;
                if (chars[j] == '$') countGoals++;
                if(chars[j] == '1') countPerson++;
            }
        }
        if (countBox != countGoals) {
            System.out.println("Boxes and Goals can't match," +
                    "\nNumber of free boxes at the map: " + countBox +
                    "\n" + "Number of free goals at the map: " + countGoals);
            return;
        }

        if (countPerson > 1) {
            throw new ArrayIndexOutOfBoundsException("There is are more than 1 player on board");
        } else if (countPerson == 0) {
            throw new ArrayIndexOutOfBoundsException("There is no player on board");
        }


        int[] startP = BoardUtils.findPlayer(level);        //starting position

        Node start = new Node(BoardUtils.copyGrid(level), startP[0], startP[1], null, 0, HeuristicEvaluator.heuristic(level));  //starting grod


        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.f)); //prioritize smaller f value
        Set<Node> visited = new HashSet<>();    //hashset

        open.add(start);
        visited.add(start);

        long startTime = System.currentTimeMillis();
        while (!open.isEmpty()) {       //until no open moves exist
            Node current = open.poll();

            if (BoardUtils.isGoal(current)) {                       //check if the game ended
                if (BoardUtils.noMoneyOrBox(current.grid)) {
                    printSolutionPath(current);
                    System.out.println("Solution found! timeMs=" + (System.currentTimeMillis() - startTime));
                } else {
                    continue;
                }
                return;
            }

            for (int dir = 0; dir < Main.DIRECTIONS.length; dir++) {    //for every possible next move
                int newR = current.playerRow + Main.DIRECTIONS[dir][0];
                int newC = current.playerCol + Main.DIRECTIONS[dir][1];

                if (!BoardUtils.isValidMove(newR, newC, current.grid, dir)) continue;   //check if the move can happen
                if (DeadlockDetector.isDeadlock(current.grid)) continue;                //check if the moves causes deadlock

                char[][] newGrid = BoardUtils.updateGrid(newR, newC, BoardUtils.copyGrid(current.grid), dir);
                Node child = new Node(newGrid, newR, newC, current, current.g + 1, HeuristicEvaluator.heuristic(newGrid));

                if (!visited.contains(child)) {
                    visited.add(child);
                    open.add(child);
                }
            }
        }
        System.out.println("Not possible to find solution. Time: " + (System.currentTimeMillis() - startTime) + " ms");
    }



    static void printSolutionPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node cur = goal;
        while (cur != null) {
            path.add(cur);
            cur = cur.parent;
        }
        Collections.reverse(path);
        int step = 0;
        for (Node n : path) {
            System.out.println("Step " + (step++));
            n.print();
            System.out.println("h=" + n.h);
            System.out.println();
            System.out.println("f=" + n.f);
            System.out.println();
            System.out.println("----------------------");
        }
    }

}
