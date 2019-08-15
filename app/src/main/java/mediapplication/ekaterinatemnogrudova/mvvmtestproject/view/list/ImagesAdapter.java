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

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.RepoViewHolder>{

    private ImageSelectedListener imageSelectedListener;
    private final List<Image> data = new ArrayList<>();

    ImagesAdapter(ImagesListViewModel viewModel, LifecycleOwner lifecycleOwner, ImageSelectedListener imageSelectedListener) {
        this.imageSelectedListener = imageSelectedListener;
        viewModel.getImageList().observe(lifecycleOwner, imageList -> {
            data.clear();
            if (imageList != null) {
                data.addAll(imageList);
                notifyDataSetChanged();
            }
        });
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewDataBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_image_view_item, parent, false);

        ImagesAdapter.RepoViewHolder holder = new ImagesAdapter.RepoViewHolder(binding.getRoot(), imageSelectedListener);
        holder.setBinding(binding);
        return holder;
    }


    @Override
    public void onBindViewHolder(ImagesAdapter.RepoViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
/*
    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }
*/
    static final class RepoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        private Image image;
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
        public ViewDataBinding getBinding() {
            return binding;
    }

        public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }
        void bind(Image image) {
            this.image = image;
            Glide.with(imageView.getContext())
                    .load(image.getPreviewUrl()).bitmapTransform(new CropTransformation(imageView.getContext()))
                    .into(imageView);
        }
    }


}