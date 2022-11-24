package net.n2oapp.framework.config.metadata.compile.action.condition;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionAction;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание с данными fail и success действий условного действия
 */
@Component
public class ConditionActionBinder implements BaseMetadataBinder<ConditionAction> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ConditionAction.class;
    }

    @Override
    public ConditionAction bind(ConditionAction compiled, BindProcessor p) {
        Action success = compiled.getPayload().getSuccess();
        if (success != null)
            p.bind(success);
        Action fail = compiled.getPayload().getFail();
        if (fail != null)
            p.bind(fail);
        return compiled;
    }
}
