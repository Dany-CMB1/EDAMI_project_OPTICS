package myProject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class myOPTICS {

    private double epsilon;
    private int MinPts;

    // Constructor
    public myOPTICS(double epsilon, int MinPts) {
        this.epsilon = epsilon;
        this.MinPts = MinPts;
    }

    // Getters
    public double getEpsilon() {
        return this.epsilon;
    }

    public int getMinPts() {
        return this.MinPts;
    }

    // Setters
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setMinPts(int MinPts) {
        this.MinPts = MinPts;
    }

    public void cluster(ArrayList<? extends DObject> D, FileOutputStream OrderedFile)  throws IOException { 

        for (DObject o : D){
            if (!o.isProcessed()){
                this.ExpandClusterOrder(D, o, OrderedFile);
            }
        }
    }

    public void ExpandClusterOrder(ArrayList<? extends DObject> D, DObject obj, FileOutputStream OrderedFile) throws IOException {

        obj.findNeighbors(D, this.epsilon);
        obj.setProcessed();
        obj.setReachabilityDistance(Double.NaN);
        obj.setCoreDistance(D, this.MinPts);

        String line = obj.getID() +"\n";
        OrderedFile.write(line.getBytes());

        // If the object is a core object, find new expansion candidates
        if (!Double.isNaN(obj.getCoreDistance())){
            PriorityQueue<DObject> orderSeeds = new PriorityQueue<>(Comparator.comparingDouble(DObject::getReachabilityDistance));
            obj.update(orderSeeds, D);
            while(!orderSeeds.isEmpty()){
                DObject currentObject = orderSeeds.poll();

                if (!currentObject.isProcessed()){
                    currentObject.findNeighbors(D, this.epsilon);
                    currentObject.setProcessed();
                    currentObject.setCoreDistance(D, this.MinPts);

                    line = currentObject.getID() +"\n";
                    OrderedFile.write(line.getBytes());

                    if (!Double.isNaN(currentObject.getCoreDistance())){
                        currentObject.update(orderSeeds, D);
                    }
                }
            }
        }

    }

}
