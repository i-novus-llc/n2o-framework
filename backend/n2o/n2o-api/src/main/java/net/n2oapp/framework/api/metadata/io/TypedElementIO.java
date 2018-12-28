package net.n2oapp.framework.api.metadata.io;

/**
 * Чтение / запись моделей определенного типа и элемента
 */
public interface TypedElementIO<T> extends ClassedElementIO<T>, NamedElementIO<T> {

}
