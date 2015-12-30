import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphMain {


    public static void main(String[] args) {

        Integer maxNumberOfIterations = 10;
        Integer desiredQuality = 20;

        try {
            Graph graphToDraw = TinkerGraph.open();

//            Iterator iter = graph.vertices();
//
//            Vertex v;
//            while (iter.hasNext()) {
//                v = (Vertex)iter.next();
//                v.property("x", 200.0);
//                v.property("y", 200.0);
//            }


//            graph.io(IoCore.graphml()).writeGraph("test.xml");

            GraphEvolutionGenerator generator = new GraphEvolutionGenerator(graphToDraw);
            GraphVQualityEvaluator evaluator = new GraphVQualityEvaluator(10, 10);
            Graph newEvolution;
            while (true) {
                newEvolution = generator.getGraphWithCoordinates();
                if (evaluator.isGraphGoodEnough(newEvolution)) { break; }
                else {
                    GraphDrawer.draw(newEvolution);
                }

            } 
            //Draw final graph and write it to output file.

            newEvolution.io(IoCore.graphml()).writeGraph("output.xml");
            GraphDrawer.draw(newEvolution);




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
