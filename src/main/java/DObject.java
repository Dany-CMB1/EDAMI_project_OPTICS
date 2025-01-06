import java.util.ArrayList;
import java.util.PriorityQueue;

import org.apache.commons.math3.ml.clustering.Clusterable;

public abstract class DObject implements Clusterable {
    private int ID = 0;
    private int clusterID = 0;
    private double reachabilityDistance = Double.NaN;
    private double coreDistance = Double.NaN;
    private boolean processed = false;
    public String whereProcessed;
    private ArrayList<Integer> neighbors = new ArrayList<>();

    public abstract double distance(DObject p);


    //Getters

    public int getID(){
        return this.ID;
    }

    public boolean isProcessed(){
        return this.processed;
    }

    public double getReachabilityDistance(){
        return this.reachabilityDistance;
    }

    public double getCoreDistance(){
        return this.coreDistance;
    }

    public int getClusterID(){
        return this.clusterID;
    }

    public ArrayList<Integer> getNeighborsID(){
        return this.neighbors;
    }

    public ArrayList<? extends DObject> getNeighbors(ArrayList<? extends DObject> D){
        ArrayList<DObject> neighborhood = new ArrayList<>();
        for (int nID : this.neighbors){
            neighborhood.add(D.get(nID));    
        }
        return neighborhood;
    }

    //Setters
    public void setID(int ID){
        this.ID = ID;
    }

    public void setProcessed(String where){
        if(this.processed){
            throw new IllegalStateException("\nObject " + this.ID + " already processed in " + this.whereProcessed + " and now in " + where + "\n" );
        }
        this.processed = true;
        this.whereProcessed = where;
    }

    public void setReachabilityDistance(double rDistance){
        this.reachabilityDistance = rDistance;
    }

    public void setCoreDistance(ArrayList<? extends DObject> D, int MinPts){

        int countedNeighbors = 0;
        ArrayList<? extends DObject> neighborhood = this.getNeighbors(D);

        // Find the MinPts-th closest neighbor and set the core distance to the distance to that neighbor
        ArrayList<Double> distancesToNeighbors = new ArrayList<>();
        for (DObject n : neighborhood){
            distancesToNeighbors.add(this.distance(n));
            if (++countedNeighbors == MinPts){
                break;
            }
        }
        distancesToNeighbors.sort(null);
        if (countedNeighbors >= MinPts){
            this.coreDistance = distancesToNeighbors.get(MinPts - 1);
        }else{
            this.coreDistance = Double.NaN;
        }
    }   

    public void setClusterID(int clusterID){
        this.clusterID = clusterID;
    }

    //Methods of DObject abstract class
    public void findNeighbors(ArrayList<? extends DObject> D, double epsilon){
        for (DObject q : D){
            if (this.getID() != q.getID() && this.distance(q) <= epsilon){
                this.neighbors.add(q.getID());
            }
        }
    }

    public void update(PriorityQueue<DObject> orderSeeds, ArrayList<? extends DObject> D){

        for (DObject n : this.getNeighbors(D)){
            if (!n.isProcessed()){
                double newReachDist = Math.max(this.coreDistance, this.distance(n));

                // n is not in orderSeeds
                if (!orderSeeds.contains(n)){
                    n.setReachabilityDistance(newReachDist);                   
                }
                // n is in orderSeeds ==> update reachability distance if newReachDist is smaller
                else{             
                    if (newReachDist < n.getReachabilityDistance()){
                        orderSeeds.remove(n);
                        n.setReachabilityDistance(newReachDist);      
                    }
                }
                orderSeeds.add(n);
            }
        }   
    }

    @Override
    public String toString(){
        return this.getID() + "," + this.getCoreDistance() + "," + this.getReachabilityDistance();
    }

}




