package ren.yale.android.retrofitcachelibrx2.intercept;

import ren.yale.android.retrofitcachelibrx2.RetrofitCache;
import ren.yale.android.retrofitcachelibrx2.util.LogUtil;
import ren.yale.android.retrofitcachelibrx2.util.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yale on 2017/6/13.
 */

public class CacheForceInterceptorNoNet extends BaseInterceptor implements Interceptor  {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response  mockResponse = mockResponse(chain);
        if (mockResponse!=null){
            return mockResponse;
        }
        String url = request.url().url().toString();
        boolean forceCacheNoNet =  RetrofitCache.getInstance().getCacheTime(url).isForceCacheNoNet();
        if (forceCacheNoNet&&!NetUtils.isConnectNet(RetrofitCache.getInstance().getContext())){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();

            LogUtil.d("get data from cache");
        }

        String mockUrl = mockUrl(chain);
        if (mockUrl!=null){
            request = request.newBuilder().url(mockUrl).header(KEY_HEADER_PRE_URL,request.url().toString())
                    .build();
        }
        Response response = chain.proceed(request);
        if (response.code() == 504){
            LogUtil.d("not find in cache, go to chain");
            response = chain.proceed(chain.request());
        }
        return response;
    }
}
