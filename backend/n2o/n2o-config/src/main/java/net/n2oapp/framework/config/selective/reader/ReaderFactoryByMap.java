package net.n2oapp.framework.config.selective.reader;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.ReaderFactoryAware;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.IOProcessorAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.reader.*;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import org.jdom.Namespace;

import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика, генерирующая сервис для чтения xml файлов в объекты n2o
 */
public class ReaderFactoryByMap implements NamespaceReaderFactory, IOProcessorAware, MetadataEnvironmentAware {

    // первый параметр - namespace, второй element
    private Map<String, Map<String, NamespaceReader>> map = new HashMap<>();
    private IOProcessor ioProcessor = new IOProcessorImpl(this);


    public ReaderFactoryByMap() {
    }


    public ReaderFactoryByMap register(NamespaceReader reader) {
        add(reader);
        return this;
    }

    public ReaderFactoryByMap register(NamespaceIO io) {
        return register(new ProxyNamespaceIO(io, this.ioProcessor));
    }

    @Override
    public NamespaceReader produce(Namespace namespace, String elementName) {
        Map<String, NamespaceReader> innerEngines = map.get(namespace.getURI());
        if (innerEngines == null)
            throw new EngineNotFoundException(namespace.getURI());
        NamespaceReader reader;
        reader = innerEngines.get(elementName);
        if (reader == null)
            throw new EngineNotFoundException(elementName);
        if (reader instanceof ReaderFactoryAware)
            ((ReaderFactoryAware) reader).setReaderFactory(this);
        if (reader instanceof IOProcessorAware)
            ((IOProcessorAware) reader).setIOProcessor(this.ioProcessor);
        return reader;
    }

    @Override
    public boolean check(Namespace namespace, String elementName) {
        Map<String, NamespaceReader> innerEngines = map.get(namespace.getURI());
        return innerEngines != null && innerEngines.get(elementName) != null;
    }

    @Override
    public void add(NamespaceReader reader) {
        map.computeIfAbsent(reader.getNamespaceUri(), (n) -> new HashMap<>()).put(reader.getElementName(), reader);
    }

    @Override
    public void setIOProcessor(IOProcessor ioProcessor) {
        this.ioProcessor = ioProcessor;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        if (ioProcessor != null && ioProcessor instanceof IOProcessorImpl) {
            ((IOProcessorImpl) ioProcessor).setSystemProperties(environment.getSystemProperties());
            ((IOProcessorImpl) ioProcessor).setMessageSourceAccessor(environment.getMessageSource());
        }
    }
}