
import org.apache.commons.math3.distribution.NormalDistribution;
import org.javatuples.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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

    private GraphQualityEvaluator evaluator;
    public GraphEvolutionGenerator(PSZTGraph graph, int populationSize, int canvasWidth, int canvasHeight, double vertexDiameter, double variance){
        this.graph = graph;
        this.populationSize = populationSize;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.vertexDiameter = vertexDiameter;

        this.variance = variance;
        this.evaluator = new GraphQualityEvaluator(new GraphQualityArguments(20));
        population = generateStartingPopulation(graph, populationSize);


    }

    public ArrayList<PSZTGraph> getPopulation() {
        return population;
    }

    private PSZTGraph generateRandomGraph(PSZTGraph graph) {


        for (PSZTVertex v : graph.getVertices()) {

            double randomX =  ThreadLocalRandom.current().nextDouble(0, canvasHeight);
            double randomY = ThreadLocalRandom.current().nextDouble(0, canvasHeight);
            v.setX(randomX);
            v.setY(randomY);
        }

        for (PSZTEdge edge: graph.getEdges()) {



            double midPointX = (edge.getFrom().getX() + edge.getTo().getX()) / 2.0;
            double midPointY = (edge.getFrom().getY() + edge.getTo().getY() / 2.0);



//            double randomX = ThreadLocalRandom.current().nextDouble(- canvasWidth, 2 * canvasWidth);
//            double randomY = ThreadLocalRandom.current().nextDouble(- canvasHeight, 2 * canvasHeight);

            edge.setPointX(midPointX);
            edge.setPointY(midPointY);
        }
        return graph;


    }

    private ArrayList<PSZTGraph> generateStartingPopulation(PSZTGraph startingGraph, int size) {
        ArrayList<PSZTGraph> list = new ArrayList<PSZTGraph>();

        for (int i = 0; i < size; i++) {
            list.add(generateRandomGraph(startingGraph));
        }
        return list;

    }

    //Function that takes

    synchronized ArrayList<PSZTGraph> generateNextPopulation() {

        ArrayList<Pair<PSZTGraph, Double>> list = new ArrayList<Pair<PSZTGraph, Double>>();

        for (PSZTGraph g: population) {
            double quality = evaluator.qualityOfGraph(g);
            Pair<PSZTGraph, Double> newPair = new Pair<PSZTGraph, Double>(g, quality);
            list.add(newPair);
        }
        list.sort(new Comparator<Pair<PSZTGraph, Double>>() {
            public int compare(Pair<PSZTGraph, Double> o1, Pair<PSZTGraph, Double> o2) {
                if (o1.getValue1() > o2.getValue1()) { return 1; }
                else if (o1.getValue1() < o2.getValue1()) { return -1; }
                return 0;
            }
        });

        //Now population is sorted in list.


        double percentOfPopulationToRemove = 0.5;
        int elementsToRemove = this.populationSize * (int)percentOfPopulationToRemove;

        ArrayList<PSZTGraph> tempPopulation = new ArrayList<PSZTGraph>();
        for (int i = elementsToRemove; i < population.size(); i++) {
            tempPopulation.add(this.population.get(i));
        }


        ArrayList<PSZTGraph> sumOfPopulations = new ArrayList<PSZTGraph>(tempPopulation);
        for (int i = 0; i < tempPopulation.size(); i++) {

            int indx1 = ThreadLocalRandom.current().nextInt(0, tempPopulation.size());
            int indx2 = ThreadLocalRandom.current().nextInt(0, tempPopulation.size());


            PSZTGraph firstGraph = tempPopulation.get(indx1);
            PSZTGraph secondGraph = tempPopulation.get(indx2);

            PSZTGraph newGraph = crossPopulation(firstGraph, secondGraph);
            sumOfPopulations.add(newGraph);
        }



        mutatePopulation(sumOfPopulations);


        return sumOfPopulations;
    }




    //Mutation is performed by adding independent Gaussian noise to each of vertices.
    //

    private void mutatePopulation(ArrayList<PSZTGraph> population) {

        for (PSZTGraph graph : population) {
            for (PSZTVertex v : graph.getVertices()) {
                NormalDistribution xDistribution = new NormalDistribution(v.getX(), this.variance);
                NormalDistribution yDistribution = new NormalDistribution(v.getY(), this.variance);
                v.setX(xDistribution.sample());
                v.setY(yDistribution.sample());
            }
        }
    }




    private PSZTGraph crossPopulation(PSZTGraph graph1, PSZTGraph graph2) {

        try {
            PSZTGraph newGraph = (PSZTGraph)graph1.clone();

            int length = graph1.getVertices().size();

            for (int i = 0; i < length; i++) {



                PSZTVertex next1 = graph1.getVertices().get(i);
                PSZTVertex next2 = graph2.getVertices().get(i);

                PSZTVertex newVertex = newGraph.getVertices().get(i);


                if (ThreadLocalRandom.current().nextBoolean()) {
                    newVertex.setX(next1.getX());
                }
                else {
                    newVertex.setX(next2.getX());
                }
                if (ThreadLocalRandom.current().nextBoolean()) {
                    newVertex.setY(next1.getY());

                }
                else {
                    newVertex.setY(next2.getY());
                }
            }

            int edgesCount = graph2.getEdges().size();
            for (int i = 0; i < edgesCount; i++) {


                PSZTEdge edge1 = graph1.getEdges().get(i);
                PSZTEdge edge2 = graph2.getEdges().get(i);
                PSZTEdge newEdge = newGraph.getEdges().get(i);


                if (ThreadLocalRandom.current().nextBoolean()) {
                    newEdge.setPointX(edge1.getPointX());
                }
                else {
                    newEdge.setPointX(edge2.getPointX());
                }
                if (ThreadLocalRandom.current().nextBoolean()) {
                    newEdge.setPointY(edge1.getPointY());

                }
                else {
                    newEdge.setPointY(edge2.getPointY());
                }

            }

            return graph1;
        }
        catch (Exception e) {
            System.out.println("Exception" + e.toString());
            return null;
        }


    }

    synchronized PSZTGraph stopAlgorithm() {
        return getBestGraphFromCurrentPopulation();
    }


    PSZTGraph getBestGraphFromCurrentPopulation() {
        return Collections.max(this.population, new Comparator<PSZTGraph>() {
            public int compare(PSZTGraph o1, PSZTGraph o2) {
                double quality1 = evaluator.qualityOfGraph(o1);
                double quality2 = evaluator.qualityOfGraph(o2);
                if (quality1 > quality2) { return 1; }
                if (quality1 < quality2) { return -1; }
                return 0;
            }
        });

    }
}
