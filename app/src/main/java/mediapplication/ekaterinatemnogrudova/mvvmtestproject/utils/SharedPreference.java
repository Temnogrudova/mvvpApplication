package mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants.EMPTY_STRING;


public class SharedPreference {

    public static final String PREFS_NAME = "IMAGES_APP";
    public static final String QUERY = "QUERY";

    public SharedPreference() {
        super();
    }

    public void saveQuery(Context context, String query) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(QUERY, query);

        editor.commit();
    }

    public String getQuery(Context context) {
        SharedPreferences settings;
        String query;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(QUERY)) {
            query = settings.getString(QUERY, EMPTY_STRING);
            return query;
        } else
            return null;
    }
}
