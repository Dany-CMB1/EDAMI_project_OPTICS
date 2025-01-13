package myProject.Datatype;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset; 

public abstract class DObject {
    protected int ID = 0;
    protected int clusterID = 0;
    protected double reachabilityDistance = Double.NaN;
    protected double coreDistance = Double.NaN;
    protected boolean processed = false;

    // Array of intergers containing the IDs of the neighbors of the object
    protected ArrayList<Integer> neighbors = new ArrayList<>();

    public abstract double distance(DObject p);


    //Clone 
    public abstract DObject clone();

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

    // Get the IDs of the neighbors of the object
    public ArrayList<Integer> getNeighborsID(){
        return this.neighbors;
    }

    // Get the neighbors of the object, ie the objects of the dataset D whose IDs are in the neighbors array
    public ArrayList<? extends DObject> getNeighbors(ArrayList<? extends DObject> D){
        ArrayList<DObject> neighborhood = new ArrayList<>();
        for (int nID : this.neighbors){
            neighborhood.add(D.get(nID));    
        }
        return neighborhood;
    }

    public abstract ArrayList<Double> getValues();

    //Setters
    public void setID(int ID){
        this.ID = ID;
    }

    public void setProcessed(){
        if(this.processed){
            throw new IllegalStateException("\nObject " + this.ID + " already processed \n" );
        }
        else{
            this.processed = true;
        }
    }

    public void setReachabilityDistance(double rDistance){
        this.reachabilityDistance = rDistance;
    }

    // Set the core distance of the object, as described in the OPTICS algorithm
    public void setCoreDistance(ArrayList<? extends DObject> D, int MinPts){

        ArrayList<? extends DObject> neighborhood = this.getNeighbors(D);
        ArrayList<Double> distancesToNeighbors = new ArrayList<>();

        // Find the MinPts-th closest neighbor and set the core distance to the distance to that neighbor
        if (neighborhood.size() >= MinPts){
            for (DObject n : neighborhood){
                distancesToNeighbors.add(this.distance(n));
            }
            distancesToNeighbors.sort(null);
            this.coreDistance = distancesToNeighbors.get(MinPts - 1);
        }else{
            this.coreDistance = Double.NaN;
        }
    }   

    public void setClusterID(int clusterID){
        this.clusterID = clusterID;
    }

    //Find the neighbors of the object within a distance epsilon
    //!  An object is NOT considered as a neighbor of itself ! 
    public void findNeighbors(ArrayList<? extends DObject> D, double epsilon) throws IllegalStateException{
        // Attribute neighbors can be populated during statistics calculation, but the definition of neighbors is not the same as here
        if (!this.neighbors.isEmpty()){
            this.neighbors.clear();
        }
        for (DObject q : D){
            if (this.getID() != q.getID() && this.distance(q) <= epsilon && this.neighbors.indexOf(q.getID()) == -1){
                this.neighbors.add(q.getID());
            }
        }
    }

    //Find the maxSamples-closest neighbors of an object among a list of objects
    //!  An object is NOT considered as a neighbor of itself ! 
    public void findClosestNeighbors(ArrayList<? extends DObject> D, int maxSamples){
        // Min-heap to store <ObjectID, Distance>, keeping closest neighbors
        PriorityQueue<SimpleEntry<Integer, Double>> pq = new PriorityQueue<>(
            Comparator.comparingDouble(SimpleEntry::getValue)
        );

        for (int i = 0; i < D.size(); i++) {
            DObject other = D.get(i);
            if (this == other) continue; // Skip self

            double distance = this.distance(other);
            pq.add(new SimpleEntry<>(i, distance));

            // Remove farthest neighbor if the heap exceeds maxSamples
            if (pq.size() > maxSamples) {
                pq.poll();
            }
        }

        // Retrieve neighbors from the priority queue in reverse order (closest first)
        while (!pq.isEmpty()) {
            this.neighbors.add(0,pq.poll().getKey());
        }
    }


    @Override
    public String toString(){
        return this.getID() + "," + this.getCoreDistance() + "," + this.getReachabilityDistance();
    }

    public static void plotReachability(ArrayList<? extends DObject> D){
       
        // Create dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (DObject o : D){
            dataset.addValue(o.getReachabilityDistance(), "Reachability", Integer.toString(o.getID()));
        }

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Reachability Plot",  // Chart title
                "Cluster-order of the objects",          // X-Axis Label
                "Reachability",  // Y-Axis Label
                dataset
        );

        // Show it in a JFrame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

}




