import org.apache.batik.dom.svg.SVGDOMImplementation;
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
    private Integer canvasWidth, canvasHeight;
    private Double radius;

    public GraphToSVGConverter(Graph graph, Integer canvasWidth, Integer canvasHeight, Double radius) {
        this.graph = graph;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.radius = radius;
    }

    public static void saveDocumentToSVG(Document d, String filename) throws IOException {
        FileOutputStream ps = new FileOutputStream(filename);
        org.apache.xml.serialize.OutputFormat of =
                new org.apache.xml.serialize.OutputFormat("XML", "ISO-8859-1", true);
        of.setIndent(1);
        of.setIndenting(true);
        org.apache.xml.serialize.XMLSerializer serializer =
                new org.apache.xml.serialize.XMLSerializer(ps, of);
        //   As a DOM Serializer
        serializer.asDOMSerializer();
        serializer.serialize(d);
    }

    public Document generateDocument() throws IOException {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document doc = impl.createDocument(svgNS, "svg", null);

        // Get the root element (the 'svg' element).
        Element svgRoot = doc.getDocumentElement();

        // Set the width and height attributes on the root 'svg' element.
        svgRoot.setAttributeNS(null, "width", canvasWidth.toString());
        svgRoot.setAttributeNS(null, "height", canvasHeight.toString());

        // ------------------------------------------
        // Iterate over vertices
        Iterator<Vertex> verticesIterator = graph.vertices();
        Vertex v;
        while (verticesIterator.hasNext()) {
            v = verticesIterator.next();
            Double x = (Double) v.property("x").value();
            Double y = (Double) v.property("y").value();
            // TODO draw circle

            Element circle = doc.createElementNS(svgNS, "circle");
            circle.setAttributeNS(null, "cx", x.toString());
            circle.setAttributeNS(null, "cy", y.toString());
            circle.setAttributeNS(null, "r", radius.toString());

            // TODO hardcoded
            circle.setAttributeNS(null, "stroke", "green");
            circle.setAttributeNS(null, "stroke-width", "4");
            circle.setAttributeNS(null, "fill", "yellow");

            svgRoot.appendChild(circle);
        }

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

//            Double ox = (Double) e.property("Ox").value();
//            Double oy = (Double) e.property("Oy").value();
            // TODO draw arc

//            x1 = 2*xc - x0/2 - x2/2
//            y1 = 2*yc - y0/2 - y2/2

//            Double hvx = 2*ox - v1x/2 - v2x/2;
//            Double hvy = 2*oy - v1y/2 - v2y/2;

            // TODO hardcoded
            Element path = doc.createElementNS(null, "line");
            path.setAttributeNS(null, "x1", v1x.toString());
            path.setAttributeNS(null, "y1", v1y.toString());
            path.setAttributeNS(null, "x2", v2x.toString());
            path.setAttributeNS(null, "y2", v2y.toString());
//            path.setAttributeNS(null, "style", "stroke:rgb(255,0,0);stroke-width:2");

            // Element path = doc.createElementNS(null, "path");
            path.setAttributeNS(null, "stroke", "blue");
            path.setAttributeNS(null, "stroke-width", "4");
            // path.setAttributeNS(null, "fill-opacity", "0");

            // bezier
            // path.setAttributeNS(null, "d", "M "+v1x+" "+v1y+"Q "+hvx+" "+hvy+" "+v2x+" "+v2y);

            // arc on circle
            //path.setAttributeNS(null, "d", "M "+v2x+","+v2y+" A "+ox+","+oy+" 0 0,1 "+v1x+","+v1y);
            // M x y A rad rad rot=0 1,0 x,y Z

            svgRoot.appendChild(path);
        }

        return doc;
    }

    public static Document getTestingDocument() throws IOException {
        Graph g = TinkerGraph.open();
        Vertex v1 = g.addVertex("first");
        v1.property("x", 100.);
        v1.property("y", 200.);

        Vertex v2 = g.addVertex("first");
        v2.property("x", 200.);
        v2.property("y", 100.);

        Edge e1 = v1.addEdge("edge", v2);
//        e1.property("Ox", 100.+100.*.707);
//        e1.property("Oy", 100.+100.*.707);
//        e1.property("r", 100.);

        Vertex v3 = g.addVertex("first");
        v3.property("x", 400.);
        v3.property("y", 200.);

        Vertex v4 = g.addVertex("first");
        v4.property("x", 500.);
        v4.property("y", 100.);

        Edge e2 = v4.addEdge("edge", v3);
//        e2.property("Ox", 400.+100.*.707);
//        e2.property("Oy", 100.+100.*.707);
//        e2.property("r", 100.);


        GraphToSVGConverter conv = new GraphToSVGConverter(g, 600, 400, 30.);
        Document doc = conv.generateDocument();

        return doc;
    }

    public static void main(String[] args) throws IOException {
        Document doc = GraphToSVGConverter.getTestingDocument();
        try {
            GraphToSVGConverter.saveDocumentToSVG(doc, "/tmp/test.svg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
