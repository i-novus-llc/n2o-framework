package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.*;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.SubModelsScope;
import net.n2oapp.framework.config.util.CompileUtil;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

public abstract class ListControlCompiler<T extends ListControl, S extends N2oListField> extends StandardFieldCompiler<T, S> {

    protected StandardField<T> compileListControl(T listControl, S source, CompileContext<?, ?> context, CompileProcessor p) {
        listControl.setFormat(p.resolveJS(source.getFormat()));
        listControl.setLabelFieldId(p.cast(p.resolveJS(source.getLabelFieldId()), "name"));
        listControl.setValueFieldId(p.cast(p.resolveJS(source.getValueFieldId()), "id"));
        listControl.setIconFieldId(p.resolveJS(source.getIconFieldId()));
        listControl.setBadgeFieldId(p.resolveJS(source.getBadgeFieldId()));
        listControl.setBadgeColorFieldId(p.resolveJS(source.getBadgeColorFieldId()));
        listControl.setImageFieldId(p.resolveJS(source.getImageFieldId()));
        listControl.setGroupFieldId(p.resolveJS(source.getGroupFieldId()));
        if (source.getQueryId() != null)
            initDataProvider(listControl, source, p);
        else if (source.getOptions() != null) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Map<String, String> option : source.getOptions()) {
                DataSet dataItem = new DataSet();
                option.forEach((f, v) -> dataItem.put(f, p.resolve(v)));
                list.add(dataItem);
            }
            listControl.setData(list);
        }
        listControl.setValueFieldId(p.cast(p.resolveJS(listControl.getValueFieldId()), "id"));
        listControl.setLabelFieldId(p.cast(p.resolveJS(listControl.getLabelFieldId()), "name"));
        listControl.setCaching(source.getCache());
        listControl.setHasSearch(p.cast(source.getSearch(), false));
        initSubModel(source, p.getScope(SubModelsScope.class));
        return compileStandardField(listControl, source, context, p);
    }

    @Override
    protected Object compileDefValues(S source, CompileProcessor p) {
        if (source.getDefValue() == null) {
            return null;
        }
        DefaultValues values = new DefaultValues();
        values.setValues(new HashMap<>());
        source.getDefValue().forEach((f, v) -> values.getValues().put(f, p.resolve(v)));
        return source.isSingle() ? values : Collections.singletonList(values);
    }

    protected StandardField<T> compileFetchDependencies(T listControl, S source, CompileContext<?, ?> context, CompileProcessor p) {
        StandardField<T> result = compileListControl(listControl, source, context, p);
        if (source.getPreFilters() != null) {

            for (N2oPreFilter filter : source.getPreFilters()) {
                String resolveOnJS = p.resolveJS(filter.getValue());
                if (StringUtils.hasLink(filter.getValue()) &&
                        result.getDependencies().stream().noneMatch(d -> d.getType() == ValidationType.fetch &&
                                d.getOn().contains(resolveOnJS))) {
                    ControlDependency fetchCD = new ControlDependency();
                    fetchCD.setType(ValidationType.fetch);
                    fetchCD.setOn(Collections.singletonList(resolveOnJS));
                    result.addDependency(fetchCD);
                }
            }
        }
        return result;
    }

    private void initSubModel(S source, SubModelsScope scope) {
        if (scope == null)
            return;
        if (source.getQueryId() != null)
            scope.add(createSubModel(source));
    }

    private SubModelQuery createSubModel(N2oListField item) {
        return new SubModelQuery(
                item.getId(),
                item.getQueryId(),
                item.getValueFieldId() != null ? item.getValueFieldId() : "id",
                item.getLabelFieldId() != null ? item.getLabelFieldId() : "name",
                !item.isSingle()
        );
    }

    private void initDataProvider(T listControl, N2oListField source, CompileProcessor p) {
        WidgetDataProvider dataProvider = new WidgetDataProvider();
        QueryContext queryContext = new QueryContext(source.getQueryId());
        ModelsScope modelsScope = p.getScope(ModelsScope.class);
        queryContext.setFailAlertWidgetId(modelsScope != null ? modelsScope.getWidgetId() : null);
        CompiledQuery query = p.getCompiled(queryContext);
        String route = query.getRoute();
        p.addRoute(route, queryContext);
        dataProvider.setUrl(p.resolveText(property("n2o.config.data.route")) + route);

        String searchFilterId = p.cast(source.getSearchFieldId(), source.getLabelFieldId());
        if (query.getFilterIdToParamMap().containsKey(searchFilterId)) {
            dataProvider.setQuickSearchParam(query.getFilterIdToParamMap().get(searchFilterId));
        }

        N2oPreFilter[] preFilters = source.getPreFilters();
        Map<String, BindLink> queryMap = new StrictMap<>();
        if (preFilters != null) {
            for (N2oPreFilter preFilter : preFilters) {
                N2oQuery.Filter filter = query.getFilterByPreFilter(preFilter);
                String filterParam = query.getFilterIdToParamMap().get(filter.getFilterField());
                Object prefilterValue = getPrefilterValue(preFilter);
                if (StringUtils.isJs(prefilterValue)) {
                    String widgetId = modelsScope.getWidgetId();
                    if (preFilter.getRefWidgetId() != null) {
                        PageScope pageScope = p.getScope(PageScope.class);
                        widgetId = preFilter.getRefPageId() == null ?
                                pageScope.getGlobalWidgetId(preFilter.getRefWidgetId())
                                : CompileUtil.generateWidgetId(preFilter.getRefPageId(), preFilter.getRefWidgetId());
                    }
                    ModelLink link = new ModelLink(p.cast(preFilter.getRefModel(), modelsScope.getModel()), widgetId);
                    link.setValue(prefilterValue);
                    queryMap.put(filterParam, link);
                } else {
                    queryMap.put(filterParam, new ModelLink(prefilterValue));
                }
            }
        }
        dataProvider.setQueryMapping(queryMap);
        listControl.setDataProvider(dataProvider);
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}
