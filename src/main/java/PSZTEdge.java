/**
 * Created by kacper on 11.01.2016.
 */
public class PSZTEdge implements Cloneable{
    private PSZTVertex from;
    private PSZTVertex to;
    private double pointX;
    private double pointY;

    public PSZTEdge(PSZTVertex from, PSZTVertex to, double pointX, double pointY) {
        this.from = from;
        this.to = to;
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public PSZTVertex getFrom() {
        return from;
    }

    public void setFrom(PSZTVertex from) {
        this.from = from;
    }

    public PSZTVertex getTo() {
        return to;
    }

    public void setTo(PSZTVertex to) {
        this.to = to;
    }

    public double getPointX() {
        return pointX;
    }

    public void setPointX(double pointX) {
        this.pointX = pointX;
    }

    public double getPointY() {
        return pointY;
    }

    public void setPointY(double pointY) {
        this.pointY = pointY;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public double getCircleRadius() {

        PVector point = new PVector(from.getX(), from.getY());
        return triangleFromBow().GetCircumcenter().dist(point);
    }

    public Triangle triangleFromBow() {
        PVector p1 = new PVector(from.getX(), from.getY());
        PVector p2 = new PVector(to.getX(), to.getY());
        PVector p3 = new PVector(pointX, pointY);

        PVector[] arr = new PVector[] {p1, p2, p3};
        return new Triangle(arr);
    }
}
