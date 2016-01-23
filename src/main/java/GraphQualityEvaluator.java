import org.apache.tinkerpop.gremlin.structure.Graph;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;

/**
 * Created by kacper on 30.12.2015.
 */
public class GraphQualityEvaluator {

    private GraphQualityArguments arguments;

    public GraphQualityEvaluator(GraphQualityArguments arguments) {
        this.arguments = arguments;

    }

    public double qualityOfGraph(PSZTGraph graph) {

        double initialQuality = 0;

        initialQuality -= numberOfCrossings(graph) * arguments.getCrossingPunishment();



        return 0;
    }





    private double edgeAnglesDeviation(PSZTGraph graph) {


        double value = 0;
        for (PSZTVertex vertex: graph.getVertices()) {

            java.util.List<PSZTEdge> edges = graph.incidentToVertex(vertex);

            for (int i = 0; i < edges.size(); i++) {
                for (int j = i + 1; j < edges.size(); j++) {
                    PSZTEdge first = edges.get(i);
                    PSZTEdge second = edges.get(j);

                    Line2D.Double firstLine = new Line2D.Double(first.getFrom().getX(), first.getFrom().getY(), first.getTo().getX(), first.getTo().getY());
                    Line2D.Double secondLine = new Line2D.Double(second.getFrom().getX(), second.getFrom().getY(), second.getTo().getX(), second.getTo().getY());

                    double angle = angleBetween2Lines(firstLine, secondLine);
                    double val = 2 * Math.PI / (double)edges.size();
                    value += angle - val;




                }



            }



        }



        return value;

    }

    private int numberOfVerticesCrossings(PSZTGraph graph) {

        int sum = 0;
        for(PSZTVertex vertex: graph.getVertices()) {





        }

        return sum;






    }

    private double relativeErrorOfEdgeLengths(PSZTGraph graph) {

        ArrayList<PSZTEdge> edges = graph.getEdges();

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


        ArrayList<PSZTEdge> edges = graph.getEdges();

        int length = edges.size();
        int numberOfCrossingEdges = 0;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; i++) {

                PSZTEdge firstEdge = edges.get(i);
                PSZTEdge secondEdge = edges.get(j);

                double firstEdgeFirstX = firstEdge.getFrom().getX();
                double firstEdgeFirstY = firstEdge.getFrom().getY();
                double firstEdgeSecondX = firstEdge.getTo().getX();
                double firstEdgeSecondY = firstEdge.getTo().getY();

                Line2D.Double firstLine = new Line2D.Double(firstEdgeFirstX, firstEdgeFirstY, firstEdgeSecondX, firstEdgeSecondY);

                double secondEdgeFirstX = secondEdge.getFrom().getX();
                double secondEdgeFirstY = secondEdge.getFrom().getY();
                double secondEdgeSecondX = secondEdge.getTo().getX();
                double secondEdgeSecondY = secondEdge.getTo().getY();

                Line2D.Double secondLine = new Line2D.Double(secondEdgeFirstX, secondEdgeFirstY, secondEdgeSecondX, secondEdgeSecondY);

                if (get_line_intersection(firstLine, secondLine) != null) {

                    numberOfCrossingEdges += 1;
                }
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


    private static Point get_line_intersection(Line2D.Double pLine1, Line2D.Double pLine2)
    {
        Point
                result = null;

        double
                s1_x = pLine1.x2 - pLine1.x1,
                s1_y = pLine1.y2 - pLine1.y1,

                s2_x = pLine2.x2 - pLine2.x1,
                s2_y = pLine2.y2 - pLine2.y1,

                s = (-s1_y * (pLine1.x1 - pLine2.x1) + s1_x * (pLine1.y1 - pLine2.y1)) / (-s2_x * s1_y + s1_x * s2_y),
                t = ( s2_x * (pLine1.y1 - pLine2.y1) - s2_y * (pLine1.x1 - pLine2.x1)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
        {
            // Collision detected
            result = new Point(
                    (int) (pLine1.x1 + (t * s1_x)),
                    (int) (pLine1.y1 + (t * s1_y)));
        }   // end if

        return result;
    }

}
