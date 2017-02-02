package com.erith.api.imageprocessing.com.erith.api.imageprocessing.exercise;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CountPixel {

    enum MODE {
        EASY, NORMAL, HARD, EXTREME
    }

    private static final float MODIFIER_EASY        = 2f;
    private static final float MODIFIER_NORMAL      = 2.5f;
    private static final float MODIFIER_HARD        = 3.0f;
    private static final float MODIFIER_EXTREME     = 3.5f;

    public static void main(String args[]) {
        try {
            // get the BufferedImage, using the ImageIO class
            BufferedImage image = ImageIO.read(CountPixel.class.getResource("/sprite.png"));
            countPixelNotTransparent(image);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void printPixelARGB(int pixel, long totalPixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb(" + alpha + ", " + red + ", " + green + ", " + blue + ") is used " + totalPixel + " pixel");
    }

    private static void countPixelNotTransparent(BufferedImage image) {
        double pixelToCm = 14.0 / 52.0;
        int w = image.getWidth();
        int h = image.getHeight();
        int totalPixel = 0;
        Map<Integer, Long> dataCounter = new HashMap<Integer, Long>();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                // System.out.println("x,y: " + j + ", " + i);
                int pixel = image.getRGB(j, i);
                if(pixel != 0) {
                    totalPixel++;
                    if(dataCounter.containsKey(pixel)) {
                        long totalPerPixel = dataCounter.get(pixel);
                        dataCounter.put(pixel, ++totalPerPixel);
                    } else {
                        dataCounter.put(pixel, 1l);
                    }
                }
                // printPixelARGB(pixel);
            }
        }

        MODE mode = MODE.NORMAL;

        System.out.println("totalPixel: " + totalPixel);
        System.out.println("--------------------------------------");
        for(int pixel : dataCounter.keySet()) {
            printPixelARGB(pixel, dataCounter.get(pixel));
        }
        System.out.println("--------------------------------------");
        System.out.println("Width: " + (pixelToCm * w) + " cm");
        System.out.println("Height: " + (pixelToCm * h) + " cm");
        long modal = totalPixel * 25;
        System.out.println("Modal: " + modal);

        if(mode == MODE.EASY) {
            System.out.println("Price: " + (modal * MODIFIER_EASY));
        } else if(mode == MODE.NORMAL) {
            System.out.println("Price: " + (modal * MODIFIER_NORMAL));
        } else if(mode == MODE.HARD) {
            System.out.println("Price: " + (modal * MODIFIER_HARD));
        } else if(mode == MODE.EXTREME) {
            System.out.println("Price: " + (modal * MODIFIER_EXTREME));
        }

    }
}
