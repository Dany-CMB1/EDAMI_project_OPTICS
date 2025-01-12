package myProject.DataExtractionMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import myProject.Datatype.Iris;


public class IrisExtractionMethod implements DataExtractionMethod{
    
    private ArrayList<Iris> data;

    @Override
    public ArrayList<Iris> getData(){
        return this.data;
    }

    @Override
    public void extractData(String path) throws Exception {

        this.data = new ArrayList<>();

        Scanner scanner = new Scanner(new File(path));
        scanner.useDelimiter("\n");

        // Skip the first line
        scanner.next();
        
        int i = 0;
        while(scanner.hasNext()){

            String line = scanner.next();

            if (line.isEmpty()){
                break;
            }
            String[] parts = line.split(",");

            Iris iris = new Iris();

            iris.setSepalLength(Double.parseDouble(parts[0]));
            iris.setSepalWidth(Double.parseDouble(parts[1]));
            iris.setPetalLength(Double.parseDouble(parts[2]));
            iris.setPetalWidth(Double.parseDouble(parts[3]));
            iris.setSpecies(parts[4]);
            iris.setID(i);

            this.data.add(iris);

            i++;
        }
        scanner.close();
    }
}
