package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oAlert;
import net.n2oapp.framework.api.metadata.meta.control.FieldAlert;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция поля для вывода оповещения
 */
@Component
public class FieldAlertCompiler extends FieldCompiler<FieldAlert, N2oAlert> {

    @Override
    public FieldAlert compile(N2oAlert source, CompileContext<?, ?> context, CompileProcessor p) {
        FieldAlert alert = new FieldAlert();
        if (source.getText() != null)
            alert.setText(p.resolveJS(source.getText().trim()));
        alert.setTitle(p.resolveJS(source.getTitle()));
        alert.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        alert.setColor(source.getColor());
        alert.setHref(source.getHref());
        alert.setHeader(p.resolveJS(source.getHeader()));
        alert.setFooter(p.resolveJS(source.getFooter()));
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
