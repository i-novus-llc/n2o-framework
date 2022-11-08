package net.n2oapp.framework.config.reader.util;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.reader.ElementReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * User: iryabov
 * Date: 03.07.13
 * Time: 10:44
 */
@Deprecated
public class ReaderJdomUtil {

    private static JdomTextProcessing jdomTextProcessing = new N2oJdomTextProcessing();

    public ReaderJdomUtil() {
        jdomTextProcessing = getBean(N2oJdomTextProcessing.class);
    }

    public static void clearTextProcessing() {
        jdomTextProcessing = null;
    }


    public static String getAttributeString(Element element, String attribute) {
        if (element == null) return null;
        Attribute attr = element.getAttribute(attribute);
        if (attr != null) {
            return getText(attr);
        }
        return null;
    }


    public static String getAttributeString(Element element, String attribute, String defaultValue) {
        String res = getAttributeString(element, attribute);
        if (res == null) res = defaultValue;
        return res;
    }

    public static Integer getAttributeInteger(Element element, String attribute) {
        if (element == null) return null;
        if (element.getAttribute(attribute) != null) {
            try {
                return Integer.valueOf(getText(element.getAttribute(attribute)).trim());
            } catch (NumberFormatException e) {
                throw new MetadataReaderException(e);
            }
        }
        return null;
    }

    public static Boolean getAttributeBoolean(Element element, String attribute) {
        if (element == null) return null;
        if (element.getAttribute(attribute) != null) {
            return Boolean.valueOf(getText(element.getAttribute(attribute)));
        }
        return null;
    }

    public static Boolean getAttributeBoolean(Element element, String attribute, String reserveAttribute) {
        Boolean res = getAttributeBoolean(element, attribute);
        if (res != null)
            return res;
        return getAttributeBoolean(element, reserveAttribute);
    }


    public static <T extends Enum<T>> T getElementEnum(Element parentElement, String elementName, Class<T> enumClass) {
        return stringToEnum(getElementString(parentElement, elementName), enumClass);
    }


    public static <T extends Enum<T>> T getAttributeEnum(Element element, String attribute, Class<T> enumClass) {
        if (element.getAttribute(attribute) != null) {
            String value = getText(element.getAttribute(attribute));
            return stringToEnum(value, enumClass);
        }
        return null;
    }

    public static <T extends Enum<T>> T getElementAttributeEnum(Element parentElement, String elementName, String attributeName, Class<T> enumClass) {
        Element child = parentElement.getChild(elementName, parentElement.getNamespace());
        if (child != null) {
            return getAttributeEnum(child, attributeName, enumClass);
        }
        return null;
    }

    public static String getElementAttributeString(Element parentElement, String elementName, String attributeName) {
        Element child = parentElement.getChild(elementName, parentElement.getNamespace());
        if (child != null) {
            return getAttributeString(child, attributeName);
        }
        return null;
    }

    public static Integer getElementAttributeInteger(Element parentElement, String elementName, String attributeName) {
        Element child = parentElement.getChild(elementName, parentElement.getNamespace());
        if (child != null) {
            return getAttributeInteger(child, attributeName);
        }
        return null;
    }

    public static Boolean getElementAttributeBoolean(Element parentElement, String elementName, String attributeName) {
        Element child = parentElement.getChild(elementName, parentElement.getNamespace());
        if (child != null) {
            return getAttributeBoolean(child, attributeName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> T stringToEnum(String value, Class<T> enumClass) {
        if (value == null) return null;
        T res = null;
        boolean idAware = IdAware.class.isAssignableFrom(enumClass);
        if (idAware) {
            for (Enum enumValue : enumClass.getEnumConstants()) {
                IdAware idEnum = (IdAware) enumValue;
                if (idEnum.getId().equalsIgnoreCase(value)) {
                    res = (T) enumValue;
                }
            }
        } else {
            for (Enum enumValue : enumClass.getEnumConstants()) {
                if (enumValue.name().equalsIgnoreCase(value)) {
                    res = (T) enumValue;
                }
            }
        }
        return res;
    }

    public static Boolean getElementBoolean(Element element, String elementName) {
        Element child = element.getChild(elementName, element.getNamespace());
        if (child != null) {
            return Boolean.valueOf(getText(child));
        }
        return null;
    }

    public static String getElementString(Element element, String elementName) {
        Element child = element.getChild(elementName, element.getNamespace());
        if (child != null) {
            String text = getText(child);
            return text.isEmpty() ? null : text;
        }
        return null;
    }

    public static Integer getElementInteger(Element element, String elementName) {
        String value = getElementString(element, elementName);
        return value != null ? Integer.valueOf(value) : null;
    }


    public static List<String> getElementsStringList(Element parentElement, String elementName) {
        if (parentElement == null) return null;
        List<String> result = new ArrayList<>();
        List<Element> elements = parentElement.getChildren();
        if (elements == null) return null;
        for (Element element : elements) {
            if (element.getName().equals(elementName)) {
                result.add(getText(element));
            }
        }
        return result;
    }

    public static String[] getElementsStringArray(Element parentElement, String elementName) {
        if (parentElement == null) return null;
        String[] result = new String[parentElement.getChildren().size()];
        List<Element> elements = parentElement.getChildren();
        if (elements == null) return null;
        int i = 0;
        for (Element element : elements) {
            if (element.getName().equals(elementName)) {
                result[i] = getText(element);
                i++;
            }
        }
        return result;
    }

    public static <T> T getChild(Element element, String elementName, ElementReader<T> reader) {
        if (element == null) return null;
        Element child = element.getChild(elementName, element.getNamespace());
        if (child == null) return null;
        return reader.read(child);
    }

    public static <T> T getChild(Element element, String sequenceName, String elementName, ElementReader<T> reader) {
        if (element == null) return null;
        Element child = element.getChild(sequenceName, element.getNamespace());
        if (child == null) return null;
        child = child.getChild(elementName, element.getNamespace());
        if (child == null) return null;
        return reader.read(child);
    }

    public static List<Element> getChildrenElements(Element element, String sequenceName, String elementName) {

        Element child = element;
        if (sequenceName != null)
            child = element.getChild(sequenceName, element.getNamespace());
        if (child == null) {
            return null;
        }
        List<Element> result = new ArrayList<>();
        List<Element> sequences = child.getChildren();
        if (sequences == null)
            return null;
        for (Element seqElement : sequences) {
            if ((elementName == null) || (seqElement.getName().equals(elementName))) {
                result.add(seqElement);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends NamespaceUriAware> List<T> getChildrenAsList(Element element, String sequenceName,
                                                NamespaceReaderFactory readerFactory,
                                                String defaultReaderNamespaceUri) {
        List<Element> elements = getChildrenElements(element, sequenceName, null);
        if (elements == null) return null;
        List<T> result = new ArrayList<>();
        for (Element e : elements) {
            if (e.getNamespace().getURI().equals(element.getNamespace().getURI())) {
                T child = (T) readerFactory.produce(e, element.getNamespace(), Namespace.getNamespace(defaultReaderNamespaceUri)).read(e);
                child.setNamespaceUri(element.getNamespaceURI());
                result.add(child);
            } else {
                result.add((T) readerFactory.produce(e).read(e));
            }
        }
        return result;
    }

    public static <T> List<T> getChildrenAsList(Element element, String sequenceName, String elementName,
                                                ElementReader<T> reader) {
        List<Element> elements = getChildrenElements(element, sequenceName, elementName);
        if (elements == null) return null;
        return elements.stream().map(reader::read).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <T extends NamespaceUriAware> T[] getChildren(Element element, String sequenceName,
                                                                NamespaceReaderFactory readerFactory, String defaultNamespaceUri,
                                                                Class<T> elementClass) {

        List<T> result = getChildrenAsList(element, sequenceName, readerFactory, defaultNamespaceUri);
        if (result == null)
            return null;
        T[] resultArray = (T[]) Array.newInstance(elementClass, result.size());
        for (int k = 0; k < resultArray.length; k++) {
            resultArray[k] = result.get(k);
        }
        return resultArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] getChildren(Element element, String sequenceName, String elementName,
                                      TypedElementReader<T> reader) {

        List<T> result = getChildrenAsList(element, sequenceName, elementName, reader);
        if (result == null)
            return null;
        T[] resultArray = (T[]) Array.newInstance(reader.getElementClass(), result.size());
        for (int k = 0; k < resultArray.length; k++) {
            resultArray[k] = result.get(k);
        }
        return resultArray;
    }

    public static <T> T[] getChildren(Element element, String sequenceName, String elementName,
                                      ElementReader<T> reader, Class<T> type) {
        return getChildren(element, sequenceName, elementName, new TypedElementReader<T>() {
            @Override
            public String getElementName() {
                return elementName;
            }

            @Override
            public Class<T> getElementClass() {
                return type;
            }

            @Override
            public T read(Element element) {
                return reader.read(element);
            }
        });
    }

    public static <T> List<T> getChildrenAsList(Element element, ElementReader<T> reader) {
        return getChildrenAsList(element, null, null, reader);
    }

    public static String getText(Attribute attr) {
        return processText(attr.getValue());
    }

    private static String processText(String res) {
        if (res == null)
            return null;
        return jdomTextProcessing != null ? jdomTextProcessing.process(res) : res;
    }

    public static String getElementText(Element child) {
        String res = getText(child);
        return res.isEmpty() ? null : res;
    }

    public static String getText(Element child) {
        return processText(child.getValue());
    }

    public static boolean isElementExists(Element field, String name) {
        return field != null && field.getChild(name, field.getNamespace()) != null;
    }

    @Deprecated //use analog without namespace
    public static String getElementString(Element element, Namespace namespace, String elementName) {
        return getElementString(element, elementName);
    }

    @Deprecated //use without namespace
    public static String getElementString(Element element, String elementName, Namespace namespace) {
        return getElementString(element, elementName);
    }

    @Deprecated //use analog without namespace
    public static <T> T getChild(Element element, Namespace namespace,
                                 String elementName, TypedElementReader<T> reader) {
        return getChild(element, elementName, reader);
    }

    @Deprecated //use getChildren
    public static <T> T[] getChilds(Element element, Namespace namespace,
                                    String sequenceName, String elementName, TypedElementReader<T> reader) {
        return getChildren(element, sequenceName, elementName, reader);
    }

    @Deprecated //use analog without namespace
    public static Element[] getChilds(Element element, Namespace namespace,
                                      String sequenceName, String elementName) {

        return getChilds(element, namespace, sequenceName, elementName, new TypedElementReader<Element>() {
            @Override
            public String getElementName() {
                return elementName;
            }

            @Override
            public Element read(Element element) {
                return element;
            }

            @Override
            public Class<Element> getElementClass() {
                return Element.class;
            }
        });
    }


    @Deprecated //этот метод считывет антипаттерны в xsd. Это когда основные элементы и ##other идут вперемешку,
    // поэтому правильным будет отказ от антипаттерна, и следом отказ от этого метода
    /**
     * Пример:
     * getChilds(root, root.getNamespace(), "field-set", fieldsetReader)
     * XML:
     * <form>
     *     <name>это не прочтет</name>
     *     <field-set>это не прочтет</field-set>
     *     <fs:field-set>это прочтет</fs:field-set>
     *     <fs:field-set>это прочтет</fs:field-set>
     * </form>
     */
    public static <T> T[] getChilds(Element element, Namespace namespace,
                                    String elementName, TypedElementReader<T> reader) {
        if (element == null) return null;
        List<T> result = new ArrayList<>();
        List<Element> sequences = element.getChildren();
        for (Element seqElement : sequences) {
            if (((elementName == null) && !(seqElement.getNamespace().equals(namespace)))
                    || (seqElement.getName().equals(elementName)))
                result.add(reader.read(seqElement));
        }
        T[] resultArray = (T[]) Array.newInstance(reader.getElementClass(), result.size());
        for (int k = 0; k < resultArray.length; k++) {
            resultArray[k] = result.get(k);
        }
        return resultArray;
    }
}
