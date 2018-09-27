package ren.yale.android.retrofitcachelib.intercept;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ren.yale.android.retrofitcachelib.CacheInterceptorListener;
import ren.yale.android.retrofitcachelib.RetrofitCache;
import ren.yale.android.retrofitcachelib.bean.CacheConfig;

/**
 * Created by Yale on 2017/6/13.
 */

public class CacheInterceptorOnNet extends BaseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response  mockResponse = mockResponse(chain);
        if (mockResponse!=null){
            return mockResponse;
        }
        String url = getOriginUrl(request.url().url().toString());

        String mockPreUrl = request.header(KEY_HEADER_PRE_URL);
        if (!TextUtils.isEmpty(mockPreUrl)){
            url = mockPreUrl;
        }

        CacheConfig cacheConfig = RetrofitCache.getInstance().getCacheTime(url);
        Long maxAge = cacheConfig.getTime();
        Response response = chain.proceed(request);

        if (response.code()==301||response.code()==302){
            String location = response.headers().get("Location");
            RetrofitCache.getInstance().addUrlArgs(location,cacheConfig);
        }
        CacheInterceptorListener listener = RetrofitCache.getInstance().getCacheInterceptorListener();

        if (listener!=null&&!listener.canCache(request,response)){
            return response;
        }
        return   response.newBuilder()
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public,max-age="+maxAge)
                .removeHeader("Pragma")
                .build();
    }
}
