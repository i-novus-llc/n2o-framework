package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class SetValueActionBinder implements BaseMetadataBinder<SetValueAction> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return SetValueAction.class;
    }

    @Override
    public SetValueAction bind(SetValueAction action, BindProcessor p) {
        String sourceMapper = p.resolveTextWithQuotes(action.getPayload().getSourceMapper());
        action.getPayload().setSourceMapper(sourceMapper);
        action.getPayload().getSource().setField(p.resolveTextByParams(action.getPayload().getSource().getField()));
        action.getPayload().getTarget().setField(p.resolveTextByParams(action.getPayload().getTarget().getField()));
        return action;
    }
}
