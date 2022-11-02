package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oStatus;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.api.metadata.meta.control.Status;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента отображения статуса
 */
@Component
public class StatusFieldCompiler extends FieldCompiler<Status, N2oStatus> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStatus.class;
    }

    @Override
    public Status compile(N2oStatus source, CompileContext<?, ?> context, CompileProcessor p) {
        Status status = new Status();
        status.setColor(source.getColor());
        status.setText(p.resolveJS(source.getText()));
        status.setTextPosition(p.cast(source.getTextPosition(),
                p.resolve(property("n2o.api.control.status_field.text_position"), Position.class)));

        compileField(status, source, context, p);
        return status;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.status_field.src";
    }
}
