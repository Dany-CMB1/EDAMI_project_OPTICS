import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class IrisExtractionStrategy implements DataExtractionStrategy{
    
    private ArrayList<Iris> data;

    public ArrayList<Iris> getData(){
        return this.data;
    }

    public void extractData(String path){

        this.data = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(path));
            scanner.useDelimiter("\n");

            int i = 0;
            while(scanner.hasNext()){

                String line = scanner.next();
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

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
