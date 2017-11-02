package ren.yale.android.retrofitcachelib;

import android.text.TextUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ren.yale.android.retrofitcachelib.anno.Cache;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yale on 2017/11/2.
 */

public class RetrofitCache2 {

    static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
    static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);

    private HashMap<String,MethodInfo> methodsMaps = new HashMap<>();


    private void createMethodInfo(String base,List<String> paths,List<String> keys){
        MethodInfo methodInfo = new MethodInfo();
        try {
            String baseHost = base;
            Matcher matcher = PARAM_URL_REGEX.matcher(base);
            if (matcher.find()){
                for (int i = 0 ;i <matcher.groupCount();i++){
                    String p = matcher.group(i);
                    if (!paths.contains(p)){
                        return;
                    }
                }
            }
            URL url = new URL(base);
            String q = url.getQuery();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }


    public void createService(Retrofit retrofit,Class<?> service){

        for (Method method : service.getDeclaredMethods()) {
            if (method.getAnnotation(Cache.class)==null){
                continue;
            }
            Annotation annotation = method.getAnnotation(GET.class);
            if (annotation!=null){
                List<String> keys = new ArrayList<>();
                List<String> paths = new ArrayList<>();
                String base = retrofit.baseUrl().toString();
                String value = "";
                Annotation [][] anps =  method.getParameterAnnotations();
                for (int i = 0;i<anps.length;i++){
                    Annotation ap = anps[i][0];
                    if (ap instanceof Path){
                        value = ((Path) ap).value();
                        if (!TextUtils.isEmpty(value)){
                            paths.add(value);
                        }
                    }else if(ap instanceof Query){
                        value = ((Query) ap).value();
                        if (!TextUtils.isEmpty(value)){
                            keys.add(value);
                        }
                    }
                }
                createMethodInfo(base,paths,keys);
            }
        }
    }

}
