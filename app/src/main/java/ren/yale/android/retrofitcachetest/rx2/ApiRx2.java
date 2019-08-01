package ren.yale.android.retrofitcachetest.rx2;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import ren.yale.android.retrofitcachelib.anno.Cache;
import ren.yale.android.retrofitcachelibrx2.anno.Mock;
import ren.yale.android.retrofitcachetest.bean.GankAndroid;
import retrofit2.http.GET;

/**
 * Created by Yale on 2017/6/13.
 */

public interface ApiRx2 {

    @Mock(assets = "mock/mock.json")
    @Cache(time = 10,timeUnit = TimeUnit.SECONDS)
    @GET("Android/9/1")
    Observable<GankAndroid> getGankAndroid();

}
