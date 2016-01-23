/**
 * Created by kacper on 11.01.2016.
 */
public class GraphQualityArguments {

    private double distancePunishment;
    private double lengthPunishment;
    private double crossingPunishment;
    private double vertexCrossingPunishment;
    private double vertexAnglesPunishment;

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
