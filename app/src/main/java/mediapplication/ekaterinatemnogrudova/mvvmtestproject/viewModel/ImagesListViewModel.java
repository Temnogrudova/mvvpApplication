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
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource.factory.FeedDataFactory;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.NetworkState;

public class ImagesListViewModel extends ViewModel{

    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Item>> articleLiveData;
    FeedDataFactory feedDataFactory;
    private Repository mRepository;
    private String queryString;

    public ImagesListViewModel(Repository repository, String queryString){
        mRepository = repository;
        this.queryString = queryString;
        init();
    }
    private void init() {
        executor = Executors.newFixedThreadPool(5);

        feedDataFactory = new FeedDataFactory(mRepository, queryString);
        networkState = Transformations.switchMap(feedDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10)
                        .build();

        articleLiveData = (new LivePagedListBuilder(feedDataFactory, pagedListConfig))
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
        feedDataFactory.getFeedDataSource().clear();
    }
    public void replaceSubscription(LifecycleOwner lifecycleOwner, String query) {
        this.queryString = query;
        articleLiveData.removeObservers(lifecycleOwner);
        networkState.removeObservers(lifecycleOwner);
        init();
    }
}