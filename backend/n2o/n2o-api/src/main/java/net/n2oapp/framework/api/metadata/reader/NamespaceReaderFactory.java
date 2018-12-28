package net.n2oapp.framework.api.metadata.reader;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Фабрика ридеров порожденных по неймспейсу
 * @param <T> Тип моделей
 * @param <R> Тип ридеров
 */
public interface NamespaceReaderFactory<T extends NamespaceUriAware, R extends NamespaceReader<? extends T>> extends ElementReaderFactory<T, R> {

    R produce(Namespace namespace, String elementName);

    default R produce(Element element) {
        return produce(element.getNamespace(), element.getName());
    }

    default R produce(Element element, Namespace parentNamespace, Namespace defaultNamespace){
        if (defaultNamespace != null && element.getNamespace().getURI().equals(parentNamespace.getURI())) {
            return produce(defaultNamespace, element.getName());
        } else {
            return produce(element);
        }
    }

    void add(NamespaceReader<T> reader);

}
