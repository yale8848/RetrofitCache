package ren.yale.android.retrofitcachelib.transformer;


import android.util.Log;

import java.lang.reflect.Field;

import ren.yale.android.retrofitcachelib.RetrofitCache;
import rx.Observable;
import rx.internal.operators.OnSubscribeLift;

/**
 * Created by Yale on 2017/6/14.
 */

public class CacheTransformer {

    private static final String TAG="CacheTransformer";

    public static <T> Observable.Transformer<T, T> emptyTransformer() {

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                Field fdOnSubscribe = null;
                Object serviceMethodObj;
                Object [] args;
                try {

                    long startTime = System.currentTimeMillis();

                    fdOnSubscribe = tObservable.getClass().getDeclaredField("onSubscribe");
                    fdOnSubscribe.setAccessible(true);
                    OnSubscribeLift onSubscribe = (OnSubscribeLift) fdOnSubscribe.get(tObservable);

                    Field fdparent =  onSubscribe.getClass().getDeclaredField("parent");
                    fdparent.setAccessible(true);
                   Object onSubscribeObj =  fdparent.get(onSubscribe);

                    Class cls  = Class.forName("retrofit2.adapter.rxjava.RxJavaCallAdapterFactory$CallOnSubscribe");

                    Field foriginalCall = cls.getDeclaredField("originalCall");
                    foriginalCall.setAccessible(true);

                   Object OkhttpCallObj  = foriginalCall.get(onSubscribeObj);

                    Class clsOkhttpCall = Class.forName("retrofit2.OkHttpCall");
                    Field fdArgs = clsOkhttpCall.getDeclaredField("args");
                    fdArgs.setAccessible(true);
                    args = (Object[]) fdArgs.get(OkhttpCallObj);

                    Field fdserviceMethod  = clsOkhttpCall.getDeclaredField("serviceMethod");
                    fdserviceMethod.setAccessible(true);

                   serviceMethodObj =  fdserviceMethod.get(OkhttpCallObj);

                    Log.d(TAG,(System.currentTimeMillis()-startTime)+"ms");
                    if (serviceMethodObj!=null){
                        RetrofitCache.getInatance().addMethodInfo(serviceMethodObj,args);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return tObservable;
            }
        };
    }
}
