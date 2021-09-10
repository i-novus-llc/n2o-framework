package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.MetadataRegister;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Операция кэширования исходных метаданных в конвеере с учетом локализации
 */
public class LocalizedSourceCacheOperation<S extends SourceMetadata> extends SourceCacheOperation<S> {

    public LocalizedSourceCacheOperation() {
        super();
    }

    public LocalizedSourceCacheOperation(CacheTemplate cacheTemplate, MetadataRegister metadataRegister) {
        super(cacheTemplate, metadataRegister);
    }

    @Override
    protected String getKey(String id, Class<? extends SourceMetadata> sourceClass) {
        Locale locale = LocaleContextHolder.getLocale();
        return id + "." + sourceClass.getSimpleName() + "." + locale.getLanguage();
    }
}
