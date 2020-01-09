package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oAlert;
import net.n2oapp.framework.api.metadata.meta.control.Alert;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция поля для вывода оповещения
 */
@Component
public class AlertCompiler extends StandardFieldCompiler<Alert, N2oAlert> {

    @Override
    public StandardField<Alert> compile(N2oAlert source, CompileContext<?, ?> context, CompileProcessor p) {
        Alert alert = new Alert();
        alert.setText(source.getText());
        alert.setHeader(source.getHeader());
        alert.setFooter(source.getFooter());
        alert.setColor(source.getColor());
        alert.setFade(p.cast(source.getFade(), p.resolve(property("n2o.api.control.alert.fade"), Boolean.class)));
        alert.setTag(source.getTag());
        return compileStandardField(alert, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.alert.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAlert.class;
    }
}
