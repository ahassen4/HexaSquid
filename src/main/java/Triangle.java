import javafx.scene.shape.Polygon;

/**
 * This class contains a single static function to draw an
 * equilateral triangle centered at a given point with a given
 * side length.
 */
public class Triangle {
    /**
     * This function does exactly as it states. Namely, it draws an equilateral
     * triangle centered at the given points with the given side length.
     *
     * @param centerX    X coordinate of the center of the triangle
     * @param centerY    Y coordinate of the center of the triangle
     * @param sideLength Side length of each of the sides
     * @return The triangle represented as a polygon
     */
    // TODO: Fill in this function using the above logic
    public static Polygon equilateralTriangleCenteredOn(double centerX, double centerY, double sideLength) {


        double x1 = sideLength * (Math.cos(-90 * Math.PI / 180)) / Math.sqrt(3) + centerX;
        double x2 = sideLength * -(Math.cos(-210 * Math.PI / 180)) / Math.sqrt(3) + centerX;
        double x3 = sideLength * (Math.cos(-210 * Math.PI / 180) )/ Math.sqrt(3) + centerX;

        double y1 = sideLength * (Math.sin(-90 * Math.PI / 180)) / Math.sqrt(3) + centerY;
        double y2 = sideLength * (Math.sin(-210 * Math.PI / 180)) / Math.sqrt(3) + centerY;
        double y3 = sideLength * (Math.sin(-210 * Math.PI / 180)) / Math.sqrt(3) + centerY;

        Polygon equaTriangle = new Polygon(x1, y1, x2, y2, x3, y3);


        // to find X spit into 3
        // to find Y spit into 3



        return equaTriangle;
    }
}
