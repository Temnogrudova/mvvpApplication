package mediapplication.ekaterinatemnogrudova.mvvmtestproject.models;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

public class Item {
    private int columns = 0;
    private String url ;
    private double imageRatio;
    private long id;

    public Item(String url, double imageRatio, long id) {
        this.url = url;
        this.imageRatio = imageRatio;
        this.id = id;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getImageRatio() {
        return imageRatio;
    }

    public void setImageRatio(double imageRatio) {
        this.imageRatio = imageRatio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Item item = (Item) obj;
        return item.id == this.id;
    }
}
