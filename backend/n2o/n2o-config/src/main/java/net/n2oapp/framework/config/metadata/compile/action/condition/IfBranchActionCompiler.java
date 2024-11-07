package net.n2oapp.framework.config.metadata.compile.action.condition;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionActionPayload;
import net.n2oapp.framework.config.metadata.compile.PageIndexScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

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
                                  ConditionBranchesScope failBranchesScope, CompileContext<?, ?> context,
                                  CompileProcessor p, PageIndexScope indexScope) {
        N2oIfBranchAction ifSource = (N2oIfBranchAction) source;
        initDatasource(payload, ifSource, p);
        payload.setModel(castDefault(ifSource.getModel(), () -> getLocalModel(p)));
        super.compilePayload(source, payload, failBranchesScope, context, p, indexScope);
    }

    private void initDatasource(ConditionActionPayload payload, N2oIfBranchAction source, CompileProcessor p) {
        payload.setDatasource(getClientDatasourceId(castDefault(source.getDatasourceId(), () -> getLocalDatasourceId(p)), p.getScope(PageScope.class).getPageId()));
        if (payload.getDatasource() == null) {
            throw new N2oException(String.format("Источник данных не определен для \"<if-branch>\" с атрибутом 'test=%s'",
                    source.getTest()));
        }
    }
}
