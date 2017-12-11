package com.example.demo.pageProcessor;

import com.example.demo.util.httpUtil.CookieUtil;
import com.example.demo.util.httpUtil.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GeetestProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        System.out.println("page = " + page);
    }

    @Override
    public Site getSite() {
        return Site.me().setAcceptStatCode(new HashSet<Integer>() {{
            add(200);
        }}).setCycleRetryTimes(3).setRetrySleepTime(998).setRetryTimes(3).setRetrySleepTime(997).setTimeOut(60 * 1000).setDomain("www.gsxt.gov.cn").setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36").addHeader("Referer", "http://www.gsxt.gov.cn/corp-query-search-1.html");
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        CookieStore cookieStore = new BasicCookieStore();
        String cookies = "__jsluid=9316c9b6edc876dcc6a87e66641dfede; UM_distinctid=15cdda948dd29d-066515cabd488b-6b1b1279-100200-15cdda948de301; tlb_cookie=118query_8080; CNZZDATA1261033118=396541298-1487400696-http%253A%252F%252Fgsxt.saic.gov.cn%252F%7C1512920118; JSESSIONID=11B05D282F2E8164EDCB9F5B6B7C9440-n2:0; Hm_lvt_cdb4bc83287f8c1282df45ed61c4eac9=1512911002,1512915923,1512916084,1512916802; Hm_lpvt_cdb4bc83287f8c1282df45ed61c4eac9=1512924095";
        CookieUtil.setCookies(cookieStore,cookies);
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        String url = "http://www.gsxt.gov.cn/corp-query-search-1.html";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HttpHeaders.REFERER,"http://www.gsxt.gov.cn/corp-query-homepage.html");
        httpPost.setHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        httpPost.setEntity(new StringEntity("tab=ent_tab&token=121303867&searchword=%E9%87%8F%E5%AF%8C%E5%BE%81%E4%BF%A1&geetest_challenge=06d0fda53a673200f8f8f5513220a262c7&geetest_validate=9fed04bc5652566831002cda02e3161b&geetest_seccode=9fed04bc5652566831002cda02e3161b%7Cjordan"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println("result = " + result);
    }
}
