package net.n2oapp.framework.config.util;

/**
 * Утилита для генерации различных свойств во время компиляции
 */
public class CompileUtil {

    public static String generateWidgetId(String pageId, String localWidgetId) {
        if  ("_".equals(pageId))
            return localWidgetId;
        return pageId + "_" + localWidgetId;
    }
}
