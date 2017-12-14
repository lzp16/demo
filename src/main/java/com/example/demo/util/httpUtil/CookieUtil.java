package com.example.demo.util.httpUtil;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiZhanPing on 2017/12/10.
 */
public class CookieUtil {
    public static List<Cookie> str2Cookies(String cookies,String domain){
        List<Cookie> cookieList = new ArrayList<>();
        String[] cookieStrs = cookies.split(";");
        for(String cookieStr : cookieStrs){
            String[] keyValue = cookieStr.trim().split("=");
            BasicClientCookie cookie = new BasicClientCookie(keyValue[0],keyValue[1]);
            cookie.setDomain(domain);
            cookie.setPath("/");
            cookieList.add(cookie);
        }
        return cookieList;
    }

    public static void setCookies(CookieStore cookieStore,List<Cookie> cookieList){
        for (Cookie cookie : cookieList){
            cookieStore.addCookie(cookie);
        }
    }

    public static void setCookies(CookieStore cookieStore,String cookies,String domian){
        setCookies(cookieStore,str2Cookies(cookies,domian));
    }
}
