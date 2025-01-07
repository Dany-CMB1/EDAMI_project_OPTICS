import jsat.clustering.OPTICS;

public class Eval{

    public static main(String[] args){
        String data_path = args[0];
        String datatype = args[1];  

        if(datatype.equals("iris")){
            IrisExtractionStrategy strategy = new IrisExtractionStrategy();
            strategy.extractData(data_path);
            ArrayList<Iris> D = strategy.getData();
            try {
                FileOutputStream OrderedFile = new FileOutputStream("res/iris/ordered.csv", false);
                myOPTICS optics = new myOPTICS();
                OrderedFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            OPTICS opt = new OPTICS();


        } else if(datatype.equals("2d")){
            Point2DExtractionMethod strategy = new Point2DExtractionMethod();
            strategy.extractData(data_path);
            ArrayList<Point2D> D = new ArrayList<>();
            D = strategy.getData();
            try {
                FileOutputStream OrderedFile = new FileOutputStream("res/2d/ordered.csv", false);
                uOPTICS(D, 1,  4, OrderedFile);
                OrderedFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}