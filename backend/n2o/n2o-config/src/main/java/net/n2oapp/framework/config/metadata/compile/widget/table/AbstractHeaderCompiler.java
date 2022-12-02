package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.StylesResolver;

import java.util.ArrayList;

import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция абстрактного заголовка таблицы
 */
public abstract class AbstractHeaderCompiler<S extends AbstractColumn> implements BaseSourceCompiler<ColumnHeader, S, CompileContext<?, ?>> {

    protected void compileBaseProperties(S source, ColumnHeader compiled, CompileProcessor p) {
        compiled.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.widget.column.src"), String.class)));
        compiled.setCssClass(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));

        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (isLink(source.getVisible())) {
            Condition condition = new Condition();
            condition.setExpression(unwrapLink(source.getVisible()));
            String datasourceId = widgetScope.getClientDatasourceId();
            condition.setModelLink(new ModelLink(ReduxModel.filter, datasourceId).getBindLink());
            if (!compiled.getConditions().containsKey(ValidationType.visible)) {
                compiled.getConditions().put(ValidationType.visible, new ArrayList<>());
            }
            compiled.getConditions().get(ValidationType.visible).add(condition);
        } else {
            compiled.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        }
    }
}
