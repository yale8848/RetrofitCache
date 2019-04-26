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

public class CacheForceInterceptorNoNet extends BaseInterceptor implements Interceptor  {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response  mockResponse = mockResponse(chain);
        if (mockResponse!=null){
            return mockResponse;
        }
        String url = getOriginUrl(request.url().url().toString());
        boolean forceCacheNoNet =  RetrofitCache.getInstance().getCacheTime(url).isForceCacheNoNet();
       if (forceCacheNoNet&&!NetUtils.isConnectNet(RetrofitCache.getInstance().getContext())){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        String mockUrl = mockUrl(chain);
        if (mockUrl!=null){
            LogUtil.d("get data from mock url: "+mockUrl);
            request = request.newBuilder().url(mockUrl).header(KEY_HEADER_PRE_URL,request.url().toString())
                    .build();
        }
        Response response = chain.proceed(request);
        int code = response.code();
        if ( code == 504){
            response = chain.proceed(chain.request());
        }
        if(response.networkResponse()!=null){
            LogUtil.d("get data from net");
        } else
        if (response.cacheResponse()!=null){
            LogUtil.d("get data from cache");
        }

        return response;
    }
}
