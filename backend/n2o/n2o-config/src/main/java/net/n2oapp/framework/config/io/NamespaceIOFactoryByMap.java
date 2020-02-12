package net.n2oapp.framework.config.io;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.*;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import org.jdom.Namespace;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация фабрики чтения / записи элементов по неймспейсу
 * @param <T> Тип модели
 * @param <R> Ти ридера
 * @param <P> Тип персистера
 */
public class NamespaceIOFactoryByMap<T extends NamespaceUriAware, R extends NamespaceReader<? extends T>, P extends NamespacePersister<? super T>>
        implements NamespaceIOFactory<T,R,P> {
    private Class<T> baseElementClass;
    private Map<String, Map<String, R>> names = new HashMap<>();
    private Map<String, Map<Class, P>> classes = new HashMap<>();
    private NamespaceReaderFactory readerFactory;
    private NamespacePersisterFactory persisterFactory;

    public NamespaceIOFactoryByMap(Class<T> baseElementClass,
                                   NamespaceReaderFactory readerFactory,
                                   NamespacePersisterFactory persisterFactory) {
        this.baseElementClass = baseElementClass;
        this.readerFactory = readerFactory;
        this.persisterFactory = persisterFactory;
    }

    @Override
    public P produce(Namespace namespace, Class<T> clazz) {
        if (classes.containsKey(namespace.getURI()) && classes.get(namespace.getURI()).containsKey(clazz))
            return classes.get(namespace.getURI()).get(clazz);
        return (P) persisterFactory.produce(namespace, clazz);
    }

    @Override
    public boolean check(Namespace namespace, Class<T> clazz) {
        return (classes.containsKey(namespace.getURI()) && classes.get(namespace.getURI()).containsKey(clazz))
                || persisterFactory.check(namespace, clazz);
    }

    @Override
    public R produce(Namespace namespace, String elementName) {
        if (names.containsKey(namespace.getURI()) && names.get(namespace.getURI()).containsKey(elementName))
            return names.get(namespace.getURI()).get(elementName);
        return (R) readerFactory.produce(namespace, elementName);
    }

    @Override
    public boolean check(Namespace namespace, String elementName) {
        return (names.containsKey(namespace.getURI()) && names.get(namespace.getURI()).containsKey(elementName))
                || readerFactory.check(namespace, elementName);
    }

    @Override
    public NamespaceIOFactory<T, R, P> add(NamespaceIO<? extends T> nio) {
        ProxyNamespaceIO<? extends T> proxy = new ProxyNamespaceIO<>(nio);
        names.computeIfAbsent(nio.getNamespaceUri(), k -> new HashMap<>()).put(nio.getElementName(), (R) proxy);
        classes.computeIfAbsent(nio.getNamespaceUri(), k -> new HashMap<>()).put(nio.getElementClass(), (P) proxy);
        return this;
    }

    @Override
    public void add(NamespacePersister<T> persister) {
        classes.computeIfAbsent(persister.getNamespaceUri(), k -> new HashMap<>()).put(persister.getElementClass(), (P) persister);
    }

    @Override
    public void add(NamespaceReader<T> reader) {
        names.computeIfAbsent(reader.getNamespaceUri(), k -> new HashMap<>()).put(reader.getElementName(), (R) reader);
    }

    @Override
    public Class<T> getBaseElementClass() {
        return baseElementClass;
    }
}
