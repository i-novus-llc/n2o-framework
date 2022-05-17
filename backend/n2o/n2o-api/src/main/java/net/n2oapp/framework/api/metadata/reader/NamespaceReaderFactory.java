package net.n2oapp.framework.api.metadata.reader;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import org.jdom2.Element;
import org.jdom2.Namespace;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

/**
 * Фабрика ридеров порожденных по неймспейсу
 *
 * @param <T> Тип моделей
 * @param <R> Тип ридеров
 */
public interface NamespaceReaderFactory<T extends NamespaceUriAware, R extends NamespaceReader<? extends T>> extends ElementReaderFactory<T, R> {

    R produce(String elementName, Namespace... namespaces);

    default R produce(Element element) {
        return produce(element.getName(), element.getNamespace());
    }

    default R produce(Element element, Namespace parentNamespace, Namespace... defaultNamespaces) {
        String parentNameSpacePrefix = nonNull(element.getParentElement()) ? element.getParentElement().getNamespacePrefix() : null;
        if (defaultNamespaces != null && (hasText(parentNameSpacePrefix) || element.getNamespace().getURI().equals(parentNamespace.getURI()))) {
            return produce(element.getName(), defaultNamespaces);
        } else {
            return produce(element);
        }
    }

    void add(NamespaceReader<T> reader);
}
