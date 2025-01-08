import jsat.clustering.OPTICS;

import org.junit.jupiter.api.Test;

package myProject;
import myProject.DataExtractionMethod.IrisExtractionMethod;
import myProject.DataExtractionMethod.Point2DExtractionMethod;
import myProject.Datatype.Iris;
import myProject.Datatype.Point2D;
import myProject.OPTICS.myOPTICS;

public class TestOPTICS{

    @Test
    public void testIris(String[] args){
        IrisExtractionStrategy strategy = new IrisExtractionStrategy();
        strategy.extractData(data_path);
        ArrayList<Iris> D = strategy.getData();
        try {
            FileOutputStream OrderedFile = new FileOutputStream("output/iris/ordered.csv", false);
            myOPTICS optics = new myOPTICS();
            OrderedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OPTICS opt = new OPTICS();
    }

    @Test
    public void testPoint2D(){
        Point2DExtractionMethod strategy = new Point2DExtractionMethod();
        strategy.extractData(data_path);
        ArrayList<Point2D> D = new ArrayList<>();
        D = strategy.getData();
        try {
            FileOutputStream OrderedFile = new FileOutputStream("output/2d/ordered.csv", false);
            uOPTICS(D, 1,  4, OrderedFile);
            OrderedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}