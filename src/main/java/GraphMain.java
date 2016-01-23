import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphMain {


    public static void main(String[] args) throws IOException {

        MainWindow w = new MainWindow();

        Document testingDocument = GraphToSVGConverter.getTestingDocument();

        w.getSvgCanvas().setDocument(testingDocument);


        try {
            Graph graphToDraw = TinkerGraph.open();
            graphToDraw.io(IoCore.graphml()).readGraph("input.xml");

            PSZTGraph ourGraph = new PSZTGraph(graphToDraw);











            Graph converted = PSZTGraph.GraphFromPSZTGraph(ourGraph);
            converted.io(IoCore.graphml()).writeGraph("converted.xml");
            System.out.println(ourGraph.getVertices().size());




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void testTriangle (){

    }
}
