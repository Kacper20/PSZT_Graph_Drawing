/**
 * Created by kacper on 11.01.2016.
 */
public class GraphQualityArguments {

    private double distancePunishment = 0.5;
    private double lengthPunishment = 1.5;
    private double crossingPunishment = 2;
    private double vertexCrossingPunishment = 3;
    private double vertexAnglesPunishment = 1.0;

    private double  preferredVertexRadius;
    private double preferredLength;

    public GraphQualityArguments() {};  // TODO remove!

    public GraphQualityArguments(double distancePunishment, double lengthPunishment, double crossingPunishment, double vertexCrossingPunishment, double vertexAnglesPunishment, double preferredVertexRadius, double preferredLength) {
        this.distancePunishment = distancePunishment;
        this.lengthPunishment = lengthPunishment;
        this.crossingPunishment = crossingPunishment;
        this.vertexCrossingPunishment = vertexCrossingPunishment;
        this.vertexAnglesPunishment = vertexAnglesPunishment;
        this.preferredVertexRadius = preferredVertexRadius;
        this.preferredLength = preferredLength;
    }

    public GraphQualityArguments(double preferredLength, double preferredVertexRadius) {
        this.preferredLength = preferredLength;
        this.preferredVertexRadius = preferredVertexRadius;
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


    public double getVertexAnglesPunishment() {
        return vertexAnglesPunishment;
    }

    public void setVertexAnglesPunishment(double vertexAnglesPunishment) {
        this.vertexAnglesPunishment = vertexAnglesPunishment;
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
