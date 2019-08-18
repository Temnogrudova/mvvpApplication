package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.FragmentImagesBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.SharedPreference;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.ViewModelFactory;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.COLUMNS;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.EMPTY_STRING;

public class ImagesFragment extends Fragment  implements ImageSelectedListener {
    private FragmentImagesBinding binder;
    private ImagesAdapter adapter;
    private ViewModelFactory viewModelFactory;
    private ImagesViewModel imagesViewModel;
    private SharedPreference sharedPreference;

    //private String query = EMPTY_STRING;
    private Constants.STATE currentState;
    private int currentPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this Fragment across configuration changes.
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_images, container, false);
        sharedPreference = new SharedPreference();
        viewModelFactory = new ViewModelFactory(new Repository(), "");
        //get ViewModel using ViewModelProviders and then tech data
        imagesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImagesViewModel.class);
        imagesViewModel.getArticleLiveData();
        binder.searchView.setQuery(sharedPreference.getQuery(getActivity()),false);
        if (currentState == Constants.STATE.LIST)
        {
            iniImagesList();
        }
        else
        {
            initImagesGrid();

        }
        initSearchViewListener();
        return binder.getRoot();
    }

    private void initImagesGrid() {
        currentState = Constants.STATE.GRID;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), COLUMNS);
        binder.imagesList.setLayoutManager(layoutManager);
        adapter = new ImagesAdapter(currentState, this);
        binder.imagesList.setAdapter(adapter);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return adapter.spanSizeLookup(i);
            }
        });
        replaceSubscription(sharedPreference.getQuery(getActivity()));

    }
    private void iniImagesList() {
        currentState = Constants.STATE.LIST;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binder.imagesList.setLayoutManager(layoutManager);
        adapter = new ImagesAdapter(currentState, this);
        binder.imagesList.setAdapter(adapter);
        replaceSubscription(sharedPreference.getQuery(getActivity()));
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (positionStart == 0) {
                    layoutManager.scrollToPosition(currentPosition);
                }
            }
        });
    }

    private void initSearchViewListener() {
        binder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                sharedPreference.saveQuery(getActivity(), query);
                replaceSubscription(query);
                imagesViewModel.getArticleLiveData();
                binder.searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(binder.searchView.getWidth() > 0 && newText.length() == 0)
                {
                    this.onQueryTextSubmit(EMPTY_STRING);
                    return false;
                }
                return false;
            }
        });
    }

    private void replaceSubscription(String query) {
        imagesViewModel.replaceSubscription(this, query);
        observableViewModel();
    }

    private void observableViewModel() {
        imagesViewModel.getArticleLiveData().observe(this, pagedList -> {
            adapter.submitList(pagedList);
        });
        imagesViewModel.getNetworkState().observe(this, networkState -> {
            adapter.setNetworkState(networkState);
        });
    }
    @Override
    public void onImageSelected(Item item, int position) {
        currentPosition = position;
        if (currentState == Constants.STATE.LIST)
        {

        }
        else {
            iniImagesList();
        }
    }

    public void backPressed() {
        if (currentState == Constants.STATE.LIST)
        {
            initImagesGrid();
            replaceSubscription(sharedPreference.getQuery(getActivity()));
        }
        else
        {
            getActivity().finish();
        }
    }
}
