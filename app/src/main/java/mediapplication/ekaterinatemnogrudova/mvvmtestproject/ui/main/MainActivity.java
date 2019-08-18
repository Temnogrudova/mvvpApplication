package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImagesFragment;

import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.FRAGMENT_IMAGES;

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
        ImagesFragment imagesFragment = new ImagesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, imagesFragment, FRAGMENT_IMAGES);
        fragmentTransaction.addToBackStack(FRAGMENT_IMAGES);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            ((ImagesFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_IMAGES)).backPressed();
        }
    }

}