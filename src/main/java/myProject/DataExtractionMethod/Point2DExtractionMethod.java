package myProject.DataExtractionMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import myProject.Datatype.Point2D;

public class Point2DExtractionMethod implements DataExtractionMethod{
    
    private ArrayList<Point2D> data;

    @Override
    public ArrayList<Point2D> getData(){
        return this.data;
    }

    @Override
    public void extractData(String path) throws Exception {

        this.data = new ArrayList<>();

        Scanner scanner = new Scanner(new File(path));
        scanner.useDelimiter("\n");

        int i = 0;
        while(scanner.hasNext()){

            String line = scanner.next();
            if (line.isEmpty()){
                break;
            }
            String[] parts = line.split(",");

            Point2D point = new Point2D();

            // No class needed
            point.setX(Double.parseDouble(parts[0]));
            point.setY(Double.parseDouble(parts[1]));
            point.setID(i);

            this.data.add(point);

            i++;
        }
        scanner.close();

    }
}

