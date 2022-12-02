package net.n2oapp.framework.config.persister.util;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.persister.ElementPersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;


import java.util.Arrays;
import java.util.List;

/**
 * User: iryabov
 * Date: 03.07.13
 * Time: 13:53
 */
public class PersisterJdomUtil {
    public static void setAttribute(Element element, String attribute, String value) {
        if (element == null) return;
        if (value == null) return;
        element.setAttribute(attribute, value);
    }

    public static void setAttribute(Element element, String attribute, Integer value) {
        if (element == null) return;
        if (value == null) return;
        element.setAttribute(attribute, value.toString());
    }

    public static void setAttribute(Element element, String attribute, Boolean value) {
        if (element == null) return;
        if (value == null) return;
        element.setAttribute(attribute, value.toString());
    }

    public static void setAttribute(Element element, String attribute, Enum value) {
        if (element == null) return;
        if (value == null) return;
        if (value instanceof IdAware) {
            element.setAttribute(attribute, ((IdAware) value).getId());
        } else {
            element.setAttribute(attribute, value.name());
        }
    }

    public static Element setElement(String elementName) {
        return new Element(elementName);
    }

    public static Element setElement(String elementName, String prefix, String uri) {
        return new Element(elementName, Namespace.getNamespace(prefix, uri));
    }

    public static Element setElement(Element parentElement, String elementName) {
        Element element = new Element(elementName, parentElement.getNamespace());
        parentElement.addContent(element);
        return element;
    }

    public static Element setElementString(Element element, String elementName, String value) {
        Element subElement = null;
        if (value != null && element != null) {
            subElement = new Element(elementName, element.getNamespace());
            subElement.setText(value);
            element.addContent(subElement);
        }
        return subElement;
    }

    public static Element setElementBoolean(Element element, String elementName, Boolean value) {
        Element subElement = null;
        if (value != null && element != null) {
            subElement = new Element(elementName, element.getNamespace());
            subElement.setText(value.toString());
            element.addContent(subElement);
        }
        return subElement;
    }

    public static void setElementInteger(Element element, String elementName, Integer value) {
        if (element == null) return;
        if (value != null) {
            Element subElement = new Element(elementName, element.getNamespace());
            subElement.setText(value.toString());
            element.addContent(subElement);
        }
    }

    public static Element setEmptyElement(Element element, String elementName) {
        Element subElement = null;
        if (element != null) {
            subElement = new Element(elementName, element.getNamespace());
            element.addContent(subElement);
        }
        return subElement;
    }

    public static <E> Element setChild(Element element, String childName, E value, ElementPersister<E> persister) {
        if (element == null) return null;
        if (value != null) {
            Element child = persister.persist(value, element.getNamespace());
            if (childName != null)
                child.setName(childName);
            replaceNoNamespace(child, element.getNamespace());
            element.addContent(child);
            return child;
        }
        return null;
    }

    public static <E> void setChildren(Element element, E[] values, ElementPersister<E> persister) {
        if (values == null) return;
        setChildrenAsList(element, null, null, Arrays.asList(values), persister);
    }

    public static <E> void setChildrenAsList(Element element, List<E> values, ElementPersister<E> persister) {
        setChildrenAsList(element, null, null, values, persister);
    }

    public static <E> void setChildren(Element element, String sequenceName, String childName,
                                       E[] values, ElementPersister<E> persister) {
        if (values == null) return;
        setChildrenAsList(element, sequenceName, childName, Arrays.asList(values), persister);
    }

    public static <E> void setChildrenAsList(Element element, String sequenceName, String childName,
                                             List<E> values, ElementPersister<E> persister) {
        if (values == null) return;
        if (element == null) return;
        Element sequences = element;
        if (sequenceName != null) {
            sequences = new Element(sequenceName, element.getNamespace());
            element.addContent(sequences);
        }
        for (E value : values) {
            Element subChild = persister.persist(value, element.getNamespace());
            if (childName != null)
                subChild.setName(childName);
            replaceNoNamespace(subChild, element.getNamespace());
            sequences.addContent(subChild);
            PersisterJdomUtil.installPrefix(subChild, sequences);
        }
        if (sequences != element)
            PersisterJdomUtil.installPrefix(sequences, element);
    }

    @SuppressWarnings("unchecked")
    public static <E extends NamespaceUriAware> void setChildren(Element element, String sequenceName, String childName,
                                       E[] values, NamespacePersisterFactory persisterFactory, String defaultNamespaceUri) {
        if (values == null) return;
        setChildrenAsList(element, sequenceName, childName, Arrays.asList(values), persisterFactory, defaultNamespaceUri);
    }

    @SuppressWarnings("unchecked")
    public static <E extends NamespaceUriAware> void setChildrenAsList(Element element, String sequenceName, String childName,
                                                                       List<E> values, NamespacePersisterFactory persisterFactory,
                                                                       String defaultNamespaceUri) {
        if (values == null) return;
        if (element == null) return;
        Element sequences = element;
        if (sequenceName != null) {
            sequences = new Element(sequenceName, element.getNamespace());
            element.addContent(sequences);
        }
        for (E value : values) {
            NamespacePersister<E> persister;
            try {
                persister = (NamespacePersister<E>) persisterFactory.produce(value);
            } catch (EngineNotFoundException ex) {
                persister = (NamespacePersister<E>) persisterFactory.produce(value.getClass(), Namespace.getNamespace(defaultNamespaceUri));
            }
            Element subChild = persister.persist(value, element.getNamespace());
            if (childName != null)
                subChild.setName(childName);
            if (subChild.getNamespace() == null
                    || (persister.getNamespaceUri() != null && persister.getNamespaceUri().equals(defaultNamespaceUri)))
                replaceNamespace(subChild, subChild.getNamespace(), element.getNamespace());
            sequences.addContent(subChild);
            PersisterJdomUtil.installPrefix(subChild, sequences);
        }
        if (sequences != element)
            PersisterJdomUtil.installPrefix(sequences, element);
    }

    public static void installPrefix(Element element, Element rootElement) {
        if (element == null) return;
        if (rootElement.getNamespace() == null) return;
        if (element.getNamespacePrefix() == null) return;
        if (element.getNamespace() == null) return;
        if (element.getNamespace().getURI().equals(rootElement.getNamespace().getURI())) return;
        if (element.getNamespace().getURI().isEmpty()) return;
        if (rootElement.getAdditionalNamespaces().contains(element.getNamespace())) return;
        Namespace additional = element.getNamespace();
        rootElement.addNamespaceDeclaration(additional);
    }

    public static void replaceNamespace(Element element, Namespace source, Namespace target) {
        if (target == null) return;
        if (element == null) return;
        if ((element.getNamespace() != null) && (element.getNamespace().equals(target))) return;
        if ((element.getNamespace() != null) && (!element.getNamespace().equals(source))) return;
        element.setNamespace(target);
        for (Object child : element.getChildren()) {
            if (child instanceof Element)
                replaceNamespace((Element) child, source, target);
        }
    }

    public static void replaceNoNamespace(Element element, Namespace namespace) {
        replaceNamespace(element, Namespace.NO_NAMESPACE, namespace);
    }

    /**
     * use {@link #setChildren(Element, String, String, Object[], ElementPersister)}
     */
    @Deprecated
    public static <E> void setSubChild(Element element, String sequenceName, String childName, E[] values,
                                       ElementPersister<E> persister) {
        setChildren(element, sequenceName, childName, values, persister);
    }

    /**
     * use {@link #setChildren(Element, String, String, Object[], ElementPersister)}
     */
    @Deprecated
    public static <E> void setChilds(Element element, String childName, E[] values, ElementPersister<E> persister) {
        setChildren(element, null, childName, values, persister);
    }

    /**
     * user {@link #setChildrenAsList(Element, String, String, List, ElementPersister)}
     */
    @Deprecated
    public static <E> void setChilds(Element element, String childName, List<E> values, ElementPersister<E> persister) {
        setChildrenAsList(element, null, childName, values, persister);
    }
}
