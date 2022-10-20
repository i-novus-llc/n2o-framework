package net.n2oapp.framework.config.metadata.compile.action.condition;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionActionPayload;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import org.springframework.stereotype.Component;

/**
 * Компиляция ветки if условного оператора if-else
 */
@Component
public class IfBranchActionCompiler extends BaseConditionActionCompiler<N2oIfBranchAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oIfBranchAction.class;
    }

    @Override
    protected void compilePayload(N2oConditionBranch source, ConditionActionPayload payload,
                                  ConditionBranchesScope failBranchesScope, CompileContext<?, ?> context, CompileProcessor p, IndexScope indexScope) {
        N2oIfBranchAction ifSource = (N2oIfBranchAction) source;
        payload.setDatasource(p.cast(ifSource.getDatasourceId(), getLocalDatasourceId(p)));
        payload.setModel(p.cast(ifSource.getModel(), getLocalModel(p)));
        super.compilePayload(source, payload, failBranchesScope, context, p, indexScope);
    }
}
