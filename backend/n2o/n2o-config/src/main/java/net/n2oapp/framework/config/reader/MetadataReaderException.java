package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import org.jdom.Element;

/**
 * User: iryabov
 * Date: 06.05.13
 * Time: 13:25
 */
public class MetadataReaderException extends N2oException {
    public static void throwMoreThanOneChildElement(Element element) {
        throw new MetadataReaderException("More than one element in " + element);
    }

    public static void throwMissingAtLeastOneElement(Element element) {
        throw new MetadataReaderException("Missing at least one element in " + element);
    }

    public static void throwMissingAtLeastOneChildElement(Element element, String childName) {
        throw new MetadataReaderException("Missing at least one " + childName + " element in " + element);
    }

    public static void throwExpectedElement(Element element, String expected) {
        throw new MetadataReaderException(
                "Expected element '" + expected + "', but actual '" + element.getName() + "'");
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
