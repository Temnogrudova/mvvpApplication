package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.main;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.R;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImagesFragment;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.PermissionUtil;

import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.FRAGMENT_IMAGES;
import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.MY_PERMISSIONS_REQUEST_WRITE_STORAGE;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (savedInstanceState == null) {
            getPermissions();
        }
    }
    private void getPermissions() {
        PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.permitPolicy();
        if (permissionUtil.isUserHaswriteStoragePermission()) {
            showImagesFragment();
        } else {
            permissionUtil.requestWriteStoragePermission();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                    Log.i("onRequestPermissions", "granted");
                } else {
                    Log.i("onRequestPermissions", "denied");
                }
                showImagesFragment();
            }
        }
    }
}