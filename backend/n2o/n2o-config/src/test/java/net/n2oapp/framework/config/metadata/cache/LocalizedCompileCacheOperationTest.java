package net.n2oapp.framework.config.metadata.cache;

import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.config.compile.pipeline.operation.LocalizedCompileCacheOperation;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
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
public class LocalizedCompileCacheOperationTest {

    @Test
    public void test() {
        LocaleContextHolder.setLocale(new Locale("ru"));
        CacheTemplate cacheTemplate = mock(CacheTemplate.class);
        when(cacheTemplate.execute(eq("n2o.compiled"), eq("$test.Page.ru"), any())).thenReturn(getPage());
        LocalizedCompileCacheOperation operation = new LocalizedCompileCacheOperation(cacheTemplate);
        Object testPage = operation.execute(new PageContext("test"), null, this::getPage, null,
                null, null);
        assertThat(testPage instanceof SimplePage, is(true));
    }

    private SimplePage getPage() {
        SimplePage simplePage = new SimplePage();
        simplePage.setId("test");
        return simplePage;
    }
}
