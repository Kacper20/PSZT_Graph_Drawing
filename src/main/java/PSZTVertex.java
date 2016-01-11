/**
 * Created by kacper on 11.01.2016.
 */
public class PSZTVertex implements Cloneable{
    private int id;
    private double x;
    private double y;

    public PSZTVertex(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
