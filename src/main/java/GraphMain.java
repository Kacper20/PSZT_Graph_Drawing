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

        PSZTGraphGenerator graphGenerator = new PSZTGraphGenerator(0.5, 8);

        PSZTGraph ourGraph = graphGenerator.generateGraph();
        w.setPsztGraph(ourGraph);
    }
}
