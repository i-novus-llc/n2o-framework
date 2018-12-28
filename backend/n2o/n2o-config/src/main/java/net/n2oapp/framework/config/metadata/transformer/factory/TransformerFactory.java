package net.n2oapp.framework.config.metadata.transformer.factory;

import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.transformer.ClientTransformer;
import net.n2oapp.framework.api.transformer.CompileTransformer;
import net.n2oapp.framework.api.transformer.SourceTransformer;
import net.n2oapp.framework.api.transformer.Transformer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Фабрика трансформаторов метаданных
 */
@Deprecated
public class TransformerFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private volatile Map<Key, List<Transformer>> engines;
    private volatile Collection<Transformer> transformers;

    public TransformerFactory() {
    }

    public <T extends SourceMetadata,
            C extends CompileContext> SourceTransformer<T,C> produceSource(T metadata,
                                                                               C context) {
        Class<T> metadataClass = metadata != null ? (Class<T>) metadata.getClass() : null;
        Class<C> contextClass = context != null ? (Class<C>) context.getClass() : null;
        return new SourceTransformerWrapper<>(metadataClass,
                contextClass,
                getEngines(metadataClass, null, null, contextClass));
    }

    public <T extends SourceMetadata,
            D extends CompiledMetadata,
            M extends ClientMetadata,
            C extends CompileContext> CompileTransformer<D,C> produceCompile(D metadata,
                                                                                 C context) {
        Class<D> metadataClass = metadata != null ? (Class<D>) metadata.getClass() : null;
        Class<C> contextClass = context != null ? (Class<C>) context.getClass() : null;
        return new CompileTransformerWrapper<>(metadataClass,
                contextClass,
                getEngines(null, metadataClass, null, contextClass));
    }

    public <T extends SourceMetadata,
            D extends CompiledMetadata,
            M extends ClientMetadata,
            C extends CompileContext> ClientTransformer<M,C> produceClient(M metadata,
                                                                               C context) {
        Class<M> metadataClass = metadata != null ? (Class<M>) metadata.getClass() : null;
        Class<C> contextClass = context != null ? (Class<C>) context.getClass() : null;
        return new ClientTransformerWrapper<>(metadataClass,
                contextClass,
                getEngines(null, null, metadataClass, contextClass));
    }



    @SuppressWarnings("unchecked")
    private <T extends SourceMetadata,
            D extends CompiledMetadata,
            M extends ClientMetadata,
            C extends CompileContext> boolean filter(Key<T,D,M,C> key, Transformer transformer) {
        return (key.sourceClass == null || transformer.getMetadataClass() == null || transformer.getMetadataClass().isAssignableFrom(key.sourceClass))
                && (key.compiledClass == null || transformer.getCompiledMetadataClass() == null || transformer.getCompiledMetadataClass().isAssignableFrom(key.compiledClass))
                && (key.clientClass == null || transformer.getClientMetadataClass() == null || transformer.getClientMetadataClass().isAssignableFrom(key.clientClass))
                && ((key.contextClass == null && (transformer.getContextClass() == null || transformer.getContextClass().equals(CompileContext.class))) || (key.contextClass != null && transformer.getContextClass().isAssignableFrom(key.contextClass)));
    }

    private <T extends SourceMetadata,
            D extends CompiledMetadata,
            M extends ClientMetadata,
            C extends CompileContext> List<Transformer> getEngines(Class<T> sourceClass,
                                                                          Class<D> compiledClass,
                                                                          Class<M> clientClass,
                                                                          Class<C> contextClass) {
        if (engines == null)
            initFactory();
        Key<T, D, M, C> key = new Key<>(sourceClass, compiledClass, clientClass, contextClass);
        List<Transformer> found = engines.get(key);
        if (found == null) {
            found = initTransformers(key);
        }
        return found;
    }

    private synchronized <T extends SourceMetadata,
            D extends CompiledMetadata,
            M extends ClientMetadata,
            C extends CompileContext> List<Transformer> initTransformers(Key<T,D,M,C> key) {
        List<Transformer> found = engines.get(key);
        if (found == null) {
            found = transformers.stream().filter(t -> filter(key, t)).collect(Collectors.toList());
            engines.put(key, found);
        }
        return found;
    }

    private synchronized void initFactory() {
        if (engines == null || transformers == null) {
            transformers = new ArrayList<>(getBeans());
            engines = new LinkedMultiValueMap<>();
        }
    }

    protected Collection<Transformer> getBeans() {
        return OverrideBean.removeOverriddenBeans(applicationContext.getBeansOfType(Transformer.class)).values();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static class Key<T extends SourceMetadata,
            D extends CompiledMetadata,
            M extends ClientMetadata,
            C extends CompileContext> {
        private Class<T> sourceClass;
        private Class<D> compiledClass;
        private Class<M> clientClass;
        private Class<C> contextClass;

        public Key(Class<T> sourceClass, Class<D> compiledClass, Class<M> clientClass, Class<C> contextClass) {
            this.sourceClass = sourceClass;
            this.compiledClass = compiledClass;
            this.clientClass = clientClass;
            this.contextClass = contextClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key<?, ?, ?, ?> key = (Key<?, ?, ?, ?>) o;
            return Objects.equals(sourceClass, key.sourceClass) &&
                    Objects.equals(compiledClass, key.compiledClass) &&
                    Objects.equals(clientClass, key.clientClass) &&
                    Objects.equals(contextClass, key.contextClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceClass, compiledClass, clientClass, contextClass);
        }

        @Override
        public String toString() {
            return "Key{" +
                    "sourceClass=" + sourceClass +
                    ", compiledClass=" + compiledClass +
                    ", clientClass=" + clientClass +
                    ", contextClass=" + contextClass +
                    '}';
        }
    }

}
