package net.n2oapp.framework.sandbox.server.view.operations;

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
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.validation.constraints.NotNull;
import java.util.function.Supplier;

public class SandboxSourceCacheOperation<S extends SourceMetadata> extends MetadataChangeListener implements PipelineOperation<S, S>,
        PipelineOperationTypeAware,
        MetadataEnvironmentAware {

    public static String CACHE_REGION = "n2o.source";
    private CacheTemplate cacheTemplate;
    private MetadataRegister metadataRegister;
    private String projectId;

    public SandboxSourceCacheOperation(@NotNull String projectId, CacheManager cacheManager) {
        this.cacheTemplate = new CacheTemplate(cacheManager);
        this.projectId = projectId;
    }

    public SandboxSourceCacheOperation(@NotNull String projectId, CacheTemplate cacheTemplate, MetadataRegister metadataRegister) {
        this.cacheTemplate = cacheTemplate;
        this.metadataRegister = metadataRegister;
        this.projectId = projectId;
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.SOURCE_CACHE;
    }

    @Override
    public S execute(CompileContext<?, ?> context, DataSet data, Supplier<S> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor, SourceProcessor sourceProcessor) {
        String sourceId = context.getSourceId(bindProcessor);
        SourceInfo info = metadataRegister.get(sourceId, (Class<? extends SourceMetadata>) context.getSourceClass());
        String key = getKey(sourceId, info.getBaseSourceClass());
        S source = (S) cacheTemplate.execute(CACHE_REGION, key, () -> supplier.get());
        return source;
    }

    @Override
    public void handleAllMetadataChange() {
        Cache cache = cacheTemplate.getCacheManager().getCache(CACHE_REGION);
        if (cache != null)
            cacheTemplate.getCacheManager().getCache(CACHE_REGION).clear();
    }

    @Override
    public void handleMetadataChange(String id, Class<? extends SourceMetadata> sourceClass) {
        Cache cache = cacheTemplate.getCacheManager().getCache(CACHE_REGION);
        if (cache != null)
            cacheTemplate.getCacheManager().getCache(CACHE_REGION).evict(getKey(id, sourceClass));
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.metadataRegister = environment.getMetadataRegister();
    }

    public void setCacheTemplate(CacheTemplate cacheTemplate) {
        this.cacheTemplate = cacheTemplate;
    }

    private String getKey(String id, Class<? extends SourceMetadata> sourceClass) {
        return projectId + "." + id + "." + sourceClass.getSimpleName();
    }
}
