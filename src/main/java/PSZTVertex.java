/**
 * Created by kacper on 11.01.2016.
 */
public class PSZTVertex implements Cloneable{
    private String id;
    private Double x;
    private Double y;

    public PSZTVertex(String id, Double x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    protected Object clone() {

        Double newX = x != null ? Double.valueOf(x) : null;
        Double newY = y != null ? Double.valueOf(y) : null;

        return new PSZTVertex(id,newX,newY);
    }
}
