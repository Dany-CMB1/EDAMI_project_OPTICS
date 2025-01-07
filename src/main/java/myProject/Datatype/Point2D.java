package myProject.Datatype;

public class Point2D extends DObject {
    protected double x;
    protected double y;

    // Constructors
    public Point2D(){
        this.x = 0;
        this.y = 0;
    }

    public Point2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    // Getters
    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public double[] getCoordinates(){
        return new double[]{this.x, this.y};
    }

    // Setters
    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setCoordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public double distance(DObject p){
        if (!(p instanceof Point2D)){
            return -1;
        }

        Point2D point = (Point2D) p;
        return Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
