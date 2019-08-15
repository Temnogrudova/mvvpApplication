package mediapplication.ekaterinatemnogrudova.mvvmtestproject.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImagesResponse {
    @SerializedName("totalHits")
    private int totalHits;
    @SerializedName("total")
    private int total;
    @SerializedName("hits")
    private List<Image> hits;
    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Image> getHits() {
        return hits;
    }

    public void setHits(List<Image> hits) {
        this.hits = hits;
    }

}
