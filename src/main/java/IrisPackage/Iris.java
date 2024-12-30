import .DObject;

package IrisPackage;
public class Iris extends DObject {
    private int     sepal_length;
    private int     sepal_width;
    private int     petal_length;
    private int     petal_width;
    private String  species;

    // Constructors
    public Iris(){
        this.sepal_length = 0;
        this.sepal_width = 0;
        this.petal_length = 0;
        this.petal_width = 0;
        this.species = "";
    }

    public Iris(int sepal_length, int sepal_width, int petal_length, int petal_width, String species){
        this.sepal_length = sepal_length;
        this.sepal_width = sepal_width;
        this.petal_length = petal_length;
        this.petal_width = petal_width;
        this.species = species;
    }


    // Getters
    public int getSepalLength(){
        return this.sepal_length;
    }

    public int getSepalWidth(){
        return this.sepal_width;
    }

    public int getPetalLength(){
        return this.petal_length;
    }

    public int getPetalWidth(){
        return this.petal_width;
    }

    public String getSpecies(){
        return this.species;
    }


    //Setters
    public void setSepalLength(int sepal_length){
        this.sepal_length = sepal_length;
    }

    public void setSepalWidth(int sepal_width){
        this.sepal_width = sepal_width;
    }

    public void setPetalLength(int petal_length){
        this.petal_length = petal_length;
    }

    public void setPetalWidth(int petal_width){
        this.petal_width = petal_width;
    }

    public void setSpecies(String species){
        this.species = species;
    }


    // Implementation of abstract methods
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

    public Iris extractData(String line){
        Iris obj = new Iris();
        String[] values = line.split(",");
        obj.sepal_length = Integer.parseInt(values[0]);
        obj.sepal_width = Integer.parseInt(values[1]);
        obj.petal_length = Integer.parseInt(values[2]);
        obj.petal_width = Integer.parseInt(values[3]);
        obj.species = values[4];

        return obj;
    }
}
