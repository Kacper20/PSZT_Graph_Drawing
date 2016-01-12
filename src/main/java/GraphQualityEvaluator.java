import org.apache.tinkerpop.gremlin.structure.Graph;

import java.util.ArrayList;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphQualityEvaluator {

    private GraphQualityArguments arguments;
    public GraphQualityEvaluator(GraphQualityArguments arguments) {
        this.arguments = arguments;

    }

    int qualityOfGraph(PSZTGraph graph) {

        ArrayList<PSZTEdge> edges = graph.getEdges();
        int length = edges.size();

        for (int i = 0; i < length; i++) {

            for (int j = i + 1; j < length; i++) {












            }
        }





        return 0;
    }
}
