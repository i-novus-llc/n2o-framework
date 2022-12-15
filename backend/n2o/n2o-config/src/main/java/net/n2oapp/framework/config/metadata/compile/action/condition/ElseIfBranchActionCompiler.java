package net.n2oapp.framework.config.metadata.compile.action.condition;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseIfBranchAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionActionPayload;
import net.n2oapp.framework.config.metadata.compile.PageIndexScope;
import org.springframework.stereotype.Component;

/**
 * Компиляция ветки else-if условного оператора if-else
 */
@Component
public class ElseIfBranchActionCompiler extends BaseConditionActionCompiler<N2oElseIfBranchAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oElseIfBranchAction.class;
    }

    @Override
    protected void compilePayload(N2oConditionBranch source, ConditionActionPayload payload,
                                  ConditionBranchesScope failBranchesScope, CompileContext<?, ?> context,
                                  CompileProcessor p, PageIndexScope indexScope) {
        setFromScope(payload, failBranchesScope);
        super.compilePayload(source, payload, failBranchesScope, context, p, indexScope);
    }
}
