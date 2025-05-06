package net.n2oapp.framework.migrate;

import net.n2oapp.framework.api.metadata.io.ClassedElementIO;
import net.n2oapp.framework.api.metadata.io.ElementIOFactory;
import net.n2oapp.framework.api.metadata.io.NamedElementIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Реализация процессора считывания и записи DOM элементов для механизма миграции
 */
public class MigratorIOProcessorImpl extends IOProcessorImpl {

    private static final String BODY_KEY = "body";

    public MigratorIOProcessorImpl(NamespaceReaderFactory readerFactory) {
        super(readerFactory);
    }

    public MigratorIOProcessorImpl(NamespacePersisterFactory persisterFactory) {
        super(persisterFactory);
    }

    @Override
    public void attribute(Element element, String name,
                          Supplier<String> getter, Consumer<String> setter) {
        attribute(element, name, getter, setter, String::valueOf);
    }

    @Override
    public void attributeBoolean(Element element, String name,
                                 Supplier<Boolean> getter, Consumer<Boolean> setter) {
        attribute(element, name, getter, setter, Boolean::valueOf);
    }

    @Override
    public void attributeInteger(Element element, String name,
                                 Supplier<Integer> getter, Consumer<Integer> setter) {
        attribute(element, name, getter, setter, Integer::valueOf);
    }

    @Override
    public void text(Element element, Supplier<String> getter, Consumer<String> setter) {
        if (isR()) {
            String text = element.getText();
            if (text != null && !text.isEmpty()) {
                MigratorInfoHolder.addProperty(BODY_KEY, text);
                setter.accept(null);
            }
        } else {
            if (getter.get() != null) {
                element.setText(getter.get());
            } else if (MigratorInfoHolder.getProperty(BODY_KEY) != null) {
                element.setText(MigratorInfoHolder.getProperty(BODY_KEY));
            }
        }
    }

    @Override
    public void childAttribute(Element element, String childName, String name,
                               Supplier<String> getter, Consumer<String> setter) {
        childAttribute(element, childName, name, getter, setter, String::valueOf);
    }

    @Override
    public void childAttributeBoolean(Element element, String childName, String name,
                                      Supplier<Boolean> getter, Consumer<Boolean> setter) {
        childAttribute(element, childName, name, getter, setter, Boolean::valueOf);
    }

    @Override
    public void childAttributeInteger(Element element, String childName, String name,
                                      Supplier<Integer> getter, Consumer<Integer> setter) {
        childAttribute(element, childName, name, getter, setter, Integer::valueOf);
    }

    @Override
    public <T, R extends TypedElementReader<? extends T>, P extends TypedElementPersister<? super T>> ElementIOFactory<T, R, P> oneOf(Class<T> baseElementClass) {
        return new MigratorElementIOFactoryByMap<>(baseElementClass);
    }

    @Override
    protected <T> Element persist(NamedElementIO<T> io, T entity, Namespace namespace) {
        Element element = new Element(io.getElementName(), namespace);
        MigratorInfoHolder.pushCurrentSource(entity);
        io.io(element, entity, this);
        MigratorInfoHolder.popCurrentSource();
        return element;
    }

    @Override
    protected <T> T read(ClassedElementIO<T> io, Element element) {
        T entity = io.newInstance(element);
        MigratorInfoHolder.pushCurrentSource(entity);
        io.io(element, entity, this);
        MigratorInfoHolder.popCurrentSource();
        return entity;
    }

    private <T> void attribute(Element element, String name,
                               Supplier<T> getter, Consumer<T> setter,
                               Function<String, T> valueFunction) {
        if (isR()) {
            readAttribute(element, name, setter, valueFunction);
        } else {
            writeAttribute(element, name, getter);
        }
    }

    private <T> void childAttribute(Element element, String childName, String name,
                                    Supplier<T> getter, Consumer<T> setter,
                                    Function<String, T> valueFunction) {
        Element child = element.getChild(childName, element.getNamespace());
        if (super.isR()) {
            if (child == null) return;
            readAttribute(child, name, setter, valueFunction);
        } else {
            if (child == null) {
                child = new Element(childName, element.getNamespace());
                element.addContent(child);
            }
            writeAttribute(child, name, getter);
        }
    }

    private static <T> void readAttribute(Element element, String name, Consumer<T> setter,
                                          Function<String, T> valueFunction) {
        Attribute attribute = element.getAttribute(name);
        if (attribute != null) {
            String value = attribute.getValue();
            if (value != null && value.startsWith("${") && value.endsWith("}")) {
                MigratorInfoHolder.addProperty(name, value);
                setter.accept(null);
            } else {
                setter.accept(valueFunction.apply(value));
            }
        }

    }

    private static <T> void writeAttribute(Element element, String name, Supplier<T> getter) {
        if (getter.get() != null) {
            element.setAttribute(new Attribute(name, getter.get().toString()));
        } else if (MigratorInfoHolder.getProperty(name) != null) {
            element.setAttribute(new Attribute(name, MigratorInfoHolder.getProperty(name)));
        }
    }
}