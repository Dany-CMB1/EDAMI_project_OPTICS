package IrisPackage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import IOStrategy.DataExtractionStrategy;

public class IrisExtractionStrategy implements DataExtractionStrategy{
    
    private ArrayList<Iris> data;

    public ArrayList<Iris> getData(){
        return this.data;
    }

    public void extractData(String path){

        try {
            Scanner scanner = new Scanner(new File(path));
            scanner.useDelimiter("\n");

            int i = 0;
            while(scanner.hasNext()){
                String line = scanner.next();
                String[] parts = line.split(",");
                
                Iris iris = new Iris();

                iris.setSepalLength(Integer.parseInt(parts[0]));
                iris.setSepalWidth(Integer.parseInt(parts[1]));
                iris.setPetalLength(Integer.parseInt(parts[2]));
                iris.setPetalWidth(Integer.parseInt(parts[3]));
                iris.setSpecies(parts[4]);
                iris.setID(i);

                i++;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
