
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.ArrayList;
import java.util.Iterator;
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
//TODO: read x and y values too.
    public PSZTGraph(Graph graph) {

        ArrayList<PSZTEdge> psztEdges = new ArrayList<>();
        ArrayList<PSZTVertex> psztVertexes = new ArrayList<>();

        Iterator<Vertex> vertexIterator = graph.vertices();


        while (vertexIterator.hasNext()) {
            Vertex v = vertexIterator.next();
            String id = String.valueOf(v.id());

            double x = (Double)v.property("x").value();
            double y = (Double)v.property("y").value();
            PSZTVertex vertex = new PSZTVertex(id, x, y);
            psztVertexes.add(vertex);
        }



        Iterator<Edge> edgeIterator = graph.edges();
        while (edgeIterator.hasNext()) {

            Edge e = edgeIterator.next();
            Vertex in = e.inVertex();
            String inVertexId = in.id().toString();

            PSZTVertex vIn = vertexOfId(psztVertexes, inVertexId);
            assert(vIn != null);


            Vertex out = e.outVertex();
            String outVertexId = out.id().toString();
            PSZTVertex vOut = vertexOfId(psztVertexes, outVertexId);
            assert(vOut != null);

            String edgeId = e.id().toString();

            PSZTEdge psztEdge = new PSZTEdge(vIn, vOut, null, null,edgeId);
            psztEdges.add(psztEdge);
            Vertex v  = e.inVertex();

        }

        this.vertices = psztVertexes;
        this.edges = psztEdges;

    }
    public static PSZTVertex vertexOfId(ArrayList<PSZTVertex> list, String id) {
        for (PSZTVertex vertex : list) {
            if(vertex.getId().equals(id)) { return vertex; }
        }
        return null;
    }


    public static Graph GraphFromPSZTGraph(PSZTGraph psztGraph) {

        Graph newGraph = TinkerGraph.open();
        for (PSZTVertex vertex: psztGraph.getVertices()) {
            Vertex graphVertex = newGraph.addVertex(T.id, vertex.getId());
            graphVertex.property("x", vertex.getX());
            graphVertex.property("y", vertex.getY());
        }

        for (PSZTEdge edge: psztGraph.getEdges()) {

            PSZTVertex psztVertexFrom = edge.getFrom();
            Vertex vertexFromInGraph = vertexOfId(newGraph, psztVertexFrom.getId());
            PSZTVertex psztVertexTo = edge.getTo();
            Vertex vertexToInGraph = vertexOfId(newGraph, psztVertexTo.getId());


            String edgeId = edge.getId();
            Edge graphEdge = vertexToInGraph.addEdge("CR", vertexFromInGraph, T.id, edgeId);
            graphEdge.property("Ox", edge.getPointX());
            graphEdge.property("Oy", edge.getPointY());
        }
        return newGraph;
    }

    private static Vertex vertexOfId(Graph graph, String id) {
        Iterator<Vertex> vertexIterator = graph.vertices();
        while(vertexIterator.hasNext()) {
            Vertex next = vertexIterator.next();
            String nextId = next.id().toString();
            if (nextId.equals(id)) {
                return next;
            }
        }
        return null;


    }

}
