import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

/**
 * Represents a space on the board. This includes both the JavaFX parts
 * and the logical parts.
 */
public class BoardSpace {
    // Stackpane to stack the rectangle, shape, and border on top of each other
    private final StackPane stackPane;
    // Rectangle that represents the background of the space
    private final Rectangle rect;
    // What row is this space in
    private final int row;
    // What col is this space in
    private final int col;
    // What squid lives here, this can be null
    private Squid squid;
    // The highlight around the border of the space, this can be null
    private Rectangle highlight;

    // Telescoping constructors
    public BoardSpace(Rectangle rect, int row, int col) {
        this(rect, row, col, null);
    }

    public BoardSpace(Rectangle rect, int row, int col, Squid squid) {
        this(new StackPane(), rect, row, col, squid, null);
    }

    public BoardSpace(StackPane stackPane, Rectangle rect, int row, int col, Squid squid, Rectangle highlight) {
        this.stackPane = stackPane;
        this.rect = rect;
        this.row = row;
        this.col = col;
        this.squid = squid;
        this.highlight = highlight;

        reDraw();
    }

    public BoardSpace(Squid newSquid, BoardSpace boardSpace) {
        this(
                null,
                null,
                boardSpace.row,
                boardSpace.col,
                newSquid,
                null
        );
    }

    /**
     * This function recomputes the JavaFX representation
     * of this space
     */
    private void reDraw() {
        if (stackPane != null) {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(rect);
            if (squid != null) {
                stackPane.getChildren().add(squid.getShape());
            }
            if (highlight != null) {
                stackPane.getChildren().add(highlight);
            }
        }
    }

    // Getters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Squid getSquid() {
        return squid;
    }

    public StackPane getPicture() {
        return stackPane;
    }

    // Setters
    public void setSquid(Squid squid) {
        this.squid = squid;
        reDraw();
    }

    // Helpers
    public boolean containsSquid() {
        return squid != null;
    }

    public void toggleHighlight() {
        if (highlight == null) {
            highlight = new Rectangle(rect.getWidth() - 5, rect.getHeight() - 5, Color.TRANSPARENT);
            highlight.setStyle("-fx-stroke: " + squid.getTeam() + "; -fx-stroke-width: 5;");
            stackPane.getChildren().add(highlight);
        }
        else {
            stackPane.getChildren().remove(highlight);
            highlight = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardSpace space = (BoardSpace) o;
        return row == space.row && col == space.col && Objects.equals(squid, space.squid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, squid);
    }

    @Override
    public String toString() {
        return "BoardSpace{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
