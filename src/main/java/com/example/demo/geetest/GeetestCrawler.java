package com.example.demo.geetest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.httpUtil.HttpClientUtil;
import org.apache.http.HttpHeaders;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class GeetestCrawler {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient(cookieStore);
        String url = "http://www.gsxt.gov.cn/SearchItemCaptcha?v="+System.currentTimeMillis();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HttpHeaders.REFERER,"http://www.gsxt.gov.cn/index.html");
        httpGet.setHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println("result = " + result);
        JSONObject jsonObject = JSONObject.parseObject(result, JSONObject.class);
        String gt = jsonObject.getString("gt");
        String challenge = jsonObject.getString("challenge");
        url = "http://api.geetest.com/gettype.php?gt="+gt+"&callback=geetest_"+System.currentTimeMillis();
        httpGet.setURI(URI.create(url));
        response = httpClient.execute(httpGet);
        result = EntityUtils.toString(response.getEntity());
        System.out.println("result = " + result);
        url = "http://api.geetest.com/get.php?gt="+gt+"&challenge="+challenge+"&product=popup&offline=false&protocol=http://&path=/static/js/geetest.6.0.5.js&type=slide&callback=geetest_"+System.currentTimeMillis();
        httpGet.setURI(URI.create(url));
        response = httpClient.execute(httpGet);
        result = EntityUtils.toString(response.getEntity());
        result = result.substring(result.indexOf("(")+1,result.indexOf(")"));
        System.out.println("result = " + result);
        jsonObject = JSONObject.parseObject(result, JSONObject.class);
        JSONArray static_servers = jsonObject.getJSONArray("static_servers");
        String host = static_servers.getString(0);
        String bg = jsonObject.getString("bg");
        String fullbg = jsonObject.getString("fullbg");
        String slice = jsonObject.getString("slice");
        url = "http://"+host+"/"+bg;
        httpGet.setURI(URI.create(url));
        response = httpClient.execute(httpGet);
        String bgFilePath = "E://geetest/bg.png";
        Files.write(Paths.get(bgFilePath),EntityUtils.toByteArray(response.getEntity()));
        url = "http://"+host+"/"+fullbg;
        httpGet.setURI(URI.create(url));
        response = httpClient.execute(httpGet);
        String fullbgFilePath = "E://geetest/fullbg.png";
        Files.write(Paths.get(fullbgFilePath),EntityUtils.toByteArray(response.getEntity()));
        url = "http://"+host+"/"+slice;
        httpGet.setURI(URI.create(url));
        response = httpClient.execute(httpGet);
        String sliceFilePath = "E://geetest/slice.png";
        Files.write(Paths.get(sliceFilePath),EntityUtils.toByteArray(response.getEntity()));
    }
}
