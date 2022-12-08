package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oSwitchAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.switchaction.SwitchAction;
import net.n2oapp.framework.api.metadata.meta.action.switchaction.SwitchActionPayload;
import net.n2oapp.framework.config.metadata.compile.PageIndexScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция действия switch
 */
@Component
public class SwitchActionCompiler extends AbstractActionCompiler<SwitchAction, N2oSwitchAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSwitchAction.class;
    }

    @Override
    public SwitchAction compile(N2oSwitchAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        SwitchAction switchAction = new SwitchAction();
        switchAction.setType(p.resolve(property("n2o.api.action.switch.type"), String.class));
        compilePayload(source, switchAction.getPayload(), context, p);
        compileAction(switchAction, source, p);
        return switchAction;
    }

    private void compilePayload(N2oSwitchAction source, SwitchActionPayload payload, CompileContext<?, ?> context, CompileProcessor p) {
        payload.setValueFieldId(source.getValueFieldId());
        initDatasource(payload, source.getDatasourceId(), p);
        payload.setModel(p.cast(source.getModel(), getLocalModel(p)));

        PageIndexScope indexScope = p.getScope(PageIndexScope.class);
        int switchIndex = indexScope.get();
        payload.setCases(mapCases(source, context, p, indexScope, switchIndex));
        payload.setDefaultCase(compileActionCase(source.getDefaultCase(), context, p, indexScope, switchIndex));
    }

    private Map<String, Action> mapCases(N2oSwitchAction source, CompileContext<?, ?> context, CompileProcessor p, PageIndexScope indexScope, int switchIndex) {
        List<N2oSwitchAction.Case> valueCases = source.getValueCases();
        Map<String, Action> cases = new HashMap<>();
        for (N2oSwitchAction.Case valueCase : valueCases) {
            cases.put(valueCase.getValue(), compileActionCase(valueCase, context, p, indexScope, switchIndex));
        }
        return cases;
    }

    private Action compileActionCase(N2oSwitchAction.AbstractCase abstractCase, CompileContext<?, ?> context,
                                     CompileProcessor p, PageIndexScope indexScope, int switchIndex) {
        if (abstractCase == null)
            return null;
        initCaseId(abstractCase, switchIndex);

        ActionCompileStaticProcessor.initActions(abstractCase, p);
        return ActionCompileStaticProcessor.compileAction(abstractCase, context, p, null, indexScope);
    }

    private void initCaseId(N2oSwitchAction.AbstractCase abstractCase, int switchIndex) {
        String caseId;
        if (abstractCase instanceof N2oSwitchAction.Case)
            caseId = ((N2oSwitchAction.Case) abstractCase).getValue() + "_case";
        else
            caseId = "default_case_";
        abstractCase.setId(caseId + switchIndex);
    }

    private void initDatasource(SwitchActionPayload payload, String datasourceId, CompileProcessor p) {
        payload.setDatasource(getClientDatasourceId(p.cast(datasourceId, getLocalDatasourceId(p)), p));
        if (payload.getDatasource() == null) {
            throw new N2oException(String.format("Datasource is undefined for switch action with value-field-id=%s",
                    payload.getValueFieldId()));
        }
    }
}
