package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oEnablingDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oVisibilityDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDependency;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.util.CompileUtil;
import net.n2oapp.framework.config.util.StylesResolver;

import java.util.*;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция абстрактного виджета
 */
public abstract class BaseWidgetCompiler<D extends Widget, S extends N2oWidget> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected abstract String getPropertyWidgetSrc();

    /**
     * Базовая сборка виджета
     */
    protected void compileBaseWidget(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                     CompiledObject object) {
        compiled.setId(initGlobalWidgetId(source, context, p));
        compiled.setClassName(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setProperties(p.mapAttributes(source));
        compiled.setName(p.cast(source.getName(), object != null ? object.getName() : null, source.getId()));
        compiled.setSrc(initSrc(source, p));
        compiled.setIcon(source.getIcon());
        compileAutoFocus(source, compiled, p);
        compileDependencies(compiled, source, p);
    }

    private String initSrc(S source, CompileProcessor p) {
        String defaultWidgetSrc = null;
        if (getPropertyWidgetSrc() != null)
            defaultWidgetSrc = p.resolve(property(getPropertyWidgetSrc()), String.class);
        String src = p.cast(source.getSrc(), defaultWidgetSrc);
        return src;
    }

    protected MetaActions initMetaActions(S source, CompileProcessor p) {
        MetaActions metaActions = p.getScope(MetaActions.class);
        if (metaActions == null)
            metaActions = new MetaActions();
        if (source.getActions() != null) {
            for (ActionsBar actionsBar : source.getActions()) {
                metaActions.addAction(actionsBar.getId(), actionsBar);
            }
        }
        return metaActions;
    }

    /**
     * Инициализация встроенного источника данных
     */
    protected N2oDatasource initInlineDatasource(D compiled, S source, CompileProcessor p) {
        String datasourceId = source.getDatasourceId();
        N2oDatasource datasource;
        if (source.getDatasourceId() == null) {
            datasourceId = CompileUtil.generateSourceDatasourceId(source.getId());
            if (source.getDatasource() == null) {
                datasource = new N2oDatasource();
                datasource.setDefaultValuesMode(DefaultValuesMode.defaults);
            } else {
                datasource = source.getDatasource();
                source.setDatasource(null);
            }
            datasource.setId(datasourceId);
            source.setDatasourceId(datasourceId);
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            if (dataSourcesScope != null) {
                dataSourcesScope.put(datasourceId, datasource);
            }
        } else {
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            datasource = dataSourcesScope.get(datasourceId);
        }
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null) {
            pageScope.getWidgetIdSourceDatasourceMap().put(source.getId(), datasourceId);
            pageScope.getWidgetIdClientDatasourceMap().put(compiled.getId(), pageScope.getGlobalWidgetId(datasourceId));
        }
        compiled.setDatasource(pageScope != null ? pageScope.getClientDatasourceId(datasourceId) : datasourceId);
        compiled.setObjectId(datasource.getObjectId());
        return datasource;
    }

    private String initGlobalWidgetId(S source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null) {
            return pageScope.getGlobalWidgetId(source.getId());
        } else {
            return context.getCompiledId((N2oCompileProcessor) p);
        }
    }

    protected void compileToolbarAndAction(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                           WidgetScope widgetScope, MetaActions widgetActions,
                                           CompiledObject object, ValidationList validationList) {
        actionsToToolbar(source);
        compileActions(source, context, p, widgetActions, widgetScope, object, validationList);
        compileToolbar(compiled, source, context, p, object, widgetActions, widgetScope, validationList);
    }

    protected void addParamRoutes(WidgetParamScope paramScope, CompileContext<?, ?> context, CompileProcessor p) {
        if (paramScope != null && !paramScope.getQueryMapping().isEmpty()) {
            PageRoutes routes = p.getScope(PageRoutes.class);
            Models models = p.getScope(Models.class);
            paramScope.getQueryMapping().forEach((k, v) -> {
                if (context.getPathRouteMapping() == null || !context.getPathRouteMapping().containsKey(k)) {
                    if (routes != null)
                        routes.addQueryMapping(k, v.getOnGet(), v.getOnSet());
                } else {
                    if (models != null && v.getOnSet() instanceof ModelLink) {
                        ModelLink link = (ModelLink) v.getOnSet();
                        models.add(link, link);
                    }
                }
            });
        }
    }

    private void compileActions(S source, CompileContext<?,?> context, CompileProcessor p, MetaActions widgetActions,
                                WidgetScope widgetScope, CompiledObject object, ValidationList validationList) {
        if (source.getActions() != null) {
            for (ActionsBar a : source.getActions()) {
                a.setModel(p.cast(a.getModel(), ReduxModel.resolve));
                p.compile(a.getAction(), context, widgetScope, widgetActions, object, validationList, new ComponentScope(a));
            }
        }
    }

    private void compileAutoFocus(S source, D compiled, CompileProcessor p) {
        if (compiled.getComponent() == null)
            return;
        compiled.getComponent().setAutoFocus(p.cast(source.getAutoFocus(), p.resolve(property("n2o.api.widget.auto_focus"), Boolean.class), true));
    }

    private void actionsToToolbar(S source) {
        if (source.getActions() == null || source.getToolbars() == null)
            return;
        Map<String, ActionsBar> actionMap = new HashMap<>();
        Stream.of(source.getActions()).forEach(a -> actionMap.put(a.getId(), a));
        for (N2oToolbar toolbar : source.getToolbars()) {
            if (toolbar.getItems() == null) continue;
            ToolbarItem[] toolbarItems = toolbar.getItems();
            copyActionForToolbarItem(actionMap, toolbarItems);
        }
    }

    private void copyActionForToolbarItem(Map<String, ActionsBar> actionMap, ToolbarItem[] toolbarItems) {
        for (ToolbarItem item : toolbarItems) {
            if (item instanceof N2oButton) {
                copyAction((N2oButton) item, actionMap);
            } else if (item instanceof N2oSubmenu) {
                for (N2oButton subItem : ((N2oSubmenu) item).getMenuItems()) {
                    copyAction(subItem, actionMap);
                }
            } else if (item instanceof N2oGroup) {
                copyActionForToolbarItem(actionMap, ((N2oGroup) item).getItems());
            }
        }
    }

    private void copyAction(N2oButton item, Map<String, ActionsBar> actionMap) {
        N2oButton mi = item;
        if (mi.getAction() == null && mi.getActionId() != null) {
            ActionsBar action = actionMap.get(mi.getActionId());
            if (action == null) {
                throw new N2oException(String.format("Toolbar has reference to nonexistent action by actionId %s!", mi.getAction()));
            }
            mi.setAction(action.getAction());//todo скорее всего не нужно
            if (mi.getModel() == null)
                mi.setModel(action.getModel());
            if (mi.getDatasourceId() == null)
                mi.setDatasource(action.getDatasource());
            if (mi.getLabel() == null)
                mi.setLabel(action.getLabel());
            if (mi.getIcon() == null)
                mi.setIcon(action.getIcon());
        }
    }

    private void compileToolbar(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p, Object... scopes) {
        if (source.getToolbars() == null)
            return;

        Toolbar compiledToolbar = new Toolbar();
        IndexScope index = new IndexScope();
        ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property("n2o.api.widget.toolbar.place"), String.class));
        for (N2oToolbar toolbar : source.getToolbars()) {
            compiledToolbar.putAll(p.compile(toolbar, context,
                    index, toolbarPlaceScope, scopes));
        }
        compiled.setToolbar(compiledToolbar);
    }

    /**
     * Получить собранный объект виджета
     */
    protected CompiledObject getObject(S source, N2oDatasource datasource, CompileProcessor p) {
        if (datasource != null) {
            if (datasource.getObjectId() != null) {
                return p.getCompiled(new ObjectContext(datasource.getObjectId()));
            } else if (datasource.getQueryId() != null) {
                CompiledQuery query = p.getCompiled(new QueryContext(datasource.getQueryId()));
                return query.getObject();
            } else {
                PageScope pageScope = p.getScope(PageScope.class);
                if (pageScope != null && pageScope.getObjectId() != null && source.getId().equals(pageScope.getResultWidgetId())) {
                    return p.getCompiled(new ObjectContext(pageScope.getObjectId()));
                }
            }
        }
        return null;
    }


    private void compileDependencies(D compiled, S source, CompileProcessor p) {
        WidgetDependency dependency = new WidgetDependency();
        List<DependencyCondition> visibleConditions = new ArrayList<>();
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getVisible() != null) {
            Object condition = p.resolveJS(source.getVisible(), Boolean.class);
            if (StringUtils.isJs(condition)) {
                DependencyCondition visibilityCondition = new DependencyCondition();
                visibilityCondition.setCondition(StringUtils.unwrapJs(((String) condition)));
                visibleConditions.add(visibilityCondition);
            } else if (condition instanceof Boolean) {
                compiled.setVisible((Boolean) condition);
            }
        }
        if (source.getDependencies() != null) {
            List<DependencyCondition> enableConditions = new ArrayList<>();
            for (N2oDependency dep : source.getDependencies()) {
                DependencyCondition condition = new DependencyCondition();
                String unwrapped = StringUtils.unwrapJs(dep.getValue());
                condition.setCondition(unwrapped);
                ModelLink link = new ModelLink(dep.getModel(), pageScope == null ? dep.getDatasource() :
                        pageScope.getClientDatasourceId(dep.getDatasource()));
                condition.setOn(link.getBindLink());
                if (dep instanceof N2oVisibilityDependency) {
                    findByCondition(visibleConditions, unwrapped).ifPresent(visibleConditions::remove);
                    visibleConditions.add(condition);
                } else if (dep instanceof N2oEnablingDependency) {
                    enableConditions.add(condition);
                }
            }
            if (!enableConditions.isEmpty())
                dependency.setEnable(enableConditions);
        }

        if (!visibleConditions.isEmpty())
            dependency.setVisible(visibleConditions);

        if (!dependency.isEmpty()) {
            compiled.setDependency(dependency);
        }
    }

    /**
     * Получить собранную выборку виджета
     */
    protected CompiledQuery getQuery(S source, N2oDatasource datasource, CompileProcessor p) {
        if (datasource != null) {
            return datasource.getQueryId() != null ? p.getCompiled(new QueryContext(datasource.getQueryId())) : null;
        }
        return null;
    }

    protected FieldSetScope initFieldSetScope(CompiledQuery query) {
        FieldSetScope scope = new FieldSetScope();
        if (query != null) {
            Map<String, N2oQuery.Field> fieldsMap = query.getFieldsMap();
            for (Map.Entry<String, N2oQuery.Field> entry : fieldsMap.entrySet()) {
                if (entry.getValue() != null) {
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
        if (fields == null)
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

    private Optional<DependencyCondition> findByCondition(List<DependencyCondition> dependencies, String condition) {
        if (dependencies == null)
            return Optional.empty();
        return dependencies.stream()
                .filter(dependencyCondition -> condition.equals(dependencyCondition.getCondition())).findFirst();
    }
}
