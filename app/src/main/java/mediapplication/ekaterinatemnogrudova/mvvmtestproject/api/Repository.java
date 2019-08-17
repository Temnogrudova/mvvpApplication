package mediapplication.ekaterinatemnogrudova.mvvmtestproject.api;

import io.reactivex.Observable;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.ImagesResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.BASE_URL;

public class Repository {
    private Api api = null;

    public Repository() {
        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(Api.class);
        }
    }

    public Observable<ImagesResponse> getImages(String apikey, String query, long page, int perPage) {
        return  api.getImages(apikey, query, page, perPage);
    }
}