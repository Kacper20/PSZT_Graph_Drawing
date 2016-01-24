import org.apache.tinkerpop.gremlin.structure.Graph;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphQualityEvaluator {

    private GraphQualityArguments arguments;

    public GraphQualityEvaluator(GraphQualityArguments arguments) {
        this.arguments = arguments;

    }

    public double qualityOfGraph(PSZTGraph graph) {
        double crossingCumulativePunishment = (Double) arguments.getCrossingPunishment() * ( 1. / (numberOfCrossings(graph) + numberOfVerticesWithVerticesCrossings(graph) + 1.));
        double lengthCumulativePunishment = (Double) arguments.getLengthPunishment() * (1. / (relativeErrorOfEdgeLengths(graph) + 1.));
        double anglesCumulativePunishment = (Double) arguments.getVertexAnglesPunishment() * (1. / (edgeAnglesDeviation(graph) + 1.));

        return crossingCumulativePunishment + lengthCumulativePunishment + anglesCumulativePunishment;
    }

    private double edgeAnglesDeviation(PSZTGraph graph) {
        double value = 0;
        for (PSZTVertex vertex: graph.getVertices()) {
            List<PSZTEdge> edges = graph.incidentToVertex(vertex);

            for (int i = 0; i < edges.size(); i++) {
                for (int j = i + 1; j < edges.size(); j++) {
                    PSZTEdge first = edges.get(i);
                    PSZTEdge second = edges.get(j);

                    Line2D.Double firstLine = new Line2D.Double(first.getFrom().getX(), first.getFrom().getY(), first.getTo().getX(), first.getTo().getY());
                    Line2D.Double secondLine = new Line2D.Double(second.getFrom().getX(), second.getFrom().getY(), second.getTo().getX(), second.getTo().getY());

                    double angle = angleBetween2Lines(firstLine, secondLine);
                    double val = 2 * Math.PI / (double)edges.size();
                    value += Math.pow(angle - val, 2);
                }
            }
        }

        return value;
    }

    private int numberOfVerticesWithEdgesCrossings(PSZTGraph graph) {
        int sum = 0;
        for(PSZTVertex vertex: graph.getVertices()) {

            for(PSZTEdge edge: graph.getEdges()) {


                Point2D from = new Point2D.Double(edge.getFrom().getX(), edge.getFrom().getY());
                Point2D to = new Point2D.Double(edge.getTo().getX(), edge.getTo().getY());

                Point2D middleOfVertex = new Point2D.Double(vertex.getX(), vertex.getY());


                double distance = pointToLineDistance(middleOfVertex, from, to);
                double distancePlusThreshold = arguments.getPreferredVertexRadius() * arguments.getPreferredLength() * 0.05;

                if ( distance <= distancePlusThreshold) { sum += 1; }
            }
        }

        return sum;
    }

    private int numberOfVerticesWithVerticesCrossings(PSZTGraph graph) {
        int sum = 0;
        for (PSZTVertex v1 : graph.getVertices()) {
            for (PSZTVertex v2 : graph.getVertices()) {
                if (vertexToVertexDistance(v1,v2) < 2.*arguments.getPreferredVertexRadius()) sum++;
            }
        }
        return sum/2;   // because each crossing counted twice
    }

    private double relativeErrorOfEdgeLengths(PSZTGraph graph) {
        List<PSZTEdge> edges = graph.getEdges();

        double numberOfEdges = edges.size();

        double sum = 0;

        for (PSZTEdge edge: edges) {
            double edgeLength = (new PVector(edge.getFrom().getX(), edge.getFrom().getY()).dist(new PVector(edge.getTo().getX(), edge.getTo().getY())));
            double difference = (edgeLength - arguments.getPreferredLength()) / arguments.getPreferredLength();
            sum += difference * difference;
        }

        return sum;
    }

    private int numberOfCrossings(PSZTGraph graph) {
        List<PSZTEdge> edges = graph.getEdges();

        int length = edges.size();
        int numberOfCrossingEdges = 0;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                PSZTEdge firstEdge = edges.get(i);
                PSZTEdge secondEdge = edges.get(j);

                PSZTVertex v1f = firstEdge.getFrom();
                PSZTVertex v1t = firstEdge.getTo();
                PSZTVertex v2f = secondEdge.getFrom();
                PSZTVertex v2t = secondEdge.getTo();

                if (v1f == v2f || v1f == v2t || v1t == v2f || v1t == v2t) continue; // because of common vertex

                if (Line2D.linesIntersect(
                        v1f.getX(), v1f.getY(), v1t.getX(), v1t.getY(),
                        v2f.getX(), v2f.getY(), v2t.getX(), v2t.getY()
                        )) numberOfCrossingEdges++;
            }
        }
        return numberOfCrossingEdges;
    }

    private  static double angleBetween2Lines(Line2D line1, Line2D line2)
    {
        double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
                line1.getX1() - line1.getX2());
        double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
                line2.getX1() - line2.getX2());
        return angle1-angle2;
    }


    public double pointToLineDistance(Point2D A, Point2D B, Point2D P) {
        double normalLength = Math.sqrt((B.getX()-A.getX())*(B.getX()-A.getX())+(B.getY()-A.getY())*(B.getY()-A.getY()));
        return Math.abs((P.getX()-A.getX())*(B.getY()-A.getY())-(P.getY()-A.getY())*(B.getX()-A.getX()))/normalLength;
    }

    private double vertexToVertexDistance(PSZTVertex v1, PSZTVertex v2) {
        double dx = v1.getX() - v2.getX();
        double dy = v1.getY() - v2.getY();
        return Math.sqrt(dx*dx+dy*dy);
    }

}
