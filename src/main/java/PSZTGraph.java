
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kacper on 11.01.2016.
 */

public class PSZTGraph implements Cloneable {

    private List<PSZTVertex> vertices;
    private List<PSZTEdge> edges;

    public List<PSZTVertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<PSZTVertex> vertices) {
        this.vertices = vertices;
    }

    public List<PSZTEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<PSZTEdge> edges) {
        this.edges = edges;
    }

    public PSZTGraph(List<PSZTVertex> vertices, List<PSZTEdge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }


    public List<PSZTEdge> incidentToVertex(PSZTVertex vertex) {

        return this.edges.stream().filter(e -> e.getTo().getId().equals(vertex.getId()) ||
                e.getFrom().getId().equals(vertex.getId())).collect(Collectors.toList());

    }
    @Override
    protected Object clone() {
        Map<PSZTVertex, PSZTVertex> oldToNew = new HashMap<>();

        List<PSZTVertex> newVertices = new ArrayList<>();
        for (PSZTVertex v: this.vertices) {
            PSZTVertex clonedV = (PSZTVertex) v.clone();
            oldToNew.put(v, clonedV);
            newVertices.add(clonedV);
        }

        List<PSZTEdge> newEdges = new ArrayList<>();
        for (PSZTEdge e: this.edges) {
            PSZTEdge clonedE = (PSZTEdge) e.clone();
            PSZTVertex correctFrom = oldToNew.get(e.getFrom());
            PSZTVertex correctTo = oldToNew.get(e.getTo());
            clonedE.setFrom(correctFrom);
            clonedE.setTo(correctTo);
            newEdges.add(clonedE);
        }

        return new PSZTGraph(newVertices, newEdges);
    }
//TODO: read x and y values too.
    public PSZTGraph(Graph graph) {

        List<PSZTEdge> psztEdges = new ArrayList<>();
        List<PSZTVertex> psztVertexes = new ArrayList<>();

        Iterator<Vertex> vertexIterator = graph.vertices();


        while (vertexIterator.hasNext()) {
            Vertex v = vertexIterator.next();
            String id = String.valueOf(v.id());

            Double x = null;
            Double y = null;
            if (v.property("x").isPresent()) {
                x = (double)v.property("x").value();
            }
            if (v.property("y").isPresent()) {
                y = (double)v.property("y").value();
            }
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
        }

        this.vertices = psztVertexes;
        this.edges = psztEdges;
    }

    public static PSZTVertex vertexOfId(List<PSZTVertex> list, String id) {
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
