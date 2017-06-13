package ren.yale.android.retrofitcachelib;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import okhttp3.HttpUrl;
import ren.yale.android.retrofitcachelib.anno.Cache;
import retrofit2.Retrofit;

/**
 * Created by Yale on 2017/6/13.
 */
public class RetrofitCache {

    private static volatile RetrofitCache mRetrofit;
    private Vector<Map> mVector;
    private Map<String,Long> mUrlMap;

    private Context mContext;

    private RetrofitCache(){
    }

    public void init(Context context){
        mContext = context.getApplicationContext();
    }
    public Context getContext(){
        return mContext;
    }

    public void clear(){
        if (mVector!=null){
            mVector.clear();
            mVector =null;
        }
        if (mUrlMap!=null){
            mUrlMap.clear();
            mUrlMap =null;
        }
        mRetrofit = null;

    }
    public static RetrofitCache getInatance(){
        if (mRetrofit == null){
            synchronized (RetrofitCache.class){
                if (mRetrofit == null){
                    mRetrofit = new RetrofitCache();
                }
            }
        }
        return mRetrofit;
    }
    public Long getServiceMethod(String url){


        Long time = 0L;

        if (mUrlMap!=null){
            Long type = mUrlMap.get(url);
            if (type!=null){
                return type;
            }
        }
        for (Map serviceMethodCache:mVector) {

            for (Object entry:serviceMethodCache.keySet()){
                Object o = serviceMethodCache.get(entry);
                try {
                    Class   ServiceMethod =  Class.forName("retrofit2.ServiceMethod");
                    Field baseUrl =  ServiceMethod.getDeclaredField("baseUrl");
                    baseUrl.setAccessible(true);
                    HttpUrl httpurl = (HttpUrl) baseUrl.get(o);

                    Field relativeUrl = ServiceMethod.getDeclaredField("relativeUrl");
                    relativeUrl.setAccessible(true);
                    String relativeUrlStr = (String) relativeUrl.get(o);

                    String serviceUrl = httpurl.url().toString()+relativeUrlStr;
                    if (serviceUrl.equals(url)){
                        Method m = (Method) entry;
                        Cache cache =  m.getAnnotation(Cache.class);
                        if (cache!=null){
                            long t =  cache.timeUnit().toSeconds(cache.time());
                            getUrlMap().put(url, t);
                            return t;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        getUrlMap().put(url, time);
        return time;
    }

    private Map getUrlMap(){
        if (mUrlMap==null){
            mUrlMap = new HashMap<String, Long>();
        }
        return  mUrlMap;
    }

    public void addRetrofit(Retrofit retrofit){
        try {

            Class cls = retrofit.getClass();
            Field field =  cls.getDeclaredField("serviceMethodCache");
            field.setAccessible(true);
            if (mVector == null){
                mVector = new Vector<Map>();
            }
            Map m = (Map) field.get(retrofit);
            mVector.add(m);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
