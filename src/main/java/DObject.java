import java.util.ArrayList;

public abstract class DObject {
    private int ID = 0;
    private int clusterID = 0;
    private double reachabilityDistance = 0;
    private double coreDistance = Double.POSITIVE_INFINITY;
    private boolean processed = false;
    private ArrayList<Integer> neighbors = new ArrayList<>();

    public abstract double distance(DObject p);
    public abstract DObject extractData(String line);


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

    public ArrayList<Integer> getNeighbors(){
        return this.neighbors;
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

    public void setCoreDistance(ArrayList<DObject> D, int MinPts){

        int countedNeighbors = 0;
        for (int i=0; i< this.neighbors.size(); i++){
            DObject n = D.get(this.neighbors.get(i));

            if (this.distance(n) < this.coreDistance){
                this.coreDistance = this.distance(n);
            }
            countedNeighbors++;
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
        for (int i=0; i< D.size(); i++){
            DObject q = D.get(i);
            if (this.getID() !=q.getID() && this.distance(q) <= epsilon){
                this.neighbors.add(q.getID());
            }
        }
    }

}




