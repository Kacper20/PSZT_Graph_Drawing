import org.apache.tinkerpop.gremlin.structure.Graph;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphVQualityEvaluator {

    private int maxNumberOfIterations;
    private int desiredQuality;
    private int currentNumberOfIterations;

    public GraphVQualityEvaluator(int maxNumberOfIterations, int desiredQuality) {
        this.maxNumberOfIterations = maxNumberOfIterations;
        this.desiredQuality = desiredQuality;
        this.currentNumberOfIterations = 0;
    }

    public boolean isGraphGoodEnough(Graph graph) {
        currentNumberOfIterations += 1;
        return currentNumberOfIterations > maxNumberOfIterations || quality(graph) >= desiredQuality;
    }
    private static int quality(Graph graph) {
        return 0;
    }
}
