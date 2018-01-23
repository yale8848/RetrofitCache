package ren.yale.android.retrofitcachelib;

import android.content.Context;
import android.text.TextUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;
import ren.yale.android.retrofitcachelib.anno.Cache;
import ren.yale.android.retrofitcachelib.anno.Mock;
import ren.yale.android.retrofitcachelib.bean.CacheConfig;
import ren.yale.android.retrofitcachelib.util.LogUtil;
import retrofit2.Retrofit;

/**
 * Created by Yale on 2017/6/13.
 */
public class RetrofitCache {


    private static volatile RetrofitCache mRetrofit;
    private Vector<Map> mVector;
    private Map<String,CacheConfig> mUrlMap;
    private Context mContext;
    private Long mDefaultTime = 0L;
    private TimeUnit mDefaultTimeUnit =TimeUnit.SECONDS;
    private Map mUrlAragsMap =null;
    private CacheInterceptorListener mCacheInterceptorListener;

    private boolean mMock = true;

    private RetrofitCache(){
        clear();
        mUrlAragsMap = new HashMap();
    }
    public Context getContext(){
        return mContext;
    }

    public void setCacheInterceptorListener(CacheInterceptorListener listener){
        mCacheInterceptorListener = listener;
    }
    public CacheInterceptorListener getCacheInterceptorListener(){
        return mCacheInterceptorListener;
    }

    public static RetrofitCache getInstance(){
        if (mRetrofit == null){
            synchronized (RetrofitCache.class){
                if (mRetrofit == null){
                    mRetrofit = new RetrofitCache();
                }
            }
        }
        return mRetrofit;
    }
    public RetrofitCache enableMock(boolean mock){
        mMock = mock;
        return this;
    }
    public boolean canMock(){
        return mMock;
    }
    public RetrofitCache init(Context context){
        mContext = context.getApplicationContext();
        return this;
    }

    public void addMethodInfo(Object serviceMethod,Object[] args){
        String url = "";
        try {
            url =  buildRequestUrl(serviceMethod,args);
        } catch (Exception e) {
            LogUtil.l(e);
        }
        if (!TextUtils.isEmpty(url)){
            if (!mUrlAragsMap.containsKey(url)){
                mUrlAragsMap.put(url,args);
            }
        }
    }

    private String buildRequestUrl(Object serviceMethod,Object[] args) throws Exception{
        Class   clsServiceMethod =  Class.forName("retrofit2.ServiceMethod");
        Method toRequestMethod =  clsServiceMethod.getDeclaredMethod("toRequest", Object[].class );
        toRequestMethod.setAccessible(true);
        try {
            Request request = (Request) toRequestMethod.invoke(serviceMethod,new Object[]{args});
            return request.url().toString();
        }catch (Exception e){
            LogUtil.l(e);
        }
        return "";
    }
    public RetrofitCache setDefaultTime(long time){
        mDefaultTime = time;
        return this;
    }
    public RetrofitCache setDefaultTimeUnit(TimeUnit timeUnit){
        mDefaultTimeUnit = timeUnit;
        return this;
    }
    public long getDaultTime(){
        return mDefaultTime;
    }
    public TimeUnit getDefaultTimeUnit(){
        return mDefaultTimeUnit;
    }

    public Mock getMockObject(String url){
        for (Map serviceMethodCache:mVector) {

            for (Object entry:serviceMethodCache.keySet()){
                Object o = serviceMethodCache.get(entry);
                try {

                    if (mUrlAragsMap.containsKey(url)){
                        Object[] args = (Object[]) mUrlAragsMap.get(url);
                        String reqUrl =  buildRequestUrl(o,args);
                        if (reqUrl.equals(url)){
                            Method m = (Method) entry;
                            Mock mock =  m.getAnnotation(Mock.class);
                            if (mock!=null){
                                return  mock;
                            }
                            return null;
                        }
                    }
                } catch (Exception e) {
                    LogUtil.l(e);
                }
            }
        }

        return null;
    }
    public String getMockUrl(Mock mock){
        if (mock!=null){
            if (!TextUtils.isEmpty(mock.url())){
                return mock.url();
            }
        }
        return null;
    }
    public String getMockData(Mock mock){
        if (mock!=null){
            if (!TextUtils.isEmpty(mock.value())){
                return mock.value();
            }
        }
        return null;
    }
    public String getMockAssetsValue(String assetsPath){
        if(mContext==null){
            return null;
        }
        try {

            InputStream inputStream = mContext.getAssets().open(assetsPath);
            byte[] buff = new byte[1024];
            int len =0;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(buff))>0){
                sb.append(new String(buff,0,len, Charset.forName("utf-8")));
            }
            inputStream.close();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getMockAssets(Mock mock){
        if (mock!=null){
            if (!TextUtils.isEmpty(mock.assets())){
                return mock.assets();
            }
        }
        return null;
    }

    public String getMockData(String url){
        Mock mock =  getMockObject(url);
        if (mock!=null){
            if (!TextUtils.isEmpty(mock.value())){
                return mock.value();
            }
        }
        return null;
    }

    public CacheConfig getCacheTime(String url){
        CacheConfig cacheConfig = new CacheConfig();
        if (mUrlMap!=null){
            CacheConfig config = mUrlMap.get(url);
            if (config!=null){
                return config;
            }
        }
        for (Map serviceMethodCache:mVector) {

            for (Object entry:serviceMethodCache.keySet()){
                Object o = serviceMethodCache.get(entry);
                try {

                    if (mUrlAragsMap.containsKey(url)){
                        Object[] args = (Object[]) mUrlAragsMap.get(url);
                        String reqUrl =  buildRequestUrl(o,args);
                        if (reqUrl.equals(url)){
                            Method m = (Method) entry;
                            Cache cache =  m.getAnnotation(Cache.class);
                            if (cache!=null){
                                TimeUnit timeUnit =  mDefaultTimeUnit;
                                if (cache.timeUnit() != TimeUnit.NANOSECONDS){
                                    timeUnit = cache.timeUnit();
                                }
                                long t = mDefaultTime;
                                if (cache.time() != -1){
                                    t = cache.time();
                                }
                                long tm =  timeUnit.toSeconds(t);
                                cacheConfig.setTime(tm);
                                cacheConfig.setForceCacheNoNet(cache.forceCacheNoNet());
                                getUrlMap().put(url, cacheConfig);
                                return cacheConfig;
                            }else{
                                getUrlMap().put(url, cacheConfig);
                                return cacheConfig;
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.l(e);
                }
            }
        }
        getUrlMap().put(url, cacheConfig);
        return cacheConfig;
    }
    private Map getUrlMap(){
        if (mUrlMap==null){
            mUrlMap = new HashMap<String, CacheConfig>();
        }
        return  mUrlMap;
    }

    public RetrofitCache addRetrofit(Retrofit retrofit){
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

        return this;
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
        if (mUrlAragsMap!=null){
            mUrlAragsMap.clear();
            mUrlAragsMap =null;
        }
        mRetrofit = null;

    }

}
