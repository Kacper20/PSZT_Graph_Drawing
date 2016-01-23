import org.apache.batik.transcoder.TranscoderException;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by konrad on 11.01.16.
 */
public class PSZTGraphToSVGConverter {
    private PSZTGraph graph;
    private Double radius;
    private SVGDraw svgDraw;

    public PSZTGraphToSVGConverter(PSZTGraph graph, Integer canvasWidth, Integer canvasHeight, Double radius) {
        this.graph = graph;
        this.radius = radius;
        this.svgDraw = new SVGDraw(canvasWidth, canvasHeight, "Graf", "#ADFF2F", "#FFD700", 4);
    }

    public void doTheMagic() {
        Element svgRoot = svgDraw.getDoc().getDocumentElement();

        // Iterate over edges
        ArrayList<PSZTEdge> edges = graph.getEdges();
        for (PSZTEdge e : edges) {
            PSZTVertex v1 = e.getFrom();
            Double v1x = v1.getX();
            Double v1y = v1.getY();

            PSZTVertex v2 = e.getTo();
            Double v2x = v2.getX();
            Double v2y = v2.getY();

            svgRoot.appendChild(svgDraw.line(v1x, v1y, v2x, v2y));
        }

        // Iterate over vertices
        ArrayList<PSZTVertex> vertices = graph.getVertices();
        for (PSZTVertex v: vertices) {
            Double x = v.getX();
            Double y = v.getY();
            svgRoot.appendChild(svgDraw.circle(x, y, radius));
        }
    }

    public static PSZTGraph getTestingGraph() {
        Graph g = TinkerGraph.open();
        Vertex v1 = g.addVertex("first");
        v1.property("x", 100.);
        v1.property("y", 200.);

        Vertex v2 = g.addVertex("first");
        v2.property("x", 200.);
        v2.property("y", 100.);

        Edge e1 = v1.addEdge("edge", v2);

        Vertex v3 = g.addVertex("first");
        v3.property("x", 400.);
        v3.property("y", 200.);

        Vertex v4 = g.addVertex("first");
        v4.property("x", 500.);
        v4.property("y", 100.);

        Edge e2 = v4.addEdge("edge", v3);

        return new PSZTGraph(g);
    }

    public static Document getTestingDocument() throws IOException {
        PSZTGraph testingGraph = getTestingGraph();
        PSZTGraphToSVGConverter conv = new PSZTGraphToSVGConverter(testingGraph, 600, 400, 30.);
        conv.doTheMagic();
        Document doc = conv.getSvgDraw().getDoc();
        return doc;
    }

    public static void main(String[] args) throws IOException, TranscoderException {
        PSZTGraph testingGraph = getTestingGraph();
        PSZTGraphToSVGConverter PSZTGraphToSVGConverter = new PSZTGraphToSVGConverter(testingGraph, 600, 400, 30.);
        PSZTGraphToSVGConverter.doTheMagic();
        try {
            PSZTGraphToSVGConverter.getSvgDraw().toSVG(new FileOutputStream("/tmp/test456newest.svg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SVGDraw getSvgDraw() {
        return svgDraw;
    }
}
