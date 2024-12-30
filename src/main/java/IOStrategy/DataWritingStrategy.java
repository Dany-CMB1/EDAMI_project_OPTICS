package IOStrategy;
import java.io.FileOutputStream;
import java.io.IOException;

import DObject;

public interface DataWritingStrategy {

    public void writeData(FileOutputStream OrderedFile, DObject obj) throws IOException;
}
