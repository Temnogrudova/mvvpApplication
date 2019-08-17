package mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Image;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.ImagesResponse;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.NetworkState;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.API_KEY;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.COLUMNS;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.DEFAULT_PER_PAGES;


public class FeedDataSource extends PageKeyedDataSource<Long, Item>{

    private static final String TAG = FeedDataSource.class.getSimpleName();

    private Repository appController;

    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();;

    public FeedDataSource(Repository appController) {
        this.appController = appController;

        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull LoadInitialCallback<Long, Item> callback) {

        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        compositeDisposable.add(appController.getImages(API_KEY, "", "1",  String.valueOf(params.requestedLoadSize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ImagesResponse>() {
                    @Override
                    public void onNext(ImagesResponse response) {
                        // error.setValue(false);
                        ArrayList<Item> list = new ArrayList<Item>();
                        ArrayList<Item> row = new ArrayList<Item>();
                        double rowRatios = 0f;
                        List<Image> il = response.getHits();
                        for (Image it : il) {
                            double imageRatio = (double) it.getWebformatWidth() / (double) it.getWebformatHeight();
                            Item item = new Item(it.getPreviewUrl(), imageRatio);
                            list.add(item);
                            rowRatios += item.imageRatio;
                            if (rowRatios > 2f) {
                                int used = 0;
                                for (Item it2 : row) {
                                    it2.columns = (int) ((COLUMNS * it2.imageRatio) / rowRatios);
                                    used += it2.columns;
                                }
                                item.columns = COLUMNS - used;
                                row.clear();
                                rowRatios = 0f;
                            } else {
                                row.add(item);
                            }
                        }
                        //imageList.setValue(list);
                        callback.onResult(list, null, 2l);
                        initialLoading.postValue(NetworkState.LOADED);
                        networkState.postValue(NetworkState.LOADED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = e == null ? "unknown error" : e.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                        //error.setValue(true);
                    }

                    @Override
                    public void onComplete() {
                    }

                }));
    }
/*
        appController.getImages( API_KEY, 1, params.requestedLoadSize)
                .enqueue(new Callback<Feed>() {
                    @Override
                    public void onResponse(Call<Feed> call, Response<Feed> response) {
                        if(response.isSuccessful()) {
                            callback.onResult(response.body().getArticles(), null, 2l);
                            initialLoading.postValue(NetworkState.LOADED);
                            networkState.postValue(NetworkState.LOADED);

                        } else {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Feed> call, Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }*/

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Item> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, Item> callback) {

        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);

        networkState.postValue(NetworkState.LOADING);
        compositeDisposable.add(appController.getImages(API_KEY, "", String.valueOf(params.key), String.valueOf(params.requestedLoadSize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ImagesResponse>() {
                    @Override
                    public void onNext(ImagesResponse response) {
                        // error.setValue(false);
                        ArrayList<Item> list = new ArrayList<Item>();
                        ArrayList<Item> row = new ArrayList<Item>();
                        double rowRatios = 0f;
                        List<Image> il = response.getHits();
                        for (Image it : il) {
                            double imageRatio = (double) it.getWebformatWidth() / (double) it.getWebformatHeight();
                            Item item = new Item(it.getPreviewUrl(), imageRatio);
                            list.add(item);
                            rowRatios += item.imageRatio;
                            if (rowRatios > 2f) {
                                int used = 0;
                                for (Item it2 : row) {
                                    it2.columns = (int) ((COLUMNS * it2.imageRatio) / rowRatios);
                                    used += it2.columns;
                                }
                                item.columns = COLUMNS - used;
                                row.clear();
                                rowRatios = 0f;
                            } else {
                                row.add(item);
                            }
                        }
                        //imageList.setValue(list);
                        long nextKey = (params.key == response.getTotalHits()) ? null : params.key+1;
                        callback.onResult(list, nextKey);
                        networkState.postValue(NetworkState.LOADED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = e == null ? "unknown error" : e.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                        //error.setValue(true);
                    }

                    @Override
                    public void onComplete() {
                    }

                }));
/*
        appController.getRestApi().fetchFeed(QUERY, API_KEY, params.key, params.requestedLoadSize).enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                if(response.isSuccessful()) {
                    long nextKey = (params.key == response.body().getTotalResults()) ? null : params.key+1;
                    callback.onResult(response.body().getArticles(), nextKey);
                    networkState.postValue(NetworkState.LOADED);

                } else networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });

        */
    }

}
