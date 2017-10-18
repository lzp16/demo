package com.example.demo.pageProcessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashSet;

public class GeetestProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        System.out.println("page = " + page);
    }

    @Override
    public Site getSite() {
        return Site.me().setAcceptStatCode(new HashSet<Integer>() {{
            add(200);
        }}).setCycleRetryTimes(3).setRetrySleepTime(998).setRetryTimes(3).setRetrySleepTime(997).setTimeOut(60 * 1000).setDomain("www.gsxt.gov.cn").setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3095.5 Safari/537.36").addHeader("Referer","http://www.gsxt.gov.cn/corp-query-search-1.html");
    }

    public static void main(String[] args) {
        String url = "http://api.geetest.com/ajax.php?gt=1d2c042096e050f07cb35ff3df5afd92&challenge=c31f434ea34085fb578ebf52216dae3c7q&userresponse=44d5d5413&passtime=2421&imgload=186&aa=G)!)!)!)(!!Bsssvt%2Fs6ss~tssssstsssvstss*sstssssstsssssstssssstssssstsssss~sssss~sssss((((!!(2T)-145*C%3F**151340.-44*D29%2C6L15047049H2A366%3A8769%2FMa8%3A3I%24)j4C73AfM77fE%24).7E%24L%2C2S2%24)4%246W&callback=geetest_1508303544040";
        Spider.create(new GeetestProcessor()).setDownloader(new HttpClientDownloader()).addUrl(url).thread(3).run();
    }
}
