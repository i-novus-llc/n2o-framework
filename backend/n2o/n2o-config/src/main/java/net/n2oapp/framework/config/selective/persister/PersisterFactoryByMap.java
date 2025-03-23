package net.n2oapp.framework.config.selective.persister;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.PersisterFactoryAware;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.IOProcessorAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import org.jdom2.Namespace;

import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика, генерирующая сервис для записи объектов в DOM элементы в тестовой среде
 */
public class PersisterFactoryByMap implements NamespacePersisterFactory<NamespaceUriAware, NamespacePersister<NamespaceUriAware>>, IOProcessorAware, MetadataEnvironmentAware {

    @Getter
    @Setter
    private Map<Class<?>, Map<String, NamespacePersister>> map = new HashMap<>();
    private IOProcessor ioProcessor = new IOProcessorImpl(this);


    @SuppressWarnings("unchecked")
    @Deprecated
    public void register(NamespacePersister<? extends NamespaceUriAware> persister) {
        map.computeIfAbsent(persister.getElementClass(), n -> new HashMap<>()).put(persister.getNamespaceUri(), persister);
    }

    @Deprecated
    public void register(NamespaceIO<? extends NamespaceUriAware> io) {
        register(new ProxyNamespaceIO<>(io));
    }


    @Override
    public NamespacePersister<NamespaceUriAware> produce(Class clazz, Namespace... namespaces) {
        if (!map.containsKey(clazz))
            throw new EngineNotFoundException(clazz);
        Map<String, NamespacePersister> inmap = map.get(clazz);
        NamespacePersister persister = null;
        for (Namespace namespace : namespaces) {
            if (inmap.containsKey(namespace.getURI())) {
                persister = inmap.get(namespace.getURI());
                break;
            }
        }
        if (persister == null) {
            throw new EngineNotFoundException(clazz);
        }
        if (persister instanceof PersisterFactoryAware)
            ((PersisterFactoryAware) persister).setPersisterFactory(this);
        if (persister instanceof IOProcessorAware)
            ((IOProcessorAware) persister).setIOProcessor(this.ioProcessor);
        return persister;
    }

    @Override
    public void add(NamespacePersister<NamespaceUriAware> persister) {
        map.computeIfAbsent(persister.getElementClass(), n -> new HashMap<>()).put(persister.getNamespaceUri(), persister);
    }

    @Override
    public void setIOProcessor(IOProcessor processor) {
        this.ioProcessor = processor;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        if (ioProcessor != null && ioProcessor instanceof IOProcessorImpl) {
            ((IOProcessorImpl) ioProcessor).setSystemProperties(environment.getSystemProperties());
            ((IOProcessorImpl) ioProcessor).setMessageSourceAccessor(environment.getMessageSource());
        }
    }
}