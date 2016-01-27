/**
 * Created by kacper on 11.01.2016.
 */
public class GraphQualityArguments {
    private double lengthPunishment = 1.5;
    private double crossingEEPunishment = 0.3;
    private double crossingEVPunishment = 0.5;
    private double crossingVVPunishment = 0.5;
    private double vertexAnglesPunishment = 1.0;

    private double  preferredVertexRadius;
    private double preferredLength;

    public GraphQualityArguments(double lengthPunishment, double crossingEEPunishment, double crossingEVPunishment, double crossingVVPunishment, double vertexAnglesPunishment, double preferredVertexRadius, double preferredLength) {
        this.lengthPunishment = lengthPunishment;
        this.crossingEEPunishment = crossingEEPunishment;
        this.crossingEVPunishment = crossingEVPunishment;
        this.crossingVVPunishment = crossingVVPunishment;
        this.vertexAnglesPunishment = vertexAnglesPunishment;
        this.preferredVertexRadius = preferredVertexRadius;
        this.preferredLength = preferredLength;
    }

    public GraphQualityArguments(double preferredLength, double preferredVertexRadius) {
        this.preferredLength = preferredLength;
        this.preferredVertexRadius = preferredVertexRadius;
    }

    public double getLengthPunishment() {
        return lengthPunishment;
    }

    public void setLengthPunishment(double lengthPunishment) {
        this.lengthPunishment = lengthPunishment;
    }

    public double getCrossingEEPunishment() {
        return crossingEEPunishment;
    }

    public void setCrossingEEPunishment(double crossingEEPunishment) {
        this.crossingEEPunishment = crossingEEPunishment;
    }

    public double getCrossingEVPunishment() {
        return crossingEVPunishment;
    }

    public void setCrossingEVPunishment(double crossingEVPunishment) {
        this.crossingEVPunishment = crossingEVPunishment;
    }

    public double getCrossingVVPunishment() {
        return crossingVVPunishment;
    }

    public void setCrossingVVPunishment(double crossingVVPunishment) {
        this.crossingVVPunishment = crossingVVPunishment;
    }

    public double getVertexAnglesPunishment() {
        return vertexAnglesPunishment;
    }

    public void setVertexAnglesPunishment(double vertexAnglesPunishment) {
        this.vertexAnglesPunishment = vertexAnglesPunishment;
    }

    public double getPreferredVertexRadius() {
        return preferredVertexRadius;
    }

    public void setPreferredVertexRadius(double preferredVertexRadius) {
        this.preferredVertexRadius = preferredVertexRadius;
    }

    public double getPreferredLength() {
        return preferredLength;
    }

    public void setPreferredLength(double preferredLength) {
        this.preferredLength = preferredLength;
    }
}
