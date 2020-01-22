package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyActionPayload;
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
        String targetWidgetId = initTargetWidget(source, context, p);
        CopyActionPayload.ClientModel sourceModel = new CopyActionPayload.ClientModel(targetWidgetId,
                p.cast(source.getSourceModel(), ReduxModel.EDIT).getId());
        CopyActionPayload.ClientModel targetModel = new CopyActionPayload.ClientModel(targetWidgetId,
                p.cast(source.getTargetModel(), ReduxModel.RESOLVE).getId());
        copyAction.getPayload().setSource(sourceModel);
        copyAction.getPayload().setTarget(targetModel);
        return copyAction;
    }


}
