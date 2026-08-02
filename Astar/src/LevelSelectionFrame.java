import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class LevelSelectionFrame extends JFrame {

    private final JComboBox<LevelItem> levelComboBox;
    private final JButton solveButton;
    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    public LevelSelectionFrame() {
        setTitle("Sokoban A* Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        levelComboBox = new JComboBox<>(new LevelItem[]{
                new LevelItem(0, "Level 0 - Very Easy"),
                new LevelItem(1, "Level 1 - Easy"),
                new LevelItem(2, "Level 2 - Medium"),
                new LevelItem(3, "Level 3 - Hard"),
                new LevelItem(4, "Level 4 - Very Hard"),
                new LevelItem(5, "Level 5 - No Solution")
        });

        solveButton = new JButton("Solve");
        statusLabel = new JLabel(
                "Choose a level and press Solve.",
                SwingConstants.CENTER
        );

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        JPanel selectionPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        selectionPanel.add(new JLabel("Choose level:"));
        selectionPanel.add(levelComboBox);
        selectionPanel.add(solveButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(
                new BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        );

        mainPanel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(selectionPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(statusLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(progressBar);

        add(mainPanel);

        solveButton.addActionListener(event -> solveSelectedLevel());

        pack();
        setMinimumSize(new Dimension(500, 180));
        setLocationRelativeTo(null);
    }

    private void solveSelectedLevel() {
        LevelItem selectedLevel =
                (LevelItem) levelComboBox.getSelectedItem();

        if (selectedLevel == null) {
            return;
        }

        setSearchingState(true);

        SwingWorker<SolveResult, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected SolveResult doInBackground() {
                        return AstarAlgorithm.solve(
                                selectedLevel.getLevelNumber()
                        );
                    }

                    @Override
                    protected void done() {
                        setSearchingState(false);

                        try {
                            SolveResult result = get();
                            handleResult(result);

                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();

                            statusLabel.setText(
                                    "The search was interrupted."
                            );

                        } catch (ExecutionException exception) {
                            Throwable cause = exception.getCause();

                            String message;

                            if (cause == null
                                    || cause.getMessage() == null) {
                                message = "An unexpected error occurred.";
                            } else {
                                message = cause.getMessage();
                            }

                            statusLabel.setText("Solver error.");

                            JOptionPane.showMessageDialog(
                                    LevelSelectionFrame.this,
                                    message,
                                    "Solver error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                };

        worker.execute();
    }

    private void handleResult(SolveResult result) {
        if (!result.isSolved()) {
            statusLabel.setText(
                    "No solution found in "
                            + result.getElapsedTimeMs()
                            + " ms."
            );

            JOptionPane.showMessageDialog(
                    this,
                    result.getMessage()
                            + "\nTime: "
                            + result.getElapsedTimeMs()
                            + " ms",
                    "No solution",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int moves = result.getPath().size() - 1;

        statusLabel.setText(
                "Solution found: "
                        + moves
                        + " moves in "
                        + result.getElapsedTimeMs()
                        + " ms."
        );

        SokobanSolutionViewer viewer =
                new SokobanSolutionViewer(result.getPath());

        viewer.setVisible(true);
    }

    private void setSearchingState(boolean searching) {
        solveButton.setEnabled(!searching);
        levelComboBox.setEnabled(!searching);
        progressBar.setVisible(searching);

        if (searching) {
            statusLabel.setText(
                    "Searching for a solution..."
            );
        }
    }

    private static class LevelItem {

        private final int levelNumber;
        private final String description;

        private LevelItem(
                int levelNumber,
                String description
        ) {
            this.levelNumber = levelNumber;
            this.description = description;
        }

        public int getLevelNumber() {
            return levelNumber;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}