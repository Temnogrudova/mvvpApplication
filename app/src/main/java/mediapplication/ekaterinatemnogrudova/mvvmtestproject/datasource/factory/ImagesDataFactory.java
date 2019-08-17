package mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource.factory;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.datasource.ImagesDataSource;

public class ImagesDataFactory extends DataSource.Factory {

    private MutableLiveData<ImagesDataSource> mutableLiveData;
    private ImagesDataSource imagesDataSource;
    private Repository repository;
    private String query;

    public ImagesDataFactory(Repository repository, String query) {
        this.repository = repository;
        this.mutableLiveData = new MutableLiveData<ImagesDataSource>();
        this.query = query;
    }

    @Override
    public DataSource create() {
        imagesDataSource = new ImagesDataSource(repository, query);
        mutableLiveData.postValue(imagesDataSource);
        return imagesDataSource;
    }


    public MutableLiveData<ImagesDataSource> getMutableLiveData() {
        return mutableLiveData;
    }


    public ImagesDataSource getImagesDataSource() {
        return imagesDataSource;
    }
}
