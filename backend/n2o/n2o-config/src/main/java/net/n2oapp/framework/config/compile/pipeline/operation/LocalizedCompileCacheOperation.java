package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import org.springframework.cache.Cache;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * Операция кэширования собранных метаданных в конвеере с учетом локализации
 */
public class LocalizedCompileCacheOperation<S> extends CompileCacheOperation<S> {

    private String cacheRegionPrefix = "n2o.compiled.";

    public LocalizedCompileCacheOperation() {
        super();
    }

    public LocalizedCompileCacheOperation(CacheTemplate cacheTemplate) {
        super(cacheTemplate);
    }

    @Override
    public S execute(CompileContext<?,?> context, DataSet data, Supplier<S> supplier,
                     CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     ValidateProcessor validateProcessor) {
        String key = getKey(context, bindProcessor);
        Locale locale = LocaleContextHolder.getLocale();
        S compiled = (S) getCacheTemplate().execute(cacheRegionPrefix + locale.getLanguage(), key, () -> supplier.get());
        return compiled;
    }

    @Override
    public void handleAllMetadataChange() {
        Locale locale = LocaleContextHolder.getLocale();
        Cache cache = getCacheTemplate().getCacheManager().getCache(cacheRegionPrefix + locale.getLanguage());
        if (cache != null)
            cache.clear();
    }

}
