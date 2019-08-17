package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import jp.wasabeef.glide.transformations.CropTransformation;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.NetworkItemBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.NetworkState;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.FeedItemBinding;

public class ImagesAdapter extends PagedListAdapter<Item, RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;
    private ImageSelectedListener imageSelectedListener;

    private NetworkState networkState;
    public ImagesAdapter(ImageSelectedListener imageSelectedListener) {
        super(Item.DIFF_CALLBACK);
        this.imageSelectedListener = imageSelectedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_PROGRESS) {
            NetworkItemBinding headerBinding = NetworkItemBinding.inflate(layoutInflater, parent, false);
            NetworkStateItemViewHolder viewHolder = new NetworkStateItemViewHolder(headerBinding);
            return viewHolder;

        } else {
            FeedItemBinding itemBinding = FeedItemBinding.inflate(layoutInflater, parent, false);
            ArticleItemViewHolder viewHolder = new ArticleItemViewHolder(itemBinding, imageSelectedListener);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ArticleItemViewHolder) {
            ((ArticleItemViewHolder)holder).bindTo(getItem(position));
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
    }

    public int spanSizeLookup(int position) {
        return  getItem(position).getColumns();
    }
    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }


    public class ArticleItemViewHolder extends RecyclerView.ViewHolder {

        private FeedItemBinding binding;
        private Item item;

        public ArticleItemViewHolder(FeedItemBinding binding, ImageSelectedListener imageSelectedListener) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                if(item != null) {
                    imageSelectedListener.onImageSelected(item);
                }
            });
        }

        public void bindTo(Item image) {
            this.item = image;
            Glide.with(binding.image.getContext())
                    .load(image.getUrl()).bitmapTransform(new CropTransformation(binding.image.getContext()))
                    .into(binding.image);
        }
    }


    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        private NetworkItemBinding binding;
        public NetworkStateItemViewHolder(NetworkItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                binding.errorMsg.setVisibility(View.VISIBLE);
                binding.errorMsg.setText(networkState.getMsg());
            } else {
                binding.errorMsg.setVisibility(View.GONE);
            }
        }
    }
}