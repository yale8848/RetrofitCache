package ren.yale.android.retrofitcachelib.util;

import ren.yale.android.retrofitcachelib.BuildConfig;

/**
 * Created by Yale on 2017/6/13.
 */

public class LogUtil {
   private static String TAG="retrofitcache";
    public static void d(String text){
            android.util.Log.d(TAG,text);
    }
}
