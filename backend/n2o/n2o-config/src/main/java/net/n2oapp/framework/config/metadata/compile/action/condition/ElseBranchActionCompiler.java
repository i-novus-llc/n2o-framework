package net.n2oapp.framework.config.metadata.compile.action.condition;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oElseBranchAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionActionPayload;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import org.springframework.stereotype.Component;

/**
 * Компиляция ветки else условного оператора if-else
 */
@Component
public class ElseBranchActionCompiler extends BaseConditionActionCompiler<N2oElseBranchAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oElseBranchAction.class;
    }

    @Override
    protected void compilePayload(N2oConditionBranch source, ConditionActionPayload payload,
                                  ConditionBranchesScope failBranchesScope, CompileContext<?, ?> context,
                                  CompileProcessor p, IndexScope indexScope) {
        setFromScope(payload, failBranchesScope);
        super.compilePayload(source, payload, failBranchesScope, context, p, indexScope);
    }
}
