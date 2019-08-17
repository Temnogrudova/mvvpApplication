package mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import jp.wasabeef.glide.transformations.CropTransformation;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>{
    private ImageSelectedListener imageSelectedListener;
    private final List<Item> data = new ArrayList<>();

    ImagesAdapter(ImageSelectedListener imageSelectedListener) {
        this.imageSelectedListener = imageSelectedListener;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_image_view_item, parent, false);
        return new ImagesViewHolder(view, imageSelectedListener);
    }

    @Override
    public void onBindViewHolder(ImagesViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Item> imageList) {
        data.addAll(imageList);
        notifyDataSetChanged();
    }

    public int spanSizeLookup(int position) {
        return  data.get(position).columns;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    static final class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        private Item image;

        ImagesViewHolder(View itemView, ImageSelectedListener imageSelectedListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(v -> {
                if(image != null) {
                    imageSelectedListener.onImageSelected(image);
                }
            });
        }

        void bind(Item image) {
            this.image = image;
            Glide.with(imageView.getContext())
                    .load(image.url).bitmapTransform(new CropTransformation(imageView.getContext()))
                    .into(imageView);
        }
    }


}