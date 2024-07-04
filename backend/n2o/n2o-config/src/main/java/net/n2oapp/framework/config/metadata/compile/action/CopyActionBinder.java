package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание действия copy
 */
@Component
public class CopyActionBinder implements BaseMetadataBinder<CopyAction> {
    @Override
    public CopyAction bind(CopyAction action, BindProcessor p) {
        if (action.getPayload().getSource().getField() != null)
            action.getPayload().getSource().setField(p.resolveTextByParams(action.getPayload().getSource().getField()));
        if (action.getPayload().getTarget().getField() != null)
            action.getPayload().getTarget().setField(p.resolveTextByParams(action.getPayload().getTarget().getField()));
        return action;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CopyAction.class;
    }
}
