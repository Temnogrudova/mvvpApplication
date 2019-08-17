package mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.api.Repository;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Image;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.ImagesResponse;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.Item;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.util.Constants.API_KEY;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.util.Constants.COLUMNS;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.util.Constants.DEFAULT_PER_PAGES;

public class ImagesListViewModel extends ViewModel{
    private MutableLiveData<List<Item>> imageList = new MutableLiveData<List<Item>>();
    private MutableLiveData<Boolean> error = new MutableLiveData<Boolean>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();;
    private Repository mRepository;
    public LiveData<List<Item>> getImageList() {
        return imageList;
    }

    public LiveData<Boolean> getError() {
        return error;
    }

    public ImagesListViewModel(Repository repository){
        mRepository = repository;
    }

    public void fetchImages(String query) {
            compositeDisposable.add(mRepository.getImages(API_KEY, query, "1", DEFAULT_PER_PAGES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ImagesResponse>() {
                        @Override
                        public void onNext(ImagesResponse response) {
                            error.setValue(false);
                            ArrayList<Item> list = new ArrayList<Item>();
                            ArrayList<Item> row = new ArrayList<Item>();
                            double rowRatios = 0f;
                            List<Image> il = response.getHits();
                            for(Image it: il)
                            {
                                double imageRatio = (double)it.getWebformatWidth() / (double)it.getWebformatHeight();
                                Item item = new Item(it.getPreviewUrl(), imageRatio);
                                list.add(item);
                                rowRatios += item.imageRatio;
                                if (rowRatios > 2f) {
                                    int used = 0;
                                    for(Item it2: row)
                                    {
                                        it2.columns = (int)((COLUMNS * it2.imageRatio) / rowRatios);
                                        used += it2.columns;
                                    }
                                    item.columns = COLUMNS - used;
                                    row.clear();
                                    rowRatios = 0f;
                                } else {
                                    row.add(item);
                                }
                            }
                            imageList.setValue(list);
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