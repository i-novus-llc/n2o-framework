package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oClearAction;
import net.n2oapp.framework.api.metadata.meta.action.clear.ClearAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Сборка действия очистки модели
 */
@Component
public class ClearActionCompiler extends AbstractActionCompiler<ClearAction, N2oClearAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oClearAction.class;
    }

    @Override
    public ClearAction compile(N2oClearAction source, CompileContext<?, ?> context, CompileProcessor p) {
        ClearAction clearAction = new ClearAction();
        compileAction(clearAction, source, p);
        clearAction.setType(p.resolve(property("n2o.api.action.clear.type"), String.class));
        clearAction.getPayload().setPrefixes(p.cast(source.getModel(), new String[]{ReduxModel.EDIT.getId()}));
        String widgetId = initTargetWidget(context, p);
        PageScope pageScope = p.getScope(PageScope.class);
        clearAction.getPayload().setKey(pageScope == null || pageScope.getWidgetIdDatasourceMap() == null
                ? widgetId : pageScope.getWidgetIdDatasourceMap().get(widgetId));
        if (Boolean.TRUE.equals(source.getCloseOnSuccess())) {
            if (clearAction.getMeta() == null)
                clearAction.setMeta(new MetaSaga());
            clearAction.getMeta().setModalsToClose(1);
        }
        return clearAction;
    }
}
