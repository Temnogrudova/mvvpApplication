package mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.List;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ViewModelFactory;

import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.util.Constants.COLUMNS;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.util.Constants.EMPTY_STRING;

public class ImagesListFragment extends Fragment  implements ImageSelectedListener {
    private ImagesAdapter mAdapter;
    private ViewModelFactory viewModelFactory;
    private ImagesListViewModel imagesListViewModel;
    RecyclerView imagesList;
    SearchView searchView;
    private boolean isSubmitedClicked = false;

    // private boolean isLoading = false;
    //private int page = 1;
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
            getImages(EMPTY_STRING);
        }
        observableViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        imagesList = view.findViewById(R.id.images_list);
        searchView  = view.findViewById(R.id.searchView);
        initImagesList();
        initSearchViewListener();
        return view;
    }

    private void initImagesList() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), COLUMNS);
        imagesList.setLayoutManager(layoutManager);
        mAdapter = new ImagesAdapter(this);
        imagesList.setAdapter(mAdapter);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mAdapter.spanSizeLookup(i);
            }
        });
    }

    private void initSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.clear();
                //page = 1;
                getImages(query);
                searchView.clearFocus();
                isSubmitedClicked = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchView.getWidth() > 0 && isSubmitedClicked && newText.length() == 0)
                {
                    this.onQueryTextSubmit(EMPTY_STRING);
                    isSubmitedClicked = false;
                    return false;
                }
                return false;
            }
        });
    }
    /*
    private void initImagesListScrollListener() {
        imagesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mImages.size() - 1) {
                        page++;
                        getImages();
                        isLoading = true;
                    }
                }
            }
        });
    }
*/
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

    private void getImages(String query) {
        imagesListViewModel.fetchImages(query);
        //mBinder.networkProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onImageSelected(Item image) {
        Log.d("onImageSelected", "!!!!");
    }
}
