package ru.netology.graphics.image;

public class ColorSchema implements TextColorSchema {

    // заполняем картинку тенью
    private static final String DENSITY = "#$@%*+-'";

    // конвертируем
    @Override
    public char convert(int color) {
        double percent = DENSITY.length() / 255.0;
        int charValue = (int)(Math.round(percent * color));
        if (charValue < 0) {
            charValue = 0;
        } else if (charValue >= DENSITY.length()) {
            charValue = DENSITY.length() - 1;
        }
        return DENSITY.charAt(charValue);
    }
}