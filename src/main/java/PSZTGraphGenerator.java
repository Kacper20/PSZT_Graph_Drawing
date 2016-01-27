import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 * Created by tomasz on 25.01.16.
 */
public class PSZTGraphGenerator {
    private double probabilityOfJoining;
    private int vertexNumber;

    public PSZTGraphGenerator(double probabilityOfJoining, int vertexNumber) {
        this.probabilityOfJoining = probabilityOfJoining;
        this.vertexNumber = vertexNumber;
    }

    public PSZTGraph generateGraph() {
        UniformRealDistribution uniformRealDistribution = new UniformRealDistribution(0.0, 1.0);
        List<PSZTVertex> vertices = new ArrayList<>();
        List<PSZTEdge> edges = new ArrayList<>();

        int i = 0;
        int edgeIndicies = vertexNumber;
        while (i < vertexNumber) {
            vertices.add(new PSZTVertex(Integer.toString(++i), 0., 0.));
            int j = 0;
            while (j < i - 1) {
                if (uniformRealDistribution.sample() <= probabilityOfJoining) {
                    edges.add(new PSZTEdge(vertices.get(j), vertices.get(i - 1), 0., 0., Integer.toString(edgeIndicies++)));
                }
                j++;
            }
        }
        return new PSZTGraph(vertices, edges);
    }
}
