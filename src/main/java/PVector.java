import static java.lang.Math.*;

/**
 * Created by kacper on 12.01.2016.
 */
public class PVector {

    public double x;
    public double y;


    public PVector(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public float dist(PVector vector) {
        double distanceX = vector.x - this.x;
        double distanceY = vector.y - this.y;
        return (float) sqrt(distanceX * distanceX + distanceY * distanceY);
    }
}
