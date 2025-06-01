package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.config.util.DatasourceUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.*;


/**
 * Сборка компонента ButtonField
 */
@Component
public class ButtonFieldCompiler extends ActionFieldCompiler<ButtonField, N2oButtonField> {
    private static final String PROPERTY_PREFIX = "n2o.api.control.button";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButtonField.class;
    }

    @Override
    public ButtonField compile(N2oButtonField source, CompileContext<?, ?> context, CompileProcessor p) {
        ButtonField field = new ButtonField();
        initDefaults(source, context, p);
        compileField(field, source, context, p);
        field.setColor(p.resolveJS(source.getColor()));
        field.setBadge(BadgeUtil.compileSimpleBadge(source, PROPERTY_PREFIX, p));
        field.setRounded(castDefault(source.getRounded(),
                () -> p.resolve(property("n2o.api.control.button.rounded"), Boolean.class)));
        initItem(field, source, context, p);
        return field;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.button.src";
    }

    protected void initItem(ButtonField button, N2oButtonField source,
                            CompileContext<?, ?> context, CompileProcessor p) {
        button.setProperties(p.mapAttributes(source));
        button.setIcon(source.getIcon());
        button.setLabel(p.resolveJS(source.getLabel()));

        compileAction(source, button, context, p);

        String hint;
        hint = source.getDescription();
        if (hint != null) {
            button.setHint(p.resolveJS(hint.trim()));
            button.setHintPosition(source.getTooltipPosition());
        }

        if (source.getModel() == null)
            source.setModel(ReduxModelEnum.RESOLVE);

        String datasource = initDatasource(source, p);
        boolean validate = initValidate(source, datasource);
        source.setValidate(validate);
        button.setValidate(compileValidate(source, p, datasource));
        button.setDatasource(DatasourceUtil.getClientDatasourceId(datasource, p));
    }
}
