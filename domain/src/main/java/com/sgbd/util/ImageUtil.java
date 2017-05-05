package com.sgbd.util;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Lazarm on 5/5/2017.
 */
public class ImageUtil {

    public static String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static String generateIcon(String pathname, String fileName) {
        Icon icon = null;
        URL url = null;
        ImageIcon imgicon = null;
        BufferedImage scaledImage = null;
        Image image;
        String iconPathname = null;
        try {
            image = ImageIO.read(new File(pathname));
            icon  = new ImageIcon(image);
            if (icon == null) {
                System.out.println("Couldn't find " + url);
            }
            BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            bi = resizeImage(bi, 200, 200);
            scaledImage = bi;
            iconPathname = AppConstants.UPLOAD_PATH + File.separator + "icon"+ fileName;
            ImageIO.write(bi, "jpg", new File(iconPathname));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconPathname;
    }

    public static BufferedImage resizeImage (BufferedImage image, int areaWidth, int areaHeight) {
        float scaleX = (float) areaWidth / image.getWidth();
        float scaleY = (float) areaHeight / image.getHeight();
        float scale = Math.min(scaleX, scaleY);
        int w = Math.round(image.getWidth() * scale);
        int h = Math.round(image.getHeight() * scale);

        int type = image.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

        boolean scaleDown = scale < 1;

        if (scaleDown) {
            int currentW = image.getWidth();
            int currentH = image.getHeight();
            BufferedImage resized = image;
            while (currentW > w || currentH > h) {
                currentW = Math.max(w, currentW / 2);
                currentH = Math.max(h, currentH / 2);

                BufferedImage temp = new BufferedImage(currentW, currentH, type);
                Graphics2D g2 = temp.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(resized, 0, 0, currentW, currentH, null);
                g2.dispose();
                resized = temp;
            }
            return resized;
        } else {
            Object hint = scale > 2 ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR;

            BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resized.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(image, 0, 0, w, h, null);
            g2.dispose();
            return resized;
        }
    }

    public static String convertToURI(String pathname) throws IOException {
        File fnew = new File(pathname);
        BufferedImage iconImage = ImageIO.read(fnew);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(iconImage, "jpg", baos);
        System.out.println("baos.toByteArray() " + baos.toByteArray());
        System.out.println("baos.toByteArray().length " + baos.toByteArray().length);
        String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
        return  "data:image/jpg;base64," + data;
    }

}
