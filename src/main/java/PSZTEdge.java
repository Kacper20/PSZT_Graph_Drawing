/**
 * Created by kacper on 11.01.2016.
 */
public class PSZTEdge implements Cloneable {
    private PSZTVertex from;
    private PSZTVertex to;
    private Double pointX;
    private Double pointY;
    private String id;

    public PSZTEdge(PSZTVertex from, PSZTVertex to, Double pointX, Double pointY, String id) {
        this.from = from;
        this.to = to;
        this.pointX = pointX;
        this.pointY = pointY;
        this.id = id;
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


    public String getId() {
        return id;
    }

    @Override
    protected Object clone() {
        // Does not clone vertices! Only values!
        Double newPointX = pointX != null ? Double.valueOf(pointX) : null;
        Double newPointY = pointY != null ? Double.valueOf(pointY) : null;
        return new PSZTEdge(null, null, newPointX, newPointY, "" + id);
    }
}
