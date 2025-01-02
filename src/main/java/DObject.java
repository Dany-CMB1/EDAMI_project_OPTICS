import static java.lang.Double.isNaN;
import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class DObject {
    private int ID = 0;
    private int clusterID = 0;
    private double reachabilityDistance =  Double.POSITIVE_INFINITY;
    private double coreDistance = Double.POSITIVE_INFINITY;
    private boolean processed = false;
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

    public void setProcessed(){
        this.processed = true;
    }

    public void setReachabilityDistance(double reachabilityDistance){
        this.reachabilityDistance = reachabilityDistance;
    }

    public void setCoreDistance(ArrayList<? extends DObject> D, int MinPts){

        int countedNeighbors = 0;
        ArrayList<? extends DObject> neighbors = this.getNeighbors(D);

        for (DObject n : neighbors){

            if (this.distance(n) < this.coreDistance){
                this.coreDistance = this.distance(n);
                countedNeighbors++;
            }
        }

        if (countedNeighbors < MinPts){
            this.coreDistance = Double.POSITIVE_INFINITY;
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
                if (isNaN(n.getReachabilityDistance())){
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




