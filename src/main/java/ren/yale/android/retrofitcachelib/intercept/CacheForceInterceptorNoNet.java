package ren.yale.android.retrofitcachelib.intercept;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ren.yale.android.retrofitcachelib.RetrofitCache;
import ren.yale.android.retrofitcachelib.util.LogUtil;
import ren.yale.android.retrofitcachelib.util.NetUtils;

/**
 * Created by Yale on 2017/6/13.
 */

public class CacheForceInterceptorNoNet implements Interceptor  {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
       if (!NetUtils.isConnectNet(RetrofitCache.getInatance().getContext())){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();

            LogUtil.d("get data from cache");
        }
        Response response = chain.proceed(request);
        if (response.code() == 504){
            LogUtil.d("not find in cache, go to chain");
            response = chain.proceed(chain.request());
        }
        return response;
    }
}
