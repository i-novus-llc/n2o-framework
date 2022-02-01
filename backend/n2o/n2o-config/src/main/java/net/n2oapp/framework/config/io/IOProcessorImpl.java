package net.n2oapp.framework.config.io;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.*;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Реализация процессора считывания и записи DOM элементов
 */
public final class IOProcessorImpl implements IOProcessor {

    /**
     * Если true, то чтение, false запись
     */
    private boolean r;
    private NamespaceReaderFactory readerFactory;
    private NamespacePersisterFactory persisterFactory;
    private MessageSourceAccessor messageSourceAccessor;
    private PropertyResolver systemProperties;
    private boolean failFast = true;

    public IOProcessorImpl(boolean read) {
        this.r = read;
    }

    public IOProcessorImpl(NamespaceReaderFactory readerFactory) {
        this.r = true;
        this.readerFactory = readerFactory;
        if (readerFactory instanceof IOProcessorAware)
            ((IOProcessorAware) readerFactory).setIOProcessor(this);
    }

    public IOProcessorImpl(NamespacePersisterFactory persisterFactory) {
        this.r = false;
        this.persisterFactory = persisterFactory;
        if (persisterFactory instanceof IOProcessorAware)
            ((IOProcessorAware) persisterFactory).setIOProcessor(this);
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
            Element child;
            if (sequences != null) {
                child = element.getChild(sequences, element.getNamespace());
                if (child == null)
                    return;
            } else {
                child = element;
            }
            child = child.getChild(childName, element.getNamespace());
            if (child != null) {
                setter.accept(read(io, child));
            }
        } else {
            T entity = getter.get();
            if (entity != null) {
                Element seqE = element;
                if (sequences != null) {
                    seqE = new Element(sequences, element.getNamespace());
                    element.addContent(seqE);
                }
                Element childE = persist(io, entity, element.getNamespace());
                installNamespace(childE, element.getNamespace());
                seqE.addContent(childE);
            }
        }
    }

    @Override
    public <T> void child(Element element, String sequences, String childName,
                          Supplier<T> getter, Consumer<T> setter,
                          Class<T> elementClass, ElementIO<T> io) {
        child(element, sequences, childName, getter, setter, new TypedElementIO<T>() {
            @Override
            public Class<T> getElementClass() {
                return elementClass;
            }

            @Override
            public void io(Element e, T t, IOProcessor p) {
                io.io(e, t, p);
            }

            @Override
            public String getElementName() {
                return childName;
            }
        });
    }

    @Override
    public <T> void child(Element element, String sequences, String childName,
                          Supplier<? extends T> getter, Consumer<? super T> setter,
                          Supplier<? extends T> newInstance, ElementIO<T> io) {
        child(element, sequences, childName, getter, setter, new TypedElementIO<T>() {
            private Class<T> elementClass;

            @Override
            public String getElementName() {
                return childName;
            }

            @Override
            public Class<T> getElementClass() {
                if (elementClass == null)
                    throw new IllegalStateException("you shall first to call #newInstance(Element)");
                return elementClass;
            }

            @Override
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
        });
    }

    @Override
    public <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void anyChild(Element element, String sequences,
                                                                      Supplier<? extends T> getter, Consumer<? super T> setter,
                                                                      ElementIOFactory<T, R, P> factory) {
        if (r) {
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            for (Object child : seqE.getChildren()) {
                Element childE = (Element) child;
                setter.accept(read(factory, childE));
            }
        } else {
            T entity = getter.get();
            if (entity == null) return;
            Element seqE;
            if (sequences != null) {
                seqE = new Element(sequences, element.getNamespace());
                element.addContent(seqE);
            } else {
                seqE = element;
            }
            Element childE = persist(factory, entity, element.getNamespace());
            if (childE != null) {
                installNamespace(childE, element.getNamespace());
                seqE.addContent(childE);
            }
        }
    }

    @Override
    public <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void anyChild(Element element, String sequences,
                                                                   Supplier<T> getter, Consumer<T> setter,
                                                                   NamespaceIOFactory<T, R, P> factory,
                                                                   Namespace defaultNamespace) {
        if (r) {
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            for (Object child : seqE.getChildren()) {
                Element childE = (Element) child;
                T childT = read(factory, childE, seqE.getNamespace(), defaultNamespace);
                if (childT != null) {
                    setter.accept(childT);
                    return;
                }
            }
        } else {
            T entity = getter.get();
            if (entity == null) return;
            Element seqE;
            if (sequences != null) {
                seqE = new Element(sequences, element.getNamespace());
                element.addContent(seqE);
            } else {
                seqE = element;
            }
            Element childE = persist(factory, entity, seqE.getNamespace(), defaultNamespace);
            if (childE != null) {
                installNamespace(childE, element.getNamespace());
                seqE.addContent(childE);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void children(Element element, String sequences, String childrenName,
                             Supplier<T[]> getter, Consumer<T[]> setter,
                             TypedElementIO<T> io) {
        if (r) {
            List<T> result = new ArrayList<>();
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            for (Object child : seqE.getChildren(childrenName, seqE.getNamespace())) {
                Element childE = (Element) child;
                T entity = read(io, childE);
                result.add(entity);
            }
            if (result.size() > 0) {
                T[] res = (T[]) Array.newInstance(io.getElementClass(), result.size());
                res = result.toArray(res);
                setter.accept(res);
            }
        } else {
            T[] entity = getter.get();
            if (entity == null) return;
            Element seqE;
            if (sequences != null) {
                seqE = new Element(sequences, element.getNamespace());
                element.addContent(seqE);
            } else {
                seqE = element;
            }
            for (T child : entity) {
                Element childE = persist(io, child, seqE.getNamespace());
                installNamespace(childE, seqE.getNamespace());
                seqE.addContent(childE);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void childrenToMap(Element element, String sequences, String childrenName,
                              String keyName, String valueName,
                              Supplier<Map<String, Object>> getter, Consumer<Map<String, Object>> setter) {
        if (r) {
            Map<String, Object> result = new HashMap<>();
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            for (Object child : seqE.getChildren(childrenName, seqE.getNamespace())) {
                Element childE = (Element) child;
                String key = process(childE.getAttribute(keyName).getValue());
                String value = valueName == null ?
                        childE.getValue() :
                        childE.getAttribute(valueName).getValue();
                Object objValue = DomainProcessor.getInstance().doDomainConversion(null, value);
                result.put(key, objValue);
            }
            setter.accept(result);
        } else {
            persistChildrenMap(element, sequences, childrenName, keyName, valueName, getter);
        }
    }

    @Override
    public void childrenToMap(Element element, String sequences, String childrenName, Supplier<Map<String, Object>> getter, Consumer<Map<String, Object>> setter) {
        if (r) {
            Map<String, Object> result = new HashMap<>();
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            for (Object child : seqE.getChildren(childrenName, seqE.getNamespace())) {
                Element childE = (Element) child;
                Attribute attribute = childE.getAttributes().get(0);
                String key = attribute.getName();
                String value = attribute.getValue();
                Object objValue = DomainProcessor.getInstance().doDomainConversion(null, value);
                result.put(key, objValue);
            }
            setter.accept(result);
        } else {
            persistChildrenMap(element, sequences, childrenName, getter);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void childrenToStringMap(Element element, String sequences, String childrenName,
                                    String keyName, String valueName,
                                    Supplier<Map<String, String>> getter, Consumer<Map<String, String>> setter) {
        if (r) {
            Map<String, String> result = new HashMap<>();
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            for (Object child : seqE.getChildren(childrenName, seqE.getNamespace())) {
                Element childE = (Element) child;
                String key = process(childE.getAttribute(keyName).getValue());
                String value = valueName == null ?
                        childE.getValue() :
                        childE.getAttribute(valueName).getValue();
                result.put(key, value);
            }
            setter.accept(result);
        } else {
            persistChildrenMap(element, sequences, childrenName, keyName, valueName, getter);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void childrenToStringArray(Element element, String sequences, String childrenName,
                                      Supplier<String[]> getter, Consumer<String[]> setter) {
        if (r) {
            List<String> result = new ArrayList<>();
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            for (Object child : seqE.getChildren(childrenName, seqE.getNamespace())) {
                Element childE = (Element) child;
                String value = process(childE.getValue());
                result.add(value);
            }
            if (!result.isEmpty()) {
                String[] res = new String[result.size()];
                setter.accept(result.toArray(res));
            }
        } else {
            String[] values = getter.get();
            if (values == null) return;
            Element seqE;
            seqE = persistSequences(element, sequences);
            for (String k : values) {
                Element childE = new Element(childrenName, element.getNamespace());
                childE.setText(k);
                seqE.addContent(childE);
            }
        }
    }

    private Element persistSequences(Element element, String sequences) {
        Element seqE;
        if (sequences != null) {
            seqE = element.getChild(sequences, element.getNamespace());
            if (seqE == null) {
                seqE = new Element(sequences, element.getNamespace());
                element.addContent(seqE);
            }
        } else {
            seqE = element;
        }
        return seqE;
    }

    private <T> void persistChildrenMap(Element element, String sequences, String childrenName,
                                        String keyName, String valueName,
                                        Supplier<Map<String, T>> getter) {
        Map<String, T> values = getter.get();
        if (values == null) return;
        Element seqE;
        seqE = persistSequences(element, sequences);
        for (String k : values.keySet()) {
            Element childE = new Element(childrenName, element.getNamespace());
            childE.setAttribute(keyName, k);
            if (valueName == null) {
                childE.setText(values.get(k).toString());
            } else {
                if (values.get(k) != null) {
                    childE.setAttribute(valueName, values.get(k).toString());
                }
            }
            seqE.addContent(childE);
        }
    }

    private <T> void persistChildrenMap(Element element, String sequences, String childrenName,
                                        Supplier<Map<String, T>> getter) {
        Map<String, T> values = getter.get();
        if (values == null) return;
        Element seqE;
        seqE = persistSequences(element, sequences);
        for (String k : values.keySet()) {
            Element childE = new Element(childrenName, element.getNamespace());
            childE.setAttribute(k, values.get(k).toString());
            seqE.addContent(childE);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void children(Element element, String sequences, String childrenName,
                             Supplier<T[]> getter, Consumer<T[]> setter,
                             Class<T> elementClass, ElementIO<T> io) {
        children(element, sequences, childrenName, getter, setter, new TypedElementIO<T>() {
            @Override
            public String getElementName() {
                return childrenName;
            }

            @Override
            public Class<T> getElementClass() {
                return elementClass;
            }

            @Override
            public void io(Element e, T t, IOProcessor p) {
                io.io(e, t, p);
            }
        });

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void children(Element element, String sequences, String childrenName,
                             Supplier<T[]> getter, Consumer<T[]> setter,
                             Supplier<T> newInstance, ElementIO<T> io) {
        children(element, sequences, childrenName, getter, setter, new TypedElementIO<T>() {
            private Class<T> elementClass;

            @Override
            public String getElementName() {
                return childrenName;
            }

            @Override
            public Class<T> getElementClass() {
                if (elementClass == null)
                    throw new IllegalStateException("you shall first to call #newInstance(Element)");
                return elementClass;
            }

            @Override
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
        });

    }

    @Override
    public <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> void anyChildren(Element element, String sequences,
                                                                         Supplier<T[]> getter, Consumer<T[]> setter,
                                                                         ElementIOFactory<T, R, P> factory) {
        if (r) {
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            List<T> result = new ArrayList<>();
            for (Object child : seqE.getChildren()) {
                Element childE = (Element) child;
                T childT = read(factory, childE);
                if (childT != null) {
                    result.add(childT);
                }
            }
            @SuppressWarnings("unchecked") T[] res = (T[]) Array.newInstance(factory.getBaseElementClass(), result.size());
            res = result.toArray(res);
            setter.accept(res);
        } else {
            T[] entities = getter.get();
            if (entities == null) return;
            Element seqE;
            if (sequences != null) {
                seqE = new Element(sequences, element.getNamespace());
                element.addContent(seqE);
            } else {
                seqE = element;
            }
            for (T child : entities) {
                Element childE = persist(factory, child, element.getNamespace());
                if (childE != null) {
                    installNamespace(childE, element.getNamespace());
                    seqE.addContent(childE);
                }
            }
        }
    }

    @Override
    public <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> void anyChildren(Element element, String sequences,
                                                                      Supplier<T[]> getter, Consumer<T[]> setter,
                                                                      NamespaceIOFactory<T, R, P> factory,
                                                                      Namespace... defaultNamespace) {
        if (r) {
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            List<T> result = new ArrayList<>();
            for (Object child : seqE.getChildren()) {
                Element childE = (Element) child;
                T childT = read(factory, childE, seqE.getNamespace(), defaultNamespace);
                if (childT != null) {
                    result.add(childT);
                }
            }
            if (result.size() > 0) {
                @SuppressWarnings("unchecked") T[] res;
                if (factory.getBaseElementClass() == null) {
                    res = (T[]) Array.newInstance(NamespaceUriAware.class, result.size());
                } else {
                    res = (T[]) Array.newInstance(factory.getBaseElementClass(), result.size());
                }
                res = result.toArray(res);
                setter.accept(res);
            }
        } else {
            T[] entities = getter.get();
            if (entities == null) return;
            Element seqE;
            if (sequences != null) {
                if (element.getChild(sequences, element.getNamespace()) != null)
                    seqE = element.getChild(sequences, element.getNamespace());
                else {
                    seqE = new Element(sequences, element.getNamespace());
                    element.addContent(seqE);
                }
            } else {
                seqE = element;
            }
            for (T child : entities) {
                Element childE = persist(factory, child, seqE.getNamespace(), defaultNamespace);
                if (childE != null) {
                    installNamespace(childE, element.getNamespace());
                    seqE.addContent(childE);
                }
            }
        }
    }

    @Override
    public <T, E extends Enum<E>> void childrenByEnum(Element element, String sequences,
                                                      Supplier<T[]> getterList, Consumer<T[]> setterList,
                                                      Class<E> enumClass, Function<T, E> getterEnum, BiConsumer<T, E> setterEnum,
                                                      ClassedElementIO<T> io) {
        if (r) {
            List<T> result = new ArrayList<>();
            Element seqE;
            if (sequences != null) {
                seqE = element.getChild(sequences, element.getNamespace());
                if (seqE == null) return;
            } else {
                seqE = element;
            }
            for (Object child : seqE.getChildren()) {
                Element childE = (Element) child;
                E en = stringToEnum(childE.getName(), enumClass);
                if (en != null) {
                    T entity = read(io, childE);
                    setterEnum.accept(entity, en);
                    result.add(entity);
                }
            }
            if (result.size() > 0) {
                T[] res = (T[]) Array.newInstance(io.getElementClass(), result.size());
                res = result.toArray(res);
                setterList.accept(res);
            }
        } else {
            T[] entity = getterList.get();
            if (entity == null) return;
            Element seqE;
            if (sequences != null) {
                seqE = new Element(sequences, element.getNamespace());
                element.addContent(seqE);
            } else {
                seqE = element;
            }
            for (T child : entity) {
                final E en = getterEnum.apply(child);
                Element childE = null;
                if (en != null) {
                    childE = persist(new NamedElementIO<T>() {
                        @Override
                        public String getElementName() {
                            return en.name();
                        }

                        @Override
                        public void io(Element e, T t, IOProcessor p) {
                            io.io(e, t, p);
                        }
                    }, child, seqE.getNamespace());
                    installNamespace(childE, element.getNamespace());
                    seqE.addContent(childE);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, E extends Enum<E>> void childrenByEnum(Element element, String sequences,
                                                      Supplier<T[]> getterList, Consumer<T[]> setterList,
                                                      Function<T, E> getterEnum, BiConsumer<T, E> setterEnum,
                                                      Supplier<T> newInstance, Class<E> enumClass, ElementIO<T> io) {
        childrenByEnum(element, sequences, getterList, setterList, enumClass, getterEnum, setterEnum,
                new ClassedElementIO<T>() {
                    private Class<T> elementClass;

                    @Override
                    public Class<T> getElementClass() {
                        if (elementClass == null)
                            throw new IllegalStateException("you shall first to call #newInstance(Element)");
                        return elementClass;
                    }

                    @Override
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

                });

    }

    @Override
    public void attribute(Element element, String name, Supplier<String> getter, Consumer<String> setter) {
        if (r) {
            Attribute attribute = element.getAttribute(name);
            if (attribute != null) {
                setter.accept(process(attribute.getValue()));
            }
        } else {
            if (getter.get() == null) return;
            element.setAttribute(new Attribute(name, getter.get()));
        }
    }

    @Override
    public void text(Element element, Supplier<String> getter, Consumer<String> setter) {
        if (r) {
            String text = element.getText();
            if (text != null && !text.isEmpty()) {
                setter.accept(process(text));
            }
        } else {
            if (getter.get() == null) return;
            element.setText(getter.get());
        }
    }

    @Override
    public void childrenText(Element element, String childName, Supplier<String> getter, Consumer<String> setter) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child == null) return;
            String text = child.getText();
            if (text != null && !text.isEmpty()) {
                setter.accept(process(text));
            }
        } else {
            if (getter.get() == null) return;
            Element childElement = element.getChild(childName, element.getNamespace());
            if (childElement == null) {
                childElement = new Element(childName, element.getNamespace());
                childElement.setText(getter.get());
                element.addContent(childElement);
            } else {
                childElement.setText(getter.get());
            }
        }
    }

    @Override
    public void childAttribute(Element element, String childName, String name, Supplier<String> getter, Consumer<String> setter) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child == null) return;
            Attribute attribute = child.getAttribute(name);
            if (attribute != null) {
                setter.accept(process(attribute.getValue()));
            }
        } else {
            if (getter.get() == null) return;
            Element childElement = element.getChild(childName, element.getNamespace());
            if (childElement == null) {
                childElement = new Element(childName, element.getNamespace());
                childElement.setAttribute(new Attribute(name, getter.get()));
                element.addContent(childElement);
            } else {
                childElement.setAttribute(new Attribute(name, getter.get()));
            }
        }
    }

    @Override
    public void childAttributeBoolean(Element element, String childName, String name, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child == null) return;
            Attribute attribute = child.getAttribute(name);
            if (attribute != null) {
                setter.accept(Boolean.valueOf(process(attribute.getValue())));
            }
        } else {
            if (getter.get() == null) return;
            Element childElement = element.getChild(childName, element.getNamespace());
            if (childElement == null) {
                childElement = new Element(childName, element.getNamespace());
                childElement.setAttribute(new Attribute(name, getter.get().toString()));
                element.addContent(childElement);
            } else {
                childElement.setAttribute(new Attribute(name, getter.get().toString()));
            }
        }
    }

    @Override
    public void childAttributeInteger(Element element, String childName, String name, Supplier<Integer> getter, Consumer<Integer> setter) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child == null) return;
            Attribute attribute = child.getAttribute(name);
            if (attribute != null) {
                setter.accept(Integer.parseInt(process(attribute.getValue())));
            }
        } else {
            if (getter.get() == null) return;
            Element childElement = element.getChild(childName, element.getNamespace());
            if (childElement == null) {
                childElement = new Element(childName, element.getNamespace());
                childElement.setAttribute(new Attribute(name, getter.get().toString()));
                element.addContent(childElement);
            } else {
                childElement.setAttribute(new Attribute(name, getter.get().toString()));
            }
        }
    }

    @Override
    public <T extends Enum<T>> void childAttributeEnum(Element element, String childName, String name, Supplier<T> getter, Consumer<T> setter, Class<T> enumClass) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child == null) return;
            Attribute attribute = child.getAttribute(name);
            if (attribute != null) {
                setter.accept(stringToEnum(process(attribute.getValue()), enumClass));
            }
        } else {
            if (getter.get() == null) return;
            Element childElement = element.getChild(childName, element.getNamespace());
            if (childElement == null) {
                childElement = new Element(childName, element.getNamespace());
                childElement.setAttribute(new Attribute(name, getter.get().toString()));
                element.addContent(childElement);
            } else {
                childElement.setAttribute(new Attribute(name, getter.get().toString()));
            }
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
                    if (attribute.getValue() instanceof String){
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
    public void childAnyAttributes(Element element, String childName, Supplier<Map<N2oNamespace, Map<String, String>>> getter, Consumer<Map<N2oNamespace, Map<String, String>>> setter) {
        if (r) {
            Element child = element.getChild(childName, element.getNamespace());
            if (child == null) return;
            anyAttributes(child, getter, setter);
        } else {
            if (getter.get() == null) return;
            Element childElement = element.getChild(childName, element.getNamespace());
            if (childElement == null) {
                childElement = new Element(childName, element.getNamespace());
                anyAttributes(childElement, getter, setter);
                element.addContent(childElement);
            } else {
                anyAttributes(childElement, getter, setter);
            }
        }
    }

    @Override
    public void anyAttributes(Element element, Supplier<Map<N2oNamespace, Map<String, String>>> getter,
                              Consumer<Map<N2oNamespace, Map<String, String>>> setter) {
        if (r) {
            N2oNamespace elementNamespace = new N2oNamespace(element.getNamespace());
            Map<N2oNamespace, Map<String, String>> extensions = new HashMap<>();
            for (Object o : element.getAttributes()) {
                Attribute attribute = (Attribute) o;
                if (elementNamespace.getUri().equals(attribute.getNamespaceURI()) || attribute.getNamespaceURI().isEmpty()) {
                    continue;
                } else {
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
        if (r) {
            Attribute attribute = element.getAttribute(name);
            if (attribute != null) {
                setter.accept(Boolean.valueOf(process(attribute.getValue())));
            }
        } else {
            if (getter.get() == null) return;
            element.setAttribute(new Attribute(name, getter.get().toString()));
        }
    }

    @Override
    public void attributeInteger(Element element, String name, Supplier<Integer> getter, Consumer<Integer> setter) {
        if (r) {
            Attribute attribute = element.getAttribute(name);
            if (attribute != null) {
                setter.accept(Integer.valueOf(process(attribute.getValue())));
            }
        } else {
            if (getter.get() == null) return;
            element.setAttribute(new Attribute(name, getter.get().toString()));
        }
    }

    @Override
    public void attributeArray(Element element, String name, String separator, Supplier<String[]> getter, Consumer<String[]> setter) {
        if (r) {
            Attribute attribute = element.getAttribute(name);
            if (attribute != null) {
                setter.accept(process(attribute.getValue()).split(separator));
            }
        } else {
            if (getter.get() == null) return;
            StringBuilder str = new StringBuilder();
            for (String s : getter.get()) {
                str.append(s).append(",");
            }
            element.setAttribute(new Attribute(name, str.toString().substring(0, str.length() - 1)));
        }
    }

    @Override
    public <T extends Enum<T>> void attributeEnum(Element element, String name, Supplier<T> getter, Consumer<T> setter, Class<T> enumClass) {
        if (r) {
            Attribute attribute = element.getAttribute(name);
            if (attribute != null) {
                setter.accept(stringToEnum(process(attribute.getValue()), enumClass));
            }
        } else {
            if (getter.get() == null) return;
            if (IdAware.class.isAssignableFrom(enumClass)) {
                element.setAttribute(new Attribute(name, ((IdAware) getter.get()).getId()));
            } else {
                element.setAttribute(new Attribute(name, getter.get().name()));
            }
        }
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
            Element childElement = element.getChild(name, element.getNamespace());
            if (childElement == null) {
                childElement = new Element(name, element.getNamespace());
                childElement.setText(getter.get());
                element.addContent(childElement);
            } else {
                childElement.setText(getter.get());
            }
        }
    }

    @Override
    public void hasElement(Element element, String name, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        if (r) {
            Element child = element.getChild(name, element.getNamespace());
            setter.accept(child != null);
        } else {
            if (getter.get() == null || !getter.get()) return;
            Element childElement = new Element(name, element.getNamespace());
            element.addContent(childElement);
        }
    }

    @Override
    public <T, R extends TypedElementReader<? extends T>, P extends TypedElementPersister<? super T>> ElementIOFactory<T, R, P> oneOf(Class<T> baseElementClass) {
        return new ElementIOFactoryByMap<>(baseElementClass);
    }

    @Override
    public <T extends NamespaceUriAware, R extends NamespaceReader<? extends T>, P extends NamespacePersister<? super T>> NamespaceIOFactory<T, R, P> anyOf(Class<T> baseElementClass) {
        return new NamespaceIOFactoryByMap<>(baseElementClass, readerFactory, persisterFactory);
    }

    @Override
    public <T extends NamespaceUriAware, R extends NamespaceReader<? extends T>, P extends NamespacePersister<? super T>> NamespaceIOFactory<T, R, P> anyOf() {
        return new NamespaceIOFactoryByMap<>(null, readerFactory, persisterFactory);
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
    private String process(String text) {
        if (text == null) {
            return null;
        }
        String resolved = StringUtils.resolveProperties(text, MetadataParamHolder.getParams());
        resolved = systemProperties == null ? resolved : StringUtils.resolveProperties(resolved, systemProperties::getProperty);
        resolved = messageSourceAccessor == null ? resolved : StringUtils.resolveProperties(resolved, (msg) -> messageSourceAccessor.getMessage(msg, msg));
        if (failFast && StringUtils.hasProperty(resolved))
            throw new N2oException("Cannot resolve property in '" + text + "'");
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

    private <T> Element persist(NamedElementIO<T> io, T entity, Namespace namespace) {
        Element element = new Element(io.getElementName(), namespace);
        io.io(element, entity, this);
        return element;
    }

    private <T> T read(ClassedElementIO<T> io, Element element) {
        T entity = io.newInstance(element);
        io.io(element, entity, this);
        return entity;
    }

    private <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> T read(ElementIOFactory<T, R, P> factory, Element element) {
        R reader = factory.produce(element);
        if (reader != null) {
            if (reader instanceof IOProcessorAware)
                ((IOProcessorAware) reader).setIOProcessor(this);
            return reader.read(element);
        } else
            return null;
    }

    private <T,
            R extends TypedElementReader<? extends T>,
            P extends TypedElementPersister<? super T>> Element persist(ElementIOFactory<T, R, P> factory, T entity, Namespace namespace) {
        P persister = factory.produce(entity);
        if (persister != null) {
            if (persister instanceof IOProcessorAware)
                ((IOProcessorAware) persister).setIOProcessor(this);
            return persister.persist(entity, namespace);
        } else
            return null;
    }

    private <T extends NamespaceUriAware,
            R extends NamespaceReader<? extends T>,
            P extends NamespacePersister<? super T>> T read(NamespaceIOFactory<T, R, P> factory, Element element,
                                                            Namespace parentNamespace, Namespace... defaultNamespaces) {
        R reader;
        if (defaultNamespaces != null && defaultNamespaces.length > 0 && defaultNamespaces[0] != null && parentNamespace.getURI().equals(element.getNamespaceURI())) {
            reader = factory.produce(element, parentNamespace, defaultNamespaces);
        } else {
            reader = factory.produce(element, parentNamespace, null);
        }
        if (reader != null) {
            if (reader instanceof IOProcessorAware)
                ((IOProcessorAware) reader).setIOProcessor(this);
            T model = reader.read(element);
            model.setNamespaceUri(element.getNamespaceURI());
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
                && entity.getNamespaceUri().equals(parentNamespace.getURI()))
            persister = factory.produce((Class<T>) entity.getClass(), defaultNamespaces);
        else
            persister = factory.produce(entity);

        if (persister != null) {
            if (persister instanceof IOProcessorAware)
                ((IOProcessorAware) persister).setIOProcessor(this);
            Element element = persister.persist(entity, parentNamespace);
            installNamespace(element, entity.getNamespace(), element.getNamespace());
            return element;
        } else
            return null;
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
}
