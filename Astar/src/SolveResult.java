import java.util.Collections;
import java.util.List;

public class SolveResult {

    private final boolean solved;
    private final List<Node> path;
    private final String message;
    private final long elapsedTimeMs;
    private final int visitedStates;

    private SolveResult(
            boolean solved,
            List<Node> path,
            String message,
            long elapsedTimeMs,
            int visitedStates
    ) {
        this.solved = solved;
        this.path = path;
        this.message = message;
        this.elapsedTimeMs = elapsedTimeMs;
        this.visitedStates = visitedStates;
    }

    public static SolveResult success(
            List<Node> path,
            long elapsedTimeMs,
            int visitedStates
    ) {
        return new SolveResult(
                true,
                path,
                "Solution found.",
                elapsedTimeMs,
                visitedStates
        );
    }

    public static SolveResult failure(
            String message,
            long elapsedTimeMs
    ) {
        return new SolveResult(
                false,
                Collections.emptyList(),
                message,
                elapsedTimeMs,
                0
        );
    }

    public boolean isSolved() {
        return solved;
    }

    public List<Node> getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public long getElapsedTimeMs() {
        return elapsedTimeMs;
    }

    public int getVisitedStates() {
        return visitedStates;
    }
}