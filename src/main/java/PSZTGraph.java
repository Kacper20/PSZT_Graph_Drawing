import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    public List<PSZTEdge> incidentToVertex(PSZTVertex vertex) {

        return this.edges.stream().filter(e -> e.getTo().equals(vertex) || e.getFrom().equals(vertex)).collect(Collectors.toList());

    }
    @Override
    protected Object clone() {



        return new PSZTGraph((ArrayList<PSZTVertex>)this.vertices.clone(), (ArrayList<PSZTEdge>) this.edges.clone());


    }
}
