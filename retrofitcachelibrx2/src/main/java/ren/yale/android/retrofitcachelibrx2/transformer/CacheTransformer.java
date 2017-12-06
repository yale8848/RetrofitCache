package ren.yale.android.retrofitcachelibrx2.transformer;


import java.lang.reflect.Field;

import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import ren.yale.android.retrofitcachelibrx2.RetrofitCache;

/**
 * Created by Yale on 2017/6/14.
 */

public class CacheTransformer {

    private static final String CLASS_NAME1 ="retrofit2.adapter.rxjava2.BodyObservable";
    private static final String CLASS_NAME2 ="retrofit2.adapter.rxjava2.ResultObservable";
    private static final String CLASS_NAME3 ="retrofit2.adapter.rxjava2.CallEnqueueObservable";
    private static final String CLASS_NAME4 ="retrofit2.adapter.rxjava2.CallExecuteObservable";


    public static  <T> ObservableTransformer<T, T> emptyTransformer(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {

                String name = upstream.getClass().getName();
                if (name.equals(CLASS_NAME1)||name.equals(CLASS_NAME2)){
                    observable(upstream);
                }
                return upstream;
            }
        };
    }

    private static <T> void observable(io.reactivex.Observable<T> up){

        try {

            Field upstream = up.getClass().getDeclaredField("upstream");
            upstream.setAccessible(true);

            Object ov = upstream.get(up);

            Class cls = null;
            if (ov.getClass().getName().equals(CLASS_NAME3)){
                cls  = Class.forName(CLASS_NAME3);

            }else if (ov.getClass().getName().equals(CLASS_NAME4)){
                cls  = Class.forName(CLASS_NAME4);
            }
            if (cls == null){
                return;
            }
            Field foriginalCall = cls.getDeclaredField("originalCall");
            foriginalCall.setAccessible(true);
            Object OkhttpCallObj  = foriginalCall.get(ov);

            Class clsOkhttpCall = Class.forName("retrofit2.OkHttpCall");
            Field fdArgs = clsOkhttpCall.getDeclaredField("args");
            fdArgs.setAccessible(true);
            Object []args = (Object[]) fdArgs.get(OkhttpCallObj);

            Field fdserviceMethod  = clsOkhttpCall.getDeclaredField("serviceMethod");
            fdserviceMethod.setAccessible(true);
            Object serviceMethodObj =  fdserviceMethod.get(OkhttpCallObj);

            if (serviceMethodObj!=null){
                RetrofitCache.getInstance().addMethodInfo(serviceMethodObj,args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
