package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.FragmentImagesBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.screenFactory.GridScreen;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.screenFactory.ListScreen;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.screenFactory.Screen;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.screenFactory.ScreenFactory;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.NetworkState;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.SharedPreference;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ViewModelFactory;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ImagesViewModel;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.COLUMNS;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.EMPTY_STRING;

public class ImagesFragment extends Fragment {
    private FragmentImagesBinding binder;
    private ViewModelFactory viewModelFactory;
    private ImagesViewModel imagesViewModel;
    private SharedPreference sharedPreference;
    private ScreenFactory screenFactory = new ScreenFactory();
    private ImagesAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_images, container, false);
        sharedPreference = new SharedPreference();
        viewModelFactory = new ViewModelFactory(new Repository(), EMPTY_STRING);
        imagesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImagesViewModel.class);
        binder.searchView.setQuery(sharedPreference.getQuery(getActivity()),false);
        if (screenFactory.getCurrentState() == Constants.STATE.LIST) {
            openListScreen();
        } else {
            openGridScreen();
        }
        initSearchViewListener();
        return binder.getRoot();
    }

    private void openListScreen() {
        Screen listScreen = screenFactory.getScreen(Constants.STATE.LIST,
                screenFactory.getCurrentPosition(),
                imagesViewModel,
                binder,
                adapter,
                getActivity(),
                screenFactory);
        listScreen.draw();
    }

    private void openGridScreen() {
        Screen gridScreen = screenFactory.getScreen(Constants.STATE.GRID,
                screenFactory.getCurrentPosition(),
                imagesViewModel,
                binder,
                adapter,
                getActivity(),
                screenFactory);
        gridScreen.draw();
    }

    private void initSearchViewListener() {
        binder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                sharedPreference.saveQuery(getActivity(), query);
                openGridScreen();
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



    public void backPressed() {
        if (screenFactory.getCurrentState() == Constants.STATE.LIST)
        {
            openGridScreen();
        }
        else
        {
            getActivity().finish();
        }
    }
}
