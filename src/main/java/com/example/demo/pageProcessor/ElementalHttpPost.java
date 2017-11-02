package com.example.demo.pageProcessor;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;

import java.net.Socket;

public class ElementalHttpPost {
    public static void main(String[] args) throws Exception {
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("Test/1.1"))
                .add(new RequestExpectContinue(true)).build();

        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

        HttpCoreContext coreContext = HttpCoreContext.create();
        HttpHost host = new HttpHost("ipcrs.pbccrc.org.cn", 8080);
        coreContext.setTargetHost(host);

        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
        ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

        try {
            if (!conn.isOpen()) {
                Socket socket = new Socket(host.getHostName(), host.getPort());
                conn.bind(socket);
            }
            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST",
                    "/login.do",HttpVersion.HTTP_1_1);
            request.addHeader("Content-Type","application/x-www-form-urlencoded");
            request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36");
            request.addHeader("Referer","https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp");
            request.addHeader("Cookie","JSESSIONID=xFpJZyGLzdfTRtQhnQ6Qfp9Fh9ZdRDKCPcGYyvhjnGcCb9vYzN3T!1480799272; BIGipServerpool_ipcrs_app=5MpL6nWJEnq1Mycvb+H7Of3zy4BZ/9yDNPUHWVud4w+dKedKicobz03tCC87ssRcJ0PBSnCuk5XQIVWCznw38d+dI0nGo6T5KvOa1+weUMws1+rFV9wB0aYZOeSYwqf6Vskye8nvJ3pPhvuuqb3hcGMp+F7CrA==; BIGipServerpool_ipcrs_web=LxNLQX7Gu4kOhWIvb+H7Of3zy4BZ/+plRXWPDosIVo5D3vhP291eVttCwTjgZoiefGXI5nBqaQ9A; TSf75e5b=8ebbe971a8fbbc0e99a834754856013438990d4e6005979f59e8048b");
//            request.setEntity(requestBodies[i]);
            System.out.println(">> Request URI: " + request.getRequestLine().getUri());

            httpexecutor.preProcess(request, httpproc, coreContext);
            HttpResponse response = httpexecutor.execute(request, conn, coreContext);
            httpexecutor.postProcess(response, httpproc, coreContext);

            System.out.println("<< Response: " + response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("==============");
            if (!connStrategy.keepAlive(response, coreContext)) {
                conn.close();
            } else {
                System.out.println("Connection kept alive...");
            }
        } finally {
            conn.close();
        }
    }
}
