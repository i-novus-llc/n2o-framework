package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;

/**
 * Абстрактный класс для связывания конкретных страниц с данными
 */
public abstract class AbstractFilteredPageBinder implements BaseMetadataBinder<Page> {
    private final String pageId;

    /**
     * Конструктор
     * @param pageId Страница, которую необходимо связать с данными
     */
    protected AbstractFilteredPageBinder(String pageId) {
        this.pageId = pageId;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Page.class;
    }

    @Override
    public boolean matches(Page compiled, BindProcessor p) {
        return compiled.getId().equals(pageId);
    }
}
