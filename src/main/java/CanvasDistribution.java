import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.awt.geom.Point2D;

/**
 * Created by kacper on 25.01.2016.
 */


enum WhichPoint {
    X, Y
}
/*
Class that takes care of sampling proper point.

 */
public class CanvasDistribution {


    private NormalDistribution distribution;



    private double height;
    private double width;
    private double radius;

    public CanvasDistribution(double height, double width, double radius, NormalDistribution distribution) {
        this.height = height;
        this.width = width;
        this.radius = radius;
        this.distribution = distribution;
    }

    public Double getValidSample(WhichPoint point) {

        Double value;
        double radius = 2 * this.radius;
        value = distribution.sample();
        if (point == WhichPoint.X && value > (width - radius))  {
                double diff = value - (width -  radius);
                value = width - radius - diff;
        }
        else if (point == WhichPoint.Y && value > (height - radius)) {
                double diff = value - (height - radius);
                value = height- radius - diff;
        }
        else if (value < radius) {
            double diff = radius - value;
            value = radius + diff;
        }
        if (point == WhichPoint.X) {

            assert (value <= (width - radius));
        }
        if (point == WhichPoint.Y) {

            assert (value  <= (height - radius));
        }

        if (value < radius) {
            System.out.println("Value!!! less" + value);
        }
        assert(value >= radius);

        return value;

    }
}
