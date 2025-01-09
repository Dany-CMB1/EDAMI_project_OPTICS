package myProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import DataMiningSandbox.DBPoint;
import DataMiningSandbox.Optics;
import jsat.ARFFLoader;
import jsat.DataSet;
import jsat.clustering.OPTICS;
import myProject.DataExtractionMethod.IrisExtractionMethod;
import myProject.Datatype.Iris;
import myProject.utils.CSVToARFFConverter;
import myProject.utils.ConvertProjectToDMS;
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
        
        // Use custom OPTICS implementation
        IrisExtractionMethod method = new IrisExtractionMethod();
        method.extractData("data/iris/iris.data");
        ArrayList<Iris> D = method.getData();

        // Estimate radius as done in jsat.clustering.OPTICS.cluster
        DataSetStats stats = new DataSetStats(D, 4);
        double radius = stats.getMean() + stats.getStandardDeviation() * 3;

        try {
            FileOutputStream OrderedFile = new FileOutputStream("output/iris/ordered.csv", false);
            myOPTICS optics = new myOPTICS(radius, 4);
            optics.cluster(D, OrderedFile);
            OrderedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Use DataMiningSandbox OPTICS implementation
        DataMiningSandbox.Optics DMOptics = new Optics();
        HashMap<Integer, DBPoint> dataset = ConvertProjectToDMS.convert(D);
        DMOptics.optics(dataset, radius, 4);
    }
}
