package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Базовое связывание данных в хедере
 */
@Component
public class ApplicationBinder implements BaseMetadataBinder<Application> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Application.class;
    }

    @Override
    public Application bind(Application compiled, BindProcessor p) {
        if (compiled.getHeader() != null && compiled.getHeader().getExtraMenu() != null
                && compiled.getHeader().getExtraMenu().getItems() != null)
            compiled.getHeader().getExtraMenu().getItems()
                    .stream()
                    .filter(item -> item.getTitle() != null)
                    .forEach(item -> item.setTitle(p.resolveText(item.getTitle())));
        return compiled;
    }
}
