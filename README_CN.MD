# RetrofitCache
Android Retrofit Rxjava Okhttp Cache 帮助库, 此库依赖retrofit2,okhttp3，rx1,RetrofitCache主要功能是无网，有网皆可缓存，有网缓存可以针对每一个接口配置不同的缓存时间；
用注解配置，让使用更简单；


## 使用方法:

 - 添加 jenter lib

 ```
 compile 'ren.yale.android:retrofitcachelib:0.1.5'
 ```

 如果你的工程已经添加retrofit2 and okhttp3库，可以去掉相应的库


 ```
 compile ('ren.yale.android:retrofitcachelib:0.1.5') {
      exclude module: 'okhttp-urlconnection', group: 'com.squareup.okhttp3'
      exclude module: 'retrofit', group: 'com.squareup.retrofit2'
      exclude module: 'rxjava', group: 'io.reactivex'
      exclude module: 'adapter-rxjava', group: 'com.squareup.retrofit2'
 }

 ```

 - 在Android Application里初始化

 ```
  RetrofitCache.getInatance().init(this);
 ```

也可以修改默认配置，默认time=0，timeUnit = TimeUnit.SECONDS

```
RetrofitCache.getInatance().init(this).setDefaultTimeUnit(TimeUnit.MINUTES).setDefaultTime(1);
```

 - cOkHttpClient初始化时配置缓存目录

 ```
  okhttp3.OkHttpClient.Builder clientBuilder=new okhttp3.OkHttpClient.Builder();
  ...
  int cacheSize = 200 * 1024 * 1024;
  File cacheDirectory = new File(mContext.getCacheDir(), "httpcache");
  Cache cache = new Cache(cacheDirectory, cacheSize);
  OkHttpClient client =  clientBuilder.cache(cache).build();
  ...

 ```

- 给okhttp添加拦截器

 ```
  okhttp3.OkHttpClient.Builder clientBuilder=new okhttp3.OkHttpClient.Builder();
  ...
 clientBuilder.addInterceptor(new CacheForceInterceptorNoNet());
 clientBuilder.addNetworkInterceptor(new CacheInterceptorOnNet());
  ...

 ```

 > 添加CacheForceInterceptorNoNet作用是在无网时强制走缓存,如果只添加了CacheInterceptorOnNet,那么在有网和无网的缓存策略就会一样
 

- 添加retrofit对象

```
 Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
 RetrofitCache.getInatance().addRetrofit(retrofit);
```
- 添加 rx Observable compose

```
 api.test().compose(CacheTransformer.emptyTransformer())...

```

 - 添加cache control例子

 > @Cache注解只影响有网时的缓存策略,而且只缓存http GET 请求
 
 
   不走缓存例子

  ```
   @GET("users")
   Observable<HttpResult> test();
  ```

 缓存设置为20秒

 ```
 @Cache(time = 20)
 @GET("users")
 Observable<HttpResult> test();

 ```

 
 缓存设置为20分钟

 ```
 @Cache(time = 20,timeUnit = TimeUnit.MINUTES)
 @GET("users")
 Observable<HttpResult> test();

 ```

默认时间缓存,默认是0秒

  ```
  @Cache()
  @GET("users")
  Observable<HttpResult> test();
  ```

- 混淆配置

```
-keepattributes *Annotation*,InnerClasses

-dontwarn ren.yale.android.retrofitcachelib.**
-keep class ren.yale.android.retrofitcachelib.** { *; }

#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-dontwarn org.robovm.**
-keep class org.robovm.** { *; }

#okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *;}
-keep class okio.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *;}
-dontwarn okio.**
-dontwarn okhttp3.**

#rxjava
-dontwarn rx.**
-keep class rx.** { *; }

-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

```
