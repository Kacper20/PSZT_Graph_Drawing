import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.lang.NotImplementedException;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by konrad on 11.01.16.
 */
public class GraphToSVGConverter {
    private Graph graph;
    private Double radius;
    private SVGDraw svgDraw;

    public GraphToSVGConverter(Graph graph, Integer canvasWidth, Integer canvasHeight, Double radius) {
        this.graph = graph;
        this.radius = radius;
        this.svgDraw = new SVGDraw(canvasWidth, canvasHeight, "Graf", "#ADFF2F", "#FFD700", 4);
    }

    public void doTheMagic() {
        Element svgRoot = svgDraw.getDoc().getDocumentElement();

        // Iterate over edges
        Iterator<Edge> edgeIterator = graph.edges();
        Edge e;
        while (edgeIterator.hasNext()) {
            e = edgeIterator.next();
            Vertex v1 = e.inVertex();
            Double v1x = (Double) v1.property("x").value();
            Double v1y = (Double) v1.property("y").value();

            Vertex v2 = e.outVertex();
            Double v2x = (Double) v2.property("x").value();
            Double v2y = (Double) v2.property("y").value();

            svgRoot.appendChild(svgDraw.line(v1x, v1y, v2x, v2y));
        }

        // Iterate over vertices
        Iterator<Vertex> verticesIterator = graph.vertices();
        Vertex v;
        while (verticesIterator.hasNext()) {
            v = verticesIterator.next();
            Double x = (Double) v.property("x").value();
            Double y = (Double) v.property("y").value();
            svgRoot.appendChild(svgDraw.circle(x, y, radius));
        }
    }

    public static Graph getTestingGraph() {
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

        return g;
    }

    public static Document getTestingDocument() throws IOException {
        Graph testingGraph = getTestingGraph();
        GraphToSVGConverter conv = new GraphToSVGConverter(testingGraph, 600, 400, 30.);
        conv.doTheMagic();
        Document doc = conv.getSvgDraw().getDoc();
        return doc;
    }

    public static void main(String[] args) throws IOException, TranscoderException {
        Graph testingGraph = getTestingGraph();
        GraphToSVGConverter graphToSVGConverter = new GraphToSVGConverter(testingGraph, 600, 400, 30.);
        graphToSVGConverter.doTheMagic();
        try {
            graphToSVGConverter.getSvgDraw().toSVG(new FileOutputStream("/tmp/test456.svg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SVGDraw getSvgDraw() {
        return svgDraw;
    }
}
