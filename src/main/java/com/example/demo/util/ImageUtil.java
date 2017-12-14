package com.example.demo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by LiZhanPing on 2017/12/15.
 */
public class ImageUtil {
    public static void restorePicture(String sourePicturePath, String targePicturePath) throws IOException {
        int rows = 2, columns = 26, sliceWidth = 10, sliceHeight = 58;
        BufferedImage image = ImageIO.read(Files.newInputStream(Paths.get(sourePicturePath)));
        BufferedImage[] slices = new BufferedImage[rows*columns];
        int[] widths1 = {157, 145, 265, 277, 181, 169, 241, 253, 109, 97, 289, 301, 85, 73, 25, 37, 13, 1, 121, 133, 61, 49, 217, 229, 205, 193};
        int[] widths2 = {145, 157, 277, 265, 169, 181, 253, 241, 97,109, 301, 289, 73, 85, 37, 25, 1, 13, 133, 121, 49, 61, 229, 217, 193, 205};
        for (int i=0;i<widths1.length ;i++) {
            slices[i] = new BufferedImage(sliceWidth, sliceHeight, image.getType());
            Graphics2D gr = slices[i].createGraphics();
            gr.drawImage(image, 0, 0, sliceWidth, sliceHeight,
                    widths1[i], sliceHeight, widths1[i] + 10, sliceHeight * 2, null);
            gr.dispose();
        }
        for (int i=0;i<widths2.length ;i++) {
            slices[i+12] = new BufferedImage(sliceWidth, sliceHeight, image.getType());
            Graphics2D gr = slices[i+12].createGraphics();
            gr.drawImage(image, 0, 0, sliceWidth, sliceHeight,
                    widths1[i], 0, widths1[i] + 10, sliceHeight , null);
            gr.dispose();
        }
        for (int i = 0; i < slices.length; i++) {
                ImageIO.write(slices[i], "png", new File("D:/image/slices" + i + ".png"));
        }
        BufferedImage newImage = new BufferedImage(columns * sliceWidth, rows * sliceHeight, image.getType());
        int num = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newImage.createGraphics().drawImage(slices[num], sliceWidth * j, sliceHeight * i, null);
                num++;
            }
        }
        ImageIO.write(newImage, "png", new File(targePicturePath));
    }

    public static void main(String[] args) throws IOException {
        restorePicture("D:/image/fullbg2.png","D:/image/good-fullbg2.png");
    }
}
