package com.facewnd.core.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class RandomValidateCodeUtils {
    private static Random random = new Random();
    private static final String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //可以去掉 0 O 1 i 等不好分辨
    private static int width = 90;// 图片宽
    private static int height = 40;// 图片高
    private static int lineSize = 40;// 干扰线数量
    private static int stringNum = 4;// 随机产生字符数量

    /**
     * 随机图片
     * @param request
     * @param response
     * @throws IOException 
     */
    public static String getRandcode(OutputStream outputStream) throws IOException {
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.CENTER_BASELINE, 18));
        g.setColor(getRandColor(110, 133));
        // 干扰线
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g);
        }
        // 随机字符
        String randomString = "";
        for (int i = 1; i <= stringNum; i++) {
            randomString = drowString(g, randomString, i);
        }

        g.dispose();
        ImageIO.write(image, "JPEG", outputStream);
        
        return randomString;
    }

    /**
     * 字体
     * @return
     */
    private static Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 22);
    }

    /**
     * 随机颜色
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }
    
    /**
     * 字符串
     * @param g
     * @param randomString
     * @param i
     * @return
     */
    private static String drowString(Graphics g, String randomString, int i) {
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
                .nextInt(121)));
        String rand = String.valueOf(getRandomString(random.nextInt(randString
                .length())));
        randomString += rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 13 * i, 25);
        return randomString;
    }

    /**
     * 干扰线
     * @param g
     */
    private static void drowLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取随机字符
     * @param num
     * @return
     */
    private static String getRandomString(int num) {
        return String.valueOf(randString.charAt(num));
    }
}
