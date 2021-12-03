package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
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
        if (header != null) {
            bindMenu(header.getMenu(), p);
            bindMenu(header.getExtraMenu(), p);
        }
    }

    private void bindSidebar(BindProcessor p, Sidebar sidebar) {
        if (sidebar != null) {
            bindMenu(sidebar.getMenu(), p);
            bindMenu(sidebar.getExtraMenu(), p);
        }
    }

    private void bindMenu(SimpleMenu menu, BindProcessor p) {
        if (menu != null && menu.getItems() != null)
            menu.getItems()
                .forEach(i -> resolveItem(i, p));
    }

    private void resolveItem(HeaderItem item, BindProcessor p) {
        item.setTitle(p.resolveText(item.getTitle()));
        item.setBadge(p.resolve(item.getBadge()));
        item.setBadgeColor(p.resolve(item.getBadgeColor(), String.class));
        item.setImageSrc(p.resolve(item.getImageSrc(), String.class));
        if (item.getSubItems() != null)
            item.getSubItems().forEach(i -> resolveItem(i, p));
    }
}
