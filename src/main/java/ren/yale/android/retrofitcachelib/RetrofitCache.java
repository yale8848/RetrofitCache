package ren.yale.android.retrofitcachelib;

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Request;
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
    private Long mDefaultTime = 0L;
    private TimeUnit mDefaultTimeUnit =TimeUnit.SECONDS;

    private Map mUrlAragsMap =null;

    private RetrofitCache(){
        clear();
        mUrlAragsMap = new HashMap();
    }
    public Context getContext(){
        return mContext;
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
    public RetrofitCache init(Context context){
        mContext = context.getApplicationContext();
        return this;
    }

    public void addMethodInfo(Object serviceMethod,Object[] args){
        String url = "";
        try {
            url =  buildRequestUrl(serviceMethod,args);
        } catch (Exception e) {
            e.printStackTrace();
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
        Request request = (Request) toRequestMethod.invoke(serviceMethod,new Object[]{args});
        return request.url().toString();
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
    private String getRetrofitDeclareUrl(Object o) throws Exception{
        Class   ServiceMethod =  Class.forName("retrofit2.ServiceMethod");
        Field baseUrl =  ServiceMethod.getDeclaredField("baseUrl");
        baseUrl.setAccessible(true);
        HttpUrl httpurl = (HttpUrl) baseUrl.get(o);

        Field relativeUrl = ServiceMethod.getDeclaredField("relativeUrl");
        relativeUrl.setAccessible(true);
        String relativeUrlStr = (String) relativeUrl.get(o);

        String serviceUrl = httpurl.url().toString()+relativeUrlStr;

        Field parameterHandlers = ServiceMethod.getDeclaredField("parameterHandlers");
        parameterHandlers.setAccessible(true);

        Class   path =  Class.forName("retrofit2.ParameterHandler$Path");
        Field fPathName = path.getDeclaredField("name");
        fPathName.setAccessible(true);

        Class   query =  Class.forName("retrofit2.ParameterHandler$Query");
        Field  fQueryName = query.getDeclaredField("name");
        fQueryName.setAccessible(true);

        Field fcallAdapter = ServiceMethod.getDeclaredField("callAdapter");
        fcallAdapter.setAccessible(true);
        Object callAdapter = fcallAdapter.get(o);

/*        Class  QueryMap =  Class.forName("retrofit2.ParameterHandler$QueryMap");
        Class father = QueryMap.getSuperclass();
        Method mArray = father.getDeclaredMethod("array");
        mArray.setAccessible(true);*/


        Object[] os= (Object[]) parameterHandlers.get(o);

        String name = "";
        if (os!=null){
            for(Object ob :os){
                if (ob.toString().indexOf("$Path")>0){
                    name = (String) fPathName.get(ob);
                }else if (ob.toString().indexOf("$QueryMap")>0){

                } else if (ob.toString().indexOf("$Query")>0){
                    name = (String) fQueryName.get(ob);
                }
            }
        }

        return name;


    }
    public Long getCacheTime(String url){
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
                                getUrlMap().put(url, tm);
                                return tm;
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        getUrlMap().put(url, mDefaultTimeUnit.toSeconds(mDefaultTime));
        return mDefaultTimeUnit.toSeconds(mDefaultTime);
    }
    private Map getUrlMap(){
        if (mUrlMap==null){
            mUrlMap = new HashMap<String, Long>();
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
