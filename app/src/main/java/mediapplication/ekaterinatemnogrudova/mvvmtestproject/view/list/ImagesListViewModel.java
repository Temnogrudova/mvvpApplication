package mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Image;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.ImagesResponse;

public class ImagesListViewModel extends ViewModel{
    private MutableLiveData<List<Image>> imageList = new MutableLiveData<List<Image>>();
    private MutableLiveData<Boolean> error = new MutableLiveData<Boolean>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();;
    private Repository mRepository;
    public LiveData<List<Image>> getImageList() {
        return imageList;
    }

    public LiveData<Boolean> getError() {
        return error;
    }

    public ImagesListViewModel(Repository repository){
        mRepository = repository;
    }

    public void fetchImages() {
            compositeDisposable.add(mRepository.getImages("12733826-a936f08aa28501a4cc137e2d7", "", "", "40")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ImagesResponse>() {
                        @Override
                        public void onNext(ImagesResponse response) {
                            error.setValue(false);
                            imageList.setValue(response.getHits());
                        }

                        @Override
                        public void onError(Throwable e) {
                            error.setValue(true);
                        }

                        @Override
                        public void onComplete() {
                        }

                    }));
    }

    @Override
    public void onCleared(){
        //prevents memory leaks by disposing pending observable objects
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}