import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.javatuples.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;




public class GraphEvolutionGenerator {

    private final Graph graph;

    private Graph currentGraphWithCoordinates;
    private ArrayList<Graph> population;
    private int populationSize;
    private int canvasWidth;
    private int canvasHeight;
    private double vertexDiameter;

    private GraphQualityEvaluator evaluator;



    /*
        Parametry:
        populacja -> parametr



     */








    public GraphEvolutionGenerator(Graph graph, int populationSize, int canvasWidth, int canvasHeight, double vertexDiameter){
        this.graph = graph;
        this.populationSize = populationSize;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.vertexDiameter = vertexDiameter;

        this.evaluator = new GraphQualityEvaluator(new GraphQualityArguments(20));
        population = generateStartingPopulation(graph, populationSize);


    }
    private Graph generateRandomGraph(Graph graph) {


        Iterator<Vertex> verticesIterator = graph.vertices();
        while (verticesIterator.hasNext()) {

            Vertex next = verticesIterator.next();
            double randomX =  ThreadLocalRandom.current().nextDouble(0, canvasHeight);
            double randomY = ThreadLocalRandom.current().nextDouble(0, canvasHeight);
            next.property("x", randomX);
            next.property("y", randomY);
        }

        Iterator<Edge> edgesIterator = graph.edges();
        while (edgesIterator.hasNext()) {
            Edge edge = edgesIterator.next();

            double randomX = ThreadLocalRandom.current().nextDouble(- canvasWidth, 2 * canvasWidth);
            double randomY = ThreadLocalRandom.current().nextDouble(- canvasHeight, 2 * canvasHeight);

            edge.property("Ox", randomX);
            edge.property("Oy", randomY);
        }
        return graph;


    }

    private ArrayList<Graph> generateStartingPopulation(Graph startingGraph, int size) {
        ArrayList<Graph> list = new ArrayList<Graph>();

        for (int i = 0; i < size; i++) {
            list.add(generateRandomGraph(startingGraph));
        }
        return list;

    }

    //Function that takes

    synchronized ArrayList<Graph> generateNextPopulation() {

        ArrayList<Pair<Graph, Double>> list = new ArrayList<Pair<Graph, Double>>();

        for (Graph g: population) {
            double quality = evaluator.qualityOfGraph(g);
            Pair<Graph, Double> newPair = new Pair<Graph, Double>(g, quality);
            list.add(newPair);
        }
        list.sort(new Comparator<Pair<Graph, Double>>() {
            public int compare(Pair<Graph, Double> o1, Pair<Graph, Double> o2) {
                if (o1.getValue1() > o2.getValue1()) { return 1; }
                else if (o1.getValue1() < o2.getValue1()) { return -1; }
                return 0;
            }
        });

        return new ArrayList<Graph>();













    }

    synchronized Graph stopAlgorithm() {
        return getBestGraphFromCurrentPopulation();



    }


    Graph getBestGraphFromCurrentPopulation() {






        return population.get(0);
    }




    Graph getGraphWithCoordinates() {
        //TODO: Mutate currentGraphWithCoordinates...

        return currentGraphWithCoordinates;

    }






}
