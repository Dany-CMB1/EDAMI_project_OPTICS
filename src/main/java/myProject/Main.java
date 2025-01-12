package myProject;

import java.io.FileWriter;
import java.util.ArrayList;

import myProject.DataExtractionMethod.Point2DExtractionMethod;
import myProject.Datatype.DObject;
import myProject.Datatype.Point2D;
import myProject.utils.DataSetStats;


public class Main {
    public static void main(String[] args) {

        final int minPoints = 3;
        final String category = "2d";
        final String datasetName = "2spiral";
        String datasetFile = "data/" + category + "/" + datasetName + ".csv";
        String outputDir = "output/" + category + "/" + datasetName + "/";
        String arffPath = "data/" + category + "/" + datasetName + ".arff";

        // Convert CSV to ARFF
        // try {
        //     CSVToARFFConverter.convert(filePath, arffPath);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // // Use JSAT OPTICS implementation
        // File file = new File(arffPath);
        // DataSet dataSet = ARFFLoader.loadArffFile(file);
        // OPTICS opt = new OPTICS(4);
        // opt.cluster(dataSet);
        // double[] expectedRDists = opt.getReachabilityArray();
        
        // Use custom OPTICS implementation
        Point2DExtractionMethod method = new Point2DExtractionMethod();
        method.extractData(datasetFile);
        ArrayList<Point2D> D = method.getData();

        // Estimate radius as done in jsat.clustering.OPTICS.cluster
        DataSetStats stats = new DataSetStats(D, 4);
        System.out.println("Mean: " + stats.getMean());
        System.out.println("Standard Deviation: " + stats.getStandardDeviation());
        final double radius = stats.getMean() + stats.getStandardDeviation() * 3;
        System.out.println("Radius: " + radius);


        ArrayList<Integer> orderedFile = new ArrayList<>();
        myOPTICS optics = new myOPTICS(radius, minPoints);
        optics.cluster(D, orderedFile);

        try {
            FileWriter rDistsFile = new FileWriter(outputDir + "RDists.csv");
            FileWriter cDistsFile = new FileWriter(outputDir + "CDists.csv");
            for (DObject o : D){
                rDistsFile.write(o.getReachabilityDistance() + "\n");
                cDistsFile.write(o.getCoreDistance() + "\n");
            }
            rDistsFile.close();
            cDistsFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Use DataMiningSandbox OPTICS implementation
        // DataMiningSandbox.Optics DMOptics = new Optics();
        // HashMap<Integer, DBPoint> dataset = ConvertProjectToDMS.convert(D);
        // DMOptics.optics(dataset, radius, 4);
        // ArrayList<Integer> orderedFile_DMS = new ArrayList<>();
        // for (DBPoint point :  DMOptics.list) {
        //     orderedFile_DMS.add(point.ID);
        // }

        // Compare results
        // System.out.println("myProject OPTICS: " + orderedFile);
        // System.out.println("DataMiningSandbox OPTICS: " + orderedFile_DMS);
        // System.out.println(orderedFile.equals(orderedFile_DMS));
    }
}
