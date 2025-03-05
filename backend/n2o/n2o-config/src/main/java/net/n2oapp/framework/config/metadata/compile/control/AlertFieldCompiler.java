package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oAlertField;
import net.n2oapp.framework.api.metadata.meta.control.AlertField;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция поля для вывода уведомления
 */
@Component
public class AlertFieldCompiler extends FieldCompiler<AlertField, N2oAlertField> {

    @Override
    public AlertField compile(N2oAlertField source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        AlertField alert = new AlertField();
        if (source.getText() != null)
            alert.setText(p.resolveJS(source.getText().trim()));
        alert.setTitle(p.resolveJS(source.getTitle()));
        alert.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        alert.setClassName(source.getCssClass());
        alert.setCloseButton(castDefault(source.getCloseButton(),
                () -> p.resolve(property("n2o.api.control.alert.close_button"), Boolean.class)));
        alert.setColor(castDefault(source.getColor(),
                () -> p.resolve(property("n2o.api.control.alert.color"), String.class)));
        alert.setHref(StringUtils.hasLink(source.getHref())
                ? p.resolveJS(source.getHref())
                : RouteUtil.normalize(source.getHref()));
        compileField(alert, source, context, p);
        return alert;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.alert.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAlertField.class;
    }
}
