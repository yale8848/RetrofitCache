package ren.yale.android.retrofitcachetest.rx2;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ren.yale.android.retrofitcachelibrx2.CacheInterceptorListener;
import ren.yale.android.retrofitcachelibrx2.RetrofitCache;
import ren.yale.android.retrofitcachelibrx2.intercept.CacheForceInterceptorNoNet;
import ren.yale.android.retrofitcachelibrx2.intercept.CacheInterceptorOnNet;
import ren.yale.android.retrofitcachelibrx2.transformer.CacheTransformer;
import ren.yale.android.retrofitcachetest.LogTestUtil;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yale on 2017/6/12.
 */
public enum OKHttpUtilsRx2 {
    INSTANCE;
    private Context mContext;
    private  static ApiRx2 apiRx2;
    public void init(Context context){
        mContext = context;
        if (apiRx2 ==null){
            apiRx2 = configRetrofit(ApiRx2.class,"http://gank.io/api/data/");
        }
        RetrofitCache.getInstance().init(context);
        RetrofitCache.getInstance().setCacheInterceptorListener(
                new CacheInterceptorListener() {
            @Override
            public boolean canCache(Request request,Response response) {
                return true;
            }
        });

    }

    public OkHttpClient getOkHttpClient(){
        OkHttpClient.Builder clientBuilder=new OkHttpClient.Builder();
        clientBuilder.readTimeout(20, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(20, TimeUnit.SECONDS);
        clientBuilder.addInterceptor(new LogInterceptor());
        clientBuilder.addInterceptor(new CacheForceInterceptorNoNet());
        clientBuilder.addNetworkInterceptor(new CacheInterceptorOnNet());

        int cacheSize = 200 * 1024 * 1024;
        File cacheDirectory = new File(mContext.getCacheDir(), "httpcache");
        Cache cache = new Cache(cacheDirectory, cacheSize);
        return clientBuilder.cache(cache).build();
    }

    private static void showLog(String str) {
        str = str.trim();
        int index = 0;
        int maxLength = 2000;
        String finalString="";

        while (index < str.length()) {
            if (str.length() <= index + maxLength) {
                finalString = str.substring(index);
            } else {
                finalString = str.substring(index, index+maxLength);
            }
            index += maxLength;
            LogTestUtil.d( finalString.trim());
        }
    }
    private class LogInterceptor implements Interceptor {


        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            StringBuffer sb = new StringBuffer();


            Response response = chain.proceed(chain.request());
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();


            sb.append("======== request: "+request.toString()+"\r\n ======== request headers: "+request.headers().toString()+"\r\n======= response header:"+response.headers().toString()+"\r\n---------- response body:\r\n");
            LogTestUtil.d(sb.toString());
            try {
                showLog(content);
            }catch (Exception e){
                e.printStackTrace();
            }

            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }


    public <T> ObservableTransformer<T, T> IoMain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
                return upstream.compose(CacheTransformer.<T>emptyTransformer()).
                        subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }




    public ApiRx2 getApi(){
        return apiRx2;
    }


    private <T> T configRetrofit(Class<T> service,String url ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        RetrofitCache.getInstance().addRetrofit(retrofit);
        return retrofit.create(service);
    }
}
