package net.n2oapp.framework.config.metadata.compile.events;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.N2oOnChangeEvent;
import net.n2oapp.framework.api.metadata.meta.event.Event;
import net.n2oapp.framework.api.metadata.meta.event.OnChangeEvent;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.compileAction;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initActions;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция события изменения модели данных
 */
@Component
public class OnChangeEventCompiler extends BaseEventCompiler<N2oOnChangeEvent, OnChangeEvent> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOnChangeEvent.class;
    }

    @Override
    public OnChangeEvent compile(N2oOnChangeEvent source, CompileContext<?, ?> context, CompileProcessor p) {
        OnChangeEvent event = new OnChangeEvent();
        initEvents(event, source, context, p);

        event.setType(p.resolve(property("n2o.api.page.event.on_change.type"), String.class));
        event.setDatasource(getClientDatasourceId(source.getDatasourceId(), p));
        event.setModel(p.cast(source.getModel(), ReduxModel.resolve));
        event.setField(source.getFieldId());
        event.setAction(compileAction(source, context, p, null));

        return event;
    }

    @Override
    protected void initEvents(Event event, N2oOnChangeEvent source, CompileContext<?, ?> context, CompileProcessor p) {
        source.setActions(initActions(source, p));
        super.initEvents(event, source, context, p);
    }
}
