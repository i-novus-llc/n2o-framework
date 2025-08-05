package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.table.BaseColumn;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import java.util.ArrayList;

import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.StylesResolver.resolveStyles;

/**
 * Компиляция базового столбца таблицы
 */
public abstract class BaseColumnCompiler<S extends N2oBaseColumn> implements BaseSourceCompiler<BaseColumn, S, CompileContext<?, ?>> {

    protected void compileBaseProperties(S source, BaseColumn compiled, CompileProcessor p) {
        compiled.setSrc(castDefault(source.getSrc(), () -> p.resolve(property("n2o.api.widget.column.src"), String.class)));
        compiled.setEnabled(true);
        compiled.setVisibleState(true);
        compiled.getElementAttributes().put("className", source.getCssClass());
        compiled.getElementAttributes().put("style", resolveStyles(source.getStyle()));

        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (isLink(source.getVisible())) {
            Condition condition = new Condition();
            condition.setExpression(unwrapLink(source.getVisible()));
            String datasourceId = widgetScope.getClientDatasourceId();
            condition.setModelLink(new ModelLink(ReduxModelEnum.FILTER, datasourceId).getLink());
            if (!compiled.getConditions().containsKey(ValidationTypeEnum.VISIBLE)) {
                compiled.getConditions().put(ValidationTypeEnum.VISIBLE, new ArrayList<>());
            }
            compiled.getConditions().get(ValidationTypeEnum.VISIBLE).add(condition);
        } else {
            compiled.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        }
    }
}