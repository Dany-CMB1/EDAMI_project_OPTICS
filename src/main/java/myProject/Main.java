package myProject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import myProject.DataExtractionMethod.IrisExtractionMethod;
import myProject.Datatype.Iris;


public class Main {
    public static void main(String[] args) {
        IrisExtractionMethod method = new IrisExtractionMethod();
        method.extractData("data/iris/iris.data");
        ArrayList<Iris> D = method.getData();
        try {
            FileOutputStream OrderedFile = new FileOutputStream("res/iris/ordered.csv", false);
            myOPTICS optics = new myOPTICS(0.5, 4);
            optics.cluster(D, OrderedFile);
            OrderedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
