package myProject;
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

    // OPTICS main loop
    public void cluster(ArrayList<? extends DObject> D, ArrayList<Integer> orderedFile) { 

        for (DObject o : D){
            if (!o.isProcessed()){
                this.ExpandClusterOrder(D, o, orderedFile);
            }
        }
    }

    // Expand a cluster order
    private void ExpandClusterOrder(ArrayList<? extends DObject> D, DObject obj, ArrayList<Integer> orderedFile) {

        obj.findNeighbors(D, this.epsilon);
        obj.setProcessed();
        obj.setReachabilityDistance(Double.NaN);
        obj.findCoreDistance(D, this.MinPts);

        orderedFile.add(obj.getID());

        // If the object is a core object, find new expansion candidates
        if (!Double.isNaN(obj.getCoreDistance())){
            this.orderSeedsupdate(obj, D);
            while(!this.orderSeeds.isEmpty()){
                DObject currentObject = orderSeeds.poll();
                if (orderSeeds.contains(currentObject)){
                    throw new IllegalStateException("Object " + currentObject.getID() + " is still in orderSeeds after being polled");
                }

                currentObject.findNeighbors(D, this.epsilon);
                currentObject.setProcessed();
                currentObject.findCoreDistance(D, this.MinPts);

                orderedFile.add(currentObject.getID());

                if (!Double.isNaN(currentObject.getCoreDistance())){
                    this.orderSeedsupdate(currentObject, D);
                }
            }
        }

    }

    // Update the priority queue seed list
    private void orderSeedsupdate(DObject currentObject, ArrayList<? extends DObject> D){

        double core_distance = currentObject.getCoreDistance();

        for (DObject n : currentObject.getNeighbors(D)){
            if (!n.isProcessed()){
                double newReachDist = Math.max(core_distance, currentObject.distance(n));

                // n is not in orderSeeds
                if (!orderSeeds.contains(n)){
                    n.setReachabilityDistance(newReachDist);
                    this.orderSeeds.add(n);              
                }
                // n is in orderSeeds ==> update reachability distance if newReachDist is smaller
                else if (newReachDist < n.getReachabilityDistance()){
                    n.setReachabilityDistance(newReachDist);  
                    this.orderSeeds.remove(n);    
                    this.orderSeeds.add(n);
                }       
            }
        }   
    }
}
