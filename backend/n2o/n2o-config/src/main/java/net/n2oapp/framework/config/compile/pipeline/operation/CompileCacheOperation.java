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
import org.springframework.cache.Cache;
import org.springframework.cache.support.NoOpCacheManager;

import java.util.function.Supplier;

/**
 * Операция кэширования собранных метаданных в конвеере
 */
public class CompileCacheOperation<S> extends MetadataChangeListener implements PipelineOperation<S, S>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private CacheTemplate cacheTemplate;
    private String cacheRegion = "n2o.compiled";

    public CompileCacheOperation() {
        this.cacheTemplate = new CacheTemplate(new NoOpCacheManager());
    }

    public CompileCacheOperation(CacheTemplate cacheTemplate) {
        this.cacheTemplate = cacheTemplate;
    }

    @Override
    public S execute(CompileContext<?,?> context, DataSet data, Supplier<S> supplier,
                     CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     SourceProcessor sourceProcessor) {
        String key = getKey(context, bindProcessor);
        S compiled = (S) cacheTemplate.execute(cacheRegion, key, () -> supplier.get());
        return compiled;
    }

    @Override
    public void handleAllMetadataChange() {
        Cache cache = cacheTemplate.getCacheManager().getCache(cacheRegion);
        if (cache != null)
            cache.clear();
    }

    @Override
    public void handleMetadataChange(String id, Class<? extends SourceMetadata> sourceClass) {
        handleAllMetadataChange();
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
    }

    public void setCacheTemplate(CacheTemplate cacheTemplate) {
        this.cacheTemplate = cacheTemplate;
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.COMPILE_CACHE;
    }


    protected String getKey(CompileContext<?, ?> context, BindProcessor p) {
        return context.getCompiledId(p) + "." + context.getCompiledClass().getSimpleName();
    }

    protected CacheTemplate getCacheTemplate() {
        return cacheTemplate;
    }
}
