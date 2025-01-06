import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Double.isNaN;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        IrisExtractionStrategy strategy = new IrisExtractionStrategy();
        strategy.extractData("data/iris/iris.data");

        ArrayList<Iris> D = strategy.getData();

        try {
            FileOutputStream OrderedFile = new FileOutputStream("res/iris/ordered.txt", false);
            OPTICS(D, 0.5,  3, OrderedFile);
            OrderedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DBSCANClusterer<Iris> clusterer = new DBSCANClusterer(0.5, 3);
        try {
            FileOutputStream DBSCANResults = new FileOutputStream("res/iris/DBSCAN.txt", false);
            List<Cluster<Iris>> clusters = clusterer.cluster(D);
            for (Cluster<Iris> c : clusters){
                DBSCANResults.write("Cluster\n".getBytes());
                for (Iris i : c.getPoints()){
                    DBSCANResults.write(i.toString().getBytes());
                }
            }
            DBSCANResults.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void OPTICS(ArrayList<? extends DObject> D, double epsilon, int MinPts, FileOutputStream OrderedFile)  throws IOException { 

        OrderedFile.write("ID,CoreDistance,ReachabilityDistance\n".getBytes());
        for (DObject o : D){
            if (!o.isProcessed()){
                ExpandClusterOrder(D, o, epsilon, MinPts, OrderedFile);
            }
        }
    }

    public static void ExpandClusterOrder(ArrayList<? extends DObject> D, DObject obj, double epsilon, int MinPts, FileOutputStream OrderedFile) throws IOException {

        obj.findNeighbors(D, epsilon);
        obj.setProcessed("ExpandClusterOrder - arg object:" + obj.getID());
        obj.setReachabilityDistance(Double.NaN);
        obj.setCoreDistance(D, MinPts);

        OrderedFile.write(obj.toString().getBytes());

        // If the object is a core object, find new expansion candidates
        if (!isNaN(obj.getCoreDistance())){
            PriorityQueue<DObject> orderSeeds = new PriorityQueue<>(Comparator.comparingDouble(DObject::getReachabilityDistance));
            obj.update(orderSeeds, D);
            while(!orderSeeds.isEmpty()){
                DObject currentObject = orderSeeds.poll();

                if (!currentObject.isProcessed()){
                    currentObject.findNeighbors(D, epsilon);
                    currentObject.setProcessed("ExpandClusterOrder - arg object: " + obj.getID());
                    currentObject.setCoreDistance(D, MinPts);
                    OrderedFile.write(currentObject.toString().getBytes());
                    if (!isNaN(currentObject.getCoreDistance())){
                        currentObject.update(orderSeeds, D);
                    }
                }
            }
        }

    }

}
