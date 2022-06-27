package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.event.action.N2oSubmitAction;
import net.n2oapp.framework.api.metadata.meta.action.submit.SubmitAction;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция действия submit
 */
@Component
public class SubmitActionCompiler extends AbstractActionCompiler<SubmitAction, N2oSubmitAction> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubmitAction.class;
    }

    @Override
    public SubmitAction compile(N2oSubmitAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        SubmitAction submit = new SubmitAction();
        submit.setType(p.resolve(property("n2o.api.action.submit.type"), String.class));
        compileAction(submit, source, p);

        if (source.getDatasource() != null) {
            submit.getPayload().setDatasource(source.getDatasource());
        } else {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                N2oButtonField button = componentScope.unwrap(N2oButtonField.class);
                if (button != null) {
                    submit.getPayload().setDatasource(button.getDatasourceId());
                }
            }
        }

        return submit;
    }
}
