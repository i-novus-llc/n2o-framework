package net.n2oapp.framework.config.metadata.compile.header;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.header.CompiledHeader;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Базовое связывание данных в хедере
 */
@Component
public class HeaderBinder implements BaseMetadataBinder<CompiledHeader> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CompiledHeader.class;
    }

    @Override
    public CompiledHeader bind(CompiledHeader compiled, BindProcessor p) {
        if (compiled.getExtraItems() != null)
            compiled.getExtraItems()
                    .stream()
                    .filter(item -> item.getLabel() != null)
                    .forEach(item -> item.setLabel(p.resolveText(item.getLabel())));
        return compiled;
    }
}
