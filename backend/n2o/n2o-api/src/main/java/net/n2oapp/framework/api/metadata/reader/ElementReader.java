package net.n2oapp.framework.api.metadata.reader;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Считывание элемента в сущность
 */
@FunctionalInterface
public interface ElementReader<T> {

    /**
     * Считывает элемент xml в entity
     * @param element элемент, из которого нужно прочесть значения
     * @return сущность, в которую нужно заполнить значения из element
     */
    T read(Element element);

}
