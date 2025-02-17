package myProject.Datatype;

import java.util.ArrayList;

public class Iris extends DObject {
    protected double     sepal_length;
    protected double     sepal_width;
    protected double     petal_length;
    protected double     petal_width;
    protected String  species;

    // Constructors
    public Iris(){
        this.sepal_length = 0;
        this.sepal_width = 0;
        this.petal_length = 0;
        this.petal_width = 0;
        this.species = "";
    }

    public Iris(double sepal_length, double sepal_width, double petal_length, double petal_width, String species){
        this.sepal_length = sepal_length;
        this.sepal_width = sepal_width;
        this.petal_length = petal_length;
        this.petal_width = petal_width;
        this.species = species;
    }


    // Getters
    public double getSepalLength(){
        return this.sepal_length;
    }

    public double getSepalWidth(){
        return this.sepal_width;
    }

    public double getPetalLength(){
        return this.petal_length;
    }

    public double getPetalWidth(){
        return this.petal_width;
    }

    public String getSpecies(){
        return this.species;
    }

    public ArrayList<Double> getValues(){
        ArrayList<Double> values = new ArrayList<>();
        values.add(this.sepal_length);
        values.add(this.sepal_width);
        values.add(this.petal_length);
        values.add(this.petal_width);
        return values;
    }

    //Setters
    public void setSepalLength(double sepal_length){
        this.sepal_length = sepal_length;
    }

    public void setSepalWidth(double sepal_width){
        this.sepal_width = sepal_width;
    }

    public void setPetalLength(double petal_length){
        this.petal_length = petal_length;
    }

    public void setPetalWidth(double petal_width){
        this.petal_width = petal_width;
    }

    public void setSpecies(String species){
        this.species = species;
    }


    // Implementation of abstract methods
    @Override
    public double distance(DObject p) {
        if (!(p instanceof Iris)){
            return -1;
        }

        Iris q = (Iris)p;

        double dist = Math.sqrt(
          Math.pow(this.sepal_length - q.sepal_length, 2)
         + Math.pow(this.sepal_width - q.sepal_width, 2) 
         + Math.pow(this.petal_length - q.petal_length, 2) 
         + Math.pow(this.petal_width - q.petal_width, 2));

        return dist;
    }

    @Override
    public String toString(){
        // return 
        //     this.sepal_length + "," + this.sepal_width + "," + this.petal_length + "," + this.petal_width + "," + this.species 
        //     + "," + super.toString() +"\n";
        return super.toString();
    }

    @Override
    public Iris clone(){
        return new Iris(this.sepal_length, this.sepal_width, this.petal_length, this.petal_width, this.species);
    }

}
