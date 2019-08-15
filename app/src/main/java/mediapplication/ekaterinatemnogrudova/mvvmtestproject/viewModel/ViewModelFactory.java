package mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list.ImagesListViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    Repository repository;
 
    public ViewModelFactory(Repository localRepo) {
        repository = localRepo;
    }
 
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImagesListViewModel.class)) {
            return (T) new ImagesListViewModel(repository);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
} 