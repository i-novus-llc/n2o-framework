package net.n2oapp.framework.config.metadata.compile.widget;


import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.FormMode;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.util.N2oClientDataProviderUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Компиляция виджета форма
 */
@Component
public class FormCompiler extends BaseWidgetCompiler<Form, N2oForm> {
    @Override
    public Class<N2oForm> getSourceClass() {
        return N2oForm.class;
    }

    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.form.src";
    }

    @Override
    public Form compile(N2oForm source, CompileContext<?, ?> context, CompileProcessor p) {
        Form form = new Form();
        form.getComponent().setPrompt(initPrompt(source, p));
        N2oDatasource datasource = initInlineDatasource(form, source, p);
        CompiledQuery query = getQuery(source, datasource, p);
        CompiledObject object = getObject(source, datasource, p);
        compileBaseWidget(form, source, context, p, object);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope == null)
            widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setClientWidgetId(form.getId());
        widgetScope.setDatasourceId(source.getDatasourceId());
        MetaActions widgetActions = initMetaActions(source);
        Models models = p.getScope(Models.class);
        SubModelsScope subModelsScope = p.cast(p.getScope(SubModelsScope.class), new SubModelsScope());
        CopiedFieldScope copiedFieldScope = p.cast(p.getScope(CopiedFieldScope.class), new CopiedFieldScope());
        WidgetParamScope paramScope = null;
        if (datasource != null && DefaultValuesMode.defaults.equals(datasource.getDefaultValuesMode()))
            paramScope = new WidgetParamScope();
        ValidationScope validationScope = null;
        ValidationList validationList = p.getScope(ValidationList.class) == null ? new ValidationList() : p.getScope(ValidationList.class);
        if (datasource != null) {
            validationScope = new ValidationScope(datasource, ReduxModel.RESOLVE, validationList);
        }
         form.getComponent().setFieldsets(initFieldSets(source.getItems(), context, p,
                 widgetScope, query, object, widgetActions,
                new ModelsScope(ReduxModel.RESOLVE, form.getId(), models),
                 subModelsScope,
                 new MomentScope(N2oValidation.ServerMoment.beforeOperation),
                 copiedFieldScope,
                 paramScope,
                 new ComponentScope(source),
                 validationScope));
        addParamRoutes(paramScope, context, p);
        compileToolbarAndAction(form, source, context, p, widgetScope, widgetActions, object, validationList);
        form.getComponent().setModelPrefix(FormMode.TWO_MODELS.equals(source.getMode()) ? "edit" : "resolve");
//        form.setFormDataProvider(initDataProvider(source, object, context, p));
        return form;
    }

    private N2oDatasource getDatasource(N2oForm source, CompileProcessor p) {
        DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
        return dataSourcesScope != null ? dataSourcesScope.get(source.getDatasourceId()) : null;
    }

    private Boolean initPrompt(N2oForm source, CompileProcessor p) {
        return p.cast(source.getPrompt(),
                p.resolve(Placeholders.property("n2o.api.widget.form.unsaved_data_prompt"), Boolean.class));
    }

    private ClientDataProvider initDataProvider(N2oForm source, CompiledObject compiledObject,
                                                CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null)
            return null;
        N2oClientDataProvider dataProvider = N2oClientDataProviderUtil.initFromSubmit(source.getSubmit(), source.getId(), compiledObject, p);

        dataProvider.setSubmitForm(true);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            dataProvider.setTargetWidgetId(widgetScope.getClientWidgetId());

        dataProvider.getActionContextData().setSuccessAlertWidgetId(source.getId());
        dataProvider.getActionContextData().setFailAlertWidgetId(source.getId());

        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }

}
