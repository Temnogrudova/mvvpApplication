package mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Image;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.util.Constants;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ViewModelFactory;

public class ImagesListFragment extends Fragment  implements ImageSelectedListener {
    private ImagesAdapter mAdapter;
    private ViewModelFactory viewModelFactory;
    private ImagesListViewModel imagesListViewModel;
    public static ImagesListFragment newInstance() {
        return new ImagesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this Fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModelFactory = new ViewModelFactory(new Repository());
        //get ViewModel using ViewModelProviders and then tech data
        imagesListViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImagesListViewModel.class);
        if (savedInstanceState ==null) {
            getImages();
        }
        observableViewModel();
    }

    RecyclerView imagesList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        imagesList = view.findViewById(R.id.images_list);
        initImagesListWithOrientationParams();

        return view;
    }
    public void initImagesListWithOrientationParams() {
        int imagePreviewSize = 1;//getPreviewSize();
        initImagesList(imagePreviewSize);
      //  initImagesListScrollListener();
    }
    int columns = 15;
    private void initImagesList(int imagePreviewSize) {

      //  mAdapter = new ImagesAdapter(this, imagePreviewSize);
      //  imagesList.setAdapter(mAdapter);


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns);
        imagesList.setLayoutManager(layoutManager);
        mAdapter = new ImagesAdapter(this, imagePreviewSize);
        imagesList.setAdapter(mAdapter);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mAdapter.spanSizeLookup(i);
            }
        });

    }

    /*
    private int getPreviewSize() {
        // Recognition of what orientation is now and getting current screen width
        int imagePreviewSize;
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        GridLayoutManager gridLayoutManager;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            imagePreviewSize = size.x / Constants.COLUMNS_IN_PORTRAIT;
            gridLayoutManager = new GridLayoutManager(getActivity(), Constants.COLUMNS_IN_PORTRAIT);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    return 0;
                }
            });
            imagesList.setLayoutManager(gridLayoutManager);
        } else {
            imagePreviewSize = size.x / Constants.COLUMNS_IN_LANDSCAPE;
            gridLayoutManager = new GridLayoutManager(getActivity(), Constants.COLUMNS_IN_LANDSCAPE);
            imagesList.setLayoutManager(gridLayoutManager);
        }
        return imagePreviewSize;
    }*/
    private void observableViewModel() {
        imagesListViewModel.getImageList().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> imageList) {
                mAdapter.updateData(imageList);
            }
        });
        imagesListViewModel.getError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isError) {
                Log.d("onChanged", "!!!!");
            }
        });

    }

    private void getImages() {
        imagesListViewModel.fetchImages();
        //mBinder.networkProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onImageSelected(Item image) {
        Log.d("onImageSelected", "!!!!");
    }
}
