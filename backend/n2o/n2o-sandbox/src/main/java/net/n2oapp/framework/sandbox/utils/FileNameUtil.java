package net.n2oapp.framework.sandbox.utils;

import org.springframework.util.StringUtils;

/**
 * Утилитный класс для работы с названиями файлов
 */
public class FileNameUtil {

    /**
     * Получение имени файла из полного названия с расширением
     * @param file Полное название файла
     * @return Имя файла
     */
    public static String getNameFromFile(String file) {
        return file.split("\\.")[0];
    }

    /**
     * Получение типа xml-файла
     * @param file Полное название файла
     * @return Тип xml-файла
     */
    public static String getTypeFromFile(String file) {
        return file.split("\\.")[1];
    }

    /**
     * Проверка, что файл является исходным N2O файлом
     * @param file Полное название файла
     * @return
     */
    public static Boolean isSourceFile(String file) {
        String[] split = file.split("\\.");
        return split.length == 3 && "xml".equals(split[2]);
    }

    /**
     * Проверка, что файл является property файлом
     * @param file Полное название файла
     * @return
     */
    public static Boolean isPropertyFile(String file) {
        return "properties".equals(StringUtils.getFilenameExtension(file));
    }
}
