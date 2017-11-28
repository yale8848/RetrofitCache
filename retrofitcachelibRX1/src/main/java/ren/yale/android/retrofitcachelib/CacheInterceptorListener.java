package ren.yale.android.retrofitcachelib;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yale on 2017/10/20.
 */

public interface CacheInterceptorListener {

    boolean canCache(Request request,Response response);
}
