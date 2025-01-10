package myProject.utils;

import java.util.ArrayList;
import java.util.HashMap;

import DataMiningSandbox.DBPoint;
import myProject.Datatype.DObject;

public class ConvertProjectToDMS {
    public static HashMap<Integer, DBPoint> convert(ArrayList<? extends DObject> D)  {
        HashMap<Integer, DBPoint> dataset = new HashMap<>();

        for (DObject obj : D) {
            DBPoint point = new DBPoint();
            point.values = obj.getValues();
            point.ID = obj.getID();
            dataset.put(obj.getID(), point);
        }
        return dataset;
    }
    
}
