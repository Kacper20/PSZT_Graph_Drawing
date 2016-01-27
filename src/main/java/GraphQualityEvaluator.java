import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
        double crossingEECumulativePunishment =
                arguments.getCrossingPunishment() * ( 1. / (numberOfCrossings(graph) + 1.));

        double crossingVVCumulativePunishment = arguments.getVertexCrossingPunishment() * (1./ (1. + numberOfVerticesWithVerticesCrossings(graph)));
        double crossingVECumulativePunishment = arguments.getVertexCrossingPunishment() * (1./ (1. + numberOfVerticesWithEdgesCrossings(graph)));
        double lengthCumulativePunishment = (Double) arguments.getLengthPunishment() * (1. / (relativeErrorOfEdgeLengths(graph) + 1.));
        double anglesCumulativePunishment = (Double) arguments.getVertexAnglesPunishment() * (1. / (edgeAnglesDeviation(graph) + 1.));

        return crossingEECumulativePunishment +
                crossingVVCumulativePunishment +
                crossingVECumulativePunishment +
                lengthCumulativePunishment +
                anglesCumulativePunishment;
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

    public int numberOfVerticesWithEdgesCrossings(PSZTGraph graph) {
        int sum = 0;
        for(PSZTVertex vertex: graph.getVertices()) {
            for(PSZTEdge edge: graph.getEdges()) {
                //If edge is indicent to the vertex, let's continue
                if (edge.getTo() == vertex || edge.getFrom() == vertex) { continue; }
                double distance = vertexToEdgeDistance(vertex, edge);

                //We're making sure, that we have some space between line and vertex circle
                double distancePlusThreshold = arguments.getPreferredVertexRadius();// + arguments.getPreferredLength() * 0.05;

                if ( distance <= distancePlusThreshold) { sum += 1; }
            }
        }

        return sum;
    }

    public int numberOfVerticesWithVerticesCrossings(PSZTGraph graph) {
        int sum = 0;
        for (PSZTVertex v1 : graph.getVertices()) {
            for (PSZTVertex v2 : graph.getVertices()) {
                if (v1 == v2) continue;
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

    public int numberOfCrossings(PSZTGraph graph) {
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

    private static double angleBetween2Lines(Line2D line1, Line2D line2)
    {
        double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
                line1.getX1() - line1.getX2());
        double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
                line2.getX1() - line2.getX2());
        return angle1-angle2;
    }




    private static double vertexToVertexDistance(PSZTVertex v1, PSZTVertex v2) {
        return Math.sqrt(vertexToVertexDistanceSquared(v1,v2));
    }

    private static double vertexToEdgeDistance(PSZTVertex p, PSZTEdge e) {
        PSZTVertex v = e.getFrom();
        PSZTVertex w = e.getTo();

        if (v==w) return vertexToVertexDistance(p, v);  // loop edge, maybe not necessary

        // Consider the line extending the segment, parameterized as v + t (w - v).
        // We find projection of point p onto the line.
        // It falls where t = [(p-v) . (w-v)] / |w-v|^2
        double t = dotProduct(v, p, v, w) / vertexToVertexDistanceSquared(v, w);
        if (t < 0.0) return vertexToVertexDistance(p, v);       // Beyond the 'v' end of the segment
        else if (t > 1.0) return vertexToVertexDistance(p, w);  // Beyond the 'w' end of the segment
        // else use library method:

        //From and to are representing points of begin and end of the edge
        Point2D from = new Point2D.Double(v.getX(), v.getY());
        Point2D to = new Point2D.Double(w.getX(), w.getY());
        //Middle vertex point
        Point2D middleOfVertex = new Point2D.Double(p.getX(), p.getY());
        //Distance between edge line and middle of the vertex
        Line2D.Double line = new Line2D.Double(from, to);
        return line.ptLineDist(middleOfVertex);
    }

    private static double dotProduct(PSZTVertex a, PSZTVertex b, PSZTVertex c, PSZTVertex d) {
        // v1 = b-a
        // v2 = d-c
        // returns v1 . v2
        double v1x = b.getX() - a.getX();
        double v1y = b.getY() - a.getY();
        double v2x = d.getX() - c.getX();
        double v2y = d.getY() - c.getY();
        return v1x*v2x + v1y*v2y;
    }

    private static double vertexToVertexDistanceSquared(PSZTVertex v1, PSZTVertex v2){
        double dx = v1.getX() - v2.getX();
        double dy = v1.getY() - v2.getY();
        return dx*dx+dy*dy;
    }

}
