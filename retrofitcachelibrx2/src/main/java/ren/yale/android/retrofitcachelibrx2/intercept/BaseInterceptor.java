package ren.yale.android.retrofitcachelibrx2.intercept;

import java.util.Set;

import ren.yale.android.retrofitcachelibrx2.RetrofitCache;
import ren.yale.android.retrofitcachelibrx2.anno.Mock;
import ren.yale.android.retrofitcachelibrx2.util.LogUtil;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Yale on 2017/7/5.
 */

public class BaseInterceptor {

    protected  static final String KEY_HEADER_PRE_URL = "retrofictcache_mock-pre-url";

    protected  String mockUrl(Interceptor.Chain chain){
        if (!RetrofitCache.getInstance().canMock()){
            return null;
        }
        Request request = chain.request();
        String url = getOriginUrl(request.url().url().toString());
        Mock mock = RetrofitCache.getInstance().getMockObject(url);
        return  RetrofitCache.getInstance().getMockUrl(mock);
    }
    protected  String getOriginUrl(String url){

        Set<String> params = RetrofitCache.getInstance().getIgnoreParam();
        if (params==null){
            return url;
        }
        for (String p:params) {

            int index = url.indexOf(p+"=");
            if (index>=0){
                int end = url.indexOf("&",index);
                url = url.substring(0,index);
                if (end>=0){
                    url +=url.substring(end+1);
                }
            }
        }
        if (url.length()>1){
            char ch = url.charAt(url.length()-1);
            if (ch=='?'||ch=='&'){
                url = url.substring(0,url.length()-1);
            }
        }

        return url;
    }
    protected Response mockResponse(Interceptor.Chain chain){
        if (!RetrofitCache.getInstance().canMock()){
            return null;
        }
        Request request = chain.request();
        try{
            String url = getOriginUrl(request.url().url().toString());
            Mock mock = RetrofitCache.getInstance().getMockObject(url);
            String mockData = RetrofitCache.getInstance().getMockData(mock);
            if (mockData != null){
                LogUtil.d("get data from mock");
                Response response = new Response.Builder().protocol(Protocol.HTTP_1_0)
                        .code(200).request(request).body(ResponseBody.create(null,mockData))
                        .build();
                return response;
            }

            String mockAssets = RetrofitCache.getInstance().getMockAssets(mock);
            if (mockAssets!=null){
                String md = RetrofitCache.getInstance().getMockAssetsValue(mockAssets);
                if (md!=null){
                    LogUtil.d("get data from asset: "+mockAssets);
                    Response response = new Response.Builder().protocol(Protocol.HTTP_1_0)
                            .code(200).request(request).body(ResponseBody.create(null,md))
                            .build();
                    return response;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
