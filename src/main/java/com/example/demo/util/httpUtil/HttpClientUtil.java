package com.example.demo.util.httpUtil;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LiZhanPing on 2017/12/10.
 */
public class HttpClientUtil {

    public static CloseableHttpClient getHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return getHttpClient(null);
    }
    public static CloseableHttpClient getHttpClient(CookieStore cookieStore) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true).build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setDefaultCookieStore(cookieStore)
                .build();
        return httpClient;
    }
}
