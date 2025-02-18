package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.event.MetadataChangeListener;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.support.NoOpCacheManager;

import java.util.function.Supplier;

/**
 * Операция кэширования исходных метаданных в конвейере
 */
public class SourceCacheOperation<S extends SourceMetadata> extends MetadataChangeListener implements PipelineOperation<S, S>,
        PipelineOperationTypeAware, MetadataEnvironmentAware {

    private static final String CACHE_REGION = "n2o.source";
    private final CacheTemplate<String, S> cacheTemplate;
    private MetadataRegister metadataRegister;

    public SourceCacheOperation() {
        this.cacheTemplate = new CacheTemplate<>(new NoOpCacheManager());
    }

    public SourceCacheOperation(CacheTemplate<String, S> cacheTemplate, MetadataRegister metadataRegister) {
        this.cacheTemplate = cacheTemplate;
        this.metadataRegister = metadataRegister;
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.SOURCE_CACHE;
    }

    @Override
    public S execute(CompileContext<?, ?> context, DataSet data, Supplier<S> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     SourceProcessor sourceProcessor) {
        String sourceId = context.getSourceId(bindProcessor);
        SourceInfo info = metadataRegister.get(sourceId, (Class<? extends SourceMetadata>) context.getSourceClass());
        String key = getKey(sourceId, info.getBaseSourceClass());
        return cacheTemplate.execute(CACHE_REGION, key, supplier::get);
    }

    @Override
    public void handleAllMetadataChange() {
        Cache cache = cacheTemplate.getCacheManager().getCache(CACHE_REGION);
        if (cache != null)
            cacheTemplate.getCacheManager().getCache(CACHE_REGION).clear();
    }

    @Override
    public void handleMetadataChange(String id, Class<? extends SourceMetadata> sourceClass) {
        handleAllMetadataChange();
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.metadataRegister = environment.getMetadataRegister();
    }

    protected String getKey(String id, Class<? extends SourceMetadata> sourceClass) {
        return id + "." + sourceClass.getSimpleName();
    }

    protected MetadataRegister getMetadataRegister() {
        return metadataRegister;
    }
}
