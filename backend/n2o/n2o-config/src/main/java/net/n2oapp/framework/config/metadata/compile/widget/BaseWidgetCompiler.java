package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oEnablingDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oVisibilityDependency;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDependency;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.util.StylesResolver;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientWidgetId;

/**
 * Компиляция абстрактного виджета
 */
public abstract class BaseWidgetCompiler<D extends Widget, S extends N2oWidget> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected abstract String getPropertyWidgetSrc();

    /**
     * Базовая сборка виджета
     */
    protected void compileBaseWidget(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p) {
        compiled.setId(initClientWidgetId(source, context, p));
        compiled.setClassName(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setProperties(p.mapAttributes(source));
        compiled.setSrc(initSrc(source, p));
        compiled.setIcon(source.getIcon());
        compiled.setFetchOnInit(p.cast(source.getFetchOnInit(), true));
        compiled.setFetchOnVisibility(p.cast(source.getFetchOnVisibility(), true));
        compileComponent(compiled, source, p);
        initDatasource(compiled, source, p);
        compileDependencies(compiled, source, p);
    }

    private void compileComponent(D compiled, S source, CompileProcessor p) {
        if (isNull(compiled.getComponent()))
            return;
        compileAutoFocus(source, compiled, p);
    }

    private String initSrc(S source, CompileProcessor p) {
        String defaultWidgetSrc = null;
        if (nonNull(getPropertyWidgetSrc()))
            defaultWidgetSrc = p.resolve(property(getPropertyWidgetSrc()), String.class);
        return p.cast(source.getSrc(), defaultWidgetSrc);
    }

    /**
     * Инициализация источника данных виджета
     */
    protected N2oAbstractDatasource initDatasource(D compiled, S source, CompileProcessor p) {
        N2oAbstractDatasource datasource;
        if (isNull(source.getDatasourceId())) {
            if (isNull(source.getDatasource())) {
                datasource = new N2oStandardDatasource();
                ((N2oStandardDatasource) datasource).setDefaultValuesMode(DefaultValuesMode.defaults);
            } else {
                datasource = source.getDatasource();
                source.setDatasource(null);
            }
            datasource.setId(Objects.nonNull(datasource.getId()) ? datasource.getId() : source.getId());
            source.setDatasourceId(datasource.getId());
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            if (nonNull(dataSourcesScope)) {
                dataSourcesScope.put(datasource.getId(), datasource);
            }
        } else {
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            datasource = dataSourcesScope.get(source.getDatasourceId());
        }
        PageScope pageScope = p.getScope(PageScope.class);
        if (nonNull(pageScope)) {
            pageScope.getWidgetIdSourceDatasourceMap().put(source.getId(), datasource.getId());
            pageScope.getWidgetIdClientDatasourceMap().put(compiled.getId(), getClientDatasourceId(datasource.getId(), p));
        }

        if (datasource instanceof N2oStandardDatasource)
            compiled.setObjectId(((N2oStandardDatasource) datasource).getObjectId());
        compiled.setDatasource(getClientDatasourceId(datasource.getId(), p));
        return datasource;
    }

    /**
     * Получение источника данных по id
     */
    protected N2oAbstractDatasource getDatasourceById(String datasourceId, CompileProcessor p) {
        if (datasourceId == null) return null;
        DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
        if (dataSourcesScope != null)
            return dataSourcesScope.get(datasourceId);
        return null;
    }

    private String initClientWidgetId(S source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        return nonNull(pageScope) ?
                getClientWidgetId(source.getId(), pageScope.getPageId()) :
                context.getCompiledId((N2oCompileProcessor) p);
    }

    protected void compileToolbarAndAction(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                           WidgetScope widgetScope, MetaActions metaActions,
                                           CompiledObject object, ValidationScope validationScope) {
        actionsToToolbar(source, metaActions);
        compileMetaActions(source, context, p, p.getScope(PageIndexScope.class), metaActions, widgetScope, object, validationScope);
        compiled.setToolbar(compileToolbar(source, "n2o.api.widget.toolbar.place", context, p, object,
                metaActions, widgetScope, validationScope));
    }

    protected void addParamRoutes(WidgetParamScope paramScope, CompileContext<?, ?> context, CompileProcessor p) {
        if (nonNull(paramScope) && MapUtils.isNotEmpty(paramScope.getQueryMapping())) {
            PageRoutes routes = p.getScope(PageRoutes.class);
            Models models = p.getScope(Models.class);
            paramScope.getQueryMapping().forEach((k, v) -> {
                if (context.getPathRouteMapping() == null || !context.getPathRouteMapping().containsKey(k)) {
                    if (nonNull(routes))
                        routes.addQueryMapping(k, v.getOnGet(), v.getOnSet());
                } else {
                    if (nonNull(models) && v.getOnSet() instanceof ModelLink) {
                        ModelLink link = (ModelLink) v.getOnSet();
                        models.add(link, link);
                    }
                }
            });
        }
    }

    private void compileAutoFocus(S source, D compiled, CompileProcessor p) {
        if (isNull(compiled.getComponent()))
            return;
        compiled.getComponent().setAutoFocus(p.cast(source.getAutoFocus(),
                () -> p.resolve(property("n2o.api.widget.auto_focus"), Boolean.class), () -> true));
    }

    /**
     * Получить собранный объект виджета
     */
    protected CompiledObject getObject(S source, N2oAbstractDatasource datasource, CompileProcessor p) {
        if (!(datasource instanceof N2oStandardDatasource))
            return null;

        N2oStandardDatasource standardDatasource = ((N2oStandardDatasource) datasource);

        if (nonNull(standardDatasource.getObjectId())) {
            return p.getCompiled(new ObjectContext(standardDatasource.getObjectId()));
        } else if (nonNull(standardDatasource.getQueryId())) {
            CompiledQuery query = p.getCompiled(new QueryContext(standardDatasource.getQueryId()));
            return query.getObject();
        } else {
            PageScope pageScope = p.getScope(PageScope.class);
            if (nonNull(pageScope) && nonNull(pageScope.getObjectId()) && source.getId().equals(pageScope.getResultWidgetId())) {
                return p.getCompiled(new ObjectContext(pageScope.getObjectId()));
            }
        }
        return null;
    }

    private void compileDependencies(D compiled, S source, CompileProcessor p) {
        WidgetDependency dependency = new WidgetDependency();
        List<Dependency> visibleConditions = new ArrayList<>();
        if (nonNull(source.getVisible())) {
            Object condition = p.resolveJS(source.getVisible(), Boolean.class);
            if (StringUtils.isJs(condition)) {
                Dependency visibilityCondition = new Dependency();
                visibilityCondition.setCondition(StringUtils.unwrapJs(((String) condition)));
                visibleConditions.add(visibilityCondition);
            } else if (condition instanceof Boolean) {
                compiled.setVisible((Boolean) condition);
            }
        }
        if (nonNull(source.getDependencies())) {
            List<Dependency> enableConditions = new ArrayList<>();
            for (N2oDependency dep : source.getDependencies()) {
                Dependency condition = new Dependency();
                String unwrapped = ScriptProcessor.resolveFunction(StringUtils.unwrapJs(dep.getValue()));
                condition.setCondition(unwrapped);
                String datasourceId = dep.getDatasource() == null ? compiled.getDatasource() :
                        getClientDatasourceId(dep.getDatasource(), p);
                ModelLink link = new ModelLink(
                        p.cast(dep.getModel(), ReduxModel.resolve),
                        datasourceId);
                condition.setOn(link.getBindLink());
                if (dep instanceof N2oVisibilityDependency) {
                    findByCondition(visibleConditions, unwrapped).ifPresent(visibleConditions::remove);
                    visibleConditions.add(condition);
                } else if (dep instanceof N2oEnablingDependency) {
                    enableConditions.add(condition);
                }
            }
            if (CollectionUtils.isNotEmpty(enableConditions))
                dependency.setEnabled(enableConditions);
        }

        if (CollectionUtils.isNotEmpty(visibleConditions))
            dependency.setVisible(visibleConditions);

        if (dependency.isNotEmpty()) {
            compiled.setDependency(dependency);
        }
    }

    /**
     * Получить собранную выборку виджета
     */
    protected CompiledQuery getQuery(N2oAbstractDatasource datasource, CompileProcessor p) {
        if (!(datasource instanceof N2oStandardDatasource))
            return null;

        N2oStandardDatasource standardDatasource = ((N2oStandardDatasource) datasource);
        return nonNull(standardDatasource.getQueryId()) ? p.getCompiled(new QueryContext(standardDatasource.getQueryId())) : null;
    }

    protected FieldSetScope initFieldSetScope(CompiledQuery query) {
        FieldSetScope scope = new FieldSetScope();
        if (nonNull(query)) {
            Map<String, QuerySimpleField> fieldsMap = query.getSimpleFieldsMap();
            for (Map.Entry<String, QuerySimpleField> entry : fieldsMap.entrySet()) {
                if (nonNull(entry.getValue())) {
                    scope.put(entry.getKey(), entry.getValue().getName());
                }
            }
        }
        return scope;
    }

    /**
     * Инициализация филдсетов
     *
     * @param fields      Список полей или филдсетов или строк или столбцов
     * @param context     Контекст сборки
     * @param widgetQuery Выборка виджета
     * @param p           Процессор сборки
     * @return Список филдсетов
     */
    protected List<FieldSet> initFieldSets(SourceComponent[] fields, CompileContext<?, ?> context, CompileProcessor p,
                                           WidgetScope widgetScope,
                                           CompiledQuery widgetQuery,
                                           Object... scopes) {
        if (isNull(fields))
            return Collections.emptyList();
        FieldSetScope fieldSetScope = initFieldSetScope(widgetQuery);
        IndexScope indexScope = new IndexScope();
        List<FieldSet> fieldSets = new ArrayList<>();
        int i = 0;
        while (i < fields.length) {
            N2oFieldSet fieldSet;
            if (fields[i] instanceof N2oFieldSet) {
                fieldSet = (N2oFieldSet) fields[i];
                i++;
            } else {
                N2oSetFieldSet newFieldset = new N2oSetFieldSet();
                List<SourceComponent> newFieldsetItems = new ArrayList<>();
                while (i < fields.length && !(fields[i] instanceof N2oFieldSet)) {
                    newFieldsetItems.add(fields[i]);
                    i++;
                }
                SourceComponent[] items = new SourceComponent[newFieldsetItems.size()];
                newFieldset.setItems(newFieldsetItems.toArray(items));
                fieldSet = newFieldset;
            }
            fieldSets.add(p.compile(fieldSet, context,
                    widgetQuery, widgetScope, fieldSetScope, indexScope, scopes));
        }
        return fieldSets;
    }

    private Optional<Dependency> findByCondition(List<Dependency> dependencies, String condition) {
        if (isNull(dependencies))
            return Optional.empty();
        return dependencies.stream()
                .filter(dependencyCondition -> condition.equals(dependencyCondition.getCondition())).findFirst();
    }
}
