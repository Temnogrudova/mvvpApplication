package mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    Repository repository;
    String queryString;
 
    public ViewModelFactory(Repository localRepo, String queryString) {
        repository = localRepo;
        this.queryString = queryString;
    }
 
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImagesGridViewModel.class)) {
            return (T) new ImagesGridViewModel(repository, queryString);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
} 