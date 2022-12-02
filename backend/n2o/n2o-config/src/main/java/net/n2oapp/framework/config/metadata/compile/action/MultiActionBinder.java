package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание действий последовательности с данными
 */
@Component
public class MultiActionBinder implements BaseMetadataBinder<MultiAction> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return MultiAction.class;
    }

    @Override
    public MultiAction bind(MultiAction compiled, BindProcessor p) {
        if (compiled.getPayload().getActions() != null)
            compiled.getPayload().getActions().forEach(p::bind);
        return compiled;
    }
}
