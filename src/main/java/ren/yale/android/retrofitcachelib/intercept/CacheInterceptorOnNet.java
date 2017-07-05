package ren.yale.android.retrofitcachelib.intercept;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ren.yale.android.retrofitcachelib.RetrofitCache;
import ren.yale.android.retrofitcachelib.util.LogUtil;

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
        String url = request.url().url().toString();
        Response response = chain.proceed(request);
        LogUtil.d("get data from net = "+response.code());
        Long maxAge = RetrofitCache.getInatance().getCacheTime(url);
        return   response.newBuilder()
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public,max-age="+maxAge)
                .removeHeader("Pragma")
                .build();
    }
}
