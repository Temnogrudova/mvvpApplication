package mediapplication.ekaterinatemnogrudova.mvvmtestproject.models;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

public class Item {
    public int columns = 0;
    public String url ;
    public double imageRatio;
    private long id;
    public Item(String url, double imageRatio, long id) {
        this.url = url;
        this.imageRatio = imageRatio;
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
