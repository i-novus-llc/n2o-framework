package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oAlert;
import net.n2oapp.framework.api.metadata.meta.control.Alert;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция поля для вывода оповещения
 */
@Component
public class AlertCompiler extends FieldCompiler<Alert, N2oAlert> {

    @Override
    public Alert compile(N2oAlert source, CompileContext<?, ?> context, CompileProcessor p) {
        Alert alert = new Alert();
        alert.setText(source.getText().trim());
        alert.setHeader(source.getHeader());
        alert.setFooter(source.getFooter());
        alert.setColor(source.getColor());
        alert.setFade(p.cast(source.getFade(), p.resolve(property("n2o.api.control.alert.fade"), Boolean.class)));
        alert.setTag(source.getTag());
        compileField(alert, source, context, p);
        return alert;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.alert.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAlert.class;
    }
}
