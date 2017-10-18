package com.example.demo.pageProcessor;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PbccrcProcessor2 implements PageProcessor {
    @Override
    public void process(Page page) {
        System.out.println("page = " + page);
    }

    @Override
    public Site getSite() {
        return Site.me().setAcceptStatCode(new HashSet<Integer>() {{
            add(200);
        }}).setCycleRetryTimes(3).setRetrySleepTime(998).setRetryTimes(3).setRetrySleepTime(997).setTimeOut(60 * 1000).setDomain("ipcrs.pbccrc.org.cn");
    }

    public static void main(String[] args) {
        String loginUrl = "https://ipcrs.pbccrc.org.cn/login.do";
        Request loginReq = new Request(loginUrl);
        loginReq.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
        loginReq.addHeader("Referer","https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp");
        loginReq.setMethod("POST");
        Map<String, String> map = new HashMap<>();
        map.put("org.apache.struts.taglib.html.TOKEN","839d10cbef421393bf7059453b46cad1");
        map.put("method","login");
        map.put("date","1508330807613");
        map.put("loginname","110Roey");
        map.put("password","lzp110");
        map.put("_@IMGRC@","a8pbyf");
        NameValuePair[] valuePairs = new NameValuePair[map.size()];
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        int index = 0;
        for (Map.Entry<String, String> entry : entrySet) {
            valuePairs[index] = new BasicNameValuePair(entry.getKey(), entry.getValue());
            index++;
        }
        loginReq.putExtra("nameValuePair", valuePairs);
        Spider.create(new PbccrcProcessor2()).setDownloader(new HttpClientDownloader()).addRequest(loginReq).thread(3).run();
    }
}
