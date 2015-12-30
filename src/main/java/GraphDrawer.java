import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphDrawer {


    public static void main(String[] args) {

        try {
            final Graph graphToDraw = TinkerGraph.open();
            graphToDraw.io(IoCore.graphml()).readGraph("tinkerpop-modern.xml");
            


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
