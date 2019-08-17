package mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource.factory;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource.FeedDataSource;

public class FeedDataFactory extends DataSource.Factory {

    private MutableLiveData<FeedDataSource> mutableLiveData;
    private FeedDataSource feedDataSource;
    private Repository repository;

    public FeedDataFactory(Repository repository) {
        this.repository = repository;
        this.mutableLiveData = new MutableLiveData<FeedDataSource>();
    }

    @Override
    public DataSource create() {
        feedDataSource = new FeedDataSource(repository);
        mutableLiveData.postValue(feedDataSource);
        return feedDataSource;
    }


    public MutableLiveData<FeedDataSource> getMutableLiveData() {
        return mutableLiveData;
    }


    public FeedDataSource getFeedDataSource() {
        return feedDataSource;
    }
}
