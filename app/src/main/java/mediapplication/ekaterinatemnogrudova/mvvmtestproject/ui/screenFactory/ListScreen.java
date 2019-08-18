package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.screenFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.net.URL;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.FragmentImagesBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.ImageItemBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImagesAdapter;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.BitmapUtil;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ImagesViewModel;

public class ListScreen extends BaseScreen  {
    private Constants.STATE currentState;
    private int currentPosition;

    public ListScreen(Constants.STATE currentState,
                      int currentPosition,
                      ImagesViewModel imagesViewModel,
                      FragmentImagesBinding _binder,
                      ImagesAdapter _adapter,
                      Activity _activity) {
        super(imagesViewModel, _binder, _adapter, _activity);
        this.currentState = currentState;
        this.currentPosition = currentPosition;
    }

    @Override
    public void draw() {
        binder.searchView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        binder.imagesList.setLayoutManager(layoutManager);
        adapter = new ImagesAdapter(activity, currentState, this);
        binder.imagesList.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (positionStart == 0) {
                    layoutManager.scrollToPosition(currentPosition);
                }
            }
        });
        replaceSubscription(sharedPreference.getQuery(activity));
    }
    @Override
    public void setImageParams(ImageItemBinding binding) {
        super.setImageParams(binding);
        ViewGroup.LayoutParams params = binding.image.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
        binding.image.setLayoutParams(params);
    }

    @Override
    public void onImageSelected(Item item, int position) {
        shareImage(item, binding);
    }

    @Override
    public void bindTo(Item item) {
        Glide.with(binding.image.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_no_image)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(binding.image);
    }

    private void shareImage(Item item, ImageItemBinding binding){
        Snackbar snackbar = Snackbar.make(binding.getRoot(), R.string.action_share_message, Snackbar.LENGTH_SHORT);
        TextView textView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_share, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        textView.setOnClickListener(v1 -> {
            try {
                URL url = new URL(item.getImageUrl());
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, BitmapUtil.getLocalBitmapUri(image));
                activity.startActivity(Intent.createChooser(i, "Share Image"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        snackbar.show();
    }

}