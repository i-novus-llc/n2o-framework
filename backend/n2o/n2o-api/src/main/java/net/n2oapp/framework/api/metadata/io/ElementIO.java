package net.n2oapp.framework.api.metadata.io;

import org.jdom.Element;

/**
 * Считываение и запись DOM элементов
 */
@FunctionalInterface
public interface ElementIO<T> {

    /**
     * Считывает или записывает DOM элементы
     * @param e   DOM элемент
     * @param m   модель
     * @param p   процессор считывания / записи
     */
    void io(Element e, T m, IOProcessor p);

}
