import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Double.NaN;
import java.util.ArrayList;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IrisExtractionStrategy strategy = new IrisExtractionStrategy();
        strategy.extractData("data/iris/iris.data");

        ArrayList<Iris> D = strategy.getData();
   
        for (int i=0; i< D.size(); i++){
            D.get(i).findNeighbors(D, 3);
            System.out.println(D.get(i));
        }
    }

    public static void OPTICS(ArrayList<DObject> D, double epsilon, int MinPts, FileOutputStream OrderedFile)  throws IOException { 

        for (int i=0; i< D.size(); i++){
            DObject q = D.get(i);
            if (!q.isProcessed()){
                ExpandClusterOrder(D, q, epsilon, MinPts, OrderedFile);
            }
        }
        
        OrderedFile.close();

    }

    public static void ExpandClusterOrder(ArrayList<DObject> D, DObject obj, double epsilon, int MinPts, FileOutputStream OrderedFile) throws IOException {
        ArrayList<Integer> neighbors = obj.getNeighbors();
        obj.setProcessed();
        obj.setReachabilityDistance(NaN);

        OrderedFile.write(obj.getID());


    }

}
