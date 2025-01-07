package myProject;
import java.util.ArrayList;
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
    protected String whereProcessed;
    protected ArrayList<Integer> neighbors = new ArrayList<>();

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
        if(this.processed){
            throw new IllegalStateException("\nObject " + this.ID + " already processed\n" );
        }
        else this.processed = true;
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




