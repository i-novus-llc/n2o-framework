package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Сборка действия вызова операции
 */
@Component
public class CopyActionCompiler extends AbstractActionCompiler<CopyAction, N2oCopyAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCopyAction.class;
    }

    @Override
    public CopyAction compile(N2oCopyAction source, CompileContext<?, ?> context, CompileProcessor p) {
        CopyAction copyAction = new CopyAction();
        compileAction(copyAction, source, p);
        copyAction.setType(p.resolve(property("n2o.api.action.copy.type"), String.class));
        CopyActionPayload.ClientModel sourceModel = new CopyActionPayload.ClientModel(initWidgetId(source.getSourceWidgetId(), context, p),
                p.cast(source.getSourceModel(), ReduxModel.RESOLVE).getId());
        if (source.getSourceFieldId() != null)
            sourceModel.setField(source.getSourceFieldId());
        CopyActionPayload.ClientModel targetModel = new CopyActionPayload.ClientModel(initWidgetId(source.getTargetWidgetId(), context, p),
                p.cast(source.getTargetModel(), ReduxModel.RESOLVE).getId());
        if (source.getTargetFieldId() != null)
            targetModel.setField(source.getTargetFieldId());

        copyAction.getPayload().setSource(sourceModel);
        copyAction.getPayload().setTarget(targetModel);
        copyAction.getPayload().setMode(p.cast(source.getMode(), CopyMode.merge));

        MetaSaga meta = new MetaSaga();
        meta.setCloseLastModal(p.cast(source.getCloseOnSuccess(), p.resolve(property("n2o.api.action.copy.close_on_success"), Boolean.class)));
        copyAction.setMeta(meta);

        return copyAction;
    }

    private String initWidgetId(String widgetId, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (widgetId != null) {
            /// TODO - external page widget
            return pageScope != null ? pageScope.getGlobalWidgetId(widgetId) : widgetId;
        } else {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            return pageScope != null && widgetScope != null ?
                    widgetScope.getClientWidgetId() :
                    context.getCompiledId((N2oCompileProcessor) p);
        }
    }
}
