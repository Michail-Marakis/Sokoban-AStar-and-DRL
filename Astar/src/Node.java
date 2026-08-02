import java.util.Arrays;
import java.util.Objects;

public class Node {

    char[][] grid;

    int playerRow;
    int playerCol;

    Node parent;

    int h;
    int g;
    int f;

    Node(
            char[][] grid,
            int row,
            int column,
            Node parent,
            int g,
            int h
    ) {
        this.grid = copyGrid(grid);
        this.playerRow = row;
        this.playerCol = column;
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    private static char[][] copyGrid(char[][] original) {
        char[][] copy = new char[original.length][];

        for (int row = 0; row < original.length; row++) {
            copy[row] = original[row].clone();
        }

        return copy;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Node)) {
            return false;
        }

        Node other = (Node) object;

        return playerRow == other.playerRow
                && playerCol == other.playerCol
                && Arrays.deepEquals(grid, other.grid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(playerRow, playerCol);
        result = 31 * result + Arrays.deepHashCode(grid);
        return result;
    }

    void print() {
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
    }
}