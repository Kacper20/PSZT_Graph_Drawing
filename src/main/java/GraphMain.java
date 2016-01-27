import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.w3c.dom.Document;

import java.io.IOException;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphMain {
    public static void main(String[] args) throws IOException {
        MainWindow w = new MainWindow();

        PSZTGraphGenerator graphGenerator = new PSZTGraphGenerator(0.2, 20);


        try {
            Graph graphToDraw = TinkerGraph.open();
            graphToDraw.io(IoCore.graphml()).readGraph("converted.xml");
            PSZTGraph ourGraph = graphGenerator.generateGraph();
//            PSZTGraph ourGraph = new PSZTGraph(graphToDraw);


            w.setPsztGraph(ourGraph);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void testTriangle (){
//        PSZTGraphGenerator graphGenerator = new PSZTGraphGenerator(0.5, 20);
//
//        PSZTGraph ourGraph = graphGenerator.generateGraph();
//        w.setPsztGraph(ourGraph);
    }
}
