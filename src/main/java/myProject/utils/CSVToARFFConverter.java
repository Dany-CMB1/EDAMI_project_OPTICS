package myProject.utils;

import weka.core.converters.CSVLoader;
import weka.core.converters.ArffSaver;
import weka.core.Instances;
import java.io.File;

public class CSVToARFFConverter {

    /**
     * Converts a CSV file to an ARFF file.
     *
     * @param csvFilePath  Path to the input CSV file
     * @param arffFilePath Path to the output ARFF file
     * @throws Exception if an error occurs during conversion
     */
    public static void convert(String csvFilePath, String arffFilePath) throws Exception {
        // Load CSV file
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvFilePath));
        Instances data = loader.getDataSet();
        
        // Save as ARFF file
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(arffFilePath));
        saver.writeBatch();
    }

    public static void main(String[] args) {
        // Example usage
        String csvFile = "path/to/input.csv";
        String arffFile = "path/to/output.arff";
        try {
            convert(csvFile, arffFile);
            System.out.println("Conversion completed successfully.");
        } catch (Exception e) {
            System.err.println("Error during conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
