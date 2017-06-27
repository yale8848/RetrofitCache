# RetrofitCache

[中文文档](https://github.com/yale8848/RetrofitCache/blob/master/README_CN.MD)

Android Retrofit Rxjava Okhttp Cache util lib , this lib dependent on retrofit2,okhttp3,rx1


## Useage:

 - add jenter lib

 ```
 compile 'ren.yale.android:retrofitcachelib:0.1.5'
 ```

 if you had use retrofit2 and okhttp3 in your project please exclude


 ```
 compile ('ren.yale.android:retrofitcachelib:0.1.5') {
        exclude module: 'okhttp-urlconnection', group: 'com.squareup.okhttp3'
        exclude module: 'retrofit', group: 'com.squareup.retrofit2'
        exclude module: 'rxjava', group: 'io.reactivex'
        exclude module: 'adapter-rxjava', group: 'com.squareup.retrofit2'
 }

 ```

 - init in android Application

 ```
  RetrofitCache.getInatance().init(this);
 ```

also can moidify default config , default time=0，timeUnit = TimeUnit.SECONDS

```
RetrofitCache.getInatance().init(this).setDefaultTimeUnit(TimeUnit.MINUTES).setDefaultTime(1);
```

 - config okhttp cache dir when init OkHttpClient

 ```
  okhttp3.OkHttpClient.Builder clientBuilder=new okhttp3.OkHttpClient.Builder();
  ...
  int cacheSize = 200 * 1024 * 1024;
  File cacheDirectory = new File(mContext.getCacheDir(), "httpcache");
  Cache cache = new Cache(cacheDirectory, cacheSize);
  OkHttpClient client =  clientBuilder.cache(cache).build();
  ...

 ```

- add okhttp cache Interceptor

 ```
  okhttp3.OkHttpClient.Builder clientBuilder=new okhttp3.OkHttpClient.Builder();
  ...
 clientBuilder.addInterceptor(new CacheForceInterceptorNoNet());
 clientBuilder.addNetworkInterceptor(new CacheInterceptorOnNet());
  ...

 ```

 > if add CacheForceInterceptorNoNet make force cache when not have net connection ,if only add CacheInterceptorOnNet,
 the same cache strategy no matter have net connnetion

- add retrofit

```
 Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

 ** RetrofitCache.getInatance().addRetrofit(retrofit); **

```
- add rx Observable compose

```
 api.test().compose(CacheTransformer.emptyTransformer())...

```

 - add cache control

 > the Cache Annotation only affect the cache strategy when have net connection,and only cached with @GET request

   no cached

  ```
   @GET("users")
   Observable<HttpResult> test();
  ```

 20 seconds cached

 ```
 @Cache(time = 20)
 @GET("users")
 Observable<HttpResult> test();

 ```

 20 mininuts cached

 ```
 @Cache(time = 20,timeUnit = TimeUnit.MINUTES)
 @GET("users")
 Observable<HttpResult> test();

 ```

 default time cached，default is 0s

  ```
  @Cache()
  @GET("users")
  Observable<HttpResult> test();
  ```

- proguard-rule

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
