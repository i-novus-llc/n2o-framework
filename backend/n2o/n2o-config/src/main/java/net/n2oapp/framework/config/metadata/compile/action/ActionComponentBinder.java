package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.ActionAware;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание с данными компонента с действием
 */
@Component
public class ActionComponentBinder implements BaseMetadataBinder<ActionAware> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ActionAware.class;
    }

    @Override
    public ActionAware bind(ActionAware compiled, BindProcessor p) {
        p.bind(compiled.getAction());
        return compiled;
    }
}