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
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Image;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.ImagesResponse;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.NetworkState;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.API_KEY;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.COLUMNS;


public class ImagesDataSource extends PageKeyedDataSource<Long, Item>{

    private static final String TAG = ImagesDataSource.class.getSimpleName();

    private Repository appController;

    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();;
    private String queryString;

    public ImagesDataSource(Repository appController, String queryString) {
        this.appController = appController;

        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        this.queryString = queryString;
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
        compositeDisposable.add(appController.getImages(API_KEY, queryString, 1,  params.requestedLoadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ImagesResponse>() {
                    @Override
                    public void onNext(ImagesResponse response) {
                        ArrayList<Item> list = new ArrayList<Item>();
                        ArrayList<Item> row = new ArrayList<Item>();
                        double rowRatios = 0f;
                        List<Image> il = response.getHits();
                        for (Image it : il) {
                            double imageRatio = (double) it.getWebformatWidth() / (double) it.getWebformatHeight();
                            Item item = new Item(it.getPreviewUrl(), it.getLargeImageURL(), imageRatio, it.getId());
                            list.add(item);
                            rowRatios += item.getImageRatio();
                            if (rowRatios > 2f) {
                                int used = 0;
                                for (Item it2 : row) {
                                    it2.setColumns((int) ((COLUMNS * it2.getImageRatio()) / rowRatios));
                                    used += it2.getColumns();
                                }
                                item.setColumns(COLUMNS - used);
                                row.clear();
                                rowRatios = 0f;
                            } else {
                                row.add(item);
                            }
                        }
                        callback.onResult(list, null, 2l);
                        initialLoading.postValue(NetworkState.LOADED);
                        networkState.postValue(NetworkState.LOADED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = e == null ? "unknown error" : e.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }

                    @Override
                    public void onComplete() {
                    }

                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Item> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, Item> callback) {

        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);

        networkState.postValue(NetworkState.LOADING);
        compositeDisposable.add(appController.getImages(API_KEY, queryString, params.key, params.requestedLoadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ImagesResponse>() {
                    @Override
                    public void onNext(ImagesResponse response) {
                        ArrayList<Item> list = new ArrayList<Item>();
                        ArrayList<Item> row = new ArrayList<Item>();
                        double rowRatios = 0f;
                        List<Image> il = response.getHits();
                        for (Image it : il) {
                            double imageRatio = (double) it.getWebformatWidth() / (double) it.getWebformatHeight();
                            Item item = new Item(it.getPreviewUrl(), it.getLargeImageURL(), imageRatio, it.getId());
                            list.add(item);
                            rowRatios += item.getImageRatio();
                            if (rowRatios > 2f) {
                                int used = 0;
                                for (Item it2 : row) {
                                    it2.setColumns((int) ((COLUMNS * it2.getImageRatio()) / rowRatios));
                                    used += it2.getColumns();
                                }
                                item.setColumns(COLUMNS - used);
                                row.clear();
                                rowRatios = 0f;
                            } else {
                                row.add(item);
                            }
                        }
                        long nextKey = (params.key == response.getTotalHits()) ? null : params.key+1;
                        callback.onResult(list, nextKey);
                        networkState.postValue(NetworkState.LOADED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = e == null ? "unknown error" : e.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }

                    @Override
                    public void onComplete() {
                    }

                }));
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
