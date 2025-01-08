package myProject;

import java.io.File;

import jsat.ARFFLoader;
import jsat.DataSet;
import jsat.clustering.OPTICS;
import myProject.utils.CSVToARFFConverter;

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

        // IrisExtractionMethod method = new IrisExtractionMethod();
        // method.extractData("data/iris/iris.data");
        // ArrayList<Iris> D = method.getData();
        // try {
        //     FileOutputStream OrderedFile = new FileOutputStream("output/iris/ordered.csv", false);
        //     myOPTICS optics = new myOPTICS(0.5, 4);
        //     optics.cluster(D, OrderedFile);
        //     OrderedFile.close();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

    }
}
