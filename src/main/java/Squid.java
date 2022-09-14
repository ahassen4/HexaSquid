import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.Objects;

/**
 * Simple class to represent the shapes on the board
 */
public class Squid {
    // Simple enum to represent the two players
    public enum Team {
        WHITE(Color.WHITE), BLACK(Color.BLACK);

        private final Color color;

        Team(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
    // Simple enum to represent the type of shapes, used for hashing
    public enum ShapeType {
        CIRCLE, TRIANGLE, SQUARE
    }

    private final Team team;
    private final ShapeType shapeType;
    private final Shape shape;

    public Squid(Team team, ShapeType shapeType, Shape shape) {
        this.team = team;
        this.shapeType = shapeType;
        this.shape = shape;
    }

    public Team getTeam() {
        return team;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Squid squid = (Squid) o;
        return team == squid.team && shapeType == squid.shapeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, shapeType);
    }
}
