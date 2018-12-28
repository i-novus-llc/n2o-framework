package net.n2oapp.framework.config.factory;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.engine.factory.EngineNotUniqueException;
import net.n2oapp.engine.factory.locator.EngineLocator;
import net.n2oapp.engine.factory.locator.SimpleEngineLocator;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * Базовый класс для всех фабрик метаданных
 * @param <G> Тип движка
 */
public abstract class BaseMetadataFactory<G> implements MetadataFactory<G>, MetadataEnvironmentAware {
    private MetadataEnvironment environment;
    private SimpleEngineLocator<G> locator;

    public BaseMetadataFactory() {
        locator = new SimpleEngineLocator<>(new HashMap<>());
    }

    public BaseMetadataFactory(Map<String, G> beans) {
        locator = new SimpleEngineLocator<>(beans);
    }

    public <T> G produce(BiPredicate<G, T> predicate, T target) {
        G engine = null;
        try {
            engine = getLocator().locate(predicate).produce(target);
        } catch (EngineNotFoundException e) {
            throw new EngineNotFoundException(this.getClass(), e.getType());
        } catch (EngineNotUniqueException e) {
            throw new EngineNotUniqueException(this.getClass(), e.getType());
        }
        enrich(engine);
        return engine;
    }

    public <T> List<G> produceList(BiPredicate<G, T> predicate, T target) {
        List<G> engines = getLocator().locate(predicate).produceList(target);
        engines.forEach(this::enrich);
        return engines;
    }

    @Override
    public MetadataFactory<G> add(G... g) {
        for (G engine : g) {
            locator.add(engine);
        }
        return this;
    }

    protected EngineLocator<G> getLocator() {
        return locator;
    }

    protected void enrich(G engine) {
        AwareFactorySupport.enrich(engine, environment);
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.environment = environment;
    }
}
