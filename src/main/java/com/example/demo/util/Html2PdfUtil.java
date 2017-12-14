package com.example.demo.util;

import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Html2PdfUtil {

    public static final String REPLACE_META_BEFORE = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">";
    private static final String REPLACE_META_AFTER = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta> \n <style type=\"text/css\">body{font-family:STKaiti;}</style>";

    /**
     * 转pdf  by  pd4ml
     *
     * @param path 零时文件路径
     * @throws IOException
     */
    private static void htmlToPdfUsingPath(String path) throws IOException {


        File inputFile = new File(path);
        String outputFile = inputFile.getParent() + System.currentTimeMillis() + ".pdf";
        OutputStream os = new FileOutputStream(outputFile);

        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String html = new String(bytes,"gbk").replace(REPLACE_META_BEFORE,REPLACE_META_AFTER);
        InputStreamReader is = new InputStreamReader(new ByteArrayInputStream(html.getBytes()),"utf-8");

        PD4ML pd4ml = new PD4ML();
        pd4ml.setHtmlWidth(1050);
        pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
        pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
        pd4ml.useTTF("java:fonts", true);
        pd4ml.setDefaultTTFs("STKaiti", "STKaiti", "STKaiti");
        pd4ml.enableDebugInfo();
        pd4ml.render(is, os);
    }

    public static void main(String[] args) throws IOException {
        htmlToPdfUsingPath("C:\\image\\pbcCreditReportCrawler\\creditReport\\zip\\test0002\\20171116\\9fba5266-31a8-4cb5-a6cd-9c36d8e90e96.html");
    }
}
