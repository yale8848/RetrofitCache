package ren.yale.android.retrofitcachelib.intercept;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ren.yale.android.retrofitcachelib.util.LogUtil;

/**
 * Created by yale on 2019/7/24.
 */
public class MockInterceptor extends BaseInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response  mockResponse = mockResponse(chain);
        if (mockResponse!=null){
            return mockResponse;
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
