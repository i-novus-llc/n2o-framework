package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.FetchValueDependency;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import net.n2oapp.framework.config.util.CompileUtil;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class FieldCompiler<D extends Field, S extends N2oField> extends ComponentCompiler<D, S> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.field.src";
    }

    protected void compileField(D field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        compileComponent(field, source, context, p);

        field.setId(source.getId());

        field.setVisible(source.getVisible());
        field.setEnabled(source.getEnabled());

        field.setLabel(initLabel(source, p));
        field.setLabelClass(p.resolveJS(source.getLabelClass()));
        field.setHelp(p.resolveJS(source.getHelp()));
        field.setDescription(p.resolveJS(source.getDescription()));
        field.setClassName(p.resolveJS(source.getCssClass()));
        compileDependencies(field, source, p);
    }

    protected String initLabel(S source, CompileProcessor p) {
        if (source.getNoLabel() == null || !source.getNoLabel()) {
            return p.resolveJS(source.getLabel());
        }
        return null;
    }


    /**
     * Компиляция зависимостей между полями
     *
     * @param field  клиентская модель элемента ввода
     * @param source исходная модель поля
     */
    protected void compileDependencies(Field field, S source, CompileProcessor p) {

        if (source.getDependencies() != null) {
            for (N2oField.Dependency d : source.getDependencies()) {
                ControlDependency dependency = null;
                if (d instanceof N2oField.FetchValueDependency) {
                    dependency = new FetchValueDependency();
                    dependency.setType(ValidationType.fetchValue);
                    ((FetchValueDependency) dependency)
                            .setValueFieldId(p.cast(((N2oField.FetchValueDependency) d).getValueFieldId(), "name"));
                    ((FetchValueDependency) dependency).setDataProvider(compileDataProvider((N2oField.FetchValueDependency) d, p));
                } else {
                    dependency = new ControlDependency();
                    if (d instanceof N2oField.EnablingDependency)
                        dependency.setType(ValidationType.enabled);
                    else if (d instanceof N2oField.RequiringDependency)
                        dependency.setType(ValidationType.required);
                    else if (d instanceof N2oField.VisibilityDependency)
                        dependency.setType(ValidationType.visible);
                    else if (d instanceof N2oField.SetValueDependency)
                        dependency.setType(ValidationType.setValue);
                    else if (d instanceof N2oField.FetchDependency)
                        dependency.setType(ValidationType.fetch);
                    dependency.setExpression(ScriptProcessor.resolveFunction(d.getValue()));
                }
                dependency.setApplyOnInit(p.cast(d.getApplyOnInit(), true));
                if (d.getOn() != null) {
                    List<String> ons = Arrays.asList(d.getOn());
                    ons.replaceAll(String::trim);
                    dependency.getOn().addAll(ons);
                }
                field.addDependency(dependency);
            }
        }

        if (source.getDependsOn() != null) {
            ControlDependency dependency = new ControlDependency();
            List<String> ons = Arrays.asList(source.getDependsOn());
            ons.replaceAll(String::trim);
            dependency.setOn(ons);
            dependency.setType(ValidationType.reRender);
            field.addDependency(dependency);
        }
    }

    private WidgetDataProvider compileDataProvider(N2oField.FetchValueDependency field, CompileProcessor p) {
        WidgetDataProvider dataProvider = new WidgetDataProvider();
        QueryContext queryContext = new QueryContext(field.getQueryId());
        ModelsScope modelsScope = p.getScope(ModelsScope.class);
        queryContext.setFailAlertWidgetId(modelsScope != null ? modelsScope.getWidgetId() : null);
        CompiledQuery query = p.getCompiled(queryContext);
        String route = query.getRoute();
        p.addRoute(new QueryContext(field.getQueryId(), route));
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + route);
        N2oPreFilter[] preFilters = field.getPreFilters();
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
        return dataProvider;
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}
