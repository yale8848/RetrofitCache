package ren.yale.android.retrofitcachetest;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import ren.yale.android.retrofitcachelib.anno.Cache;
import ren.yale.android.retrofitcachetest.bean.GankAndroid;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Yale on 2017/6/13.
 */

public interface Api {

    @Cache(time = 10)
    @GET("users/{pathname}")
    Observable<HttpResult> test(@Path("pathname") String pathname,@Query("name") String name, @QueryMap Map<String,String> qm);

    @Cache(time = 10)
    @GET("users")
    Observable<HttpResult> test2();

    @Cache(time = 10,timeUnit = TimeUnit.HOURS)
    //@Mock(value = "{\"error\":false,\"results\":[{\"_id\":\"5941f5f3421aa92c7be61c16\",\"createdAt\":\"2017-06-15T10:50:27.317Z\",\"desc\":\"\\\\u4effNice\\\\u9996\\\\u9875\\\\u56fe\\\\u7247\\\\u5217\\\\u88689\\\\u56fe\\\\u6837\\\\u5f0f\\\\uff0c\\\\u5e76\\\\u5b9e\\\\u73b0\\\\u62d6\\\\u62fd\\\\u6548\\\\u679c\",\"images\":[\"http://img.gank.io/4f54c011-e293-436a-ada1-dc03669ffb10\"],\"publishedAt\":\"2017-06-15T13:55:57.947Z\",\"source\":\"web\",\"type\":\"Android\",\"url\":\"http://www.jianshu.com/p/0ea96b952170\",\"used\":true,\"who\":\"www的事发生飞洒地方bbb\"}]}",url = "https://mock.daoxuehao.com/mock/59afc6e63fcc3d119c392037/test/test2")
    @GET("Android/10/1")
    Observable<GankAndroid> getGankAndroid(@Query("test") String test);


}
