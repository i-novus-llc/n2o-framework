package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oAlert;
import net.n2oapp.framework.api.metadata.meta.control.AlertField;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

/**
 * Компиляция поля для вывода оповещения
 */
@Component
public class FieldAlertCompiler extends FieldCompiler<AlertField, N2oAlert> {

    @Override
    public AlertField compile(N2oAlert source, CompileContext<?, ?> context, CompileProcessor p) {
        AlertField alert = new AlertField();
        if (source.getText() != null)
            alert.setText(p.resolveJS(source.getText().trim()));
        alert.setTitle(p.resolveJS(source.getHeader()));
        alert.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        alert.setColor(source.getColor());
        alert.setHref(source.getHref());
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
