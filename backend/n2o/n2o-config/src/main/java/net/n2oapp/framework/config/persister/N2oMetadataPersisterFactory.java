package net.n2oapp.framework.config.persister;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.PersisterFactoryAware;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.IOProcessorAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import org.jdom2.Namespace;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика, генерирующая сервис для записи объектов в DOM элементы
 * Необходимый класс выбирается по типу объекта для записи, если для этого типа больше одного варианта,
 * то отбирается  N2oMetadataPersister с namespaceURI идентичным namespaceURI объекта
 */
public class N2oMetadataPersisterFactory
        implements NamespacePersisterFactory<NamespaceUriAware, NamespacePersister<NamespaceUriAware>>,
        ApplicationContextAware, IOProcessorAware {

    private ApplicationContext context;
    private IOProcessor processor;

    private volatile Map<String, Map<Class, NamespacePersister<NamespaceUriAware>>> engines;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public NamespacePersister<NamespaceUriAware> produce(Class clazz, Namespace... namespaces) {
        if (engines == null)
            initFactory();
        Map<Class, NamespacePersister<NamespaceUriAware>> typedEngines = new HashMap<>();
        for (Namespace namespace : namespaces) {
            if (engines.containsKey(namespace.getURI()))
                typedEngines.putAll(engines.get(namespace.getURI()));
        }
        if (typedEngines.isEmpty())
            throw new EngineNotFoundException(clazz);
        NamespacePersister<NamespaceUriAware> persister = typedEngines.get(clazz);
        if (persister == null)
            throw new EngineNotFoundException(clazz);
        if (persister instanceof PersisterFactoryAware)
            ((PersisterFactoryAware) persister).setPersisterFactory(this);
        if (persister instanceof IOProcessorAware)
            ((IOProcessorAware) persister).setIOProcessor(this.processor);
        return persister;
    }

    @Override
    public void add(NamespacePersister<NamespaceUriAware> persister) {
        throw new UnsupportedOperationException();
    }

    private synchronized void initFactory() {
        if (engines == null) {
            engines = new HashMap<>();
            Collection<NamespacePersister> beans = new ArrayList<>(context.getBeansOfType(NamespacePersister.class).values());
            if (processor != null) {
                for (NamespaceIO ioBean : context.getBeansOfType(NamespaceIO.class).values()) {
                    beans.add(new ProxyNamespaceIO<>(ioBean, processor));
                }
            }
            beans.forEach(b -> {
                if (engines.containsKey(b.getNamespaceUri())) {
                    engines.get(b.getNamespaceUri()).put(b.getElementClass(), b);
                } else {
                    Map<Class, NamespacePersister<NamespaceUriAware>> typedEngines = new HashMap<>();
                    typedEngines.put(b.getElementClass(), b);
                    engines.put(b.getNamespaceUri(), typedEngines);
                }
            });
        }
    }

    @Override
    public void setIOProcessor(IOProcessor processor) {
        this.processor = processor;
    }

}
