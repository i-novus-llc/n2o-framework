package net.n2oapp.framework.config.io;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import net.n2oapp.framework.api.metadata.io.*;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.merge.MergeUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.StringUtils.removeSpaces;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 * Реализация процессора считывания и записи DOM элементов
 */
@Slf4j
public class IOProcessorImpl implements IOProcessor {

    /**
     * Если true, то чтение, false запись
     */
    @Getter
    private final boolean r;
    private NamespaceReaderFactory readerFactory;
    private NamespacePersisterFactory persisterFactory;
    private MessageSourceAccessor messageSourceAccessor;
    private PropertyResolver systemProperties;
    private boolean failFast = true;
    private final DomainProcessor domainProcessor = new DomainProcessor();
    private static final String EXCEPTION_MESSAGE = "you should first call #newInstance(Element)";
    private static final Pattern XML_ESCAPE_SYMBOLS = Pattern.compile("[&<>'\"]");

    private MetadataEnvironment environment;

    public IOProcessorImpl(boolean read) {
        this.r = read;
    }

    public IOProcessorImpl(NamespaceReaderFactory readerFactory, MetadataEnvironment environment) {
        this.r = true;
        this.environment = environment;
        this.readerFactory = readerFactory;
        injectIOProcessor(readerFactory);
    }

    public IOProcessorImpl(NamespacePersisterFactory persisterFactory, MetadataEnvironment environment) {
        this(persisterFactory);
        this.environment = environment;
    }

    public IOProcessorImpl(NamespacePersisterFactory persisterFactory) {
        this.r = false;
        this.persisterFactory = persisterFactory;
        injectIOProcessor(persisterFactory);
    }

    @Override
    public <T> void read(Element element, T entity, BiConsumer<Element, T> reader) {
        if (r) {
            reader.accept(element, entity);
        }
    }

    @Override
    public <T> void persist(T entity, Element element, BiConsumer<T, Element> persister) {
        if (!r) {
            persister.accept(entity, element);
        }
    }

    @Override
    public <T> void child(Element element, String sequences, String childName,
                          Supplier<? extends T> getter, Consumer<? super T> setter,
                          TypedElementIO<T> io) {
        if (r) {
            Element seqE = findSequenceElement(element, sequences);
            if (seqE == null) return;
            Element childE = seqE.getChild(childName, element.getNamespace());
            if (childE != null)
                setter.accept(read(io, childE));
        } else {
            T entity = getter.get();
            if (entity == null) return;
            Element seqE = persistSequences(element, sequences);
            addChildElement(seqE, persist(io, entity, element.getNamespace()), element.getNamespace());
        }
    }

    @Override
    public <T> void child(Element element, String sequences, String childName,
                          Supplier<T> getter, Consumer<T> setter,
                          Class<T> elementClass, ElementIO<T> io) {
        child(element, sequences, childName, getter, setter, typedIO(childName, elementClass, io));
    }

    @Override
    public <T> void child(Element element, String sequences, String childName,
                          Supplier<? extends T> getter, Consumer<? super T> setter,
                          Supplier<? extends T> newInstance, ElementIO<T> io) {
        child(element, sequences, childName, getter, setter, typedIO(childName, newInstance, io));
    }

    @Override
    public <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void anyChild(Element element, String sequences,
                                                                      Supplier<? extends T> getter, Consumer<? super T> setter,
                                                                      ElementIOFactory<T, R, P> factory) {
        if (r)
            readAnyChild(element, sequences, setter, factory);
        else
            persistAnyChild(element, sequences, getter, factory);
    }

    private <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void readAnyChild(Element element, String sequences, Consumer<? super T> setter,
                      ElementIOFactory<T, R, P> factory) {
        Element seqE = findSequenceElement(element, sequences);
        if (seqE == null) return;
        for (Object child : seqE.getChildren()) {
            Element childE = (Element) child;
            setter.accept(read(factory, childE));
        }
    }

    private <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void persistAnyChild(Element element, String sequences, Supplier<? extends T> getter,
                         ElementIOFactory<T, R, P> factory) {
        T entity = getter.get();
        if (entity == null) return;
        Element seqE = persistSequences(element, sequences);
        addChildElement(seqE, persist(factory, entity, element.getNamespace()), element.getNamespace());
    }

    @Override
    public <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void anyChild(Element element, String sequences,
                                                                   Supplier<T> getter, Consumer<T> setter,
                                                                   NamespaceIOFactory<T, R, P> factory,
                                                                   Namespace defaultNamespace) {
        if (r)
            readAnyChild(element, sequences, setter, factory, defaultNamespace);
        else
            persistAnyChild(element, sequences, getter, factory, defaultNamespace);
    }

    private <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void readAnyChild(Element element, String sequences, Consumer<T> setter,
                      NamespaceIOFactory<T, R, P> factory, Namespace defaultNamespace) {
        Element seqE = findSequenceElement(element, sequences);
        if (seqE == null) return;
        for (Object child : seqE.getChildren()) {
            T childT = read(factory, (Element) child, seqE.getNamespace(), defaultNamespace);
            if (childT != null) {
                setter.accept(childT);
                return;
            }
        }
    }

    private <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void persistAnyChild(Element element, String sequences, Supplier<T> getter,
                         NamespaceIOFactory<T, R, P> factory, Namespace defaultNamespace) {
        T entity = getter.get();
        if (entity == null) return;
        Element seqE = persistSequences(element, sequences);
        addChildElement(seqE, persist(factory, entity, seqE.getNamespace(), defaultNamespace), element.getNamespace());
    }

    @Override
    public <T> void children(Element element, String sequences, String childrenName,
                             Supplier<T[]> getter, Consumer<T[]> setter,
                             TypedElementIO<T> io) {
        if (r)
            readChildrenToArray(element, sequences, childrenName, childE -> read(io, childE), io::getElementClass, setter);
        else
            persistChildren(element, sequences, getter, io);
    }

    private <T> void persistChildren(Element element, String sequences, Supplier<T[]> getter,
                                     TypedElementIO<T> io) {
        T[] entity = getter.get();
        if (entity == null) return;
        Element seqE = persistSequences(element, sequences);
        for (T child : entity) {
            addChildElement(seqE, persist(io, child, seqE.getNamespace()), seqE.getNamespace());
        }
    }

    @Override
    public void childrenToMap(Element element, String sequences, String childrenName,
                              String keyName, String valueName,
                              Supplier<Map<String, Object>> getter, Consumer<Map<String, Object>> setter) {
        if (r)
            readChildrenToMap(element, sequences, childrenName,
                    childE -> process(childE.getAttribute(keyName).getValue()),
                    childE -> domainProcessor.deserialize(getChildValue(childE, valueName), (String) null),
                    setter);
        else
            persistChildrenMap(element, sequences, childrenName, keyName, valueName, getter);
    }

    @Override
    public void childrenAttributesToMap(Element element, String sequences, String childrenName,
                                        Supplier<Map<String, Object>> getter, Consumer<Map<String, Object>> setter) {
        if (r)
            readChildrenToMap(element, sequences, childrenName,
                    childE -> getFirstAttribute(childE).getName(),
                    childE -> domainProcessor.deserialize(getFirstAttribute(childE).getValue()),
                    setter);
        else
            persistChildrenAttributesToMap(element, sequences, childrenName, getter);
    }

    /**
     * Получить первый атрибут элемента
     *
     * @throws N2oMetadataValidationException если у элемента нет атрибутов
     */
    private Attribute getFirstAttribute(Element childE) {
        List<Attribute> attributes = childE.getAttributes();
        if (attributes.isEmpty())
            throw new N2oMetadataValidationException(
                    String.format("Элемент \"<%s>\" не содержит атрибутов. Ожидается хотя бы один атрибут для сопоставления ключ-значение.",
                            childE.getName())
            );
        return attributes.get(0);
    }

    private void persistChildrenAttributesToMap(Element element, String sequences, String childrenName,
                                                Supplier<Map<String, Object>> getter) {
        Map<String, Object> values = getter.get();
        if (values == null) return;
        Element seqE = persistSequences(element, sequences);
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            Element childE = new Element(childrenName, element.getNamespace());
            childE.setAttribute(entry.getKey(), entry.getValue().toString());
            seqE.addContent(childE);
        }
    }

    @Override
    public void childrenToStringMap(Element element, String sequences, String childrenName,
                                    String keyName, String valueName,
                                    Supplier<Map<String, String>> getter, Consumer<Map<String, String>> setter) {
        if (r)
            readChildrenToMap(element, sequences, childrenName,
                    childE -> {
                        Attribute keyAttr = childE.getAttribute(keyName);
                        return keyAttr != null ? process(keyAttr.getValue()) : null;
                    },
                    childE -> getChildValue(childE, valueName),
                    setter);
        else
            persistChildrenMap(element, sequences, childrenName, keyName, valueName, getter);
    }

    @Override
    public void childrenToStringArray(Element element, String sequences, String childrenName,
                                      Supplier<String[]> getter, Consumer<String[]> setter) {
        if (r)
            readChildrenToArray(element, sequences, childrenName, childE -> process(childE.getValue()), () -> String.class, setter);
        else
            persistChildrenToStringArray(element, sequences, childrenName, getter);
    }

    /**
     * Считать дочерние элементы childrenName из обертки sequences в массив.
     * Если обертки нет или дочерних элементов нет, setter не вызывается
     *
     * @param mapper       преобразование дочернего элемента в значение
     * @param elementClass класс элементов массива (запрашивается после чтения всех дочерних элементов)
     */
    private <T> void readChildrenToArray(Element element, String sequences, String childrenName,
                                         Function<Element, T> mapper, Supplier<Class<?>> elementClass,
                                         Consumer<T[]> setter) {
        Element seqE = findSequenceElement(element, sequences);
        if (seqE == null) return;
        List<T> result = new ArrayList<>();
        for (Element childE : seqE.getChildren(childrenName, seqE.getNamespace()))
            result.add(mapper.apply(childE));
        if (!result.isEmpty())
            setter.accept(toArray(result, elementClass.get()));
    }

    /**
     * Считать дочерние элементы childrenName из обертки sequences в мапу.
     * Если обертки нет, setter не вызывается
     *
     * @param keyMapper   получение ключа из дочернего элемента
     * @param valueMapper получение значения из дочернего элемента
     */
    private <V> void readChildrenToMap(Element element, String sequences, String childrenName,
                                       Function<Element, String> keyMapper, Function<Element, V> valueMapper,
                                       Consumer<Map<String, V>> setter) {
        Element seqE = findSequenceElement(element, sequences);
        if (seqE == null) return;
        Map<String, V> result = new HashMap<>();
        for (Element childE : seqE.getChildren(childrenName, seqE.getNamespace()))
            result.put(keyMapper.apply(childE), valueMapper.apply(childE));
        setter.accept(result);
    }

    private void persistChildrenToStringArray(Element element, String sequences, String childrenName,
                                              Supplier<String[]> getter) {
        String[] values = getter.get();
        if (values == null) return;
        Element seqE = persistSequences(element, sequences);
        for (String k : values) {
            Element childE = new Element(childrenName, element.getNamespace());
            childE.setText(k);
            seqE.addContent(childE);
        }
    }

    /**
     * Получить (создав при необходимости) элемент-обертку sequences, либо вернуть сам element, если sequences не задан
     */
    private Element persistSequences(Element element, String sequences) {
        return sequences != null ? getOrCreateChild(element, sequences) : element;
    }

    private <T> void persistChildrenMap(Element element, String sequences, String childrenName,
                                        String keyName, String valueName,
                                        Supplier<Map<String, T>> getter) {
        Map<String, T> values = getter.get();
        if (values == null) return;
        Element seqE = persistSequences(element, sequences);
        for (Map.Entry<String, T> entry : values.entrySet()) {
            Element childE = new Element(childrenName, element.getNamespace());
            childE.setAttribute(keyName, entry.getKey());
            if (valueName == null) {
                childE.setText(entry.getValue().toString());
            } else if (entry.getValue() != null) {
                childE.setAttribute(valueName, entry.getValue().toString());
            }
            seqE.addContent(childE);
        }
    }

    @Override
    public <T> void children(Element element, String sequences, String childrenName,
                             Supplier<T[]> getter, Consumer<T[]> setter,
                             Class<T> elementClass, ElementIO<T> io) {
        children(element, sequences, childrenName, getter, setter, typedIO(childrenName, elementClass, io));
    }

    @Override
    public <T> void children(Element element, String sequences, String childrenName,
                             Supplier<T[]> getter, Consumer<T[]> setter,
                             Supplier<T> newInstance, ElementIO<T> io) {
        children(element, sequences, childrenName, getter, setter, typedIO(childrenName, newInstance, io));
    }

    /**
     * Обертка над io с заданными именем и классом элемента
     */
    private <T> TypedElementIO<T> typedIO(String elementName, Class<T> elementClass, ElementIO<T> io) {
        return new TypedElementIO<>() {
            @Override
            public String getElementName() {
                return elementName;
            }

            @Override
            public Class<T> getElementClass() {
                return elementClass;
            }

            @Override
            public void io(Element e, T t, IOProcessor p) {
                io.io(e, t, p);
            }
        };
    }

    /**
     * Обертка над io с заданным именем элемента, класс которого определяется по первому созданному экземпляру
     */
    private <T> TypedElementIO<T> typedIO(String elementName, Supplier<? extends T> newInstance, ElementIO<T> io) {
        return new TypedElementIO<>() {
            private Class<T> elementClass;

            @Override
            public String getElementName() {
                return elementName;
            }

            @Override
            public Class<T> getElementClass() {
                if (elementClass == null)
                    throw new IllegalStateException(EXCEPTION_MESSAGE);
                return elementClass;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T newInstance(Element element) {
                T entity = newInstance.get();
                if (elementClass == null)
                    elementClass = (Class<T>) entity.getClass();
                return entity;
            }

            @Override
            public void io(Element e, T t, IOProcessor p) {
                io.io(e, t, p);
            }
        };
    }

    @Override
    public <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void anyChildren(Element element, String sequences,
                                                                         Supplier<T[]> getter, Consumer<T[]> setter,
                                                                         ElementIOFactory<T, R, P> factory) {
        if (r)
            readAnyChildren(element, sequences, setter, factory);
        else
            persistAnyChildren(element, sequences, getter, factory);
    }

    private <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void readAnyChildren(Element element, String sequences, Consumer<T[]> setter,
                                                                             ElementIOFactory<T, R, P> factory) {
        Element seqE = findSequenceElement(element, sequences);
        if (seqE == null) return;
        List<T> result = new ArrayList<>();
        for (Object child : seqE.getChildren()) {
            T childT = read(factory, (Element) child);
            if (childT != null)
                result.add(childT);
        }
        setter.accept(toArray(result, factory.getBaseElementClass()));
    }

    private <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void persistAnyChildren(Element element, String sequences, Supplier<T[]> getter,
                                                                                ElementIOFactory<T, R, P> factory) {
        T[] entities = getter.get();
        if (entities == null) return;
        Element seqE = persistSequences(element, sequences);
        for (T child : entities) {
            addChildElement(seqE, persist(factory, child, element.getNamespace()), element.getNamespace());
        }
    }

    @Override
    public <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void anyChildren(Element element, String sequences,
                                                                      Supplier<T[]> getter, Consumer<T[]> setter,
                                                                      NamespaceIOFactory<T, R, P> factory,
                                                                      Namespace... defaultNamespace) {
        if (r)
            readAnyChildren(element, sequences, setter, factory, defaultNamespace);
        else
            persistAnyChildren(element, sequences, getter, factory, defaultNamespace);
    }

    private <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void readAnyChildren(Element element, String sequences, Consumer<T[]> setter,
                                                                          NamespaceIOFactory<T, R, P> factory,
                                                                          Namespace... defaultNamespace) {
        Element seqE = findSequenceElement(element, sequences);
        if (seqE == null) return;
        List<T> result = new ArrayList<>();
        for (Object child : seqE.getChildren()) {
            T childT = read(factory, (Element) child, seqE.getNamespace(), defaultNamespace);
            if (childT != null)
                result.add(childT);
        }
        if (!result.isEmpty()) {
            Class<?> elementClass = factory.getBaseElementClass() != null
                    ? factory.getBaseElementClass()
                    : NamespaceUriAware.class;
            setter.accept(toArray(result, elementClass));
        }
    }

    private <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void persistAnyChildren(Element element, String sequences, Supplier<T[]> getter,
                                                                             NamespaceIOFactory<T, R, P> factory,
                                                                             Namespace... defaultNamespace) {
        T[] entities = getter.get();
        if (entities == null) return;
        Element seqE = persistSequences(element, sequences);
        for (T child : entities) {
            addChildElement(seqE, persist(factory, child, seqE.getNamespace(), defaultNamespace), element.getNamespace());
        }
    }

    @Override
    public <T, E extends Enum<E>> void childrenByEnum(Element element, String sequences,
                                                      Supplier<T[]> getterList, Consumer<T[]> setterList,
                                                      Class<E> enumClass, Function<T, E> getterEnum, BiConsumer<T, E> setterEnum,
                                                      ClassedElementIO<T> io) {
        if (r)
            readChildrenByEnum(element, sequences, setterList, enumClass, setterEnum, io);
        else
            persistChildrenByEnum(element, sequences, getterList, getterEnum, io);
    }

    private <T, E extends Enum<E>> void readChildrenByEnum(Element element, String sequences, Consumer<T[]> setterList,
                                                           Class<E> enumClass, BiConsumer<T, E> setterEnum,
                                                           ClassedElementIO<T> io) {
        Element seqE = findSequenceElement(element, sequences);
        if (seqE == null) return;
        List<T> result = new ArrayList<>();
        for (Object child : seqE.getChildren()) {
            Element childE = (Element) child;
            E en = stringToEnum(childE.getName(), enumClass);
            if (en != null) {
                T entity = read(io, childE);
                setterEnum.accept(entity, en);
                result.add(entity);
            }
        }
        if (!result.isEmpty())
            setterList.accept(toArray(result, io.getElementClass()));
    }

    private <T, E extends Enum<E>> void persistChildrenByEnum(Element element, String sequences, Supplier<T[]> getterList,
                                                              Function<T, E> getterEnum, ClassedElementIO<T> io) {
        T[] entity = getterList.get();
        if (entity == null || entity.length == 0) return;
        Element seqE = persistSequences(element, sequences);
        for (T child : entity) {
            E en = getterEnum.apply(child);
            if (en != null)
                addChildElement(seqE, persist(enumNamedIO(en, io), child, seqE.getNamespace()), element.getNamespace());
        }
    }

    /**
     * Обертка над io, у которой имя элемента берется из значения перечисления
     */
    private <T> NamedElementIO<T> enumNamedIO(Enum<?> en, ElementIO<T> io) {
        return new NamedElementIO<>() {
            @Override
            public String getElementName() {
                return enumToString(en);
            }

            @Override
            public void io(Element e, T t, IOProcessor p) {
                io.io(e, t, p);
            }
        };
    }

    @Override
    public <T, E extends Enum<E>> void childrenByEnum(Element element, String sequences,
                                                      Supplier<T[]> getterList, Consumer<T[]> setterList,
                                                      Function<T, E> getterEnum, BiConsumer<T, E> setterEnum,
                                                      Supplier<T> newInstance, Class<E> enumClass, ElementIO<T> io) {
        childrenByEnum(element, sequences, getterList, setterList, enumClass, getterEnum, setterEnum,
                typedIO(null, newInstance, io));
    }

    @Override
    public void attribute(Element element, String name, Supplier<String> getter, Consumer<String> setter) {
        typedAttribute(element, name, getter, setter, Function.identity(), Function.identity());
    }

    @Override
    public void text(Element element, Supplier<String> getter, Consumer<String> setter) {
        text(element, getter, setter, s -> removeSpaces(process(s)));
    }

    @Override
    public void childrenText(Element element, String childName, Supplier<String> getter, Consumer<String> setter) {
        childrenText(element, childName, getter, setter, s -> removeSpaces(process(s)));
    }

    @Override
    public void originalText(Element element, Supplier<String> getter, Consumer<String> setter) {
        text(element, getter, setter, this::process);
    }

    @Override
    public void childrenOriginalText(Element element, String childName, Supplier<String> getter, Consumer<String> setter) {
        childrenText(element, childName, getter, setter, this::process);
    }

    @Override
    public void childAttribute(Element element, String childName, String name, Supplier<String> getter, Consumer<String> setter) {
        typedChildAttribute(element, childName, name, getter, setter, Function.identity(), Function.identity());
    }

    @Override
    public void childAttributeBoolean(Element element, String childName, String name,
                                      Supplier<Boolean> getter, Consumer<Boolean> setter) {
        typedChildAttribute(element, childName, name, getter, setter, Boolean::valueOf, Object::toString);
    }

    @Override
    public void childAttributeInteger(Element element, String childName, String name, Supplier<Integer> getter, Consumer<Integer> setter) {
        typedChildAttribute(element, childName, name, getter, setter, Integer::parseInt, Object::toString);
    }

    @Override
    public <T extends Enum<T>> void childAttributeEnum(Element element, String childName, String
            name, Supplier<T> getter, Consumer<T> setter, Class<T> enumClass) {
        typedChildAttribute(element, childName, name, getter, setter,
                value -> stringToEnum(value, enumClass), IOProcessorImpl::enumToString);
    }

    /**
     * Считать или записать атрибут элемента
     *
     * @param fromString преобразование значения атрибута (после разрешения плейсхолдеров) в значение
     * @param toString   преобразование значения в строку атрибута
     */
    private <T> void typedAttribute(Element element, String name,
                                    Supplier<T> getter, Consumer<T> setter,
                                    Function<String, T> fromString, Function<T, String> toString) {
        if (r) {
            Attribute attribute = element.getAttribute(name);
            if (attribute != null)
                setter.accept(fromString.apply(process(attribute.getValue())));
        } else {
            T value = getter.get();
            if (value == null) return;
            element.setAttribute(new Attribute(name, toString.apply(value)));
        }
    }

    /**
     * Считать или записать атрибут дочернего элемента. При записи дочерний элемент создается, если его нет
     */
    private <T> void typedChildAttribute(Element element, String childName, String name,
                                         Supplier<T> getter, Consumer<T> setter,
                                         Function<String, T> fromString, Function<T, String> toString) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child != null)
                typedAttribute(child, name, getter, setter, fromString, toString);
        } else {
            if (getter.get() == null) return;
            typedAttribute(getOrCreateChild(element, childName), name, getter, setter, fromString, toString);
        }
    }

    /**
     * Считать атрибуты другой схемы
     *
     * @param element   элемент
     * @param namespace схема, атрибуты которой нужно считать
     * @param map       мапа, в которую считать атрибуты схемы
     */
    @Override
    public void otherAttributes(Element element, Namespace namespace, Map<String, String> map) {
        if (r) {
            for (Object o : element.getAttributes()) {
                Attribute attribute = (Attribute) o;
                if (attribute.getNamespace().equals(namespace)) {
                    if (attribute.getValue() instanceof String) {
                        map.put(attribute.getName(), process(attribute.getValue()));
                    } else {
                        map.put(attribute.getName(), attribute.getValue());
                    }
                }
            }
        } else {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                element.setAttribute(new Attribute(entry.getKey(), entry.getValue(), namespace));
            }
        }
    }

    @Override
    public void childAnyAttributes(Element element, String
            childName, Supplier<Map<N2oNamespace, Map<String, String>>> getter, Consumer<Map<N2oNamespace, Map<String, String>>> setter) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child == null) return;
            anyAttributes(child, getter, setter);
        } else {
            if (getter.get() == null) return;
            anyAttributes(getOrCreateChild(element, childName), getter, setter);
        }
    }

    @Override
    public void anyAttributes(Element element, Supplier<Map<N2oNamespace, Map<String, String>>> getter,
                              Consumer<Map<N2oNamespace, Map<String, String>>> setter) {
        if (r) {
            N2oNamespace elementNamespace = new N2oNamespace(element.getNamespace());
            Map<N2oNamespace, Map<String, String>> extensions = new HashMap<>();
            for (Attribute attribute : element.getAttributes()) {
                if (!attribute.getNamespaceURI().isEmpty() && !elementNamespace.getUri().equals(attribute.getNamespaceURI())) {
                    N2oNamespace namespace = new N2oNamespace(attribute.getNamespace());
                    extensions.putIfAbsent(namespace, new HashMap<>());
                    extensions.get(namespace).put(attribute.getName(), process(attribute.getValue()));
                }
            }
            setter.accept(extensions);
        } else {
            Map<N2oNamespace, Map<String, String>> extensions = getter.get();
            if (extensions == null)
                return;
            for (Map.Entry<N2oNamespace, Map<String, String>> map : extensions.entrySet()) {
                for (Map.Entry<String, String> entry : map.getValue().entrySet()) {
                    element.setAttribute(new Attribute(entry.getKey(), entry.getValue(), Namespace.getNamespace(map.getKey().getPrefix(), map.getKey().getUri())));
                }
            }
        }
    }

    @Override
    public void attributeBoolean(Element element, String name, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        typedAttribute(element, name, getter, setter, Boolean::valueOf, Object::toString);
    }

    @Override
    public void attributeInteger(Element element, String name, Supplier<Integer> getter, Consumer<Integer> setter) {
        typedAttribute(element, name, getter, setter, Integer::parseInt, Object::toString);
    }

    @Override
    public void attributeArray(Element element, String name, String separator, Supplier<String[]>
            getter, Consumer<String[]> setter) {
        if (r) {
            Attribute attribute = element.getAttribute(name);
            if (attribute != null) {
                String value = process(attribute.getValue());
                if (value != null) {
                    setter.accept(splitBySeparator(value, separator));
                } else {
                    setter.accept(null);
                }
            }
        } else {
            if (getter.get() == null) return;
            StringJoiner str = new StringJoiner(separator);
            for (String s : getter.get())
                str.add(s);
            element.setAttribute(new Attribute(name, str.toString()));
        }
    }

    @Override
    public <T extends Enum<T>> void attributeEnumArray(Element element, String name, String separator,
                                                       Supplier<T[]> getter, Consumer<T[]> setter, Class<T> enumClass) {
        if (r)
            readAttributeEnumArray(element, name, separator, setter, enumClass);
        else
            persistAttributeEnumArray(element, name, separator, getter);
    }

    private <T extends Enum<T>> void readAttributeEnumArray(Element element, String name, String separator,
                                                            Consumer<T[]> setter, Class<T> enumClass) {
        Attribute attribute = element.getAttribute(name);
        if (attribute == null || attribute.getValue() == null || attribute.getValue().trim().isEmpty()) {
            setter.accept(null);
            return;
        }
        List<T> enumList = Arrays.stream(splitBySeparator(attribute.getValue(), separator))
                .filter(value -> value != null && !value.trim().isEmpty())
                .map(value -> stringToEnum(value.trim(), enumClass))
                .filter(Objects::nonNull)
                .toList();
        setter.accept(enumList.isEmpty() ? null : toArray(enumList, enumClass));
    }

    private <T extends Enum<T>> void persistAttributeEnumArray(Element element, String name, String separator,
                                                               Supplier<T[]> getter) {
        T[] values = getter.get();
        if (values == null || values.length == 0) return;
        StringJoiner str = new StringJoiner(separator);
        for (T value : values)
            str.add(enumToString(value));
        element.setAttribute(new Attribute(name, str.toString()));
    }

    @Override
    public <T extends Enum<T>> void attributeEnum(Element element, String
            name, Supplier<T> getter, Consumer<T> setter, Class<T> enumClass) {
        typedAttribute(element, name, getter, setter,
                value -> stringToEnum(value, enumClass), IOProcessorImpl::enumToString);
    }

    @Override
    public void element(Element element, String name, Supplier<String> getter, Consumer<String> setter) {
        if (r) {
            Element child = element.getChild(name, element.getNamespace());
            if (child != null && child.getValue() != null && !child.getValue().isEmpty()) {
                setter.accept(process(child.getValue()));
            }
        } else {
            if (getter.get() == null || getter.get().isEmpty()) return;
            getOrCreateChild(element, name).setText(getter.get());
        }
    }

    @Override
    public void hasElement(Element element, String name, BooleanSupplier getter, Consumer<Boolean> setter) {
        if (r) {
            Element child = element.getChild(name, element.getNamespace());
            setter.accept(child != null);
        } else {
            if (!getter.getAsBoolean()) return;
            getOrCreateChild(element, name);
        }
    }

    @Override
    public <T, R extends TypedElementReader<? extends T>, P extends TypedElementPersister<? super T>>
    ElementIOFactory<T, R, P> oneOf(Class<T> baseElementClass) {
        return new ElementIOFactoryByMap<>(baseElementClass);
    }

    @Override
    public <T extends NamespaceUriAware, R extends NamespaceReader<? extends T>, P extends
            NamespacePersister<? super T>> NamespaceIOFactory<T, R, P> anyOf(Class<T> baseElementClass) {
        return new NamespaceIOFactoryByMap<>(baseElementClass, readerFactory, persisterFactory);
    }

    @Override
    public <T extends NamespaceUriAware, R extends NamespaceReader<? extends T>, P extends
            NamespacePersister<? super T>> NamespaceIOFactory<T, R, P> anyOf() {
        return new NamespaceIOFactoryByMap<>(null, readerFactory, persisterFactory);
    }

    @Override
    public void additionalNamespaces(Element
                                             element, Supplier<List<Namespace>> getter, Consumer<List<Namespace>> setter) {
        if (r) {
            setter.accept(element.getAdditionalNamespaces());
        } else {
            if (getter.get() == null) return;
            for (Namespace namespace : getter.get()) {
                element.addNamespaceDeclaration(namespace);
            }
        }
    }

    /**
     * Установить схему всем дочерним элементам
     *
     * @param element   элемент
     * @param namespace схема
     */
    private void installNamespace(Element element, Namespace namespace) {
        if (element.getNamespace().equals(Namespace.NO_NAMESPACE)) {
            element.setNamespace(namespace);
            for (Object o : element.getChildren()) {
                Element child = (Element) o;
                installNamespace(child, namespace);
            }
        }
    }

    /**
     * Установить схему всем дочерним элементам
     *
     * @param element   элемент
     * @param namespace схема
     */
    private void installNamespace(Element element, Namespace namespace, Namespace oldNamespace) {
        if (element.getNamespace().equals(oldNamespace)) {
            element.setNamespace(namespace);
            for (Object o : element.getChildren()) {
                Element child = (Element) o;
                installNamespace(child, namespace, oldNamespace);
            }
        }
    }

    /**
     * Чтение настроек и локализации
     *
     * @param text текст с плейсхолдерами ${prop}
     * @return текст без плейсхолдеров, если они разрешились
     */
    protected String process(String text) {
        if (text == null) {
            return null;
        }
        String resolved = StringUtils.resolveProperties(text, MetadataParamHolder.getParams());
        resolved = systemProperties == null ? resolved : StringUtils.resolveProperties(resolved, systemProperties::getProperty);
        resolved = messageSourceAccessor == null ? resolved : StringUtils.resolveProperties(resolved, msg -> messageSourceAccessor.getMessage(msg, msg));
        if (failFast && StringUtils.hasProperty(resolved))
            throw new N2oException(String.format("Не удалось разрешить свойство в '%s'", text));
        return resolved;
    }

    /**
     * Конвертирует строку в enum
     *
     * @param value     строка
     * @param enumClass класс enum
     * @param <T>
     * @return
     */
    private static <T extends Enum<T>> T stringToEnum(String value, Class<T> enumClass) {
        if (value == null) return null;
        T res = null;
        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumToString(enumValue).equalsIgnoreCase(value))
                res = enumValue;
        }
        return res;
    }

    protected <T> Element persist(NamedElementIO<T> io, T entity, Namespace namespace) {
        Element element = new Element(io.getElementName(), namespace);
        io.io(element, entity, this);
        return element;
    }

    protected <T> T read(ClassedElementIO<T> io, Element element) {
        T entity = io.newInstance(element);
        io.io(element, entity, this);
        return entity;
    }

    private <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> T read(ElementIOFactory<T, R, P> factory, Element element) {
        R reader = factory.produce(element);
        if (reader != null) {
            injectIOProcessor(reader);
            return reader.read(element);
        } else
            return null;
    }

    private <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> Element persist(ElementIOFactory<T, R, P> factory, T
            entity, Namespace namespace) {
        P persister = factory.produce(entity);
        if (persister != null) {
            injectIOProcessor(persister);
            return persister.persist(entity, namespace);
        } else
            return null;
    }

    private <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> T read(NamespaceIOFactory<T, R, P> factory, Element element,
                                                            Namespace parentNamespace, Namespace... defaultNamespaces) {
        R reader;
        if (defaultNamespaces != null && defaultNamespaces.length > 0 && defaultNamespaces[0] != null && (hasText(getParentNameSpacePrefix(element)) || parentNamespace.getURI().equals(element.getNamespaceURI()))) {
            reader = factory.produce(element, parentNamespace, ArrayUtils.add(defaultNamespaces, element.getNamespace()));
        } else {
            reader = factory.produce(element, parentNamespace);
        }
        if (reader != null) {
            injectIOProcessor(reader);
            T model = reader.read(element);
            model.setNamespaceUri(element.getNamespaceURI());
            model.setNamespacePrefix(element.getNamespacePrefix());
            return model;
        } else
            return null;
    }

    private <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> Element persist(NamespaceIOFactory<T, R, P> factory, T entity,
                                                                     Namespace parentNamespace, Namespace... defaultNamespaces) {
        P persister;
        if (defaultNamespaces != null && defaultNamespaces.length > 0 && defaultNamespaces[0] != null
                && (isEmpty(entity.getNamespacePrefix()) || entity.getNamespaceUri().equals(parentNamespace.getURI())))
            persister = factory.produce((Class<T>) entity.getClass(), ArrayUtils.add(defaultNamespaces, entity.getNamespace()));
        else
            persister = factory.produce(entity);

        if (persister != null) {
            injectIOProcessor(persister);
            Element element = persister.persist(entity, parentNamespace);
            installNamespace(element, entity.getNamespace(), element.getNamespace());
            return element;
        } else
            return null;
    }

    private String getParentNameSpacePrefix(Element element) {
        if (isNull(element))
            return null;
        return nonNull(element.getParentElement()) ? element.getParentElement().getNamespacePrefix() : null;
    }

    /**
     * Получить дочерний элемент с заданным именем, создав и добавив его, если он отсутствует
     */
    private Element getOrCreateChild(Element parent, String childName) {
        Element childE = parent.getChild(childName, parent.getNamespace());
        if (childE == null) {
            childE = new Element(childName, parent.getNamespace());
            parent.addContent(childE);
        }
        return childE;
    }

    /**
     * Внедрить текущий процессор, если объект в нем нуждается
     */
    private void injectIOProcessor(Object o) {
        if (o instanceof IOProcessorAware ioProcessorAware)
            ioProcessorAware.setIOProcessor(this);
    }

    /**
     * Найти элемент-обертку sequences, либо вернуть сам element, если sequences не задан.
     * Возвращает null, если обертка задана, но отсутствует
     */
    private Element findSequenceElement(Element element, String sequences) {
        return sequences != null ? element.getChild(sequences, element.getNamespace()) : element;
    }

    /**
     * Добавить дочерний элемент в родительский, установив ему пространство имен
     */
    private void addChildElement(Element parent, Element childE, Namespace namespace) {
        if (childE != null) {
            installNamespace(childE, namespace);
            parent.addContent(childE);
        }
    }

    /**
     * Получить значение дочернего элемента: его текст, если valueName не задан, иначе значение атрибута valueName
     */
    private String getChildValue(Element childE, String valueName) {
        return valueName == null ? childE.getValue() : childE.getAttribute(valueName).getValue();
    }

    /**
     * Получить строковое значение перечисления: id для N2oEnum, иначе имя константы
     */
    private static String enumToString(Enum<?> en) {
        return en instanceof N2oEnum n2oEnum ? n2oEnum.getId() : en.name();
    }

    /**
     * Разбить строку по разделителю, отбрасывая пробелы вокруг него
     */
    private String[] splitBySeparator(String value, String separator) {
        return value.trim().split("\\s*" + separator + "\\s*");
    }

    /**
     * Преобразовать список в массив с заданным типом элементов
     */
    @SuppressWarnings("unchecked")
    private <T> T[] toArray(List<T> list, Class<?> elementClass) {
        return list.toArray((T[]) Array.newInstance(elementClass, list.size()));
    }

    private void text(Element element, Supplier<String> getter, Consumer<String> setter, Function<String, String> function) {
        if (r)
            readText(element, setter, function);
        else
            setterText(element, getter);
    }

    private void childrenText(Element element, String childName, Supplier<String> getter, Consumer<String> setter, Function<String, String> function) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child != null)
                readText(child, setter, function);
        } else {
            setterChildText(element, childName, getter);
        }
    }

    private void readText(Element element, Consumer<String> setter, Function<String, String> function) {
        checkCDataContent(element);
        String text = element.getText();
        if (text != null && !text.isEmpty())
            setter.accept(function.apply(text));
    }


    private void setterText(Element element, Supplier<String> getter) {
        if (getter.get() == null) return;
        element.setText(getter.get());
    }


    private void setterChildText(Element element, String childName, Supplier<String> getter) {
        if (getter.get() == null) return;
        getOrCreateChild(element, childName).setText(getter.get());
    }

    private void checkCDataContent(Element element) {

        if (CollectionUtils.isEmpty(element.getContent()))
            return;
        element.getContent().stream().filter(c -> Content.CType.CDATA.equals(c.getCType())).forEach(c -> {
            if (!(XML_ESCAPE_SYMBOLS.matcher(c.getValue()).find())) {
                log.warn("JS выражение компонента {} обернуто в CDATA, но не содержит специальных символов", element.getName());
            }
        });
    }

    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    public void setSystemProperties(PropertyResolver systemProperties) {
        this.systemProperties = systemProperties;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    @Override
    public <T extends RefIdAware> void merge(T source, String elementName) {
        MergeUtil.merge(source, environment, elementName);
    }

    public MetadataEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(MetadataEnvironment environment) {
        this.environment = environment;
    }
}
