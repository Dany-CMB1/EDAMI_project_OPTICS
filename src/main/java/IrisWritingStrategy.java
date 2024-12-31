import java.io.FileOutputStream;
import java.io.IOException;

public class IrisWritingStrategy implements DataWritingStrategy{
    
    public void writeData(FileOutputStream OrderedFile, DObject obj) throws IOException{
        Iris iris = (Iris) obj;
        String data = iris.getSepalLength() + "," + iris.getSepalWidth() + "," + iris.getPetalLength() + "," + iris.getPetalWidth() + "," + iris.getSpecies() + "\n";
        OrderedFile.write(data.getBytes());
    }
    
}
