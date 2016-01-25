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

        if (point == WhichPoint.X) {
            do {
                value = distribution.sample();
            } while (value > width - radius * 2.0  || value < radius * 2.0);
            assert(value <= width - radius * 2.0 && value >= radius * 2.0);

        }
        else {
            do {
                value = distribution.sample();
            } while (value > height - radius * 2.0 || value < radius * 2.0);
            assert(value <= height - radius * 2.0 && value >= radius * 2.0);

        }

        return value;

    }
}
