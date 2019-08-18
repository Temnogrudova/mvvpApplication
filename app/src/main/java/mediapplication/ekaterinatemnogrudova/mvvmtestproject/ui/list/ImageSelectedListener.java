package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.ImageItemBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;

public interface ImageSelectedListener {
    void setImageParams(ImageItemBinding binding);
    void onImageSelected(Item item, int position);
    void bindTo(Item item);
}