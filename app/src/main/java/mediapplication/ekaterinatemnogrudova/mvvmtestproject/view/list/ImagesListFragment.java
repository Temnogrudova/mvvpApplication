package mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Image;
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
        mAdapter = new ImagesAdapter(imagesListViewModel, getActivity(), this);
        imagesList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        imagesList.setAdapter(mAdapter);
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
        return view;
    }

    private void observableViewModel() {
        imagesListViewModel.getImageList().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> imageList) {
                Log.d("onChanged", "!!!!");
                // tv.setText(""+ coupon.getCoupon()+" "+ coupon.getCouponCode());
            }
        });
        imagesListViewModel.getError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isError) {
                Log.d("onChanged", "!!!!");
                // tv.setText(""+ coupon.getCoupon()+" "+ coupon.getCouponCode());
            }
        });

    }

    private void getImages() {
        imagesListViewModel.fetchImages();
        //mBinder.networkProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onImageSelected(Image image) {
        Log.d("onImageSelected", "!!!!");
    }
}
