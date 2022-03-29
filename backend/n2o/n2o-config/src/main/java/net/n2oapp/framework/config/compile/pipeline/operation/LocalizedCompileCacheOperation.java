package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Операция кэширования собранных метаданных в конвейере с учетом локализации
 */
public class LocalizedCompileCacheOperation<S> extends CompileCacheOperation<S> {

    public LocalizedCompileCacheOperation() {
        super();
    }

    public LocalizedCompileCacheOperation(CacheTemplate cacheTemplate) {
        super(cacheTemplate);
    }

    @Override
    protected String getKey(CompileContext<?, ?> context, BindProcessor p) {
        Locale locale = LocaleContextHolder.getLocale();
        return context.getCompiledId(p) + "." + context.getCompiledClass().getSimpleName() + "." + locale.getLanguage();
    }
}
