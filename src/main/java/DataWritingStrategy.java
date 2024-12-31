import java.io.FileOutputStream;
import java.io.IOException;

public interface DataWritingStrategy {

    public void writeData(FileOutputStream OrderedFile, DObject obj) throws IOException;
}
