package net.n2oapp.framework.config.reader;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.IOProcessorAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import org.jdom2.Namespace;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика, генерирующая сервисы чтения xml файлов(метаданных) в объекты n2o.
 * Подходящий сервис отыскивается сначала по имени DOM элемента, если он неуникальный поиск уточняется по namespaceUri.
 */
public class N2oNamespaceReaderFactory<T extends NamespaceUriAware> implements NamespaceReaderFactory<T, NamespaceReader<T>>,
        ApplicationContextAware, IOProcessorAware {

    private ApplicationContext applicationContext;
    private IOProcessor processor;
    // первый параметр - namespace, второй element
    private volatile Map<String, Map<String, NamespaceReader<T>>> engines;

    @Override
    public NamespaceReader<T> produce(String elementName, Namespace... namespaces) {
        if (engines == null)
            initFactory();
        Map<String, NamespaceReader<T>> elementReaders = new HashMap<>();
        for (Namespace namespace : namespaces) {
            if (engines.containsKey(namespace.getURI()))
                elementReaders.putAll(engines.get(namespace.getURI()));
        }
        if (elementReaders.isEmpty())
            throw new EngineNotFoundException(elementName);
        NamespaceReader reader = elementReaders.get(elementName);
        if (reader == null)
            throw new EngineNotFoundException(elementName);
        if (reader instanceof IOProcessorAware)
            ((IOProcessorAware) reader).setIOProcessor(this.processor);
        return reader;
    }

    private synchronized void initFactory() {
        if (engines == null) {
            Map<String, Map<String, NamespaceReader<T>>> result = new HashMap<>();
            Collection<NamespaceReader> beans = new ArrayList<>(applicationContext.getBeansOfType(NamespaceReader.class).values());
            if (processor != null) {
                for (NamespaceIO ioBean : applicationContext.getBeansOfType(NamespaceIO.class).values()) {
                    beans.add(new ProxyNamespaceIO<>(ioBean, processor));
                }
            }
            beans.forEach(b -> {
                String namespaceUri = b.getNamespaceUri();
                if (result.containsKey(namespaceUri)) {
                    result.get(namespaceUri).put(b.getElementName(), b);
                } else {
                    Map<String, NamespaceReader<T>> typedEngines = new HashMap<>();
                    typedEngines.put(b.getElementName(), b);
                    result.put(namespaceUri, typedEngines);
                }
            });
            engines = result;
        }
    }

    @Override
    public void add(NamespaceReader<T> reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setIOProcessor(IOProcessor processor) {
        this.processor = processor;
    }

}
