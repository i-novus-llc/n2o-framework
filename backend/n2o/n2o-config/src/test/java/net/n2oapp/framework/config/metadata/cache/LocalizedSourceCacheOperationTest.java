package net.n2oapp.framework.config.metadata.cache;

import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.config.compile.pipeline.operation.LocalizedCompileCacheOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.LocalizedSourceCacheOperation;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.register.XmlInfo;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование кэш операции с локализацией
 */
public class LocalizedSourceCacheOperationTest {

    @Test
    public void test() {
        LocaleContextHolder.setLocale(new Locale("ru"));
        CacheTemplate cacheTemplate = mock(CacheTemplate.class);
        MetadataRegister metadataRegister = mock(MetadataRegister.class);
        when(cacheTemplate.execute(eq("n2o.source"), eq("test.N2oPage.ru"), any())).thenReturn(getPage());
        when(metadataRegister.get("test", N2oPage.class)).thenReturn(new XmlInfo("test", N2oPage.class, "", ""));
        LocalizedSourceCacheOperation operation = new LocalizedSourceCacheOperation(cacheTemplate, metadataRegister);
        Object testPage = operation.execute(new PageContext("test"), null, this::getPage, null,
                null, null);
        assertThat(testPage instanceof N2oPage, is(true));
    }

    private N2oSimplePage getPage() {
        N2oSimplePage simplePage = new N2oSimplePage();
        simplePage.setId("test");
        return simplePage;
    }
}
