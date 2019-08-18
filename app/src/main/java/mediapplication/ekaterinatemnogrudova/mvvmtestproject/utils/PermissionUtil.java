package mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {
    private Activity activity;


    public PermissionUtil(Activity context) {
        activity = context;
    }

    // the activity that call this method need to implement onRequestPermissionsResult callBack

    public void requestWriteStoragePermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
    }

    // use this function when try to get user contacts list
    // before perform action check user has permission
    // if permission not granted call requestWriteStoragePermission method

    public boolean isUserHaswriteStoragePermission() {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void permitPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

}
