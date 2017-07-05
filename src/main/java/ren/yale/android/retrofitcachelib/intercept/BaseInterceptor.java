package ren.yale.android.retrofitcachelib.intercept;

import android.text.TextUtils;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ren.yale.android.retrofitcachelib.RetrofitCache;
import ren.yale.android.retrofitcachelib.util.LogUtil;

/**
 * Created by Yale on 2017/7/5.
 */

public class BaseInterceptor {

    protected Response mockResponse(Interceptor.Chain chain){
        Request request = chain.request();
        try{
            String url = request.url().url().toString();
            String mockData = RetrofitCache.getInatance().getMockData(url);
            if (!TextUtils.isEmpty(mockData)){
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
