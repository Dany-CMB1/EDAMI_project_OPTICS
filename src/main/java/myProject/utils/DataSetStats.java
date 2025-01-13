package myProject.utils;

import java.util.ArrayList;

import myProject.Datatype.DObject;

// Class to calculate the mean and standard deviation of the distances between objects and their k-nearest neighbors
public class DataSetStats {
    private double mean;
    private double standardDeviation;

    public DataSetStats(ArrayList<? extends DObject> D, int numNeighbors) throws Exception {
        ArrayList<Double> neighborDistances = new ArrayList<>();
        for (DObject obj : D) {
            obj.findClosestNeighbors(D, numNeighbors);
            for (DObject neighbor : obj.getNeighbors(D)) {
                neighborDistances.add(obj.distance(neighbor));
            }
        }
        if (neighborDistances.isEmpty()) {
            throw new Exception("No neighbors found. Please check the dataset.");
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

    public void printDatasetInfo(ArrayList<? extends DObject> D, String info) throws Exception {

        System.out.println("\nDataset Information: ");
        if (info.equals("summary") || info.equals("all")) {
            System.out.println("\tNumber of objects: " + D.size());
            System.out.println("\tAverage Distance between neighbors: " + this.getMean());
            System.out.println("\tStandard Deviation: " + this.getStandardDeviation());
            final double radius = this.getMean() + this.getStandardDeviation() * 3;
            System.out.println("\tCalculated Radius: " + radius + "\n");
        }
        if (info.equals("all")) {
            for (DObject o : D) {
                System.out.println("Object: " + o.getID());
                System.out.println("\tNeighbors: " + o.getNeighborsID());
                System.out.println("\tCore Distance: " + o.getCoreDistance());
            }
        }
    }
}
