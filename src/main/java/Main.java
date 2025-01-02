import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Double.NaN;
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

        Iris p = D.get(10);
        System.out.println(p.toString());

        p.findNeighbors(D, 0.5);

        for (DObject n : p.getNeighbors(D)){
            System.out.print(n.getID() + " - ");
            System.out.println(p.distance(n));
        }

        p.setCoreDistance(D, 5);
        System.out.println(p.getCoreDistance());

        // try {
        //     FileOutputStream OrderedFile = new FileOutputStream("data/iris/ordered.txt");
        //     OPTICS(D, 10,  3, OrderedFile);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

    }

    public static void OPTICS(ArrayList<? extends DObject> D, double epsilon, int MinPts, FileOutputStream OrderedFile)  throws IOException { 

        for (DObject o : D){
            if (!o.isProcessed()){
                ExpandClusterOrder(D, o, epsilon, MinPts, OrderedFile);
            }
        }
        OrderedFile.close();
    }

    public static void ExpandClusterOrder(ArrayList<? extends DObject> D, DObject obj, double epsilon, int MinPts, FileOutputStream OrderedFile) throws IOException {

        obj.findNeighbors(D, epsilon);
        obj.setProcessed();
        obj.setReachabilityDistance(NaN);
        obj.setCoreDistance(D, MinPts);

        OrderedFile.write(obj.toString().getBytes());

        PriorityQueue<DObject> orderSeeds = new PriorityQueue<>(Comparator.comparingDouble(DObject::getReachabilityDistance));
        if (isNaN(obj.getCoreDistance())){
            obj.update(orderSeeds, D);
            while(!orderSeeds.isEmpty()){
                DObject currentObject = orderSeeds.poll();
                currentObject.setProcessed();
                currentObject.setCoreDistance(D, MinPts);
                OrderedFile.write(currentObject.toString().getBytes());
                if (!isNaN(currentObject.getCoreDistance())){
                    currentObject.update(orderSeeds, D);
                }
            }
        }

    }

}
