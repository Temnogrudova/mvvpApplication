package mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource.factory.ImagesDataFactory;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.NetworkState;

public class ImagesGridViewModel extends ViewModel{

    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Item>> articleLiveData;
    ImagesDataFactory imagesDataFactory;
    private Repository mRepository;
    private String queryString;

    public ImagesGridViewModel(Repository repository, String queryString){
        mRepository = repository;
        this.queryString = queryString;
        init();
    }
    private void init() {
        executor = Executors.newFixedThreadPool(5);

        imagesDataFactory = new ImagesDataFactory(mRepository, queryString);
        networkState = Transformations.switchMap(imagesDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10)
                        .build();

        articleLiveData = (new LivePagedListBuilder(imagesDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Item>> getArticleLiveData() {
        return articleLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        imagesDataFactory.getImagesDataSource().clear();
    }

    public void replaceSubscription(LifecycleOwner lifecycleOwner, String query) {
        this.queryString = query;
        articleLiveData.removeObservers(lifecycleOwner);
        networkState.removeObservers(lifecycleOwner);
        init();
    }
}