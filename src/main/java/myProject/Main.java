package myProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import jsat.ARFFLoader;
import jsat.DataSet;
import jsat.clustering.OPTICS;
import myProject.DataExtractionMethod.IrisExtractionMethod;
import myProject.Datatype.Iris;
import myProject.utils.CSVToARFFConverter;
import myProject.utils.DataSetStats;

public class Main {
    public static void main(String[] args) {

        // Convert CSV to ARFF
        try {
            CSVToARFFConverter.convert("data/iris/iris.data", "data/iris/iris.arff");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Use JSAT OPTICS implementation
        File file = new File("data/iris/iris.arff");
        DataSet dataSet = ARFFLoader.loadArffFile(file);
        OPTICS opt = new OPTICS(4);
        opt.cluster(dataSet);
        double[] expectedReachabilityDistances = opt.getReachabilityArray();  

        System.out.println(opt.getParameters());

        // Use custom OPTICS implementation
        IrisExtractionMethod method = new IrisExtractionMethod();
        method.extractData("data/iris/iris.data");
        ArrayList<Iris> D = method.getData();

        // Estimate radius as done in jsat.clustering.OPTICS.cluster
        DataSetStats stats = new DataSetStats(D, 4);
        double radius = stats.getMean() + stats.getStandardDeviation() * 3;

        try {
            FileOutputStream OrderedFile = new FileOutputStream("output/iris/expected.csv", false);
            myOPTICS optics = new myOPTICS(radius, 4);
            optics.cluster(D, OrderedFile);
            OrderedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[] reachabilityDistances = new double[D.size()];
        for (Iris iris : D) {
            double reachabilityDistance = iris.getReachabilityDistance();
            if (Double.isNaN(reachabilityDistance)) {
                reachabilityDistance = Double.POSITIVE_INFINITY;
            }
            reachabilityDistances[iris.getID()] = reachabilityDistance;
        }

        System.out.println(reachabilityDistances == expectedReachabilityDistances);
    }
}
