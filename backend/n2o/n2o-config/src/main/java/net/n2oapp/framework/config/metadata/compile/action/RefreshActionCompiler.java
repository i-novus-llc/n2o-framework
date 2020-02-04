package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oRefresh;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshPayload;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Сборка действия обновления данных виджета
 */
@Component
public class RefreshActionCompiler extends AbstractActionCompiler<RefreshAction, N2oRefresh> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oRefresh.class;
    }

    @Override
    public RefreshAction compile(N2oRefresh source, CompileContext<?, ?> context, CompileProcessor p) {
        RefreshAction refreshAction = new RefreshAction();
        compileAction(refreshAction, source, p);
        refreshAction.setType(p.resolve(property("n2o.api.action.refresh.type"), String.class));
        String targetWidgetId = initTargetWidget(source, context, p);
        ((RefreshPayload)refreshAction.getPayload()).setWidgetId(targetWidgetId);
        return refreshAction;
    }
}
