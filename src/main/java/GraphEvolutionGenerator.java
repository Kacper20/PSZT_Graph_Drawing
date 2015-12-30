import org.apache.tinkerpop.gremlin.structure.Graph;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphEvolutionGenerator {

    private final Graph graph;

    private Graph currentGraphWithCoordinates;
    public GraphEvolutionGenerator(Graph graph) {
        this.graph = graph;
        currentGraphWithCoordinates = generateStartingPopulation(this.graph);
    }

    private Graph generateStartingPopulation(Graph startingGraph) {

        //TODO generate
        return  startingGraph;
    }
    //Function that takes
    Graph getGraphWithCoordinates() {
        //TODO: Mutate currentGraphWithCoordinates...

        return currentGraphWithCoordinates;


    }

}
