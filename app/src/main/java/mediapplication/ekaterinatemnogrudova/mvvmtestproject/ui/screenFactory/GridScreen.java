package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.screenFactory;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.FragmentImagesBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.ImageItemBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImagesAdapter;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ImagesViewModel;

import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.COLUMNS;

public class GridScreen extends BaseScreen {
    private  Constants.STATE currentState;
    private ScreenFactory screenFactory;
    public GridScreen(Constants.STATE currentState,
                      ImagesViewModel imagesViewModel,
                      FragmentImagesBinding _binder,
                      ImagesAdapter _adapter,
                      Activity _activity,
                      ScreenFactory screenFactory) {
        super(imagesViewModel, _binder, _adapter, _activity);
        this.currentState = currentState;
        this.screenFactory = screenFactory;
    }

    @Override
    public void draw() {
        binder.searchView.setVisibility(View.VISIBLE);
        currentState = Constants.STATE.GRID;
        GridLayoutManager layoutManager = new GridLayoutManager(activity, COLUMNS);
        binder.imagesList.setLayoutManager(layoutManager);
        adapter = new ImagesAdapter(activity, currentState, this);
        binder.imagesList.setAdapter(adapter);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return adapter.spanSizeLookup(i);
            }
        });
        replaceSubscription(sharedPreference.getQuery(activity));
    }

    @Override
    public void onImageSelected(Item item, int position) {
        screenFactory.getScreen(Constants.STATE.LIST, position,imagesViewModel, binder, adapter, activity, screenFactory).draw();
        screenFactory.setCurrentPosition(position);
    }
    @Override
    public void setImageParams(ImageItemBinding binding) {
        super.setImageParams(binding);
        ViewGroup.LayoutParams params = binding.image.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if (binding.getRoot().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.image.getLayoutParams().height = activity.getResources().getDimensionPixelSize(R.dimen.image_vertical_height);
        }
        else
        {
            binding.image.getLayoutParams().height = activity.getResources().getDimensionPixelSize(R.dimen.image_horizontal_height);

        }
        binding.image.setLayoutParams(params);
    }

    @Override
    public void bindTo(Item item) {
        Glide.with(binding.image.getContext())
                .load(item.getPreviewUrl())
                .placeholder(R.drawable.ic_no_image)
                .into(binding.image);
    }
}