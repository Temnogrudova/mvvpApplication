package mediapplication.ekaterinatemnogrudova.mvvmtestproject.api;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.models.ImagesResponse;
import retrofit2.http.GET;
import io.reactivex.Observable;
import retrofit2.http.Query;

public interface Api {
    @GET("api")
    Observable<ImagesResponse> getImages(@Query("key") String key, @Query("q") String query, @Query("page") String page, @Query("per_page") String per_page);

}