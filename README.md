# RetrofitCache
Retrofit Okhttp Cache util lib , this lib dependent on retrofit2,okhttp3


## Useage:

 - add jenter lib

 ```
 compile 'ren.yale.android:retrofitcachelib:0.0.2'
 ```

 if you had use retrofit2 and okhttp3 in your project please exclude


 ```
 compile ('ren.yale.android:retrofitcachelib:0.0.2') {
    exclude module: 'okhttp-urlconnection', group: 'com.squareup.okhttp3'
    exclude module: 'retrofit', group: 'com.squareup.retrofit2'
 }

 ```

 - init in android Application

 ```
  RetrofitCache.getInatance().init(this);
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

 - add cache control

 > the Cache Annotation only affect the cache strategy when have net connection,and only effect with @GET request

   no cached

  ```
  @Cache()
  @GET("users")
  Observable<HttpResult> test();
  ```

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

- proguard-rule

```
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-dontwarn ren.yale.android.retrofitcachelib.**
-keep class ren.yale.android.retrofitcachelib.** { *; }

```