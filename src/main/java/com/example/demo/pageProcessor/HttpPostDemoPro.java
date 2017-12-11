package com.example.demo.pageProcessor;

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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class HttpPostDemoPro {

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
        String cookieStr = "BIGipServerpool_ipcrs_app=5MpL6nWJEnq1Mycvb+H7Of3zy4BZ/9yDNPUHWVud4w+dKedKicobz03tCC87ssRcJ0PBSnCuk5XQIVWCznw38d+dI0nGo6T5KvOa1+weUMws1+rFV9wB0aYZOeSYwqf6Vskye8nvJ3pPhvuuqb3hcGMp+F7CrA==; BIGipServerpool_ipcrs_web=LxNLQX7Gu4kOhWIvb+H7Of3zy4BZ/+plRXWPDosIVo5D3vhP291eVttCwTjgZoiefGXI5nBqaQ9A; JSESSIONID=0lpnZy3dNJN50nr2J22xHFyJhV6hCp4q2bdFh27vr5HcGTLg5J2Z!1480799272; TSf75e5b=971a20b16b4f6012592f2599b43ce65138990d4e6005979f59e83754";

        CloseableHttpClient httpClient = createHttpClient();
        try {


            String uri = "https://ipcrs.pbccrc.org.cn/login.do";
            HttpPost httpost = new HttpPost(uri);
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
            httpost.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp");
            httpost.addHeader("Cookie", cookieStr);
            // 添加参数
            List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
            valuePairs.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", "2b6d91e67faa1e7b8f8b0c1f88707107"));
            valuePairs.add(new BasicNameValuePair("method", "login"));
            valuePairs.add(new BasicNameValuePair("date", "1508390916379"));
            valuePairs.add(new BasicNameValuePair("loginname", "110Roey"));
            valuePairs.add(new BasicNameValuePair("password", "lzp110"));
            valuePairs.add(new BasicNameValuePair("_@IMGRC@_", "ssr7f3"));
            // 设置请求的编码格式
            httpost.setEntity(new UrlEncodedFormEntity(valuePairs, Consts.UTF_8));
            // 登录一遍
            CloseableHttpResponse loginResponse = httpClient.execute(httpost);
            String loginResult = EntityUtils.toString(loginResponse.getEntity());
            System.out.println("loginResult = " + loginResult);
            String welcomeUrl = "https://ipcrs.pbccrc.org.cn/welcome.do";
            HttpGet httpGet = new HttpGet(welcomeUrl);
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
            httpGet.addHeader("Referer", "https://ipcrs.pbccrc.org.cn/login.do");
            httpGet.addHeader("Cookie",cookieStr);
            CloseableHttpResponse welcomeResponse = httpClient.execute(httpGet);
            String welcomeResult = EntityUtils.toString(welcomeResponse.getEntity());
            System.out.println("welcomeResult = " + welcomeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
