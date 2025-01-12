package myProject.utils;

import java.util.ArrayList;

import myProject.Datatype.DObject;

// Class to calculate the mean and standard deviation of the distances between objects and their k-nearest neighbors
public class DataSetStats {
    private double mean;
    private double standardDeviation;

    public DataSetStats(ArrayList<? extends DObject> D, int maxSamples){
        ArrayList<Double> neighborDistances = new ArrayList<>();
        for (DObject obj : D) {
            obj.findNeighbors(D, maxSamples);
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

    public static void printDatasetInfo(ArrayList<? extends DObject> D) throws Exception {
        DataSetStats stats = new DataSetStats(D, 4);
        System.out.println("Mean: " + stats.getMean());
        System.out.println("Standard Deviation: " + stats.getStandardDeviation());
        final double radius = stats.getMean() + stats.getStandardDeviation() * 3;
        System.out.println("Radius: " + radius + "\n");


        for (DObject o : D) {
            System.out.println("Object: " + o.getID());
            System.out.println("\tNeighbors: " + o.getNeighborsID());
            System.out.println("\tCore Distance: " + o.getCoreDistance());
        }
    }
}
