
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.javatuples.Pair;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class GraphEvolutionGenerator {

    private ArrayList<PSZTGraph> population;
    private int canvasWidth;
    private int canvasHeight;
    private double vertexRadius;
    private double variance;

    private PSZTGraph crossedGraph = null;
    private GraphQualityEvaluator evaluator;

    public GraphEvolutionGenerator(PSZTGraph graph, GraphQualityArguments arguments, int populationSize, int canvasWidth, int canvasHeight, double vertexRadius, double variance) {
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

    private PSZTGraph generateRandomGraph(PSZTGraph graph) {
        PSZTGraph newGraph = (PSZTGraph) graph.clone();
        for (PSZTVertex v : newGraph.getVertices()) {
            double randomX = ThreadLocalRandom.current().nextDouble(vertexRadius, canvasWidth - vertexRadius);
            double randomY = ThreadLocalRandom.current().nextDouble(vertexRadius, canvasHeight - vertexRadius);

            v.setX(randomX);
            v.setY(randomY);
        }

        for (PSZTEdge edge : newGraph.getEdges()) {
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

    synchronized List<PSZTGraph> generateNextPopulation() {
        Comparator<Pair<PSZTGraph, Double>> comparator = new Comparator<Pair<PSZTGraph, Double>>() {
            public int compare(Pair<PSZTGraph, Double> o1, Pair<PSZTGraph, Double> o2) {
                if (o1.getValue1() > o2.getValue1()) {
                    return 1;
                } else if (o1.getValue1() < o2.getValue1()) {
                    return -1;
                }
                return 0;
            }
        };

        //Temporary list of old population, with fitnesses value.
        List<Pair<PSZTGraph, Double>> oldPopulation = new ArrayList<>();
        for (PSZTGraph g : population) {
            double quality = evaluator.qualityOfGraph(g);
            Pair<PSZTGraph, Double> newPair = new Pair<>(g, quality);

            oldPopulation.add(newPair);
        }

        List<PSZTGraph> reproductedPopulation = new ArrayList<>();

        //Reproduction phase, based on tournament selection
        UniformIntegerDistribution indexDistribution = new UniformIntegerDistribution(0, oldPopulation.size() - 1);

        while (reproductedPopulation.size() != oldPopulation.size()) {
            int indexToChooseFirst = indexDistribution.sample();
            int indexToChooseSecond = indexDistribution.sample();

            Pair<PSZTGraph, Double> first = oldPopulation.get(indexToChooseFirst);
            Pair<PSZTGraph, Double> second = oldPopulation.get(indexToChooseSecond);

            reproductedPopulation.add(first.getValue1() >= second.getValue1() ? first.getValue0() : second.getValue0());
        }

        mutatePopulation(reproductedPopulation);

        PSZTGraph meanGraph = (PSZTGraph) reproductedPopulation.get(reproductedPopulation.size() - 1).clone();

        for (int i = 0; i < meanGraph.getVertices().size(); i++) {
            double sumX = 0;
            double sumY = 0;

            for (PSZTGraph graph : reproductedPopulation) {
                PSZTVertex vertex = graph.getVertices().get(i);
                sumX += vertex.getX();
                sumY += vertex.getY();
            }
            double meanX = sumX / reproductedPopulation.size();
            double meanY = sumY / reproductedPopulation.size();

            meanGraph.getVertices().get(i).setX(meanX);
            meanGraph.getVertices().get(i).setY(meanY);

        }
        crossedGraph = meanGraph;

        return reproductedPopulation;
    }


    //Mutation is performed by adding independent Gaussian noise to each of vertices.
    private void mutatePopulation(List<PSZTGraph> population) {
        UniformRealDistribution uniformRealDistribution = new UniformRealDistribution(0.0, 1.0);
        for (PSZTGraph graph : population) {
            for (PSZTVertex v : graph.getVertices()) {
                double xVariance = (1.0 / 100.0) * (canvasWidth - 2 * vertexRadius);
                double yVariance = (1.0 / 100.0) * (canvasHeight - 2 * vertexRadius);
                CanvasDistribution distributionX = new CanvasDistribution(canvasHeight, canvasWidth, vertexRadius, new NormalDistribution(v.getX(), xVariance));
                CanvasDistribution distributionY = new CanvasDistribution(canvasHeight, canvasWidth, vertexRadius, new NormalDistribution(v.getY(), yVariance));
                v.setX(distributionX.getValidSample(WhichPoint.X));
                v.setY(distributionY.getValidSample(WhichPoint.Y));
            }
        }
    }

    Pair<PSZTGraph, Double> getBestGraphFromCurrentPopulation() {
        PSZTGraph graph = Collections.max(this.population, (o1, o2) -> {
            double quality1 = evaluator.qualityOfGraph(o1);
            double quality2 = evaluator.qualityOfGraph(o2);
            if (quality1 > quality2) {
                return 1;
            }
            if (quality1 < quality2) {
                return -1;
            }
            return 0;
        });

        double value = evaluator.qualityOfGraph(graph);
        if (crossedGraph != null) {
            double quality = evaluator.qualityOfGraph(crossedGraph);
            if (quality > value) {
                return new Pair<>(crossedGraph, quality);
            }
        }

        return new Pair<>(graph, value);
    }
}
