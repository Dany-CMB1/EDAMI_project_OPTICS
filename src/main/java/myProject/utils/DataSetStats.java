package myProject.utils;

import java.util.ArrayList;

import myProject.Datatype.DObject;

// Class to calculate the mean and standard deviation of the distances between objects and their k-nearest neighbors
public class DataSetStats {
    private double mean;
    private double standardDeviation;

    public DataSetStats(ArrayList<? extends DObject> D, int numNeighbors) {
        ArrayList<Double> neighborDistances = new ArrayList<>();
        for (DObject obj : D) {
            obj.findNeighbors(D, numNeighbors);
            for (DObject neighbor : obj.getNeighbors(D)) {
                neighborDistances.add(obj.distance(neighbor));
            }
        }

        this.mean = neighborDistances.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
        this.standardDeviation = Math.sqrt(neighborDistances.stream().mapToDouble(d -> Math.pow(d - this.mean, 2)).sum() / neighborDistances.size());
    }

    public double getMean() {
        return this.mean;
    }

    public double getStandardDeviation() {
        return this.standardDeviation;
    }
}
