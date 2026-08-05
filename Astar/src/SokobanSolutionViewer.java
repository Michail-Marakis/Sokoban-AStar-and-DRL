import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SokobanSolutionViewer extends JFrame {

    private final List<Node> path;

    private int currentStep = 0;

    private final BoardPanel boardPanel;
    private final JLabel stepLabel;
    private final JLabel hLabel;
    private final JLabel gLabel;
    private final JLabel fLabel;
    private final JTextField stepField;

    private final JButton previousButton;
    private final JButton nextButton;

    public SokobanSolutionViewer(List<Node> path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException(
                    "The solution path cannot be empty."
            );
        }

        this.path = path;

        setTitle("Sokoban Solution Viewer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        boardPanel = new BoardPanel();

        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 0, 10)
        );

        add(scrollPane, BorderLayout.CENTER);

        JButton firstButton = new JButton("|<");
        previousButton = new JButton("< Previous");
        nextButton = new JButton("Next >");
        JButton lastButton = new JButton(">|");
        JButton goButton = new JButton("Go");

        stepLabel = new JLabel();
        gLabel = new JLabel();
        hLabel = new JLabel();
        fLabel = new JLabel();

        stepField = new JTextField(5);

        JPanel navigationPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 8, 5)
        );

        navigationPanel.add(firstButton);
        navigationPanel.add(previousButton);
        navigationPanel.add(stepLabel);
        navigationPanel.add(nextButton);
        navigationPanel.add(lastButton);

        JPanel jumpPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 8, 5)
        );

        jumpPanel.add(new JLabel("Go to step:"));
        jumpPanel.add(stepField);
        jumpPanel.add(goButton);

        JPanel informationPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 5)
        );

        informationPanel.add(gLabel);
        informationPanel.add(hLabel);
        informationPanel.add(fLabel);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(
                new BoxLayout(controlsPanel, BoxLayout.Y_AXIS)
        );

        controlsPanel.setBorder(
                new EmptyBorder(5, 5, 10, 5)
        );

        controlsPanel.add(navigationPanel);
        controlsPanel.add(jumpPanel);
        controlsPanel.add(informationPanel);

        add(controlsPanel, BorderLayout.SOUTH);

        firstButton.addActionListener(e -> {
            currentStep = 0;
            updateView();
        });

        previousButton.addActionListener(e -> {
            if (currentStep > 0) {
                currentStep--;
                updateView();
            }
        });

        nextButton.addActionListener(e -> {
            if (currentStep < path.size() - 1) {
                currentStep++;
                updateView();
            }
        });

        lastButton.addActionListener(e -> {
            currentStep = path.size() - 1;
            updateView();
        });

        goButton.addActionListener(e -> goToSelectedStep());

        // Pressing Enter inside the text field also changes the step.
        stepField.addActionListener(e -> goToSelectedStep());

        updateView();

        pack();
        setMinimumSize(new Dimension(700, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void goToSelectedStep() {
        try {
            int requestedStep = Integer.parseInt(
                    stepField.getText().trim()
            );

            if (requestedStep < 0 || requestedStep >= path.size()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter a step between 0 and "
                                + (path.size() - 1) + ".",
                        "Invalid step",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            currentStep = requestedStep;
            updateView();

        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid integer.",
                    "Invalid number",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateView() {
        Node node = path.get(currentStep);

        boardPanel.setNode(node);

        stepLabel.setText(
                "Step " + currentStep
                        + " / " + (path.size() - 1)
        );

        gLabel.setText("g = " + node.g);
        hLabel.setText("h = " + node.h);
        fLabel.setText("f = " + node.f);

        stepField.setText(String.valueOf(currentStep));

        previousButton.setEnabled(currentStep > 0);
        nextButton.setEnabled(currentStep < path.size() - 1);
    }

    private static class BoardPanel extends JPanel {

        private Node node;

        private static final int CELL_SIZE = 48;

        public BoardPanel() {
            setBackground(new Color(35, 35, 35));
        }

        public void setNode(Node node) {
            this.node = node;

            int rows = node.grid.length;
            int columns = 0;

            for (char[] row : node.grid) {
                columns = Math.max(columns, row.length);
            }

            setPreferredSize(
                    new Dimension(
                            columns * CELL_SIZE,
                            rows * CELL_SIZE
                    )
            );

            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            if (node == null || node.grid == null) {
                return;
            }

            Graphics2D g2 = (Graphics2D) graphics.create();

            try {
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                for (int row = 0; row < node.grid.length; row++) {
                    for (int column = 0;
                         column < node.grid[row].length;
                         column++) {

                        char tile = node.grid[row][column];

                        int x = column * CELL_SIZE;
                        int y = row * CELL_SIZE;

                        drawCell(g2, tile, x, y);
                    }
                }

            } finally {
                g2.dispose();
            }
        }

        private void drawCell(
                Graphics2D g2,
                char tile,
                int x,
                int y
        ) {
            drawFloor(g2, x, y);

            switch (tile) {
                case '#':
                    drawWall(g2, x, y);
                    break;

                case '0':
                    drawBox(g2, x, y);
                    break;

                case '$':
                    drawTarget(g2, x, y);
                    break;

                case '1':
                    drawPlayer(g2, x, y);
                    break;

                case '*':
                    drawTarget(g2, x, y);
                    drawBoxOnTarget(g2, x, y);
                    break;

                case '+':
                    drawTarget(g2, x, y);
                    drawPlayerOnTarget(g2, x, y);
                    break;

                case ' ':
                default:
                    break;
            }
        }

        private void drawPlayerOnTarget(Graphics2D g2, int x, int y) {
            int margin = 7;
            int size = CELL_SIZE - 2 * margin;

            g2.setColor(new Color(70, 170, 90));
            g2.fillOval(
                    x + margin,
                    y + margin,
                    size,
                    size
            );

            g2.setColor(new Color(25, 100, 45));
            g2.drawOval(
                    x + margin,
                    y + margin,
                    size,
                    size
            );

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    x + CELL_SIZE / 3 - 2,
                    y + CELL_SIZE / 3,
                    5,
                    5
            );

            g2.fillOval(
                    x + 2 * CELL_SIZE / 3 - 2,
                    y + CELL_SIZE / 3,
                    5,
                    5
            );

            g2.setColor(Color.BLACK);

            g2.fillOval(
                    x + CELL_SIZE / 3,
                    y + CELL_SIZE / 3 + 1,
                    2,
                    2
            );

            g2.fillOval(
                    x + 2 * CELL_SIZE / 3,
                    y + CELL_SIZE / 3 + 1,
                    2,
                    2
            );
        }

        private void drawBoxOnTarget(Graphics2D g2, int x, int y) {
            int margin = 6;
            int size = CELL_SIZE - 2 * margin;

            // Green box when it is correctly placed on a target
            g2.setColor(new Color(70, 170, 90));
            g2.fillRoundRect(
                    x + margin,
                    y + margin,
                    size,
                    size,
                    8,
                    8
            );

            g2.setColor(new Color(25, 100, 45));
            g2.drawRoundRect(
                    x + margin,
                    y + margin,
                    size,
                    size,
                    8,
                    8
            );

            // X detail
            g2.drawLine(
                    x + margin + 5,
                    y + margin + 5,
                    x + margin + size - 5,
                    y + margin + size - 5
            );

            g2.drawLine(
                    x + margin + size - 5,
                    y + margin + 5,
                    x + margin + 5,
                    y + margin + size - 5
            );

            // Small target indicator in the center
            int circleSize = 10;
            int circleX = x + (CELL_SIZE - circleSize) / 2;
            int circleY = y + (CELL_SIZE - circleSize) / 2;

            g2.setColor(new Color(240, 230, 120));
            g2.fillOval(circleX, circleY, circleSize, circleSize);

            g2.setColor(new Color(130, 110, 30));
            g2.drawOval(circleX, circleY, circleSize, circleSize);
        }

        private void drawFloor(Graphics2D g2, int x, int y) {
            g2.setColor(new Color(225, 210, 180));
            g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2.setColor(new Color(190, 175, 150));
            g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        private void drawWall(Graphics2D g2, int x, int y) {
            g2.setColor(new Color(65, 68, 75));
            g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g2.setColor(new Color(105, 108, 115));
            g2.drawRect(
                    x + 3,
                    y + 3,
                    CELL_SIZE - 6,
                    CELL_SIZE - 6
            );

            int half = CELL_SIZE / 2;

            g2.drawLine(x, y + half, x + CELL_SIZE, y + half);
            g2.drawLine(x + half, y, x + half, y + half);
            g2.drawLine(
                    x + CELL_SIZE / 4,
                    y + half,
                    x + CELL_SIZE / 4,
                    y + CELL_SIZE
            );

            g2.drawLine(
                    x + 3 * CELL_SIZE / 4,
                    y + half,
                    x + 3 * CELL_SIZE / 4,
                    y + CELL_SIZE
            );
        }

        private void drawTarget(Graphics2D g2, int x, int y) {
            int diameter = CELL_SIZE / 3;
            int offset = (CELL_SIZE - diameter) / 2;

            g2.setColor(new Color(210, 55, 55));
            g2.fillOval(
                    x + offset,
                    y + offset,
                    diameter,
                    diameter
            );

            g2.setColor(new Color(130, 30, 30));
            g2.drawOval(
                    x + offset,
                    y + offset,
                    diameter,
                    diameter
            );
        }

        private void drawBox(Graphics2D g2, int x, int y) {
            int margin = 6;
            int size = CELL_SIZE - 2 * margin;

            g2.setColor(new Color(165, 100, 42));
            g2.fillRoundRect(
                    x + margin,
                    y + margin,
                    size,
                    size,
                    8,
                    8
            );

            g2.setColor(new Color(95, 55, 25));
            g2.drawRoundRect(
                    x + margin,
                    y + margin,
                    size,
                    size,
                    8,
                    8
            );

            g2.drawLine(
                    x + margin + 5,
                    y + margin + 5,
                    x + margin + size - 5,
                    y + margin + size - 5
            );

            g2.drawLine(
                    x + margin + size - 5,
                    y + margin + 5,
                    x + margin + 5,
                    y + margin + size - 5
            );
        }

        private void drawPlayer(Graphics2D g2, int x, int y) {
            int margin = 7;
            int size = CELL_SIZE - 2 * margin;

            g2.setColor(new Color(45, 120, 220));
            g2.fillOval(
                    x + margin,
                    y + margin,
                    size,
                    size
            );

            g2.setColor(new Color(25, 70, 150));
            g2.drawOval(
                    x + margin,
                    y + margin,
                    size,
                    size
            );

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    x + CELL_SIZE / 3 - 2,
                    y + CELL_SIZE / 3,
                    5,
                    5
            );

            g2.fillOval(
                    x + 2 * CELL_SIZE / 3 - 2,
                    y + CELL_SIZE / 3,
                    5,
                    5
            );

            g2.setColor(Color.BLACK);

            g2.fillOval(
                    x + CELL_SIZE / 3,
                    y + CELL_SIZE / 3 + 1,
                    2,
                    2
            );

            g2.fillOval(
                    x + 2 * CELL_SIZE / 3,
                    y + CELL_SIZE / 3 + 1,
                    2,
                    2
            );
        }
    }
}