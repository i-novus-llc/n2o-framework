package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import org.jdom2.Element;

/**
 * User: iryabov
 * Date: 06.05.13
 * Time: 13:25
 */
public class MetadataReaderException extends N2oException {
    public static void throwMoreThanOneChildElement(Element element) {
        throw new MetadataReaderException(String.format("В элементе '%s' содержится более одного дочернего элемента", element.getName()));
    }

    public static void throwMissingAtLeastOneElement(Element element) {
        throw new MetadataReaderException(String.format("В элементе '%s' отсутствует хотя бы один дочерний элемент", element.getName()));
    }

    public static void throwMissingAtLeastOneChildElement(Element element, String childName) {
        throw new MetadataReaderException(String.format("В элементе '%s' отсутствует хотя бы один дочерний элемент '%s'", element.getName(), childName));
    }

    public static void throwExpectedElement(Element element, String expected) {
        throw new MetadataReaderException(String.format("Ожидался элемент '%s', но получен '%s'", expected, element.getName()));
    }

    public MetadataReaderException(String message) {
        super(message);
    }

    public MetadataReaderException(Throwable cause) {
        super(cause);
    }

    public MetadataReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}