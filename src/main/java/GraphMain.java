import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphMain {


    public static void main(String[] args) {



        try {
            Graph graphToDraw = TinkerGraph.open();
            graphToDraw.io(IoCore.graphml()).readGraph("input.xml");


//
////            graph.io(IoCore.graphml()).writeGraph("test.xml");
//            GraphEvolutionGenerator generator = new GraphEvolutionGenerator(graphToDraw);
//
//            GraphQualityEvaluator evaluator = new GraphQualityEvaluator(maxNumberOfIterations, desiredQuality);
//            Graph newEvolution;
//            while (true) {
//                newEvolution = generator.getGraphWithCoordinates();
//                if (evaluator.isGraphGoodEnough(newEvolution)) { break; }
//                else {
//                    GraphDrawer.draw(newEvolution);
//                }
//            }
//
//            //Draw final graph and write it to output file.
//            newEvolution.io(IoCore.graphml()).writeGraph("output.xml");
//            GraphDrawer.draw(newEvolution);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
