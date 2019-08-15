package mediapplication.ekaterinatemnogrudova.mvvmtestproject.models;

public class Item {
    public int columns = 0;
    public String url ;
    public double imageRatio;

    public Item(String url, double imageRatio) {
        this.url = url;
        this.imageRatio = imageRatio;
    }
}
