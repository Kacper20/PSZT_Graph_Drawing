
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.javatuples.Pair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;




public class GraphEvolutionGenerator {

    private final PSZTGraph graph;

    private PSZTGraph currentGraphWithCoordinates;
    private ArrayList<PSZTGraph> population;
    private int populationSize;
    private int canvasWidth;
    private int canvasHeight;
    private double vertexDiameter;
    private double variance;
    private double mutationProbability = 0.1;
//    private double crossoverProbability = 0.3;

    private double varianceCross =  0.2;

    private int generation = 1;
    private GraphQualityEvaluator evaluator;
    public GraphEvolutionGenerator(PSZTGraph graph, GraphQualityArguments arguments, int populationSize, int canvasWidth, int canvasHeight, double vertexDiameter, double variance){
        this.graph = graph;
        this.populationSize = populationSize;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.vertexDiameter = vertexDiameter;

        this.variance = variance;
        this.evaluator = new GraphQualityEvaluator(arguments);
        population = generateStartingPopulation(graph, populationSize);


    }

    public ArrayList<PSZTGraph> getPopulation() {
        return population;
    }

    private PSZTGraph generateRandomGraph(PSZTGraph graph) {

        PSZTGraph newGraph = (PSZTGraph)graph.clone();

        for (PSZTVertex v : newGraph.getVertices()) {



            double randomX =  ThreadLocalRandom.current().nextDouble(vertexDiameter/2, canvasWidth - vertexDiameter/2);
            double randomY = ThreadLocalRandom.current().nextDouble(vertexDiameter/2, canvasHeight - vertexDiameter/2);
            v.setX(randomX);
            v.setY(randomY);
        }

        for (PSZTEdge edge: newGraph.getEdges()) {


            double midPointX = (edge.getFrom().getX() + edge.getTo().getX()) / 2.0;
            double midPointY = (edge.getFrom().getY() + edge.getTo().getY()) / 2.0;



//            double randomX = ThreadLocalRandom.current().nextDouble(- canvasWidth, 2 * canvasWidth);
//            double randomY = ThreadLocalRandom.current().nextDouble(- canvasHeight, 2 * canvasHeight);

            edge.setPointX(midPointX);
            edge.setPointY(midPointY);
        }
        return newGraph;


    }

    private ArrayList<PSZTGraph> generateStartingPopulation(PSZTGraph startingGraph, int size) {
        ArrayList<PSZTGraph> list = new ArrayList<PSZTGraph>();

        for (int i = 0; i < size; i++) {
            list.add(generateRandomGraph(startingGraph));
        }
        return list;

    }

    //Function that takes


    synchronized List<PSZTGraph> generateNextPopulation() {

        Comparator<Pair<PSZTGraph, Double>> comparator = new Comparator<Pair<PSZTGraph, Double>>() {
            public int compare(Pair<PSZTGraph, Double> o1, Pair<PSZTGraph, Double> o2) {
                if (o1.getValue1() > o2.getValue1()) { return 1; }
                else if (o1.getValue1() < o2.getValue1()) { return -1; }
                return 0;
            }
        };


        List<Pair<PSZTGraph, Double>> oldPopulation = new ArrayList<>();
        for (PSZTGraph g: population) {
            double quality = evaluator.qualityOfGraph(g);
            Pair<PSZTGraph, Double> newPair = new Pair<PSZTGraph, Double>(g, quality);

            oldPopulation.add(newPair);
        }
        oldPopulation.sort(comparator);
        // the best is the last one

        List<PSZTGraph> newPopulation = new ArrayList<>();

        Pair<PSZTGraph, Double> best = oldPopulation.get(oldPopulation.size() - 1);

        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);

        double sum = -best.getValue1();
        for (Pair<PSZTGraph, Double> pair: oldPopulation) {
            sum += pair.getValue1();
        }

        for (Pair<PSZTGraph, Double> pair: oldPopulation) {
            PSZTGraph graph = pair.getValue0();

            if (graph == best.getValue0()) { break; }

            double probability = pair.getValue1() / sum;
            double sample = distribution.sample();
            if (probability <= sample) {
                newPopulation.add(graph);
            }
        }



        //TODO: Zmiana sposobu krzyzowania, wydzielic do metody
        UniformIntegerDistribution integerDistribution = new UniformIntegerDistribution(0, oldPopulation.size() - 1);
        while (newPopulation.size() < oldPopulation.size() - 1) {
            int indx1 = integerDistribution.sample();
            int indx2 = integerDistribution.sample();
            while (indx1 == indx2) { indx2 = integerDistribution.sample();  }
            PSZTGraph firstGraph = oldPopulation.get(indx1).getValue0();
            PSZTGraph secondGraph = oldPopulation.get(indx2).getValue0();
            PSZTGraph newGraph = crossPopulation(firstGraph, secondGraph, this.varianceCross);
            newPopulation.add(newGraph);
        }

        mutatePopulation(newPopulation, mutationProbability);

        newPopulation.add(best.getValue0());


        this.generation += 1;

        return newPopulation;
    }




    //Mutation is performed by adding independent Gaussian noise to each of vertices.
    //

    private void mutatePopulation(List<PSZTGraph> population, double probability) {

        UniformRealDistribution uniformRealDistribution = new UniformRealDistribution(0.0, 1.0);
        for (PSZTGraph graph : population) {
            for (PSZTVertex v : graph.getVertices()) {
                if (uniformRealDistribution.sample() <= probability) {
                    NormalDistribution xDistribution = new NormalDistribution(v.getX(), this.variance);
                    NormalDistribution yDistribution = new NormalDistribution(v.getY(), this.variance);
                    v.setY(yDistribution.sample());
                    v.setX(xDistribution.sample());

                }
            }
        }
    }




    private PSZTGraph crossPopulation(PSZTGraph graph1, PSZTGraph graph2, double variance) {

        try {
            PSZTGraph newGraph = (PSZTGraph)graph1.clone();

            for (int i=0; i<newGraph.getVertices().size(); i++) {
                PSZTVertex firstVertex = graph1.getVertices().get(i);
                PSZTVertex secondVertex = graph2.getVertices().get(i);

                PSZTVertex newVertex = newGraph.getVertices().get(i);
                double sumX = firstVertex.getX() + secondVertex.getX();
                double sumY = firstVertex.getY() + secondVertex.getY();
                NormalDistribution distributionX = new NormalDistribution(sumX / 2.0, variance);
                NormalDistribution distributionY = new NormalDistribution(sumY / 2.0, variance);

                newVertex.setX(distributionX.sample());
                newVertex.setY(distributionY.sample());
            }
            return newGraph;
        }
        catch (Exception e) {
            System.out.println("Exception" + e.toString());
            return null;
        }


    }

    synchronized PSZTGraph stopAlgorithm() {
        return getBestGraphFromCurrentPopulation().getValue0();
    }


    Pair<PSZTGraph, Double>getBestGraphFromCurrentPopulation() {
        PSZTGraph graph = Collections.max(this.population, new Comparator<PSZTGraph>() {
            public int compare(PSZTGraph o1, PSZTGraph o2) {
                double quality1 = evaluator.qualityOfGraph(o1);
                double quality2 = evaluator.qualityOfGraph(o2);
                if (quality1 > quality2) { return 1; }
                if (quality1 < quality2) { return -1; }
                return 0;
            }
        });
        double value = evaluator.qualityOfGraph(graph);
        return new Pair<PSZTGraph, Double>(graph, value);
    }
}
