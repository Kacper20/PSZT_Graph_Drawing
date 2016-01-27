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
import java.util.List;

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
        List<PSZTEdge> edges = graph.getEdges();
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
        List<PSZTVertex> vertices = graph.getVertices();
        for (PSZTVertex v : vertices) {
            Double x = v.getX();
            Double y = v.getY();
            svgRoot.appendChild(svgDraw.circle(x, y, radius));
            svgRoot.appendChild(svgDraw.textCentered(x, y, radius / 2, v.getId()));
        }
    }

    public SVGDraw getSvgDraw() {
        return svgDraw;
    }
}
