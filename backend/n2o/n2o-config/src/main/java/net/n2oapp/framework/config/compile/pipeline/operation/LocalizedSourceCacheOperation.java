package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import org.springframework.cache.Cache;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * Операция кэширования исходных метаданных в конвеере с учетом локализации
 */
public class LocalizedSourceCacheOperation<S extends SourceMetadata> extends SourceCacheOperation<S> {

    private String cacheRegionPrefix = "n2o.source.";

    public LocalizedSourceCacheOperation() {
        super();
    }

    public LocalizedSourceCacheOperation(CacheTemplate cacheTemplate, MetadataRegister metadataRegister) {
        super(cacheTemplate, metadataRegister);
    }

    @Override
    public S execute(CompileContext<?, ?> context, DataSet data, Supplier<S> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     ValidateProcessor validateProcessor) {
        String sourceId = context.getSourceId(bindProcessor);
        SourceInfo info = getMetadataRegister().get(sourceId, (Class<? extends SourceMetadata>) context.getSourceClass());
        String key = getKey(sourceId, info.getBaseSourceClass());
        Locale locale = LocaleContextHolder.getLocale();
        S source = (S) getCacheTemplate().execute(cacheRegionPrefix + locale.getLanguage(), key, () -> supplier.get());
        return source;
    }

    @Override
    public void handleAllMetadataChange() {
        Locale locale = LocaleContextHolder.getLocale();
        String cacheRegion = cacheRegionPrefix + locale.getLanguage();
        Cache cache = getCacheTemplate().getCacheManager().getCache(cacheRegion);
        if (cache != null)
            getCacheTemplate().getCacheManager().getCache(cacheRegion).clear();
    }

    @Override
    public void handleMetadataChange(String id, Class<? extends SourceMetadata> sourceClass) {
        Locale locale = LocaleContextHolder.getLocale();
        String cacheRegion = cacheRegionPrefix + locale.getLanguage();
        Cache cache = getCacheTemplate().getCacheManager().getCache(cacheRegion);
        if (cache != null)
            getCacheTemplate().getCacheManager().getCache(cacheRegion).evict(getKey(id, sourceClass));
    }

}
