package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.table.BaseColumn;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import java.util.ArrayList;

import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.config.util.StylesResolver.resolveStyles;

/**
 * Компиляция базового столбца таблицы
 */
public abstract class BaseColumnCompiler<S extends N2oBaseColumn> extends AbstractColumnCompiler<S> {

    protected void compileBaseProperties(S source, BaseColumn compiled, CompileProcessor p) {
        compileAbstractProperties(source, compiled, p);
        compiled.getElementAttributes().put("className", source.getCssClass());
        compiled.getElementAttributes().put("style", resolveStyles(source.getStyle()));

        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (isLink(source.getVisible())) {
            Condition condition = new Condition();
            condition.setExpression(unwrapLink(source.getVisible()));
            String datasourceId = widgetScope.getClientDatasourceId();
            condition.setModelLink(new ModelLink(ReduxModelEnum.filter, datasourceId).getBindLink());
            if (!compiled.getConditions().containsKey(ValidationTypeEnum.visible)) {
                compiled.getConditions().put(ValidationTypeEnum.visible, new ArrayList<>());
            }
            compiled.getConditions().get(ValidationTypeEnum.visible).add(condition);
        } else {
            compiled.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        }
    }
}