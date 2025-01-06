import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Double.isNaN;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        IrisExtractionStrategy strategy = new IrisExtractionStrategy();
        strategy.extractData("data/iris/iris.data");

        ArrayList<Iris> D = strategy.getData();

        try {
            FileOutputStream OrderedFile = new FileOutputStream("res/iris/ordered.csv", false);
            uOPTICS(D, 1,  4, OrderedFile);
            OrderedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void uOPTICS(ArrayList<? extends DObject> D, double epsilon, int MinPts, FileOutputStream OrderedFile)  throws IOException { 

        for (DObject o : D){
            if (!o.isProcessed()){
                ExpandClusterOrder(D, o, epsilon, MinPts, OrderedFile);
            }
        }
    }

    public static void ExpandClusterOrder(ArrayList<? extends DObject> D, DObject obj, double epsilon, int MinPts, FileOutputStream OrderedFile) throws IOException {

        obj.findNeighbors(D, epsilon);
        obj.setProcessed();
        obj.setReachabilityDistance(Double.NaN);
        obj.setCoreDistance(D, MinPts);

        String line = obj.getID() +"\n";
        OrderedFile.write(line.getBytes());

        // If the object is a core object, find new expansion candidates
        if (!isNaN(obj.getCoreDistance())){
            PriorityQueue<DObject> orderSeeds = new PriorityQueue<>(Comparator.comparingDouble(DObject::getReachabilityDistance));
            obj.update(orderSeeds, D);
            while(!orderSeeds.isEmpty()){
                DObject currentObject = orderSeeds.poll();

                if (!currentObject.isProcessed()){
                    currentObject.findNeighbors(D, epsilon);
                    currentObject.setProcessed();
                    currentObject.setCoreDistance(D, MinPts);

                    line = currentObject.getID() +"\n";
                    OrderedFile.write(line.getBytes());

                    if (!isNaN(currentObject.getCoreDistance())){
                        currentObject.update(orderSeeds, D);
                    }
                }
            }
        }

    }

}
