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
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.control.*;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.fieldset.MultiFieldSetScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.SubModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.FieldCompileUtil;
import net.n2oapp.framework.config.util.N2oClientDataProviderUtil;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

public abstract class ListControlCompiler<T extends ListControl, S extends N2oListField> extends StandardFieldCompiler<T, S> {
    private static final String PROPERTY_PREFIX = "n2o.api.control.list";

    protected StandardField<T> compileListControl(T listControl, S source, CompileContext<?, ?> context, CompileProcessor p) {
        listControl.setFormat(p.resolveJS(source.getFormat()));
        listControl.setLabelFieldId(castDefault(p.resolveJS(source.getLabelFieldId()), "name"));
        listControl.setSortFieldId(castDefault(source.getSortFieldId(), listControl.getLabelFieldId()));
        listControl.setValueFieldId(castDefault(p.resolveJS(source.getValueFieldId()), "id"));
        listControl.setIconFieldId(p.resolveJS(source.getIconFieldId()));
        listControl.setImageFieldId(p.resolveJS(source.getImageFieldId()));
        listControl.setGroupFieldId(p.resolveJS(source.getGroupFieldId()));
        listControl.setHasSearch(source.getSearch());
        listControl.setStatusFieldId(source.getStatusFieldId());
        compileData(source, listControl, context, p);
        listControl.setCaching(castDefault(source.getCache(), () -> p.resolve(property("n2o.api.control.list.cache"), Boolean.class)));
        listControl.setEnabledFieldId(source.getEnabledFieldId());
        listControl.setBadge(BadgeUtil.compileReferringBadge(source, PROPERTY_PREFIX, p));
        listControl.setSize(castDefault(listControl.getSize(), source::getSize,
                () -> p.resolve(property("n2o.api.control.list.size"), Integer.class)));
        initSubModel(source, listControl.getData(), p);
        return compileStandardField(listControl, source, context, p);
    }

    protected void compileData(S source, T listControl, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getQueryId() != null)
            initDataProvider(listControl, source, context, p);
        else if (source.getDatasourceId() != null)
            listControl.setDatasource(getClientDatasourceId(source.getDatasourceId(), p));
        else if (source.getOptions() != null) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Map<String, String> option : source.getOptions()) {
                DataSet dataItem = new DataSet();
                option.forEach((f, v) -> dataItem.put(f, p.resolve(v)));
                list.add(dataItem);
            }
            listControl.setData(list);
        }
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
        if (source.getParam() == null) return;
        String id = control.getId() + ".id";
        WidgetScope modelsScope = p.getScope(WidgetScope.class);
        if (modelsScope != null) {
            ModelLink onSet = compileLinkOnSet(control, source, modelsScope, p);
            ReduxAction onGet = Redux.dispatchUpdateModel(modelsScope.getClientDatasourceId(), modelsScope.getModel(), id,
                    colon(source.getParam()));
            paramScope.addQueryMapping(source.getParam(), onGet, onSet);
        }
    }

    protected ModelLink compileLinkOnSet(StandardField<T> control, S source, WidgetScope widgetScope, CompileProcessor p) {
        ModelLink onSet = new ModelLink(widgetScope.getModel(), widgetScope.getClientDatasourceId(), control.getId());
        onSet.setParam(source.getParam());
        onSet.setSubModelQuery(createSubModel(source, control.getControl().getData(),p));
        onSet.setValue("`id`");
        return onSet;
    }

    protected StandardField<T> compileFetchDependencies(StandardField<T> field, S source, CompileProcessor p) {
        if (source.getPreFilters() != null && field.getDependencies().stream().noneMatch(d -> d.getType() == ValidationTypeEnum.fetch)) {
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
                fetchCD.setType(ValidationTypeEnum.fetch);
                fetchCD.setOn(new ArrayList<>(setOn));
                field.addDependency(fetchCD);
            }
        }
        return field;
    }

    private void initSubModel(S source, List<Map<String, Object>> data, CompileProcessor p) {
        SubModelsScope scope = p.getScope(SubModelsScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (scope == null || widgetScope == null)
            return;
        if (source.getQueryId() != null || data != null)
            scope.add(createSubModel(source, data, p), widgetScope.getDatasourceId());
    }

    protected SubModelQuery createSubModel(N2oListField item, List<Map<String, Object>> data, CompileProcessor p) {
        MultiFieldSetScope multiFieldSetScope = p.getScope(MultiFieldSetScope.class);
        return new SubModelQuery(
                multiFieldSetScope == null ? null : multiFieldSetScope.getPathWithIndexes(),
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

        dataProvider.setQuickSearchParam(initQuickSearchParam(listControl, source, p));
        listControl.setDataProvider(ClientDataProviderUtil.compile(dataProvider, context, p));
    }

    protected String initQuickSearchParam(T listControl, N2oListField source, CompileProcessor p) {
        QueryContext queryContext = new QueryContext(source.getQueryId());
        CompiledQuery query = p.getCompiled(queryContext);

        if (Boolean.TRUE.equals(listControl.getHasSearch())) {
            String searchFilterId = castDefault(source.getSearchFilterId(), listControl.getLabelFieldId());
            if (query.getFilterIdToParamMap().containsKey(searchFilterId))
                return query.getFilterIdToParamMap().get(searchFilterId);
            else if (searchFilterId != null && listControl.getHasSearch())
                throw new N2oException(
                        String.format("Для поля '%s' необходимо задать фильтр в '%s.query.xml'", searchFilterId, query.getId()));
        }
        return null;
    }
}
