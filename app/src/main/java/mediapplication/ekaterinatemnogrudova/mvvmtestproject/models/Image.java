package mediapplication.ekaterinatemnogrudova.mvvmtestproject.models;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("previewURL")
    private String previewURL;
    @SerializedName("id")
    private long id;
    @SerializedName("webformatHeight")
    private int webformatHeight;
    @SerializedName("webformatWidth")
    private int webformatWidth;
    @SerializedName("largeImageURL")
    private String largeImageURL;

    public String getPreviewUrl() {
        return previewURL;
    }

    public void setPreviewUrl(String previewURL) {
        this.previewURL = previewURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getWebformatHeight() {
        return webformatHeight;
    }

    public void setWebformatHeight(int webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public int getWebformatWidth() {
        return webformatWidth;
    }

    public void setWebformatWidth(int webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Image image = (Image) obj;
        return image.id == this.id;
    }

}
