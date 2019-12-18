package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.interval.N2oIntervalField;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.config.metadata.compile.widget.FiltersScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.ControlFilterUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Абстрактная реализация компиляции интервальных полей
 */
@Component
public abstract class IntervalFieldCompiler<D extends Control, S extends N2oIntervalField> extends StandardFieldCompiler<D, S> {

    @Override
    protected Object compileDefValues(S source, CompileProcessor p) {
        if (source.getBegin() == null && source.getEnd() == null)
            return null;
        DefaultValues values = new DefaultValues();
        values.setValues(new HashMap<>());
        if (source.getBegin() != null) {
            values.getValues().put("begin", source.getBegin());
        }
        if (source.getEnd() != null) {
            values.getValues().put("end", source.getEnd());
        }
        return values;
    }

    @Override
    protected void compileFilters(S source, CompileProcessor p) {
        FiltersScope filtersScope = p.getScope(FiltersScope.class);
        if (filtersScope != null) {
            CompiledQuery query = p.getScope(CompiledQuery.class);
            if (query == null)
                return;
            WidgetScope widgetScope = p.getScope(WidgetScope.class);

            List<String> filterIds = new ArrayList<>();

            if (source.getBeginFilterId() != null)
                filterIds.add(source.getBeginFilterId());
            if (source.getEndFilterId() != null)
                filterIds.add(source.getEndFilterId());
            if (filterIds.isEmpty())
                filterIds.add(source.getId());

            for (String filterId : filterIds) {
                List<N2oQuery.Filter> filters = ControlFilterUtil.getFilters(filterId, query);
                filters.forEach(f -> {
                    Filter filter = new Filter();
                    filter.setFilterId(f.getFilterField());
                    filter.setParam(widgetScope.getWidgetId() + "_" + f.getParam());
                    filter.setReloadable(true);
                    SubModelQuery subModelQuery = findSubModelQuery(source.getId(), p);
                    ModelLink link = new ModelLink(ReduxModel.FILTER, widgetScope.getClientWidgetId());
                    link.setSubModelQuery(subModelQuery);

                    String linkValue = f.getFilterField();
                    if (source.getBeginFilterId() != null && FilterType.more.equals(f.getType()))
                        linkValue = source.getId() + ".begin";
                    else if (source.getEndFilterId() != null && FilterType.less.equals(f.getType()))
                        linkValue = source.getId() + ".end";

                    link.setValue(p.resolveJS(Placeholders.ref(linkValue)));
                    filter.setLink(link);
                    filtersScope.getFilters().add(filter);
                });
            }
        }
    }
}
