
package com.example.demo.pageProcessor;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

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

    public static void addCookie(Request request, String cookie) {
        String[] cookies = cookie.split(";");
        for (int i = 0; i < cookies.length; i++) {
            String[] keyValue = cookies[i].split("=");
            request.addCookie(keyValue[0],keyValue[1]);
        }
    }

    public static void main(String[] args) {
        String loginUrl = "https://ipcrs.pbccrc.org.cn/login.do";
        Request loginReq = new Request(loginUrl);
        loginReq.addHeader("Content-Type", "application/x-www-form-urlencoded");
        loginReq.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
        loginReq.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp");
        String cookie = "BIGipServerpool_ipcrs_app=5MpL6nWJEnq1Mycvb+H7Of3zy4BZ/9yDNPUHWVud4w+dKedKicobz03tCC87ssRcJ0PBSnCuk5XQIVWCznw38d+dI0nGo6T5KvOa1+weUMws1+rFV9wB0aYZOeSYwqf6Vskye8nvJ3pPhvuuqb3hcGMp+F7CrA==; BIGipServerpool_ipcrs_web=LxNLQX7Gu4kOhWIvb+H7Of3zy4BZ/+plRXWPDosIVo5D3vhP291eVttCwTjgZoiefGXI5nBqaQ9A; JSESSIONID=xqPdZyhScwP79ny3mJR5TbQ8y3V5lxdChYrGnPqhmB82jjJXGlnx!1480799272; TSf75e5b=7c8e5954553a7a26f7d309e57195a29438990d4e6005979f59e81964";
        addCookie(loginReq,cookie);
        loginReq.setMethod("POST");
        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
        valuePairs.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", "98883ba3578f7088f9e89270c991ed30"));
        valuePairs.add(new BasicNameValuePair("method", "login"));
        valuePairs.add(new BasicNameValuePair("date", "1508383325448"));
        valuePairs.add(new BasicNameValuePair("loginname", "110Roey"));
        valuePairs.add(new BasicNameValuePair("password", "lzp110"));
        valuePairs.add(new BasicNameValuePair("_%40IMGRC%40_", "7xnnqy"));
        loginReq.putExtra("nameValuePair", valuePairs);
        Spider.create(new PbccrcProcessor2()).setDownloader(new HttpClientDownloader()).addRequest(loginReq).thread(3).run();
    }
}