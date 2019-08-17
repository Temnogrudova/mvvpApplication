package mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource.factory;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource.FeedDataSource;

public class FeedDataFactory extends DataSource.Factory {

    private MutableLiveData<FeedDataSource> mutableLiveData;
    private FeedDataSource feedDataSource;
    private Repository repository;
    private String queryString;

    public FeedDataFactory(Repository repository, String queryString) {
        this.repository = repository;
        this.mutableLiveData = new MutableLiveData<FeedDataSource>();
        this.queryString = queryString;
    }

    @Override
    public DataSource create() {
        feedDataSource = new FeedDataSource(repository, queryString);
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
