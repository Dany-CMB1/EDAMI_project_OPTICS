package myProject;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import myProject.DataExtractionMethod.DataExtractionMethod;
import myProject.DataExtractionMethod.DataExtractionMethodFactory;
import myProject.Datatype.DObject;
import myProject.utils.DataSetStats;


public class Main {
    public static void main(String[] args) throws Exception {

        // Read arguments from file
        Scanner scanner = new Scanner(new File("args.csv"));
        scanner.useDelimiter("\n");

        // Skip the first line
        scanner.next();

        String[] fargs = scanner.next().split(",");
        scanner.close();

        final String category = fargs[0];
        final String datasetName = fargs[1];
        final int minPoints = Integer.parseInt(fargs[2]);
        final double radius = Double.parseDouble(fargs[3]);

        System.out.println("Calling Main with arguments: ");
        System.out.println("\tCategory: " + category);
        System.out.println("\tDataset: " + datasetName);
        System.out.println("\tMinPoints: " + minPoints);

        // Unique dataset for the datatype, eg iris
        String datasetFile = new String();
        String outputDir = new String();

        if (category.equals(datasetName)){
            outputDir = "output/" + category +"/";
        }
        else{
            outputDir = "output/" + category + "/" + datasetName + "/";
        }
        datasetFile = "data/" + category + "/" + datasetName + ".csv";

        System.out.println("\nDataset File: " + datasetFile);
        System.out.println("Output Directory: " + outputDir);
        
        Path outputPath = Path.of(outputDir);

        // Create output directory if it does not exist
        if (!Files.exists(outputPath)) {
            new File(outputDir).mkdirs();
        }

        // Choose appropriate data extraction method
        DataExtractionMethod method = DataExtractionMethodFactory.getMethod(category);
        method.extractData(datasetFile);
        ArrayList<? extends DObject> D = method.getData();

        if (D.isEmpty()) {
            throw new Exception("No data found. Please check the dataset.");
        }

        // rpint dataset info
        DataSetStats stats = new DataSetStats(D, minPoints+1);
        stats.printDatasetInfo(D, "summary");


        // Run OPTICS
        System.out.println("Running OPTICS with minPoints=" + minPoints + ", eps=" + radius);
        ArrayList<Integer> orderedFile = new ArrayList<>();
        myOPTICS optics = new myOPTICS(radius, minPoints);
        optics.cluster(D, orderedFile);

        // Write output files
        FileWriter rDistsFileWriter = new FileWriter(outputDir + "RDists.csv");
        FileWriter cDistsFileWriter = new FileWriter(outputDir + "CDists.csv");
        FileWriter orderedFileWriter = new FileWriter(outputDir + "orderedFile.csv");
        for (DObject o : D){
            rDistsFileWriter.write(o.getReachabilityDistance() + "\n");
            cDistsFileWriter.write(o.getCoreDistance() + "\n");
        }
        rDistsFileWriter.close();
        cDistsFileWriter.close();
        for (int i : orderedFile){
            orderedFileWriter.write(i + "\n");
        }
        orderedFileWriter.close();

        //Pass compareResults.py args to file
        System.out.println("Scikit-learn OPTICS implementation will be called in src/test/python/compareResults.py with args: min_samples=" + minPoints + ", max_eps="+ radius);
    }
}
