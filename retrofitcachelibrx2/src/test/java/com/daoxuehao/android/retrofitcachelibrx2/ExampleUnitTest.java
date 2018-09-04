package com.daoxuehao.android.retrofitcachelibrx2;

import org.junit.Test;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ren.yale.android.retrofitcachelibrx2.RetrofitCache;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    protected  String getOriginUrl(String url){
        Set<String> params = RetrofitCache.getInstance().getIgnoreParam();
        if (params==null){
            return url;
        }
        for (String p:params){
            Pattern pattern = Pattern.compile(String.format("[\\?|&]%s=.*&|[\\?|&]%s=.*",p,p));
            Matcher m = pattern.matcher(url);
            while (m.find()){
                String rep = "";
                if (m.group().startsWith("?")){
                    rep="?";
                }
                url = m.replaceAll(rep);
            }
        }

       if (url.endsWith("?")){
            return url.substring(0,url.length()-1);
       }
        return url;
    }
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void test_getOriginUrl()throws Exception{
        RetrofitCache.getInstance().addIgnoreParam("test");

       String u =  getOriginUrl("http://www.baiduc.om");
       System.out.println(u+"\r\n");
        u =  getOriginUrl("http://www.baiduc.om?a=b");
        System.out.println(u+"\r\n");
        u =  getOriginUrl("http://www.baiduc.om?a=b&test=");
        System.out.println(u+"\r\n");
        u =  getOriginUrl("http://www.baiduc.om?test=bb&c=d");
        System.out.println(u+"\r\n");
        u =  getOriginUrl("http://www.baiduc.om?1test=bb&test=rer");
        System.out.println(u+"\r\n");
        u =  getOriginUrl("http://www.baiduc.om?test=&11test=rer");
        System.out.println(u+"\r\n");
        u =  getOriginUrl("http://www.baiduc.om?test=");
        System.out.println(u+"\r\n");

    }
}