package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class ConverterImpl implements TextGraphicsConverter, TextColorSchema {

    private int width; // задаем максимальную ширину
    private int height; // задаем максимальную высоту
    private double maxRatio; // для вычисления масштаба будущей картинки

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        // скачаем картинку из интернета
        BufferedImage img = ImageIO.read(new URL(url));
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        double ratio;

        // вычисляем делитель для сторон
        if (img.getHeight() > img.getWidth()) {
            ratio = img.getHeight() / height;
        } else {
            ratio = img.getWidth() / width;
        }

        // вычисляем сторны для новой картинки
        if (img.getWidth() > width && img.getHeight() > height) {
            newWidth = (int) (img.getWidth() / ratio);
            newHeight = (int) (img.getHeight() / ratio);
        }

        // в случае если делитель за пределами максимального
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        // новые размеры картинки
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        // создадим новую пустую картинку нужных размеров, заранее указав последним
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        // инструмент для рисования
        Graphics2D graphics = bwImg.createGraphics();
        // этому инструменту скажем, чтобы он скопировал содержимое из нашей суженной картинки
        graphics.drawImage(scaledImage, 0, 0, null);

        // пройдёмся по пикселям нашего изображения
        WritableRaster bwRaster = bwImg.getRaster();

        // двойной цикл для получения изображения по всем столбцам (ширина) и строкам (высота)
        char[][] colors = new char[newHeight][newWidth];
        StringBuilder sb = new StringBuilder();
            for (int h = 0; h < newHeight; h++) {
                for (int w = 0; w < newWidth; w++) {
                    int color = bwRaster.getPixel(w, h, new int[3])[0];
                    TextColorSchema schema = new ColorSchema();
                    char c = schema.convert(color);
                    colors[h][w] = c;
                    sb
                            .append(c)
                            .append(" ");
                }
                sb.append("\n");
            }
            return sb.toString(); // собранный текст
        }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
    }

    @Override
    public char convert(int color) {
        return 0;
    }
}
