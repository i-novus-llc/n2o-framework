package net.n2oapp.framework.api.metadata.reader;

import net.n2oapp.framework.api.register.SourceInfo;
import org.jdom2.Element;

/**
 * Хранение текущего считанного элемента в рамках одного потока
 */
public class CurrentElementHolder {

    private static final ThreadLocal<Element> threadLocalElementScope = new ThreadLocal<>();

    private static final ThreadLocal<SourceInfo> threadLocalSourceInfoScope = new ThreadLocal<>();

    public static Element getElement() {
        return threadLocalElementScope.get();
    }

    public static void setElement(Element element) {
        threadLocalElementScope.set(element);
    }

    public static SourceInfo getSourceInfo() {
        return threadLocalSourceInfoScope.get();
    }

    public static void setSourceInfo(SourceInfo sourceInfo) {
        threadLocalSourceInfoScope.set(sourceInfo);
    }

    public static void clear() {
        threadLocalElementScope.remove();
        threadLocalSourceInfoScope.remove();
    }

}
