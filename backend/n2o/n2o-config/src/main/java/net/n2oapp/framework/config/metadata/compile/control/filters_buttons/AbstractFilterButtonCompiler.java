package net.n2oapp.framework.config.metadata.compile.control.filters_buttons;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oFilterButtonField;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.AbstractFilterButtonField;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.control.FieldCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.initDatasource;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

public abstract class AbstractFilterButtonCompiler<D extends AbstractFilterButtonField, S extends N2oFilterButtonField> extends FieldCompiler<D, S> {

    private static final String PROPERTY_PREFIX = "n2o.api.control.button";

    protected abstract String getDefaultLabel(CompileProcessor p);

    protected void compileButton(D button, S source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        compileField(button, source, context, p);
        button.setIcon(source.getIcon());
        button.setLabel(castDefault(p.resolveJS(source.getLabel()), getDefaultLabel(p)));
        source.setNoLabel(p.resolve(property("n2o.api.control.search_buttons.no_label"), String.class));
        button.setColor(source.getColor());
        button.setBadge(BadgeUtil.compileSimpleBadge(source, PROPERTY_PREFIX, p));
        button.setDatasource(getClientDatasourceId(initDatasource(source, p), p));
        if (source.getDescription() != null) {
            button.setHint(p.resolveJS(source.getDescription().trim()));
            button.setHintPosition(source.getTooltipPosition());
        }
        compileDependencies(button, source, context, p);
    }
}
