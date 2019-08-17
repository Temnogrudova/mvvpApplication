package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.FragmentImagesBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.main.MainActivity;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ImagesGridViewModel;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ViewModelFactory;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.COLUMNS;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.EMPTY_STRING;

public class ImagesGridFragment extends Fragment  implements ImageSelectedListener {
    private FragmentImagesBinding binder;
    private ImagesAdapter adapter;
    private ViewModelFactory viewModelFactory;
    private ImagesGridViewModel imagesGridViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this Fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModelFactory = new ViewModelFactory(new Repository(), "");
        //get ViewModel using ViewModelProviders and then tech data
        imagesGridViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImagesGridViewModel.class);
        if (savedInstanceState ==null) {
            imagesGridViewModel.getArticleLiveData();
        }
        observableViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_images, container, false);
        initImagesList();
        initSearchViewListener();
        return binder.getRoot();
    }

    private void initImagesList() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), COLUMNS);
        binder.imagesList.setLayoutManager(layoutManager);
        adapter = new ImagesAdapter(this);
        binder.imagesList.setAdapter(adapter);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return adapter.spanSizeLookup(i);
            }
        });
    }

    private void initSearchViewListener() {
        binder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                replaceSubscription(query);
                imagesGridViewModel.getArticleLiveData();
                binder.searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(binder.searchView.getWidth() > 0  && newText.length() == 0)
                {
                    this.onQueryTextSubmit(EMPTY_STRING);
                    return false;
                }
                return false;
            }
        });
    }

    private void replaceSubscription(String query) {
        imagesGridViewModel.replaceSubscription(this, query);
        observableViewModel();
    }

    private void observableViewModel() {
        imagesGridViewModel.getArticleLiveData().observe(this, pagedList -> {
            adapter.submitList(pagedList);
        });
        imagesGridViewModel.getNetworkState().observe(this, networkState -> {
            adapter.setNetworkState(networkState);
        });
    }

    @Override
    public void onImageSelected(Item image) {
        ((MainActivity) getActivity()).showSwipeFragment();
    }
}
