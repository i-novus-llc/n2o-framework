package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.control.*;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.SubModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.UploadScope;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;

public abstract class ListControlCompiler<T extends ListControl, S extends N2oListField> extends StandardFieldCompiler<T, S> {

    protected StandardField<T> compileListControl(T listControl, S source, CompileContext<?, ?> context, CompileProcessor p) {
        listControl.setFormat(p.resolveJS(source.getFormat()));
        listControl.setLabelFieldId(p.cast(p.resolveJS(source.getLabelFieldId()), "name"));
        listControl.setSortFieldId(p.cast(source.getSortFieldId(), listControl.getLabelFieldId()));
        listControl.setValueFieldId(p.cast(p.resolveJS(source.getValueFieldId()), "id"));
        listControl.setIconFieldId(p.resolveJS(source.getIconFieldId()));
        listControl.setBadgeFieldId(p.resolveJS(source.getBadgeFieldId()));
        listControl.setBadgeColorFieldId(p.resolveJS(source.getBadgeColorFieldId()));
        listControl.setImageFieldId(p.resolveJS(source.getImageFieldId()));
        listControl.setGroupFieldId(p.resolveJS(source.getGroupFieldId()));
        listControl.setHasSearch(source.getSearch());
        listControl.setStatusFieldId(source.getStatusFieldId());
        if (source.getQueryId() != null)
            initDataProvider(listControl, source, context, p);
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
        listControl.setEnabledFieldId(source.getEnabledFieldId());
        initSubModel(source, listControl.getData(), p.getScope(SubModelsScope.class));
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

    @Override
    protected void compileParams(StandardField<T> control, S source, WidgetParamScope paramScope, UploadScope uploadScope, CompileProcessor p) {
        if (source.getParam() != null) {
            String id = control.getId() + ".id";
            ModelsScope modelsScope = p.getScope(ModelsScope.class);
            if (modelsScope != null) {
                ModelLink onSet = new ModelLink(modelsScope.getModel(), modelsScope.getWidgetId(), control.getId());
                onSet.setParam(source.getParam());
                onSet.setSubModelQuery(createSubModel(source, control.getControl().getData()));
                onSet.setValue("`" + id + "`");
                ReduxAction onGet = Redux.dispatchUpdateModel(modelsScope.getWidgetId(), modelsScope.getModel(), id,
                        colon(source.getParam()));
                paramScope.addQueryMapping(source.getParam(), onGet, onSet);
            }
        }
    }

    protected StandardField<T> compileFetchDependencies(StandardField<T> field, S source, CompileProcessor p) {
        if (source.getPreFilters() != null && field.getDependencies().stream().noneMatch(d -> d.getType() == ValidationType.fetch)) {
            Set<String> setOn = new HashSet<>();
            for (N2oPreFilter filter : source.getPreFilters()) {
                if (StringUtils.hasLink(filter.getValue())) {
                    String resolveOnJS = p.resolveJS(filter.getValue());
                    resolveOnJS = resolveOnJS.substring(1, resolveOnJS.length() - 1);
                    setOn.add(resolveOnJS);
                }
            }
            if (!setOn.isEmpty()) {
                ControlDependency fetchCD = new ControlDependency();
                fetchCD.setType(ValidationType.fetch);
                fetchCD.setOn(new ArrayList<>(setOn));
                field.addDependency(fetchCD);
            }
        }
        return field;
    }

    private void initSubModel(S source, List<Map<String, Object>> data, SubModelsScope scope) {
        if (scope == null)
            return;
        if (source.getQueryId() != null || data != null)
            scope.add(createSubModel(source, data));
    }

    private SubModelQuery createSubModel(N2oListField item, List<Map<String, Object>> data) {
        return new SubModelQuery(
                item.getId(),
                item.getQueryId(),
                item.getValueFieldId() != null ? item.getValueFieldId() : "id",
                item.getLabelFieldId() != null ? item.getLabelFieldId() : "name",
                !item.isSingle(),
                data
        );
    }

    private void initDataProvider(T listControl, N2oListField source, CompileContext<?, ?> context, CompileProcessor p) {
        QueryContext queryContext = new QueryContext(source.getQueryId());
        ModelsScope modelsScope = p.getScope(ModelsScope.class);
        queryContext.setFailAlertWidgetId(modelsScope != null ? modelsScope.getWidgetId() : null);
        CompiledQuery query = p.getCompiled(queryContext);
        String route = query.getRoute();
        p.addRoute(new QueryContext(source.getQueryId(), route));

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setTargetModel(modelsScope.getModel());
        dataProvider.setTargetWidgetId(modelsScope.getWidgetId());
        dataProvider.setUrl(route);

        N2oPreFilter[] preFilters = source.getPreFilters();
        if (preFilters != null) {
            N2oParam[] queryParams = new N2oParam[preFilters.length];
            for (int i = 0; i < preFilters.length; i++) {
                N2oPreFilter preFilter = preFilters[i];
                N2oQuery.Filter filter = query.getFilterByPreFilter(preFilter);
                N2oParam queryParam = new N2oParam();
                queryParam.setName(query.getFilterIdToParamMap().get(filter.getFilterField()));

                ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
                Object value = (preFilter.getParam() != null && routeScope != null && routeScope.getQueryMapping() != null &&
                        routeScope.getQueryMapping().containsKey(preFilter.getParam())) ?
                        routeScope.getQueryMapping().get(preFilter.getParam()).getValue() :
                        getPrefilterValue(preFilter);
                queryParam.setValueList(value);
                queryParam.setRefModel(preFilter.getRefModel());
                queryParam.setRefWidgetId(preFilter.getRefWidgetId());
                queryParams[i] = queryParam;

                if (Boolean.TRUE.equals(preFilter.getResetOnChange())
                        && StringUtils.isLink(preFilter.getValue())) {
                    N2oField.ResetDependency reset = new N2oField.ResetDependency();
                    reset.setOn(new String[]{preFilter.getValue().substring(1, preFilter.getValue().length() - 1)});
                    source.addDependency(reset);
                }
            }
            dataProvider.setQueryParams(queryParams);
        }

        if (listControl.getHasSearch() != null && listControl.getHasSearch()) {
            String searchFilterId = p.cast(source.getSearchFilterId(), listControl.getLabelFieldId());
            if (query.getFilterIdToParamMap().containsKey(searchFilterId)) {
                dataProvider.setQuickSearchParam(query.getFilterIdToParamMap().get(searchFilterId));
            } else if (searchFilterId != null && listControl.getHasSearch()) {
                throw new N2oException("For search field id [{0}] is necessary this filter-id in query [{1}]").addData(searchFilterId, query.getId());
            }
        }
        listControl.setDataProvider(ClientDataProviderUtil.compile(dataProvider, context, p));
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}
