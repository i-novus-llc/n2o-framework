package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Процессор считывания и записи DOM элементов
 */
public interface IOProcessor {

    /**
     * Чтение элемента, если процессор - ридер
     *
     * @param element элемент
     * @param entity  сущность
     * @param reader  ридер
     * @param <T>     тип сущности
     */
    <T> void read(Element element, T entity, BiConsumer<Element, T> reader);

    /**
     * Запись сущности, если процессор - персистер
     *
     * @param entity    сущность, из которого будут собирать значения
     * @param element   элемент, в который будут записываться значения entity
     * @param persister персистер
     * @param <T>       тип сущности
     */
    <T> void persist(T entity, Element element, BiConsumer<T, Element> persister);

    /**
     * Считывание\запись дочернего элемента
     *
     * @param element   элемент
     * @param sequences имя дочернего элемента
     * @param childName имя дочернего элемента
     * @param getter    получение дочернего элемента
     * @param io        типизированная функция считывания\записи дочернего элемента
     * @param <T>       класс дочернего элемента
     */
    <T> void child(Element element, String sequences, String childName,
                   Supplier<? extends T> getter, Consumer<? super T> setter,
                   TypedElementIO<T> io);

    /**
     * Считывание\запись дочернего элемента
     *
     * @param element      элемент
     * @param sequences    имя дочернего элемента
     * @param childName    имя дочернего элемента
     * @param getter       получение дочернего элемента
     * @param io           функция считывания\записи дочернего элемента
     * @param elementClass класс дочернего элемента
     * @param <T>          тип дочернего элемента
     */
    <T> void child(Element element, String sequences, String childName,
                   Supplier<T> getter, Consumer<T> setter,
                   Class<T> elementClass, ElementIO<T> io);

    /**
     * Считывание\запись дочернего элемента
     *
     * @param element     элемент
     * @param sequences   имя дочернего элемента
     * @param childName   имя дочернего элемента
     * @param getter      получение дочернего элемента
     * @param io          функция считывания\записи дочернего элемента
     * @param newInstance функция создания дочернего элемента
     * @param <T>         тип дочернего элемента
     */
    <T> void child(Element element, String sequences, String childName,
                   Supplier<? extends T> getter, Consumer<? super T> setter,
                   Supplier<? extends T> newInstance, ElementIO<T> io);

    /**
     * Считывание\запись произвольного дочернего элемента из заранее заданного списка
     *
     * @param element   элемент
     * @param sequences имя дочернего элемента
     * @param getter    получение дочернего элемента
     * @param factory   билдер считывателей дочернего элемента
     * @param <T>       класс дочернего элемента
     */
    <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void anyChild(Element element, String sequences,
                                                                      Supplier<? extends T> getter, Consumer<? super T> setter,
                                                                      ElementIOFactory<T, R, P> factory);

    /**
     * Считывание\запись произвольного дочернего элемента по неймспейсу
     *
     * @param element   элемент
     * @param sequences имя дочернего элемента
     * @param getter    получение дочернего элемента
     * @param factory   фабрика считывателей дочернего элемента по неймспейсу
     * @param <T>       класс дочернего элемента
     */
    <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void anyChild(Element element, String sequences,
                                                                   Supplier<T> getter, Consumer<T> setter,
                                                                   NamespaceIOFactory<T, R, P> factory,
                                                                   Namespace defaultNamespace);

    /**
     * Считывание\запись списка дочерних элементов
     *
     * @param element      элемент
     * @param sequences    название списка
     * @param childrenName название элемента в списке
     * @param getter       получение списка дочерних элементов
     * @param setter       запись списка дочерних элементов
     * @param io           функция чтения\записи дочернего элемента
     * @param <T>          класс дочернего элемента
     */
    <T> void children(Element element, String sequences, String childrenName,
                      Supplier<T[]> getter, Consumer<T[]> setter,
                      TypedElementIO<T> io);

    /**
     * Считывание\запись списка дочерних элементов в <Map<String, Object>
     *
     * @param element      элемент
     * @param sequences    название списка
     * @param childrenName название элемента в списке
     * @param keyName      название атрибутта с ключом
     * @param valueName    название атрибута со значением, если null значит значение в содержимом элемента, а не в атрибуте
     * @param getter       получение списка дочерних элементов
     * @param setter       запись списка дочерних элементов
     */
    void childrenToMap(Element element, String sequences, String childrenName,
                       String keyName, String valueName,
                       Supplier<Map<String, Object>> getter, Consumer<Map<String, Object>> setter);

    /**
     * Считывание списка дочерних элементов в <Map<String, Object>
     *
     * @param element      элемент
     * @param sequences    название списка
     * @param childrenName название элемента в списке
     * @param getter       получение списка дочерних элементов
     * @param setter       запись списка дочерних элементов
     */
    void childrenToMap(Element element, String sequences, String childrenName,
                       Supplier<Map<String, Object>> getter, Consumer<Map<String, Object>> setter);

    /**
     * Считывание\запись списка дочерних элементов в <Map<String, String>
     *
     * @param element      элемент
     * @param sequences    название списка
     * @param childrenName название элемента в списке
     * @param keyName      название атрибутта с ключом
     * @param valueName    название атрибута со значением, если null значит значение в содержимом элемента, а не в атрибуте
     * @param getter       получение списка дочерних элементов
     * @param setter       запись списка дочерних элементов
     */
    void childrenToStringMap(Element element, String sequences, String childrenName,
                             String keyName, String valueName,
                             Supplier<Map<String, String>> getter, Consumer<Map<String, String>> setter);

    /**
     * Считывание\запись списка содержимого дочерних элементов в String[]
     * Пример
     * <values>
     * <value>test1</value>
     * <value>test2</value>
     * </values>
     * считывается в список [test1, test2]
     *
     * @param element      элемент
     * @param sequences    название списка
     * @param childrenName название элемента в списке
     * @param getter       получение списка дочерних элементов
     * @param setter       запись списка дочерних элементов
     */
    void childrenToStringArray(Element element, String sequences, String childrenName,
                               Supplier<String[]> getter, Consumer<String[]> setter);

    /**
     * Считывание\запись списка дочерних элементов
     *
     * @param element      элемент
     * @param sequences    название списка
     * @param childrenName название элемента в списке
     * @param getter       получение списка дочерних элементов
     * @param setter       запись списка дочерних элементов
     * @param io           функция чтения\записи дочернего элемента
     * @param elementClass тип сущности
     * @param <T>          класс дочернего элемента
     * @see #children(Element, String, String, Supplier, Consumer, TypedElementIO)
     */
    <T> void children(Element element, String sequences, String childrenName,
                      Supplier<T[]> getter, Consumer<T[]> setter,
                      Class<T> elementClass, ElementIO<T> io);

    /**
     * Считывание\запись списка дочерних элементов
     *
     * @param element      элемент
     * @param sequences    название списка
     * @param childrenName название элемента в списке
     * @param getter       получение списка дочерних элементов
     * @param setter       запись списка дочерних элементов
     * @param io           функция чтения\записи дочернего элемента
     * @param newInstance  функция создания сущности дочернего элемента
     * @param <T>          класс дочернего элемента
     * @see #children(Element, String, String, Supplier, Consumer, TypedElementIO)
     */
    <T> void children(Element element, String sequences, String childrenName,
                      Supplier<T[]> getter, Consumer<T[]> setter,
                      Supplier<T> newInstance, ElementIO<T> io);

    /**
     * Считывание\запись списка любых заранее заданных дочерних элементов
     *
     * @param <T>       класс дочернего элемента
     * @param element   элемент
     * @param sequences название списка
     * @param getter    получение списка дочерних элементов
     * @param setter    запись списка дочерних элементов
     * @param factory   заранее заданные считыватели элементов дочернего списка
     */
    <T, R extends TypedElementReader<? extends T>, P extends TypedElementPersister<? super T>> void anyChildren(Element element, String sequences,
                                                                                                                Supplier<T[]> getter, Consumer<T[]> setter,
                                                                                                                ElementIOFactory<T, R, P> factory);

    /**
     * Считывание\запись списка любых дочерних элементов по неймспейсу
     *
     * @param <T>              класс дочернего элемента
     * @param element          элемент
     * @param sequences        название списка
     * @param getter           получение списка дочерних элементов
     * @param setter           запись списка дочерних элементов
     * @param factory          фабрика считыввателей дочерних элементов по неймспейсу
     * @param defaultNamespace неймспейс фабрики по умолчанию
     */
    <T extends NamespaceUriAware, R extends NamespaceReader<? extends T>, P extends NamespacePersister<? super T>> void anyChildren(Element element, String sequences,
                                                                                                                                    Supplier<T[]> getter, Consumer<T[]> setter,
                                                                                                                                    NamespaceIOFactory<T, R, P> factory,
                                                                                                                                    Namespace... defaultNamespace);

    /**
     * Считывание\запись списка дочерних элементов соответсвующих enum
     *
     * @param element    элемент
     * @param sequences  название списка
     * @param getterList получение списка дочерних элементов
     * @param setterList запись списка дочерних элементов
     * @param enumClass  класс Enum
     * @param getterEnum получение элемента enum
     * @param setterEnum запись enum
     * @param io         функция чтения\записи дочернего элемента
     * @param <T>        класс дочернего элемента
     * @param <E>        класс enum
     */
    <T, E extends Enum<E>> void childrenByEnum(Element element, String sequences,
                                               Supplier<T[]> getterList, Consumer<T[]> setterList,
                                               Class<E> enumClass, Function<T, E> getterEnum, BiConsumer<T, E> setterEnum,
                                               ClassedElementIO<T> io);

    /**
     * Считывание\запись списка дочерних элементов соответсвующих enum
     *
     * @param element     элемент
     * @param sequences   название списка
     * @param getterList  получение списка дочерних элементов
     * @param setterList  запись списка дочерних элементов
     * @param enumClass   класс Enum
     * @param getterEnum  получение элемента enum
     * @param setterEnum  запись enum
     * @param newInstance функция создания сущности дочернего элемента
     * @param io          функция чтения\записи дочернего элемента
     * @param <T>         класс дочернего элемента
     * @param <E>         класс enum
     */
    @SuppressWarnings("unchecked")
    <T, E extends Enum<E>> void childrenByEnum(Element element, String sequences,
                                               Supplier<T[]> getterList, Consumer<T[]> setterList,
                                               Function<T, E> getterEnum, BiConsumer<T, E> setterEnum,
                                               Supplier<T> newInstance, Class<E> enumClass, ElementIO<T> io);

    /**
     * Считывание\запись атрибута с типом строка
     *
     * @param element элемент
     * @param name    имя атрибута
     * @param getter  получение атрибута
     * @param setter  запись атрибута
     */
    void attribute(Element element, String name, Supplier<String> getter, Consumer<String> setter);

    /**
     * Считывание\запись внутреннего текста
     *
     * @param element элемент
     * @param getter  получение текста
     * @param setter  запись текста
     */
    void text(Element element, Supplier<String> getter, Consumer<String> setter);

    /**
     * Считывание\запись внутреннего текста
     *
     * @param element элемент
     * @param getter  получение текста
     * @param setter  запись текста
     */
    void childrenText(Element element, String childrenName, Supplier<String> getter, Consumer<String> setter);

    /**
     * Считывание\запись атрибута у дочернего элемента
     *
     * @param element   элемент
     * @param childName имя дочернего элемента
     * @param name      имя атрибута
     * @param getter    получение атрибута
     * @param setter    запись атрибута
     */
    void childAttribute(Element element, String childName, String name, Supplier<String> getter, Consumer<String> setter);

    /**
     * Считывание\запись атрибута у дочернего элемента типа Boolean
     *
     * @param element   элемент
     * @param childName имя дочернего элемента
     * @param name      имя атрибута
     * @param getter    получение атрибута
     * @param setter    запись атрибута
     */
    void childAttributeBoolean(Element element, String childName, String name, Supplier<Boolean> getter, Consumer<Boolean> setter);

    /**
     * Считывание\запись атрибута у дочернего элемента типа Integer
     *
     * @param element   элемент
     * @param childName имя дочернего элемента
     * @param name      имя атрибута
     * @param getter    получение атрибута
     * @param setter    запись атрибута
     */
    void childAttributeInteger(Element element, String childName, String name, Supplier<Integer> getter, Consumer<Integer> setter);

    /**
     * Считывание\запись атрибута у дочернего элемента
     *
     * @param element   элемент
     * @param childName имя дочернего элемента
     * @param name      имя атрибута
     * @param getter    получение атрибута
     * @param setter    запись атрибута
     */
    <T extends Enum<T>> void childAttributeEnum(Element element, String childName, String name, Supplier<T> getter, Consumer<T> setter, Class<T> enumClass);

    /**
     * Считывание / запись всех атрибутов определенной схемы в мапу
     *
     * @param element   элемент
     * @param namespace схема
     * @param map       мапа
     */
    void otherAttributes(Element element, Namespace namespace, Map<String, String> map);

    /**
     * Считывание / запись любых атрибутов с внешней схемой из дочернего элемента
     *
     * @param element   элемент
     * @param childName Имя дочернего элемента
     * @param getter    получение аттрибутов
     * @param setter    запись аттрибутов
     */
    void childAnyAttributes(Element element, String childName, Supplier<Map<N2oNamespace, Map<String, String>>> getter,
                            Consumer<Map<N2oNamespace, Map<String, String>>> setter);

    /**
     * Считывание / запись любых атрибутов с внешней схемой
     *
     * @param element элемент
     * @param getter  получение аттрибутов
     * @param setter  запись аттрибутов
     */
    void anyAttributes(Element element, Supplier<Map<N2oNamespace, Map<String, String>>> getter,
                       Consumer<Map<N2oNamespace, Map<String, String>>> setter);

    /**
     * Считывание / запись дополнительных атрибутов(те, у которых namespace отличается от namespace элемента)
     *
     * @param element элемент
     * @param getter  получение доп.аттрибутов
     */
    @Deprecated
    default void extensionAttributes(Element element, Supplier<Map<N2oNamespace, Map<String, String>>> getter,
                                     Consumer<Map<N2oNamespace, Map<String, String>>> setter) {
        anyAttributes(element, getter, setter);
    }

    /**
     * Считывание\запись атрибута с типом boolean
     *
     * @param element элемент
     * @param name    имя атрибута
     * @param getter  получение атрибута
     * @param setter  запись атрибута
     */
    void attributeBoolean(Element element, String name, Supplier<Boolean> getter, Consumer<Boolean> setter);

    /**
     * Считывание\запись атрибута с типом integer
     *
     * @param element элемент
     * @param name    имя атрибута
     * @param getter  получение атрибута
     * @param setter  запись атрибута
     */
    void attributeInteger(Element element, String name, Supplier<Integer> getter, Consumer<Integer> setter);

    /**
     * Считывание\запись атрибута с типом integer
     *
     * @param element   элемент
     * @param name      имя атрибута
     * @param separator знак разделителя
     * @param getter    получение атрибута
     * @param setter    запись атрибута
     */
    void attributeArray(Element element, String name, String separator, Supplier<String[]> getter, Consumer<String[]> setter);

    /**
     * Считывание\запись атрибута enum
     *
     * @param element   элемент
     * @param name      имя атрибута
     * @param getter    получение атрибута
     * @param setter    запись атрибута
     * @param enumClass класс enum
     */
    <T extends Enum<T>> void attributeEnum(Element element, String name, Supplier<T> getter, Consumer<T> setter, Class<T> enumClass);

    /**
     * Считывание\запись строкового элемента
     *
     * @param element элемент
     * @param name    имя атрибута
     * @param getter  получение атрибута
     * @param setter  запись атрибута
     */
    void element(Element element, String name, Supplier<String> getter, Consumer<String> setter);

    /**
     * Считывание\запись наличия элемента
     *
     * @param element элемент
     * @param name    имя атрибута
     * @param getter  получение атрибута
     * @param setter  запись атрибута
     */
    void hasElement(Element element, String name, Supplier<Boolean> getter, Consumer<Boolean> setter);

    /**
     * Билдер считывателей элементов определенного базового класса
     *
     * @param baseElementClass базовый класс
     * @param <T>              тип элементов
     * @return фабрика
     */
    <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> ElementIOFactory<T, R, P> oneOf(Class<T> baseElementClass);

    /**
     * Фабрика считывателей элементов по неймспейсу определенного базового класса
     *
     * @param baseElementClass базовый класс
     * @param <T>              тип элементов
     * @return фабрика
     */
    <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> NamespaceIOFactory<T, R, P> anyOf(Class<T> baseElementClass);


    /**
     * Фабрика считывателей элементов по неймспейсу
     *
     * @param <T> тип элементов
     * @return фабрика
     */
    <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> NamespaceIOFactory<T, R, P> anyOf();


}

