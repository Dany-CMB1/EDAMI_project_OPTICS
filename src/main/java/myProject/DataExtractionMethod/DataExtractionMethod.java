package myProject.DataExtractionMethod;

import java.util.ArrayList;

import myProject.Datatype.DObject;

public interface DataExtractionMethod {
    public void extractData(String path);
    public ArrayList<? extends DObject> getData();
}
