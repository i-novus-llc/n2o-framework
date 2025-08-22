package net.n2oapp.framework.migrate;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import net.n2oapp.framework.api.metadata.io.ClassedElementIO;
import net.n2oapp.framework.api.metadata.io.ElementIOFactory;
import net.n2oapp.framework.api.metadata.io.NamedElementIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import org.jdom2.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Реализация процессора считывания и записи DOM элементов для механизма миграции
 */
public class MigratorIOProcessorImpl extends IOProcessorImpl {

    private static final String BODY_KEY = "body";
    private static final String CDATA_KEY = "cdata";

    public MigratorIOProcessorImpl(NamespaceReaderFactory readerFactory, MetadataEnvironment environment) {
        super(readerFactory, environment);
    }

    public MigratorIOProcessorImpl(NamespacePersisterFactory persisterFactory, MetadataEnvironment environment) {
        super(persisterFactory, environment);
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
        processText(element, null, getter, setter, false);
    }

    @Override
    public void childrenText(Element element, String childName, Supplier<String> getter, Consumer<String> setter) {
        processText(element, childName, getter, setter, false);
    }

    @Override
    public void originalText(Element element, Supplier<String> getter, Consumer<String> setter) {
        processText(element, null, getter, setter, true);
    }

    @Override
    public void childrenOriginalText(Element element, String childName, Supplier<String> getter, Consumer<String> setter) {
        processText(element, childName, getter, setter, true);
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

    @Override
    public <T extends RefIdAware> void merge(T source, String elementName) {
        // not applicable to migration process
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
            if (getter.get() == null) return;
            if (child == null) {
                child = new Element(childName, element.getNamespace());
                element.addContent(child);
            }
            writeAttribute(child, name, getter);
        }
    }

    /**
     * Считывание\запись содержимого элемента или дочернего элемента с сохранением информации о формате
     *
     * @param element        элемент XML
     * @param childName      имя дочернего элемента (null для обработки текста самого элемента)
     * @param getter         геттер текстового значения
     * @param setter         сеттер текстового значения
     * @param isOriginalText флаг указывающий на обработку оригинального текста
     */
    private void processText(Element element, String childName, Supplier<String> getter, Consumer<String> setter, boolean isOriginalText) {
        if (isR()) {
            readText(element, childName, setter, isOriginalText);
        } else {
            writeText(element, childName, getter);
        }
    }

    /**
     * Считывание текстового содержимого элемента с сохранением информации о CDATA и оригинальном тексте
     *
     * @param element        элемент XML
     * @param childName      имя дочернего элемента (null для обработки текста самого элемента)
     * @param setter         сеттер текстового значения
     * @param isOriginalText флаг указывающий на обработку оригинального текста
     */
    private void readText(Element element, String childName, Consumer<String> setter, boolean isOriginalText) {
        Element targetElement = childName != null ? element.getChild(childName, element.getNamespace()) : element;

        if (targetElement == null) return;

        String text = targetElement.getText();
        if (text != null && !text.isEmpty()) {

            if (targetElement.getContent().stream().anyMatch(c -> Content.CType.CDATA.equals(c.getCType()))) {
                MigratorInfoHolder.addProperty(CDATA_KEY + getKeySuffix(childName), text);
                setter.accept(text);
            } else {
                MigratorInfoHolder.addProperty(BODY_KEY + getKeySuffix(childName), text);
                setter.accept(isOriginalText ? text : null);
            }
        }
    }

    /**
     * Запись текстового содержимое элемента с восстановлением оригинального формата (CDATA или обычный текст)
     *
     * @param element        элемент XML
     * @param childName      имя дочернего элемента (null для записи текста в сам элемент)
     * @param getter         геттер текстового значения
     */
    private void writeText(Element element, String childName, Supplier<String> getter) {
        String value = getter.get();
        String keySuffix = getKeySuffix(childName);

        if (value != null) {
            Element targetElement = getOrCreateTargetElement(element, childName);
            if (MigratorInfoHolder.getProperty(CDATA_KEY + keySuffix) != null) {
                targetElement.setContent(new CDATA(value));
            } else {
                targetElement.setText(value);
            }
        } else {
            String propertyKey = BODY_KEY + keySuffix;
            String storedValue = MigratorInfoHolder.getProperty(propertyKey);
            if (storedValue != null) {
                Element targetElement = getOrCreateTargetElement(element, childName);
                targetElement.setText(storedValue);
            }
        }
    }

    /**
     * Получение\создание целевого элемента для записи текстового содержимого
     *
     * @param parent    родительский элемент
     * @param childName имя дочернего элемента (null для возврата родительского элемента)
     * @return целевой элемент для записи текста
     */
    private Element getOrCreateTargetElement(Element parent, String childName) {
        if (childName == null) {
            return parent;
        }

        Element child = parent.getChild(childName, parent.getNamespace());
        if (child == null) {
            child = new Element(childName, parent.getNamespace());
            parent.addContent(child);
        }
        return child;
    }

    /**
     * Формирование суффикса ключа для хранения свойств текстового содержимого
     *
     * @param childName имя дочернего элемента
     * @return суффикс ключа в формате ".childName" или пустая строка для основного элемента
     */
    private String getKeySuffix(String childName) {
        return childName != null ? "." + childName : "";
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

    @Override
    protected String process(String text) {
        return text;
    }
}