import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static final int[][] DIRECTIONS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            LevelSelectionFrame frame =
                    new LevelSelectionFrame();

            frame.setVisible(true);
        });
    }
}