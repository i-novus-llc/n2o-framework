package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.FetchValueDependency;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;

import java.util.Arrays;
import java.util.List;

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

        compileFieldToolbar(field, source, context, p);
        field.setLabel(initLabel(source, p));
        field.setLabelClass(p.resolveJS(source.getLabelClass()));
        field.setHelp(p.resolveJS(source.getHelp()));
        field.setDescription(p.resolveJS(source.getDescription()));
        field.setClassName(p.resolveJS(source.getCssClass()));
        compileDependencies(field, source, context, p);
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
    protected void compileDependencies(Field field, S source, CompileContext<?, ?> context, CompileProcessor p) {

        if (source.getDependencies() != null) {
            for (N2oField.Dependency d : source.getDependencies()) {
                ControlDependency dependency = null;
                if (d instanceof N2oField.FetchValueDependency) {
                    dependency = new FetchValueDependency();
                    dependency.setType(ValidationType.fetchValue);
                    ((FetchValueDependency) dependency)
                            .setValueFieldId(p.cast(((N2oField.FetchValueDependency) d).getValueFieldId(), "name"));
                    ((FetchValueDependency) dependency).setDataProvider(compileDataProvider((N2oField.FetchValueDependency) d, context, p));
                } else {
                    dependency = new ControlDependency();
                    if (d instanceof N2oField.EnablingDependency)
                        dependency.setType(ValidationType.enabled);
                    else if (d instanceof N2oField.RequiringDependency)
                        dependency.setType(ValidationType.required);
                    else if (d instanceof N2oField.VisibilityDependency) {
                        dependency.setType(ValidationType.visible);
                        Boolean isResettable = p.cast(((N2oField.VisibilityDependency) d).getReset(),
                                p.resolve(property("n2o.api.control.visibility.auto_reset"), Boolean.class));
                        if (isResettable) {
                            ControlDependency reset = new ControlDependency();
                            reset.setType(ValidationType.reset);
                            reset.setExpression(ScriptProcessor.resolveFunction(d.getValue()));
                            addToField(reset, field, d, p);
                        }
                    } else if (d instanceof N2oField.SetValueDependency)
                        dependency.setType(ValidationType.setValue);
                    else if (d instanceof N2oField.FetchDependency)
                        dependency.setType(ValidationType.fetch);
                    else if (d instanceof N2oField.ResetDependency) {
                        dependency.setType(ValidationType.reset);
                        if (d.getValue() == null) {
                            d.setValue(String.valueOf(Boolean.TRUE));
                        }
                    }
                    dependency.setExpression(ScriptProcessor.resolveFunction(d.getValue()));
                }
                addToField(dependency, field, d, p);
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

    private void addToField(ControlDependency compiled, Field field, N2oField.Dependency source, CompileProcessor p) {
        compiled.setApplyOnInit(p.cast(source.getApplyOnInit(), true));
        if (source.getOn() != null) {
            List<String> ons = Arrays.asList(source.getOn());
            ons.replaceAll(String::trim);
            compiled.getOn().addAll(ons);
        }
        field.addDependency(compiled);
    }

    private void compileFieldToolbar(D field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getToolbar() != null) {
            Toolbar toolbar = p.compile(source.getToolbar(), context);
            field.setToolbar(new Group[]{toolbar.getGroup(0)});
        }
    }

    private ClientDataProvider compileDataProvider(N2oField.FetchValueDependency field, CompileContext<?, ?> context, CompileProcessor p) {
        QueryContext queryContext = new QueryContext(field.getQueryId());
        ModelsScope modelsScope = p.getScope(ModelsScope.class);
        queryContext.setFailAlertWidgetId(modelsScope != null ? modelsScope.getWidgetId() : null);
        CompiledQuery query = p.getCompiled(queryContext);
        String route = query.getRoute();
        p.addRoute(new QueryContext(field.getQueryId(), route));

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setTargetModel(modelsScope.getModel());
        dataProvider.setTargetWidgetId(modelsScope.getWidgetId());
        dataProvider.setUrl(route);

        N2oPreFilter[] preFilters = field.getPreFilters();
        if (preFilters != null) {
            N2oParam[] queryParams = new N2oParam[preFilters.length];
            for (int i = 0; i < preFilters.length; i++) {
                N2oPreFilter preFilter = preFilters[i];
                N2oQuery.Filter filter = query.getFilterByPreFilter(preFilter);
                N2oParam queryParam = new N2oParam();
                queryParam.setName(query.getFilterIdToParamMap().get(filter.getFilterField()));
                queryParam.setValueList(getPrefilterValue(preFilter));
                queryParam.setRefModel(preFilter.getRefModel());
                queryParam.setRefWidgetId(preFilter.getRefWidgetId());
                queryParams[i] = queryParam;
            }
            dataProvider.setQueryParams(queryParams);
        }
        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}
