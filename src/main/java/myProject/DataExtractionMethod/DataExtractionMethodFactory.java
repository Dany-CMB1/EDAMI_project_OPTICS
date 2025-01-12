package myProject.DataExtractionMethod;

public class DataExtractionMethodFactory {
    public static DataExtractionMethod getMethod(String category) throws Exception {
        switch (category) {
            case "2d":
                return new Point2DExtractionMethod();
            case "iris":
                return new IrisExtractionMethod();
            default:
                throw new IllegalArgumentException("No data extraction method found for category: " + category);
        }
    }
}
