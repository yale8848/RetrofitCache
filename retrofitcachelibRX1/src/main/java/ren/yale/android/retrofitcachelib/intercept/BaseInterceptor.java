package ren.yale.android.retrofitcachelib.intercept;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ren.yale.android.retrofitcachelib.RetrofitCache;
import ren.yale.android.retrofitcachelib.anno.Mock;
import ren.yale.android.retrofitcachelib.util.LogUtil;

/**
 * Created by Yale on 2017/7/5.
 */

public class BaseInterceptor {

    protected  static final String KEY_HEADER_PRE_URL = "retrofictcache_mock-pre-url";

    protected  String mockUrl(Interceptor.Chain chain){
        Request request = chain.request();
        String url = request.url().url().toString();
        Mock mock = RetrofitCache.getInatance().getMockObject(url);
        return  RetrofitCache.getInatance().getMockUrl(mock);
    }

    protected Response mockResponse(Interceptor.Chain chain){
        Request request = chain.request();
        try{
            String url = request.url().url().toString();
            Mock mock = RetrofitCache.getInatance().getMockObject(url);
            String mockData = RetrofitCache.getInatance().getMockData(mock);
            if (mockData != null){
                LogUtil.d("get data from mock");
                Response response = new Response.Builder().protocol(Protocol.HTTP_1_0)
                        .code(200).request(request).body(ResponseBody.create(null,mockData))
                        .build();
                return response;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
