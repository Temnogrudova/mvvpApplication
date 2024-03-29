package mediapplication.ekaterinatemnogrudova.mvvmtestproject.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

public class Item  implements Parcelable {
    private int columns = 0;
    private String previewUrl;
    private String imageUrl;
    private double imageRatio;
    private long id;

    public Item(String previewUrl, String imageUrl, double imageRatio, long id) {
        this.previewUrl = previewUrl;
        this.imageUrl = imageUrl;
        this.imageRatio = imageRatio;
        this.id = id;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    @Override
    public int describeContents() {
        return 0;
    }
    protected Item(Parcel in) {
        columns = in.readInt();
        imageRatio = in.readDouble();
        id = in.readLong();
        previewUrl = in.readString();
        imageUrl = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(previewUrl);
        dest.writeString(imageUrl);
        dest.writeDouble(imageRatio);
        dest.writeInt(columns);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
