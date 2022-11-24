package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.switchaction.SwitchAction;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Связывание с данными кейсов действия switch
 */
@Component
public class SwitchActionBinder implements BaseMetadataBinder<SwitchAction> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return SwitchAction.class;
    }

    @Override
    public SwitchAction bind(SwitchAction compiled, BindProcessor p) {
        Map<String, Action> cases = compiled.getPayload().getCases();
        if (cases != null)
            cases.values().forEach(p::bind);
        p.bind(compiled.getPayload().getDefaultCase());
        return compiled;
    }
}
