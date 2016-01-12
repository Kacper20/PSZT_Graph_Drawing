import java.util.ArrayList;

/**
 * Created by kacper on 11.01.2016.
 */

public class PSZTGraph implements Cloneable {

    private ArrayList<PSZTVertex> vertices;
    private ArrayList<PSZTEdge> edges;

    public ArrayList<PSZTVertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<PSZTVertex> vertices) {
        this.vertices = vertices;
    }

    public ArrayList<PSZTEdge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<PSZTEdge> edges) {
        this.edges = edges;
    }

    public PSZTGraph(ArrayList<PSZTVertex> vertices, ArrayList<PSZTEdge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    @Override
    protected Object clone() {



        return new PSZTGraph((ArrayList<PSZTVertex>)this.vertices.clone(), (ArrayList<PSZTEdge>) this.edges.clone());


    }
}
