package com.example.demo.pageProcessor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PbccrcProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        if (page.getUrl().toString().contains("https://ipcrs.pbccrc.org.cn/login.do?method=initLogin")) {
            String token = page.getHtml().xpath("//div[@id='main']//form[@name='loginForm']//input[@name='org.apache.struts.taglib.html.TOKEN']/@value").toString();
            String method = page.getHtml().xpath("//div[@id='main']//form[@name='loginForm']//input[@name='method']/@value").toString();
            String date = page.getHtml().xpath("//div[@id='main']//form[@name='loginForm']//input[@name='date']/@value").toString();
            String imgrcUrl = "https://ipcrs.pbccrc.org.cn" + page.getHtml().xpath("//img[@id='imgrc']/@src").toString()+"&token="+token+"&method="+method+"&date="+date;
            Request imgrcRequest = new Request(imgrcUrl);
            imgrcRequest.addHeader("Referer","https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp");
            page.addTargetRequest(imgrcRequest);
        } else if(page.getUrl().toString().contains("imgrc")){
            try {
                Files.write(Paths.get("D:/test.jpeg"), page.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String oldUrl = page.getUrl().toString();
            String token = oldUrl.substring(oldUrl.indexOf("&token=")+6,oldUrl.indexOf("&method="));
            String urlMethod = oldUrl.substring(oldUrl.indexOf("&method=")+8,oldUrl.indexOf("&date="));
            String date = oldUrl.substring(oldUrl.indexOf("&date=")+6);
            String url = "https://ipcrs.pbccrc.org.cn/login.do";
            String method = "POST";
            Request loginReq = new Request(url);
            loginReq.addHeader("Referer","https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp");
            loginReq.setMethod(method);
            Map<String, String> map = new HashMap<>();
            map.put("org.apache.struts.taglib.html.TOKEN",token);
            map.put("method",urlMethod);
            map.put("date",date);
            map.put("loginname","panlefeng");
            map.put("password","plfplf520921");
            System.out.println("请输入_@IMGRC@：");
            Scanner scanner = new Scanner(System.in);
            map.put("_@IMGRC@",scanner.nextLine());
            System.out.println("结束输入数据!");
//            loginReq.setRequestBody(HttpRequestBody.form(map,"UTF-8"));
            NameValuePair[] valuePairs = new NameValuePair[map.size()];
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            int index = 0;
            for (Map.Entry<String, String> entry : entrySet) {
                valuePairs[index] = new BasicNameValuePair(entry.getKey(), entry.getValue());
                index++;
            }
            loginReq.putExtra("nameValuePair", valuePairs);
            page.addTargetRequest(loginReq);
        } else if (page.getUrl().toString().contains("login")) {
            String welcomeUrl = "https://ipcrs.pbccrc.org.cn/welcome.do";
            Request welcomeReq = new Request(welcomeUrl);
            welcomeReq.addHeader("Referer","https://ipcrs.pbccrc.org.cn/login.do");
            page.addTargetRequest(welcomeReq);
        } else if(page.getUrl().toString().contains("welcome")){
            System.out.println("page = " + page);
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setAcceptStatCode(new HashSet<Integer>() {{
            add(200);
        }}).setCycleRetryTimes(3).setRetrySleepTime(998).setRetryTimes(3).setRetrySleepTime(997).setTimeOut(60 * 1000).setDomain("ipcrs.pbccrc.org.cn").setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
    }

    public static void main(String[] args) {
        String url = "https://ipcrs.pbccrc.org.cn/login.do?method=initLogin";
        Request request = new Request(url);
        request.addHeader("Referer","https://ipcrs.pbccrc.org.cn/index1.do");
        Spider.create(new PbccrcProcessor()).setDownloader(new HttpClientDownloader()).addRequest(request).thread(3).run();

    }
}
