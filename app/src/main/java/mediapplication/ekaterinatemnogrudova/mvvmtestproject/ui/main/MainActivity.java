package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.detail.SwipeFragment;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImagesGridFragment;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.FRAGMENT_IMAGES;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.FRAGMENT_SWIPE;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState == null) {
            showImagesFragment();
        }
    }

    private void showImagesFragment() {
        ImagesGridFragment imagesFragment = new ImagesGridFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, imagesFragment, FRAGMENT_IMAGES);
        fragmentTransaction.addToBackStack(FRAGMENT_IMAGES);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showSwipeFragment() {
        SwipeFragment swipeFragment = new SwipeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, swipeFragment, FRAGMENT_SWIPE);
        fragmentTransaction.addToBackStack(FRAGMENT_SWIPE);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}