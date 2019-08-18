package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.NetworkItemBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.NetworkState;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.ImageItemBinding;

public class ImagesAdapter extends PagedListAdapter<Item, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;
    private ImageSelectedListener screenSelectedListener;
    private NetworkState networkState;
    private Constants.STATE currentState;
    private Context context;

    public ImagesAdapter(Context context, Constants.STATE currentState, ImageSelectedListener screenSelectedListener) {
        super(Item.DIFF_CALLBACK);
        this.screenSelectedListener = screenSelectedListener;
        this.currentState = currentState;
        this.context = context;
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
            ImageItemBinding itemBinding = ImageItemBinding.inflate(layoutInflater, parent, false);
            ImageItemViewHolder viewHolder = new ImageItemViewHolder(itemBinding, screenSelectedListener);
            return viewHolder;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ImageItemViewHolder) {
            ((ImageItemViewHolder)holder).bindTo(getItem(position), position);
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

    public class ImageItemViewHolder extends RecyclerView.ViewHolder {

        private ImageItemBinding binding;
        private Item item;
        private int position;
        public ImageItemViewHolder(ImageItemBinding binding, ImageSelectedListener imageSelectedListener) {
            super(binding.getRoot());
            this.binding = binding;
            imageSelectedListener.setImageParams(binding);
            binding.image.setOnClickListener(v -> {
                if(item != null) {
                    imageSelectedListener.onImageSelected(item, position);
                }
            });
        }

        public void bindTo(Item item, int position) {
            this.position = position;
            this.item = item;
            screenSelectedListener.bindTo(item);
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