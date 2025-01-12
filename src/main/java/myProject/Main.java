package myProject;

import java.io.FileWriter;
import java.util.ArrayList;

import myProject.DataExtractionMethod.DataExtractionMethod;
import myProject.DataExtractionMethod.DataExtractionMethodFactory;
import myProject.Datatype.DObject;
import myProject.utils.DataSetStats;


public class Main {
    public static void main(String[] args) throws Exception {

        final String category = args[0];
        final String datasetName = args[1];
        final int minPoints = Integer.parseInt(args[2]);

        // Unique dataset for the datatype, eg iris
        String datasetFile = "";
        String outputDir = "";
        String arffPath = "";

        if (category.equals(datasetName)){
            outputDir = "output/" + category +"/";
        }
        else{
            datasetFile = "data/" + category + "/" + datasetName + ".csv";
            outputDir = "output/" + category + "/" + datasetName + "/";
            arffPath = "data/" + category + "/" + datasetName + ".arff";
        }
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

        // Choose appropriate data extraction method
        DataExtractionMethod method = DataExtractionMethodFactory.getMethod(category);
        method.extractData(datasetFile);
        ArrayList<? extends DObject> D = method.getData();

        // Estimate radius as done in jsat.clustering.OPTICS.cluster
        DataSetStats stats = new DataSetStats(D, 6);
        System.out.println("Mean: " + stats.getMean());
        System.out.println("Standard Deviation: " + stats.getStandardDeviation());
        final double radius = stats.getMean() + stats.getStandardDeviation() * 3;
        System.out.println("Radius: " + radius);


        ArrayList<Integer> orderedFile = new ArrayList<>();
        myOPTICS optics = new myOPTICS(radius, minPoints);
        optics.cluster(D, orderedFile);

        try {
            FileWriter rDistsFileWriter = new FileWriter(outputDir + "RDists.csv");
            FileWriter cDistsFileWriter = new FileWriter(outputDir + "CDists.csv");
            FileWriter orderedFileWriter = new FileWriter(outputDir + "orderedFile.csv");
            for (DObject o : D){
                rDistsFileWriter.write(o.getReachabilityDistance() + "\n");
                cDistsFileWriter.write(o.getCoreDistance() + "\n");
                orderedFileWriter.write(o.getID() + "\n");
            }
            rDistsFileWriter.close();
            cDistsFileWriter.close();
            orderedFileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
