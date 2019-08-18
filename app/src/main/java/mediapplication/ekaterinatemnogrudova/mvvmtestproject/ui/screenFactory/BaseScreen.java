package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.screenFactory;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.FragmentImagesBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.ImageItemBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImageSelectedListener;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImagesAdapter;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.NetworkState;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.SharedPreference;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ImagesViewModel;

public class BaseScreen implements Screen, ImageSelectedListener {
    ImagesViewModel imagesViewModel;
    ImagesAdapter adapter;
    FragmentImagesBinding binder;
    Activity activity;
    SharedPreference sharedPreference =new SharedPreference();
    ImageItemBinding binding;
    public BaseScreen(
                      ImagesViewModel imagesViewModel,
                      FragmentImagesBinding _binder,
                      ImagesAdapter _adapter,
                      Activity _activity) {
        this.imagesViewModel = imagesViewModel;
        this.adapter = _adapter;
        this.binder = _binder;
        this.activity = _activity;
    }

    protected void replaceSubscription(String query) {
        imagesViewModel.replaceSubscription((LifecycleOwner) activity, query);
        observableViewModel();
    }

    private void observableViewModel() {
        imagesViewModel.getArticleLiveData().observe((LifecycleOwner) activity, pagedList -> {
            adapter.submitList(pagedList);
        });
        imagesViewModel.getNetworkState().observe((LifecycleOwner) activity, networkState -> {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED)
            {
                Snackbar.make(binder.getRoot(), activity.getString(R.string.error_message), Snackbar.LENGTH_LONG)
                        .setAction(activity.getString(R.string.error_action_refresh), v ->
                        {
                            replaceSubscription(sharedPreference.getQuery(activity));
                            observableViewModel();
                        })
                        .setActionTextColor(ContextCompat.getColor(activity, android.R.color.holo_orange_dark))
                        .show();

            }
            adapter.setNetworkState(networkState);
        });
    }

    @Override
    public void draw() {

    }

    @Override
    public void setImageParams(ImageItemBinding binding) {
        this.binding = binding;
    }

    @Override
    public void onImageSelected(Item item, int position) {

    }

    @Override
    public void bindTo(Item item) {

    }
}
