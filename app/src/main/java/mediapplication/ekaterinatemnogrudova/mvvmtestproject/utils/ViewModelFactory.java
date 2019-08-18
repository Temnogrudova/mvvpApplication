package mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImagesViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    Repository repository;
    String queryString;
 
    public ViewModelFactory(Repository localRepo, String queryString) {
        repository = localRepo;
        this.queryString = queryString;
    }
 
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImagesViewModel.class)) {
            return (T) new ImagesViewModel(repository, queryString);
        }

        throw new IllegalArgumentException("Wrong ViewModel class");
    }
} 