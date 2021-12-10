package net.n2oapp.framework.config.metadata.compile.widget;


import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
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
        form.getComponent().setPrompt(p.cast(source.getPrompt(),
                p.resolve(Placeholders.property("n2o.api.widget.form.unsaved_data_prompt"), Boolean.class)));
        copyInlineDatasource(form, source, p);
        CompiledQuery query = getQuery(source, p);
        CompiledObject object = getObject(source, p);
        compileWidget(form, source, context, p, object);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope == null)
            widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setQueryId(source.getQueryId());
        widgetScope.setClientWidgetId(form.getId());
        widgetScope.setOldRoute(p.cast(source.getRoute(), source.getId()));
        MetaActions widgetActions = initMetaActions(source);
        Models models = p.getScope(Models.class);
        UploadScope uploadScope = new UploadScope();
        uploadScope.setUpload(form.getUpload());
        SubModelsScope subModelsScope = p.cast(p.getScope(SubModelsScope.class), new SubModelsScope());
        CopiedFieldScope copiedFieldScope = p.cast(p.getScope(CopiedFieldScope.class), new CopiedFieldScope());
        WidgetParamScope paramScope = form.getUpload().equals(UploadType.defaults) ? new WidgetParamScope() : null;//todo why upload=default? maybe upload != query ?
        form.getComponent().setFieldsets(initFieldSets(source.getItems(), context, p, widgetScope, query, object, widgetActions,
                new ModelsScope(ReduxModel.RESOLVE, form.getId(), models), null, subModelsScope, uploadScope,
                new MomentScope(N2oValidation.ServerMoment.beforeOperation), copiedFieldScope, paramScope, new ComponentScope(source)));
        ValidationList validationList = p.getScope(ValidationList.class) == null ? new ValidationList(new HashMap<>()) : p.getScope(ValidationList.class);
        ValidationScope validationScope = new ValidationScope(form.getId(), ReduxModel.RESOLVE, validationList);
        compileValidation(form, source, validationScope);
        compileDataProviderAndRoutes(form, source, context, p, validationList, subModelsScope, copiedFieldScope, object);
        addParamRoutes(paramScope, context, p);
        compileToolbarAndAction(form, source, context, p, widgetScope, widgetActions, object, validationList);
        form.getComponent().setModelPrefix(FormMode.TWO_MODELS.equals(source.getMode()) ? "edit" : "resolve");
//        form.setFormDataProvider(initDataProvider(source, object, context, p));
        return form;
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

    private void compileValidation(Form form, N2oForm source, ValidationScope validationScope) {
        if (source.getItems() == null)
            return;
        Map<String, List<Validation>> clientValidations = new HashMap<>();
        form.getComponent().getFieldsets().forEach(fs -> collectValidation(fs, clientValidations, validationScope));
        form.getComponent().setValidation(clientValidations);
    }

}
