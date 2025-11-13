package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.table.BaseColumn;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.apache.commons.lang3.StringUtils;

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
        IndexScope idx = p.getScope(IndexScope.class);
        source.setId(castDefault(source.getId(),
                StringUtils.isEmpty(source.getTextFieldId()) ? null : source.getTextFieldId(),
                "cell" + idx.get()));
        compiled.setId(source.getId());

        compiled.setSrc(castDefault(source.getSrc(), () -> p.resolve(property("n2o.api.widget.column.src"), String.class)));
        compiled.setEnabled(true);
        compiled.setVisibleState(true);
        compiled.getElementAttributes().put("className", source.getCssClass());
        compiled.getElementAttributes().put("style", resolveStyles(source.getStyle()));
        compiled.setFixed(source.getFixed());

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

        if (source.getSortingDirection() != null) {
            SortingsScope sortingsScope = p.getScope(SortingsScope.class);
            if (sortingsScope != null) {
                String fieldId = castDefault(source.getSortingFieldId(), source.getTextFieldId());
                if (fieldId == null)
                    throw new N2oException(String.format("В колонке \"<column>\" c 'id=%s' задан атрибут 'sorting-direction', но не указано поле сортировки. Задайте 'sorting-field-id' или 'text-field-id'",
                            source.getId()));
                sortingsScope.put(RouteUtil.normalizeParam(fieldId),
                        source.getSortingDirection().toString().toUpperCase()
                );
            }
        }
    }
}