package net.n2oapp.framework.config.dynamic;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Поставщик динамических страниц
 * context - ссылка на страницу
 */
public class DynamicPageProvider implements DynamicMetadataProvider {
    @Override
    public String getCode() {
        return "page";
    }

    @Override
    public <T extends SourceMetadata> List<T> read(String context) {
        //todo хорошо бы избавиться от необходимость заменять id в динамических страницах, тогда бы и не пришлось клонировать
        N2oPage page = CompilerHolder.get().copy(CompilerHolder.get().getGlobal(context, N2oPage.class));
        page.setId(getCode() + "$" + context);
        return Collections.singletonList((T) page);
    }

    @Override
    public Collection<Class<? extends SourceMetadata>> getMetadataClasses() {
        return Arrays.asList(N2oStandardPage.class, N2oSimplePage.class);
    }
}
