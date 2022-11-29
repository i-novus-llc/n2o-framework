package net.n2oapp.framework.config.metadata.compile.action.condition;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseBranchAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionActionPayload;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.action.AbstractActionCompiler;
import net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 *  Базовая компиляция веток оператора if-else
 */
public abstract class BaseConditionActionCompiler<S extends N2oConditionBranch>
        extends AbstractActionCompiler<ConditionAction, S> {

    @Override
    public ConditionAction compile(S source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        IndexScope indexScope = p.getScope(IndexScope.class);
        if (indexScope == null)
            indexScope = new IndexScope();
        setBranchId(source, indexScope);
        ConditionAction conditionAction = new ConditionAction();
        conditionAction.setType(p.resolve(property("n2o.api.action.condition.type"), String.class));

        ConditionBranchesScope failBranchesScope = p.getScope(ConditionBranchesScope.class);
        compilePayload(source, conditionAction.getPayload(), failBranchesScope, context, p, indexScope);
        compileAction(conditionAction, source, p);
        return conditionAction;
    }

    protected void compilePayload(N2oConditionBranch source, ConditionActionPayload payload,
                                  ConditionBranchesScope failBranchesScope, CompileContext<?, ?> context,
                                  CompileProcessor p, IndexScope indexScope) {
        payload.setCondition(source.getTest());
        payload.setSuccess(compileSuccess(source, context, p, indexScope));
        payload.setFail(compileFail(payload, failBranchesScope, context, p, indexScope));
    }

    protected void setFromScope(ConditionActionPayload payload, ConditionBranchesScope failBranchesScope) {
        payload.setDatasource(failBranchesScope.getDatasourceId());
        payload.setModel(failBranchesScope.getModel());
    }

    private Action compileFail(ConditionActionPayload compiled, ConditionBranchesScope failBranchesScope,
                               CompileContext<?, ?> context, CompileProcessor p, IndexScope indexScope) {
        N2oConditionBranch branch = failBranchesScope.pop();
        if (branch == null)
            return null;
        if (branch instanceof N2oElseBranchAction) {
            setBranchId(branch, indexScope);
            return compileActionAware(branch, context, p, indexScope);
        }

        return p.compile(branch, context,
                new ConditionBranchesScope(failBranchesScope, compiled.getDatasource(), compiled.getModel()),
                indexScope);
    }

    private Action compileSuccess(N2oConditionBranch source, CompileContext<?, ?> context, CompileProcessor p, IndexScope indexScope) {
        return compileActionAware(source, context, p, indexScope);
    }

    private Action compileActionAware(N2oConditionBranch source, CompileContext<?, ?> context, CompileProcessor p,
                                      IndexScope indexScope) {
        source.setActions(ActionCompileStaticProcessor.initActions(source, p));
        return ActionCompileStaticProcessor.compileAction(source, context, p, null, indexScope);
    }

    private void setBranchId(N2oConditionBranch branch, IndexScope indexScope) {
        branch.setId("condition_" + indexScope.get());
    }
}
