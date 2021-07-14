package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Базовое связывание данных в шапке
 */
@Component
public class ApplicationBinder implements BaseMetadataBinder<Application> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Application.class;
    }

    @Override
    public Application bind(Application compiled, BindProcessor p) {
        bindHeader(p, compiled.getHeader());
        bindSidebar(p, compiled.getSidebar());
        return compiled;
    }

    private void bindHeader(BindProcessor p, Header header) {
        if (header != null && header.getExtraMenu() != null
                && header.getExtraMenu().getItems() != null)
            header.getExtraMenu().getItems()
                    .stream()
                    .filter(item -> item.getTitle() != null)
                    .forEach(item -> item.setTitle(p.resolveText(item.getTitle())));
    }

    private void bindSidebar(BindProcessor p, Sidebar sidebar) {
        if (sidebar != null && sidebar.getExtraMenu() != null
                && sidebar.getExtraMenu().getItems() != null)
            sidebar.getExtraMenu().getItems()
                    .stream()
                    .filter(item -> item.getTitle() != null)
                    .forEach(item -> item.setTitle(p.resolveText(item.getTitle())));
    }
}
