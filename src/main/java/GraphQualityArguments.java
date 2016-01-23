/**
 * Created by kacper on 11.01.2016.
 */
public class GraphQualityArguments {

    private double distancePunishment;
    private double lengthPunishment;
    private double crossingPunishment;
    private double vertexCrossingPunishment;


    private double  preferredVertexRadius;
    private double preferredLength;
    public GraphQualityArguments(int distancePunishment) {
        this.distancePunishment = distancePunishment;
    }


    public double getDistancePunishment() {
        return distancePunishment;
    }

    public void setDistancePunishment(double distancePunishment) {
        this.distancePunishment = distancePunishment;
    }

    public double getLengthPunishment() {
        return lengthPunishment;
    }

    public void setLengthPunishment(double lengthPunishment) {
        this.lengthPunishment = lengthPunishment;
    }

    public double getCrossingPunishment() {
        return crossingPunishment;
    }

    public void setCrossingPunishment(double crossingPunishment) {
        this.crossingPunishment = crossingPunishment;
    }

    public double getPreferredLength() {
        return preferredLength;
    }

    public void setPreferredLength(double preferredLength) {
        this.preferredLength = preferredLength;
    }


    public double getVertexCrossingPunishment() {
        return vertexCrossingPunishment;
    }

    public void setVertexCrossingPunishment(double vertexCrossingPunishment) {
        this.vertexCrossingPunishment = vertexCrossingPunishment;
    }

    public double getPreferredVertexRadius() {
        return preferredVertexRadius;
    }

    public void setPreferredVertexRadius(double preferredVertexRadius) {
        this.preferredVertexRadius = preferredVertexRadius;
    }
}
