package myProject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import myProject.Datatype.DObject;

public class myOPTICS {

    private final double epsilon;
    private final int MinPts;
    private PriorityQueue<DObject> orderSeeds = new PriorityQueue<>(Comparator.comparingDouble(DObject::getReachabilityDistance));

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

    public PriorityQueue<DObject> getOrderSeeds() {
        return this.orderSeeds;
    }

    public void cluster(ArrayList<? extends DObject> D, FileOutputStream OrderedFile)  throws IOException { 

        for (DObject o : D){
            if (!o.isProcessed()){
                this.ExpandClusterOrder(D, o, OrderedFile);
            }
        }
    }

    private void ExpandClusterOrder(ArrayList<? extends DObject> D, DObject obj, FileOutputStream OrderedFile) throws IOException {

        obj.findNeighbors(D, this.epsilon);
        obj.setProcessed();
        obj.setReachabilityDistance(Double.NaN);
        obj.setCoreDistance(D, this.MinPts);

        String line = obj.getID() +"\n";
        OrderedFile.write(line.getBytes());

        // If the object is a core object, find new expansion candidates
        if (!Double.isNaN(obj.getCoreDistance())){
            this.update(obj, D);
            while(!this.orderSeeds.isEmpty()){
                DObject currentObject = orderSeeds.poll();

                currentObject.findNeighbors(D, this.epsilon);
                currentObject.setProcessed();
                currentObject.setCoreDistance(D, this.MinPts);

                line = currentObject.getID() +"\n";
                OrderedFile.write(line.getBytes());

                if (!Double.isNaN(currentObject.getCoreDistance())){
                    this.update(currentObject, D);
                }
            }
        }

    }

    private void update(DObject currentObject, ArrayList<? extends DObject> D){

        for (DObject n : currentObject.getNeighbors(D)){
            if (!n.isProcessed()){
                double newReachDist = Math.max(currentObject.getCoreDistance(), currentObject.distance(n));

                // n is not in orderSeeds
                if (!this.orderSeeds.contains(n)){
                    n.setReachabilityDistance(newReachDist);                   
                }
                // n is in orderSeeds ==> update reachability distance if newReachDist is smaller
                else{             
                    if (newReachDist < n.getReachabilityDistance()){
                        this.orderSeeds.remove(n);
                        n.setReachabilityDistance(newReachDist);      
                    }
                }
                this.orderSeeds.add(n);
            }
        }   
    }

}
