
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.javatuples.Pair;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;




public class GraphEvolutionGenerator {

    private final PSZTGraph graph;

    private PSZTGraph currentGraphWithCoordinates;
    private ArrayList<PSZTGraph> population;
    private int populationSize;
    private int canvasWidth;
    private int canvasHeight;
    private double vertexRadius;
    private double variance;
    private double mutationProbability = 0.3;
//    private double crossoverProbability = 0.3;

    private double varianceCross =  0.2;

    private int generation = 1;
    private GraphQualityEvaluator evaluator;
    public GraphEvolutionGenerator(PSZTGraph graph, GraphQualityArguments arguments, int populationSize, int canvasWidth, int canvasHeight, double vertexRadius, double variance){
        this.graph = graph;
        this.populationSize = populationSize;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.vertexRadius = vertexRadius;

        this.variance = variance;
        this.evaluator = new GraphQualityEvaluator(arguments);
        population = generateStartingPopulation(graph, populationSize);


    }

    public GraphQualityEvaluator getEvaluator() {
        return evaluator;
    }

    public ArrayList<PSZTGraph> getPopulation() {
        return population;
    }

    private PSZTGraph generateRandomGraph(PSZTGraph graph) {

        PSZTGraph newGraph = (PSZTGraph)graph.clone();

        for (PSZTVertex v : newGraph.getVertices()) {



            double randomX =  ThreadLocalRandom.current().nextDouble(vertexRadius * 2.0 , canvasWidth - vertexRadius * 2.0);
            double randomY = ThreadLocalRandom.current().nextDouble(vertexRadius * 2.0 , canvasHeight - vertexRadius * 2.0);

            v.setX(randomX);
            v.setY(randomY);
        }

        for (PSZTEdge edge: newGraph.getEdges()) {


            double midPointX = (edge.getFrom().getX() + edge.getTo().getX()) / 2.0;
            double midPointY = (edge.getFrom().getY() + edge.getTo().getY()) / 2.0;




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


        //Temporary list of old population, with fitnesses value.
        List<Pair<PSZTGraph, Double>> oldPopulation = new ArrayList<>();
        for (PSZTGraph g: population) {
            double quality = evaluator.qualityOfGraph(g);
            Pair<PSZTGraph, Double> newPair = new Pair<PSZTGraph, Double>(g, quality);

            oldPopulation.add(newPair);
        }

        List<PSZTGraph> reproductedPopulation = new ArrayList<>();

        //Reproduction phase, based on tournament selection
        UniformIntegerDistribution indexDistribution = new UniformIntegerDistribution(0, oldPopulation.size() - 1);

        while (reproductedPopulation.size() != oldPopulation.size()) {

            int indexToChooseFirst = indexDistribution.sample();
            int indexToChooseSecond = indexDistribution.sample();

            Pair<PSZTGraph, Double> first= oldPopulation.get(indexToChooseFirst);
            Pair<PSZTGraph, Double> second = oldPopulation.get(indexToChooseSecond);

            reproductedPopulation.add(first.getValue1() >= second.getValue1() ? first.getValue0() : second.getValue0());

        }





        // the best is the last one


//        Pair<PSZTGraph, Double> best = oldPopulation.get(oldPopulation.size() - 1);
//
//        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
//
//        double sum = -best.getValue1();
//        for (Pair<PSZTGraph, Double> pair: oldPopulation) {
//            sum += pair.getValue1();
//        }
//
//        for (Pair<PSZTGraph, Double> pair: oldPopulation) {
//            PSZTGraph graph = pair.getValue0();
//
//            if (graph == best.getValue0()) { break; }
//
//            double probability = pair.getValue1() / sum;
//            double sample = distribution.sample();
//            if (probability <= sample) {
//                newPopulation.add(graph);
//            }
//        }

//        //TODO: Zmiana sposobu krzyzowania, wydzielic do metody
//        UniformIntegerDistribution integerDistribution = new UniformIntegerDistribution(0, oldPopulation.size() - 1);
//        while (newPopulation.size() < oldPopulation.size() - 1) {
//            int indx1 = integerDistribution.sample();
//            int indx2 = integerDistribution.sample();
//            while (indx1 == indx2) { indx2 = integerDistribution.sample();  }
//            PSZTGraph firstGraph = oldPopulation.get(indx1).getValue0();
//            PSZTGraph secondGraph = oldPopulation.get(indx2).getValue0();
//            PSZTGraph newGraph = crossPopulation(firstGraph, secondGraph, this.varianceCross);
//            newPopulation.add(newGraph);
//        }


        mutatePopulation(reproductedPopulation);


        this.generation += 1;

        PSZTGraph meanGraph = (PSZTGraph) reproductedPopulation.get(reproductedPopulation.size() - 1).clone();

        for (int i = 0; i < meanGraph.getVertices().size(); i++) {
            double sumX = 0;
            double sumY = 0;

            for (int j = 0; j < reproductedPopulation.size(); j++) {
                PSZTGraph graph = reproductedPopulation.get(i);
                PSZTVertex vertex = graph.getVertices().get(i);
                sumX += vertex.getX();
                sumY += vertex.getY();
            }
            double meanX = sumX / reproductedPopulation.size();
            double meanY = sumY / reproductedPopulation.size();

            meanGraph.getVertices().get(i).setX(meanX);
            meanGraph.getVertices().get(i).setY(meanY);

        }
        double quality = evaluator.qualityOfGraph(meanGraph);


        return reproductedPopulation;

    }




    //Mutation is performed by adding independent Gaussian noise to each of vertices.
    //

    private void mutatePopulation(List<PSZTGraph> population) {

        UniformRealDistribution uniformRealDistribution = new UniformRealDistribution(0.0, 1.0);
        for (PSZTGraph graph : population) {
            for (PSZTVertex v : graph.getVertices()) {

                double xVariance = (1.0 / 30.0) * (canvasWidth - 2 * vertexRadius);
                double yVariance = (1.0 / 30.0) * (canvasHeight - 2 * vertexRadius);
                CanvasDistribution distributionX = new CanvasDistribution(canvasHeight, canvasWidth, vertexRadius, new NormalDistribution(v.getX(), xVariance) );
                CanvasDistribution distributionY = new CanvasDistribution(canvasHeight, canvasWidth, vertexRadius, new NormalDistribution(v.getY(), yVariance) );
                v.setX(distributionX.getValidSample(WhichPoint.X));
                v.setY(distributionY.getValidSample(WhichPoint.Y));

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

                CanvasDistribution distributionX = new CanvasDistribution(canvasHeight, canvasWidth, vertexRadius, new NormalDistribution(sumX / 2.0, this.variance) );
                CanvasDistribution distributionY = new CanvasDistribution(canvasHeight, canvasWidth, vertexRadius, new NormalDistribution(sumY / 2.0, this.variance) );


                newVertex.setX(distributionX.getValidSample(WhichPoint.X));
                newVertex.setY(distributionY.getValidSample(WhichPoint.Y));

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
