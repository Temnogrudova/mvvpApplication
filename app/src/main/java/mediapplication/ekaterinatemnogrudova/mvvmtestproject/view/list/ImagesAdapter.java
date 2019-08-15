package mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list;

import android.arch.lifecycle.LifecycleOwner;
import android.databinding.DataBindingUtil;
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
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Image;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.RepoViewHolder>{
    private int imagePreviewSize;
    private ImageSelectedListener imageSelectedListener;
    private final List<Item> data = new ArrayList<>();

    ImagesAdapter(ImageSelectedListener imageSelectedListener, int imagePreviewSize) {
        this.imageSelectedListener = imageSelectedListener;
        this.imagePreviewSize = imagePreviewSize;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_image_view_item, parent, false);
      //  view.getLayoutParams().height = imagePreviewSize;
      //  view.getLayoutParams().width = imagePreviewSize;
      //  view.requestLayout();
        return new RepoViewHolder(view, imageSelectedListener);
    }


    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
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

    static final class RepoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        private Item image;
        private ViewDataBinding binding;

        RepoViewHolder(View itemView, ImageSelectedListener imageSelectedListener) {
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