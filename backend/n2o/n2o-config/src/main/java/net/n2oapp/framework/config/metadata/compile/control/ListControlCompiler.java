package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.control.*;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.SubModelsScope;
import net.n2oapp.framework.config.util.FieldCompileUtil;
import net.n2oapp.framework.config.util.N2oClientDataProviderUtil;

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
    protected void compileParams(StandardField<T> control, S source, WidgetParamScope paramScope, CompileProcessor p) {
        if (source.getParam() != null) {
            String id = control.getId() + ".id";
            ModelsScope modelsScope = p.getScope(ModelsScope.class);
            if (modelsScope != null) {
                ModelLink onSet = new ModelLink(modelsScope.getModel(), modelsScope.getWidgetId(), control.getId());
                onSet.setParam(source.getParam());
                onSet.setSubModelQuery(createSubModel(source, control.getControl().getData()));
                onSet.setValue("`id`");
                ReduxAction onGet = Redux.dispatchUpdateModel(modelsScope.getWidgetId(), modelsScope.getModel(), id,
                        colon(source.getParam()));
                paramScope.addQueryMapping(source.getParam(), onGet, onSet);
                if (modelsScope.hasModels())
                    modelsScope.add(control.getId(), onSet);
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
        N2oClientDataProvider dataProvider = N2oClientDataProviderUtil.initFromField(source.getPreFilters(), source.getQueryId(), p);
        source.addDependencies(FieldCompileUtil.getResetOnChangeDependency(source));

        QueryContext queryContext = new QueryContext(source.getQueryId());
        CompiledQuery query = p.getCompiled(queryContext);

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
}
