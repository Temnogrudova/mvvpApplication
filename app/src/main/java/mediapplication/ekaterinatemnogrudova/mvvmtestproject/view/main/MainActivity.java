package mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.view.list.ImagesListFragment;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.util.Constants.FRAGMENT_IMAGES;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            showImagesFragment();
        }
    }

    private void showImagesFragment() {
        ImagesListFragment imagesFragment = ImagesListFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, imagesFragment, FRAGMENT_IMAGES);
        fragmentTransaction.addToBackStack(FRAGMENT_IMAGES);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}