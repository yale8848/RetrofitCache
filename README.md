# RetrofitCache

[English](README_EN.md)

[![](https://img.shields.io/badge/jcenter-retrofitcache_1.0.4-519dd9.svg)](https://bintray.com/yale8848/maven/retrofitcache/1.0.4)
[![](https://img.shields.io/badge/jcenter-retrofitcacherx2_1.0.4-519dd9.svg)](https://bintray.com/yale8848/maven/retrofitcacherx2/1.0.4)

RetrofitCache让retrofit2+okhttp3+rxjav配置缓存如此简单。通过注解配置，可以针对每一个接口灵活配置缓存策略；同时让每一个接口方便支持数据模拟，可以代码减小侵入性，模拟数据可以从内存，Assets，url轻松获取。

## 为什么使用RetrofitCache

- 服务端接口不严格按照http缓存策略配置，有些不会针对每一个请求单独配置缓存策略
- 第三方缓存库不是很方便的针对每一个接口进行缓存策略配置，侵入性比较大
- 很方便的针对每个接口添加模拟数据

## 调用例子

- 不走缓存例子

```
@GET("users")
Observable<HttpResult> test();
```

- 缓存设置为20秒

 ```
@Cache(time = 20)
@GET("users")
Observable<HttpResult> test();
 ```

- 缓存设置为20分钟

 ```
@Cache(time = 20,timeUnit = TimeUnit.MINUTES)
@GET("users")
Observable<HttpResult> test();
 ```

- 默认时间缓存,默认是0秒

 ```
@Cache()
@GET("users")
Observable<HttpResult> test();
 ```

- 添加模拟数据（value,assets,url同时都配置的话，就按照这个顺序处理）

 ```
@Mock(value = "{\"data\":\"mockdata\"}") //模拟内存数据
@GET("users")
Observable<HttpResult> test();
 ```

 ```
@Mock(assets = "mock/mock.json") //从assets获取模拟数据
@GET("users")
Observable<HttpResult> test();
 ```

 ```
@Mock(url = "http://url.com/test") //从新的url请求数据
@GET("users")
Observable<HttpResult> test();
  ```


> 缓存只对http Get请求有效；如果要问为什么，可以问问后台开发同学

## 使用方法:

 - 添加 jcenter lib,注意根据自己的库选择

 ```
compile 'ren.yale.android:retrofitcachelib:1.0.4'   //retrofit2+okhttp3+rxjava1
compile 'ren.yale.android:retrofitcachelibrx2:1.0.4'   //retrofit2+okhttp3+rxjava2
 ```

 - 在Android Application里初始化

 ```
RetrofitCache.getInstance().init(this);
 ```

也可以修改默认配置，默认time=0，timeUnit = TimeUnit.SECONDS

```
RetrofitCache.getInstance().init(this).setDefaultTimeUnit(TimeUnit.MINUTES).setDefaultTime(1);
```

 - OkHttpClient初始化时配置缓存目录

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
-  **添加 rx Observable compose**

```
api.test().compose(CacheTransformer.emptyTransformer())...

```


## 进阶


- setCacheInterceptorListener 设置是否每一个接口都缓存

```
RetrofitCache.getInstance().setCacheInterceptorListener(
                new CacheInterceptorListener() {
            @Override
            public boolean canCache(Request request,Response response) {
                String res = "";
                try {
                    res = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
});

```


- 设置是否走模拟数据,比如说在正式接口好了后可以如下设置，让模拟数据失效

```
RetrofitCache.getInstance().enableMock(false);
```


## 混淆配置

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

## 欢迎提问讨论

[讨论区](https://github.com/yale8848/RetrofitCache/issues)

## 贡献代码

[贡献代码](CONTRIBUTING.md)

## 开源协议

```
MIT License

Copyright (c) 2017 Yale

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```