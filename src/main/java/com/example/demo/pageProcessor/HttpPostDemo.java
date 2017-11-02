package com.example.demo.pageProcessor;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.selector.Html;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HttpPostDemo {

    public static CloseableHttpClient createHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        return client;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public static void addCookie(BasicCookieStore cookieStore, String cookieStr) {
        String[] cookies = cookieStr.split(";");
        for (int i = 0; i < cookies.length; i++) {
            String[] keyValue = cookies[i].split("=");
            Cookie cookie = new BasicClientCookie(keyValue[0], keyValue[1]);
            cookieStore.addCookie(cookie);
        }
    }

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
//        String cookieStr = "BIGipServerpool_ipcrs_app=5MpL6nWJEnq1Mycvb+H7Of3zy4BZ/9yDNPUHWVud4w+dKedKicobz03tCC87ssRcJ0PBSnCuk5XQIVWCznw38d+dI0nGo6T5KvOa1+weUMws1+rFV9wB0aYZOeSYwqf6Vskye8nvJ3pPhvuuqb3hcGMp+F7CrA==; BIGipServerpool_ipcrs_web=LxNLQX7Gu4kOhWIvb+H7Of3zy4BZ/+plRXWPDosIVo5D3vhP291eVttCwTjgZoiefGXI5nBqaQ9A; JSESSIONID=0lpnZy3dNJN50nr2J22xHFyJhV6hCp4q2bdFh27vr5HcGTLg5J2Z!1480799272; TSf75e5b=971a20b16b4f6012592f2599b43ce65138990d4e6005979f59e83754";

        CloseableHttpClient httpClient = createHttpClient();
        try {
            //**********************初始页面************************
            String indexUrl = "https://ipcrs.pbccrc.org.cn/login.do?method=initLogin";
            HttpGet indexHttpGet = new HttpGet(indexUrl);
            indexHttpGet.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/index1.do");
            CloseableHttpResponse indexResponse = httpClient.execute(indexHttpGet);
            String indexResult = EntityUtils.toString(indexResponse.getEntity());
            Html html = new Html(indexResult);
            String token = html.xpath("//div[@id='main']//form[@name='loginForm']//input[@name='org.apache.struts.taglib.html.TOKEN']/@value").toString();
            String method = html.xpath("//div[@id='main']//form[@name='loginForm']//input[@name='method']/@value").toString();
            String date = html.xpath("//div[@id='main']//form[@name='loginForm']//input[@name='date']/@value").toString();
            String imgrcUrl = "https://ipcrs.pbccrc.org.cn" + html.xpath("//img[@id='imgrc']/@src").toString();
//            Header cookie = indexResponse.getFirstHeader("Cookie");
            //**********************验证码**************************
            HttpGet imgrcHttpGet = new HttpGet(imgrcUrl);
            imgrcHttpGet.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp");
//            imgrcHttpGet.addHeader(cookie);
            CloseableHttpResponse imgrcResponse = httpClient.execute(imgrcHttpGet);
            byte[] imageBytes = IOUtils.toByteArray(imgrcResponse.getEntity().getContent());
            Files.write(Paths.get("D:/test.jpeg"), imageBytes);
            //**********************登录*****************************
            String loginUrl = "https://ipcrs.pbccrc.org.cn/login.do";
            HttpPost loginHttpost = new HttpPost(loginUrl);
            loginHttpost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
            loginHttpost.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp");
//            loginHttpost.addHeader(cookie);
            // 添加参数
            List<NameValuePair> valuePairs = new ArrayList<>();
            valuePairs.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", token));
            valuePairs.add(new BasicNameValuePair("method", method));
            valuePairs.add(new BasicNameValuePair("date", date));
            valuePairs.add(new BasicNameValuePair("loginname", "manweijj"));
            valuePairs.add(new BasicNameValuePair("password", "kingdom1988vip"));
            System.out.println("请输入_@IMGRC@：");
            Scanner scanner = new Scanner(System.in);
            valuePairs.add(new BasicNameValuePair("_@IMGRC@_", scanner.nextLine()));
            System.out.println("结束输入数据!");
            // 设置请求的编码格式
            loginHttpost.setEntity(new UrlEncodedFormEntity(valuePairs, Consts.UTF_8));

            CloseableHttpResponse loginResponse = httpClient.execute(loginHttpost);
            String loginResult = EntityUtils.toString(loginResponse.getEntity());
            System.out.println("loginResult = " + loginResult);

            //**********************欢迎界面*****************************
            String welcomeUrl = "https://ipcrs.pbccrc.org.cn/welcome.do";
            HttpGet welcomeHttpGet = new HttpGet(welcomeUrl);
            welcomeHttpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
            welcomeHttpGet.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/login.do");
//            welcomeHttpGet.addHeader(cookie);
            CloseableHttpResponse welcomeResponse = httpClient.execute(welcomeHttpGet);
            String welcomeResult = EntityUtils.toString(welcomeResponse.getEntity());
            String reportState = "未申请";
            Html welcomHtml = new Html(welcomeResult);
            List<String> pList = welcomHtml.xpath("//div[@class='right_con1']/p[@class='p1 span-14 span-grey2']/text()").all();
            for (String p : pList) {
                if (p.contains("您的个人信用报告")) {
                    reportState = p.replace("您的个人信用报告", "").trim();
                }
            }
            if (!reportState.equals("加工成功")) {
                return;
            }
            //*********************身份验证*****************************
//            String reportUrl = "https://ipcrs.pbccrc.org.cn/reportAction.do";
//            HttpPost reportHttpPost = new HttpPost(reportUrl);
//            reportHttpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
//            reportHttpPost.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/reportAction.do?method=queryReport");
//            reportHttpPost.addHeader(cookie);
//            // 添加参数
//            List<NameValuePair> valuePairs2 = new ArrayList<>();
//            valuePairs2.add(new BasicNameValuePair("method", "checkTradeCode"));
//            valuePairs2.add(new BasicNameValuePair("code", "xssch4"));
//            valuePairs2.add(new BasicNameValuePair("reportformat", "21"));
//            reportHttpPost.setEntity(new UrlEncodedFormEntity(valuePairs2, Consts.UTF_8));
//            CloseableHttpResponse reportResponse = httpClient.execute(reportHttpPost);
//            String reportResult = EntityUtils.toString(reportResponse.getEntity());
//            System.out.println("reportResult = " + reportResult);

            //*********************获取报告*****************************
            String reportUrl2 = "https://ipcrs.pbccrc.org.cn/simpleReport.do?method=viewReport";
            HttpPost reportHttpPost2 = new HttpPost(reportUrl2);
            reportHttpPost2.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
            reportHttpPost2.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/reportAction.do?method=queryReport");
//            reportHttpPost2.addHeader(cookie);
            // 添加参数
            List<NameValuePair> valuePairs3 = new ArrayList<>();
            valuePairs3.add(new BasicNameValuePair("counttime", ""));
            valuePairs3.add(new BasicNameValuePair("reportformat", "21"));
            valuePairs3.add(new BasicNameValuePair("tradeCode", "aae6qe"));
            reportHttpPost2.setEntity(new UrlEncodedFormEntity(valuePairs3, Consts.UTF_8));
            CloseableHttpResponse reportResponse2 = httpClient.execute(reportHttpPost2);
//            String reportResult2 = EntityUtils.toString(reportResponse2.getEntity());
//            System.out.println("reportResult2 = " + reportResult2);
            Files.write(Paths.get("D:/report.html"), IOUtils.toByteArray(reportResponse2.getEntity().getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
