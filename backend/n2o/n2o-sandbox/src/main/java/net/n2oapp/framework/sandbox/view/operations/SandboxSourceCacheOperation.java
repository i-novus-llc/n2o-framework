package net.n2oapp.framework.sandbox.view.operations;

import com.sun.istack.NotNull;
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
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationTypeEnum;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.function.Supplier;

public class SandboxSourceCacheOperation<S extends SourceMetadata> extends MetadataChangeListener implements PipelineOperation<S, S>,
        PipelineOperationTypeAware,
        MetadataEnvironmentAware {

    public static final String CACHE_REGION = "n2o.source";
    private final CacheTemplate<String, S> cacheTemplate;
    private MetadataRegister metadataRegister;
    private final String projectId;

    public SandboxSourceCacheOperation(@NotNull String projectId, CacheManager cacheManager) {
        this.cacheTemplate = new CacheTemplate<>(cacheManager);
        this.projectId = projectId;
    }

    public SandboxSourceCacheOperation(@NotNull String projectId, CacheTemplate<String, S> cacheTemplate,
                                       MetadataRegister metadataRegister) {
        this.cacheTemplate = cacheTemplate;
        this.metadataRegister = metadataRegister;
        this.projectId = projectId;
    }

    @Override
    public PipelineOperationTypeEnum getPipelineOperationType() {
        return PipelineOperationTypeEnum.SOURCE_CACHE;
    }

    @Override
    public S execute(CompileContext<?, ?> context, DataSet data, Supplier<S> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor, SourceProcessor sourceProcessor) {
        String sourceId = context.getSourceId(bindProcessor);
        SourceInfo info = metadataRegister.get(sourceId, (Class<? extends SourceMetadata>) context.getSourceClass());
        String key = getKey(sourceId, info.getBaseSourceClass());
        return cacheTemplate.execute(CACHE_REGION, key, supplier::get);
    }

    @Override
    public void handleAllMetadataChange() {
        CacheManager cacheManager = cacheTemplate.getCacheManager();
        if (cacheManager == null)
            return;
        Cache cache = cacheManager.getCache(CACHE_REGION);
        if (cache == null)
            return;
        cache.clear();
    }

    @Override
    public void handleMetadataChange(String id, Class<? extends SourceMetadata> sourceClass) {
        CacheManager cacheManager = cacheTemplate.getCacheManager();
        if (cacheManager == null)
            return;
        Cache cache = cacheManager.getCache(CACHE_REGION);
        if (cache == null)
            return;
        cache.evict(getKey(id, sourceClass));
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.metadataRegister = environment.getMetadataRegister();
    }

    private String getKey(String id, Class<? extends SourceMetadata> sourceClass) {
        return projectId + "." + id + "." + sourceClass.getSimpleName();
    }
}
